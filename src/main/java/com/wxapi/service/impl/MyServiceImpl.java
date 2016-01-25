package com.wxapi.service.impl;

import com.core.util.ImageUtils;
import com.core.util.UploadUtil;
import com.wxapi.process.*;
import com.wxapi.service.MyService;
import com.wxapi.vo.Matchrule;
import com.wxapi.vo.MsgRequest;
import com.wxapi.vo.MsgResponseText;
import com.wxcms.domain.*;
import com.wxcms.mapper.*;
import com.wxcms.service.AccountFansService;
import com.wxcms.service.FlowService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.List;

/**
 * 业务消息处理
 * 开发者根据自己的业务自行处理消息的接收与回复；
 */

@Service
public class MyServiceImpl implements MyService{

	@Autowired
	private MsgBaseDao msgBaseDao;
	
	@Autowired
	private MsgNewsDao msgNewsDao;
	
	@Autowired
	private AccountMenuDao menuDao;
	
	@Autowired
	private AccountMenuGroupDao menuGroupDao;
	
	@Autowired
	private AccountFansDao fansDao;

	@Autowired
	private AccountFansService accountFansService;
	@Autowired
	private FlowService flowService;
	/**
	 * 处理消息
	 * 开发者可以根据用户发送的消息和自己的业务，自行返回合适的消息；
	 * @param msgRequest : 接收到的消息
	 * @param mpAccount ： mpAccount
	 * @param webRootPath : webRootPath
	 */
	public String processMsg(MsgRequest msgRequest,MpAccount mpAccount,String webRootPath,String webUrl){
		String msgtype = msgRequest.getMsgType();//接收到的消息类型
		String respXml = null;//返回的内容；
		if(msgtype.equals(MsgType.Text.toString())){
			/**
			 * 文本消息，一般公众号接收到的都是此类型消息
			 */
			respXml = this.processTextMsg(msgRequest,mpAccount);
		}else if(msgtype.equals(MsgType.Event.toString())){//事件消息
			/**
			 * 用户订阅公众账号、点击菜单按钮的时候，会触发事件消息
			 */
			respXml = this.processEventMsg(msgRequest,mpAccount,webRootPath,webUrl);
			
		//其他消息类型，开发者自行处理
		}else if(msgtype.equals(MsgType.Image.toString())){//图片消息
			
		}else if(msgtype.equals(MsgType.Location.toString())){//地理位置消息
			
		}
		
		//如果没有对应的消息，默认返回订阅消息；
		if(StringUtils.isEmpty(respXml)){
			MsgText text = msgBaseDao.getMsgTextByInputCode(MsgType.Default.toString());
			if(text != null){
				respXml = MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
			}
		}
		return respXml;
	}
	
	//处理文本消息
	private String processTextMsg(MsgRequest msgRequest,MpAccount mpAccount){
		String content = msgRequest.getContent();
		if(!StringUtils.isEmpty(content)){//文本消息
			String tmpContent = content.trim();
			List<MsgNews> msgNews = msgNewsDao.getRandomMsgByContent(tmpContent, mpAccount.getMsgcount());
			if(!CollectionUtils.isEmpty(msgNews)){
				return MsgXmlUtil.newsToXml(WxMessageBuilder.getMsgResponseNews(msgRequest, msgNews));
			}
		}
		return null;
	}
	
	//处理事件消息
	private String processEventMsg(MsgRequest msgRequest,MpAccount mpAccount,String webRootPath,String webUrl){
		String key = msgRequest.getEventKey();
		if(MsgType.SUBSCRIBE.toString().equals(msgRequest.getEvent())){//订阅消息
			//订阅消息，获取订阅扫描的二维码信息
			String eventKey=msgRequest.getEventKey();
			long userId=0;
			if(eventKey.indexOf("qrscene_")>=0)
			{
				//得到推广人信息，给推广人发送赚钱信息
				userId=Long.parseLong(eventKey.replace("qrscene_", ""));
			}
			if(userId==0){
				//避免有人没有被挂接到线下
				Date d=new Date();
				AccountFans fansTemp =accountFansService.getRandByLastUpdateTime(new Date(d.getTime() - 2 * 24 * 60 * 60 * 1000));
				if(null!=fansTemp)
					userId=fansTemp.getId();
			}
			//订阅得到用户信息保存入库
			AccountFans fans =accountFansService.getByOpenId(msgRequest.getFromUserName());
			if(null==fans){
				fans = syncAccountFans(msgRequest.getFromUserName(), mpAccount, true);//同时更新数据库
				if(null!=fans){
					if(userId!=0)
						accountFansService.updateUserReferId(msgRequest.getFromUserName(),userId);
					//发钱，发送客服消息
					Random random = new Random();
					double money=mpAccount.getInitSendMoneyMin()+ (mpAccount.getInitSendMoneyMax()-mpAccount.getInitSendMoneyMin())*random.nextDouble();
					String content="你已经获得了#{money}元红包，满"+mpAccount.getTixianMinMoney()+"元就可以提现了，点击我的海报邀请好友扫一扫就可以增加余额了。";
					content=getContent(MsgType.SUBSCRIBE_REWARD.toString(),content);
					content=content.replace("#{money}",String.format("%.2f",money));
					Flow flow=new Flow();
					flow.setCreatetime(new Date());
					flow.setUserFlowMoney(money);
					flow.setFansId(fans.getId());
					flow.setFlowType(2);//关注获取的红包。
					flow.setFromFansId(0);
					//flow.setUserFlowLog(content);
					try {
						flow.setUserFlowLogBinary(content.getBytes("UTF-8"));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					flowService.add(flow);
					//JSONObject result = WxApiClient.sendCustomTextMessage(msgRequest.getFromUserName(), content, mpAccount);
					//给自己加钱，给上级发消息，给上级发推广费。
					accountFansService.updateUserAddMoney(fans,money,userId, mpAccount,0);
					MsgText text = new MsgText();
					text.setContent(content);
					if(text != null){
						return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
					}
				}
			}
		}else if(MsgType.UNSUBSCRIBE.toString().equals(msgRequest.getEvent())){//取消订阅消息
			String openId=msgRequest.getFromUserName();//获取取消关注的用户openId
			AccountFans accountFans=accountFansService.getByOpenId(openId);
			if(null!=accountFans){
				String headImg=webRootPath+"/res/upload/"+accountFans.getOpenId()+".jpg";
				if(hasCreateRecommendPic(headImg+".text.jpg")){
					//删除图片
					File file = new File(headImg+".text.jpg");
					// 路径为文件且不为空则进行删除
					if (file.isFile() && file.exists()) {
						file.delete();
					}
				}
				if(accountFans.getUserReferId()!=0){
					Flow searchFlow=new Flow();
					searchFlow.setFansId(accountFans.getUserReferId());
					searchFlow.setFromFansId(accountFans.getId());
					searchFlow.setFlowType(1);
					List<Flow> ret=flowService.listForPage(searchFlow);
					if(null!=ret&&ret.size()>0){
						double money=ret.get(0).getUserFlowMoney();//获取当初推广赠送的金额
						//减少其上级的金额
						accountFansService.updateSubRecommendLevelMoney(money, accountFans, mpAccount, 0);
					}
				}
				accountFansService.deleteByOpenId(openId);
			}
			MsgText text = msgBaseDao.getMsgTextByInputCode(MsgType.UNSUBSCRIBE.toString());
			if(text != null){
				return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
			}
		}else{//点击事件消息
			if(!StringUtils.isEmpty(key)){
				/**
				 * 固定消息
				 * _fix_ ：在我们创建菜单的时候，做了限制，对应的event_key 加了 _fix_
				 * 
				 * 当然开发者也可以进行修改
				 */
				if(key.startsWith("_fix_")){
					String baseIds = key.substring("_fix_".length());
					if(!StringUtils.isEmpty(baseIds)){
						String[] idArr = baseIds.split(",");
						if(idArr.length > 1){//多条图文消息
							List<MsgNews> msgNews = msgBaseDao.listMsgNewsByBaseId(idArr);
							if(msgNews != null && msgNews.size() > 0){
								return MsgXmlUtil.newsToXml(WxMessageBuilder.getMsgResponseNews(msgRequest, msgNews));
							}
						}else{//图文消息，或者文本消息
							MsgBase msg = msgBaseDao.getById(baseIds);
							if(msg.getMsgtype().equals(MsgType.Text.toString())){
								MsgText text = msgBaseDao.getMsgTextByBaseId(baseIds);
								if(text != null){
									return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
								}
							}else{
								List<MsgNews> msgNews = msgBaseDao.listMsgNewsByBaseId(idArr);
								if(msgNews != null && msgNews.size() > 0){
									return MsgXmlUtil.newsToXml(WxMessageBuilder.getMsgResponseNews(msgRequest, msgNews));
								}
							}
						}
					}
				}else if(key.indexOf("my_refer")>=0){
					//生成推广的图片
					String userOpenId=msgRequest.getFromUserName();
					AccountFans accountFans=accountFansService.getByOpenId(userOpenId);
					if(null!=accountFans){
						String headImg=webRootPath+"/res/upload/"+userOpenId+".jpg";
						if(!hasCreateRecommendPic(headImg+".text.jpg")||(null==accountFans.getMediaId())||getIntervalDays(accountFans.getCreatetime(),new Date())>=30){
							//创建生成图片的线程，并且直接返回文字信息
							String log="生成我的海报大概需要5秒钟，请等待！";
							log=getContent(MsgType.WaitCreateLog.toString(),log);
							JSONObject result = WxApiClient.sendCustomTextMessage(userOpenId, log, mpAccount);
							//带参二维码
							byte[] qrcode = WxApiClient.createQRCode(2592000,accountFans.getId().intValue(),mpAccount);
							String url = webRootPath+UploadUtil.byteToImg(webRootPath, qrcode);
							String headImgUrl=accountFans.getHeadimgurl()==null?(webUrl+"/res/upload/head.jpg"):accountFans.getHeadimgurl();
							try {
								String headImgSavePath=webRootPath+"/res/upload/"+userOpenId+".jpg";
								if(!hasCreateRecommendPic(headImgSavePath))
									UploadUtil.download(headImgUrl,userOpenId+".jpg",webRootPath+"/res/upload/");
								String baseRecommendImgPath=webRootPath+"/res/css/images/base_recommend.jpg";
								//生成带图片的二维码
								ImageUtils.pressImage(headImg, url, url+".qrcode.jpg", 0, 0,true,50,50);
								ImageUtils.pressImage(url + ".qrcode.jpg", baseRecommendImgPath, url + ".last.jpg", 165, 380, false, 220, 220);//贴二维码
								ImageUtils.pressImage(headImg, url + ".last.jpg", url + ".last_head.jpg", 18, 10, false, 110, 110);//贴头像
								ImageUtils.pressText(accountFans.getNicknameStr(), url+".last_head.jpg",//贴文字
										headImg+".text.jpg","宋体", Font.BOLD, 0, 30, 225, 60);
								//上传图片到微信
								String picPath=webUrl+"/res/upload/"+userOpenId+".jpg"+".text.jpg";
								String mediaId = WxApi.uploadMedia(WxApiClient.getAccessToken(mpAccount),MediaType.Image.toString(),picPath);
								System.out.println(mediaId);
								if(null!=mediaId){
									accountFansService.updateRecommendMediaId(userOpenId, mediaId);
									log="生成海报成功，转发您的海报就可以获得推广费！";
									log=getContent(MsgType.SuccessCreateLog.toString(),log);
									WxApiClient.sendCustomImageMessage(userOpenId, mediaId, mpAccount);//图片以客服消息形式发送
								}
								MsgResponseText msgResponseText=new MsgResponseText();
								msgResponseText.setContent(log);
								return MsgXmlUtil.textToXml(msgResponseText);
							} catch (Exception e) {
								e.printStackTrace();
							}
							MsgResponseText msgResponseText=new MsgResponseText();
							log="生成图片失败，请再次点击我的海报生成推广图片！";
							log=getContent(MsgType.FailCreateLog.toString(),log);
							msgResponseText.setContent(log);
							return MsgXmlUtil.textToXml(msgResponseText);
						}else{
							String mediaId =accountFans.getMediaId();
							if(null!=mediaId&&(!"".equals(mediaId))){
								String log="生成海报成功，转发您的海报就可以获得推广费！";
								log=getContent(MsgType.SuccessCreateLog.toString(),log);
								WxApiClient.sendCustomTextMessage(userOpenId, log, mpAccount);
								return MsgXmlUtil.imageToXml(WxMessageBuilder.getMsgResponseImage(msgRequest, mediaId));
							}
						}
					}
				}
			}
		}
		return null;
	}
	private String getContent(String code,String defalut){
		MsgText text = msgBaseDao.getMsgTextByInputCode(code);
		if(text != null){
			return  text.getContent();
		}
		return defalut;
	}
	public static int getIntervalDays(Date fDate, Date oDate) {
		if (null == fDate || null == oDate) {
			return -1;
		}
		long intervalMilli = oDate.getTime()-fDate.getTime();
		return (int) (intervalMilli / (24 * 60 * 60 * 1000));
	}



	private boolean hasCreateRecommendPic(String recommendPicPath) {
		return new File(recommendPicPath).exists();
	}

	//发布菜单
	public JSONObject publishMenu(String gid,MpAccount mpAccount){
		List<AccountMenu> menus = menuDao.listWxMenus(gid);
		
		Matchrule matchrule = new Matchrule();
		String menuJson = prepareMenus(menus,matchrule);
		JSONObject rstObj = WxApiClient.publishMenus(menuJson,mpAccount);//创建普通菜单
		
		//以下为创建个性化菜单demo，只为男创建菜单；其他个性化需求 设置 Matchrule 属性即可
//		matchrule.setSex("1");//1-男 ；2-女
//		JSONObject rstObj = WxApiClient.publishAddconditionalMenus(menuJson,mpAccount);//创建个性化菜单
		
		if(rstObj != null){//成功，更新菜单组
			if(rstObj.containsKey("menu_id")){
				menuGroupDao.updateMenuGroupDisable();
				menuGroupDao.updateMenuGroupEnable(gid);
			}else if(rstObj.containsKey("errcode") && rstObj.getInt("errcode") == 0){
				menuGroupDao.updateMenuGroupDisable();
				menuGroupDao.updateMenuGroupEnable(gid);
			}
		}
		return rstObj;
	}
	
	//删除菜单
	public JSONObject deleteMenu(MpAccount mpAccount){
		JSONObject rstObj = WxApiClient.deleteMenu(mpAccount);
		if(rstObj != null && rstObj.getInt("errcode") == 0){//成功，更新菜单组
			menuGroupDao.updateMenuGroupDisable();
		}
		return rstObj;
	}

	//获取用户列表
	public boolean syncAccountFansList(MpAccount mpAccount){
		String nextOpenId = null;
		AccountFans lastFans = fansDao.getLastOpenId();
		if(lastFans != null){
			nextOpenId = lastFans.getOpenId();
		}
		return doSyncAccountFansList(nextOpenId,mpAccount);
	}
	
	//同步粉丝列表(开发者在这里可以使用递归处理)
	private boolean doSyncAccountFansList(String nextOpenId,MpAccount mpAccount){
		String url = WxApi.getFansListUrl(WxApiClient.getAccessToken(mpAccount), nextOpenId);
		JSONObject jsonObject = WxApi.httpsRequest(url, HttpMethod.POST, null);
		if(jsonObject.containsKey("errcode")){
			return false;
		}
		List<AccountFans> fansList = new ArrayList<AccountFans>();
		if(jsonObject.containsKey("data")){
			if(jsonObject.getJSONObject("data").containsKey("openid")){
				JSONArray openidArr = jsonObject.getJSONObject("data").getJSONArray("openid");
				int length = 5;//同步5个
				if(openidArr.size() < length){
					length = openidArr.size();
				}
				for(int i = 0; i < length ;i++){
					Object openId = openidArr.get(i);
					AccountFans fans = WxApiClient.syncAccountFans(openId.toString(), mpAccount);
					fansList.add(fans);
				}
				//批处理
				fansDao.addList(fansList);
			}
		}
		return true;
	}
	
	//获取用户信息接口 - 必须是开通了认证服务，否则微信平台没有开放此功能
	public AccountFans syncAccountFans(String openId, MpAccount mpAccount ,boolean merge){
		AccountFans fans = WxApiClient.syncAccountFans(openId, mpAccount);
		if (merge && null != fans) {
			AccountFans tmpFans = fansDao.getByOpenId(openId);
			if(tmpFans == null){
				fans.setUserMoney(0.00);
				fans.setUserMoneyFreezed(0.00);
				fans.setId(0L);
				fans.setUserMoneyTixian(0.00);
				fansDao.add(fans);
			}else{
				fans.setUserMoney(tmpFans.getUserMoney());
				fans.setUserMoneyFreezed(tmpFans.getUserMoneyFreezed());
				fans.setId(tmpFans.getId());
				fans.setUserMoneyTixian(tmpFans.getUserMoneyTixian());
				fans.setUserMoneyPassword(tmpFans.getUserMoneyPassword());
				fans.setUserLevel1(tmpFans.getUserLevel1());
				fans.setUserLevel2(tmpFans.getUserLevel2());
				fans.setUserLevel3(tmpFans.getUserLevel3());
				fansDao.update(fans);
			}
		}else
		{
				fans = fansDao.getByOpenId(openId);
		}
		return fans;
	}
	
	//根据openid 获取粉丝，如果没有，同步粉丝
	public AccountFans getFansByOpenId(String openId,MpAccount mpAccount){
		AccountFans fans = fansDao.getByOpenId(openId);
		if(fans == null){//如果没有，添加
			fans = WxApiClient.syncAccountFans(openId, mpAccount);
			if (null != fans) {
				fansDao.add(fans);
			}
		}
		return fans;
	}
	
	/**
	 * 获取微信公众账号的菜单
	 * @param menus	菜单列表
	 * @param matchrule	个性化菜单配置
	 * @return
	 */
	private String prepareMenus(List<AccountMenu> menus,Matchrule matchrule) {
		if(!CollectionUtils.isEmpty(menus)){
			List<AccountMenu> parentAM = new ArrayList<AccountMenu>();
			Map<Long,List<JSONObject>> subAm = new HashMap<Long,List<JSONObject>>();
			for(AccountMenu m : menus){
				if(m.getParentid() == 0L){//一级菜单
					parentAM.add(m);
				}else{//二级菜单
					if(subAm.get(m.getParentid()) == null){
						subAm.put(m.getParentid(), new ArrayList<JSONObject>());
					}
					List<JSONObject> tmpMenus = subAm.get(m.getParentid());
					tmpMenus.add(getMenuJSONObj(m));
					subAm.put(m.getParentid(), tmpMenus);
				}
			}
			JSONArray arr = new JSONArray();
			for(AccountMenu m : parentAM){
				if(subAm.get(m.getId()) != null){//有子菜单
					arr.add(getParentMenuJSONObj(m,subAm.get(m.getId())));
				}else{//没有子菜单
					arr.add(getMenuJSONObj(m));
				}
			}
			JSONObject root = new JSONObject();
			root.put("button", arr);
			root.put("matchrule", JSONObject.fromObject(matchrule).toString());
			return JSONObject.fromObject(root).toString();
		}
		return "error";
	}
	
	/**
	 * 此方法是构建菜单对象的；构建菜单时，对于  key 的值可以任意定义；
	 * 当用户点击菜单时，会把key传递回来；对已处理就可以了
	 * @param menu
	 * @return
	 */
	private JSONObject getMenuJSONObj(AccountMenu menu){
		JSONObject obj = new JSONObject();
		obj.put("name", menu.getName());
		obj.put("type", menu.getMtype());
		if("click".equals(menu.getMtype())){//事件菜单
			if("fix".equals(menu.getEventType())){//fix 消息
				obj.put("key", "_fix_" + menu.getMsgId());//以 _fix_ 开头
			}else{
				if(StringUtils.isEmpty(menu.getInputcode())){//如果inputcode 为空，默认设置为 subscribe，以免创建菜单失败
					obj.put("key", "subscribe");
				}else{
					obj.put("key", menu.getInputcode());
				}
			}
		}else{//链接菜单-view
			obj.put("url", menu.getUrl());
		}
		return obj;
	}
	
	private JSONObject getParentMenuJSONObj(AccountMenu menu,List<JSONObject> subMenu){
		JSONObject obj = new JSONObject();
		obj.put("name", menu.getName());
		obj.put("sub_button", subMenu);
		return obj;
	}

	
}


