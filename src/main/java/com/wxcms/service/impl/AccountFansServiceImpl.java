package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.core.util.ImageByteUtils;
import com.wxapi.process.MpAccount;
import com.wxapi.process.MsgType;
import com.wxapi.process.WxApiClient;
import com.wxcms.domain.AccountFans;
import com.wxcms.domain.FansTixian;
import com.wxcms.domain.Flow;
import com.wxcms.domain.MsgText;
import com.wxcms.mapper.AccountFansDao;
import com.wxcms.mapper.MsgBaseDao;
import com.wxcms.service.AccountFansService;
import com.wxcms.service.FansTixianSrevice;
import com.wxcms.service.FlowService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Service
public class AccountFansServiceImpl implements AccountFansService{

	@Autowired
	private AccountFansDao entityDao;
	@Autowired
	private MsgBaseDao msgBaseDao;
	@Autowired
	private FansTixianSrevice fansTixianSrevice;
	@Autowired
	private FlowService flowService;
	public AccountFans getById(String id){
		return entityDao.getById(id);
	}

	public AccountFans getByOpenId(String openId){
		return entityDao.getByOpenId(openId);
	}
	
	public List<AccountFans> list(AccountFans searchEntity){
		return entityDao.list(searchEntity);
	}
	public List<AccountFans> listByUserMoneyTixian(AccountFans searchEntity){
		return entityDao.listByUserMoneyTixian(searchEntity);
	}
	public Pagination<AccountFans> paginationEntity(AccountFans searchEntity,Pagination<AccountFans> pagination){
		Integer totalItemsCount = entityDao.getTotalItemsCount(searchEntity);
		List<AccountFans> items = entityDao.paginationEntity(searchEntity,pagination);
		pagination.setTotalItemsCount(totalItemsCount);
		pagination.setItems(items);
		return pagination;
	}
	
	public AccountFans getLastOpenId(){
		return entityDao.getLastOpenId();
	}
	
	public void sync(AccountFans searchEntity){
		AccountFans lastFans = entityDao.getLastOpenId();
		String lastOpenId = "";
		if(lastFans != null){
			lastOpenId = lastFans.getOpenId();
		}
		
		
	}

	public void add(AccountFans entity){
		entityDao.add(entity);
	}

	public void update(AccountFans entity){
		entityDao.update(entity);
	}

	public void delete(AccountFans entity){
		entityDao.delete(entity);
	}

	public void deleteByOpenId(String openId){
		entityDao.deleteByOpenId(openId);
	}

	public void updateRecommendMediaId(String userOpenId, String mediaId) {
		entityDao.updateRecommendMediaId(userOpenId, mediaId, new Date());
	}
	public void updateLastUpdateTime(String userOpenId,Date date){
		entityDao.updateLastUpdateTime(userOpenId, date);
	}

	public void updateRecommendImgCreateTime(String userOpenId,Date date){
		entityDao.updateRecommendImgCreateTime(userOpenId, date);
	}
	public void updateUserReferId(String userOpenId, long userReferId) {
		entityDao.updateUserReferId(userOpenId, userReferId);
	}
	public void updateUserMoneyPassword(String userOpenId, String userMoneyPassword) {
		entityDao.updateUserMoneyPassword(userOpenId, userMoneyPassword);
	}
	public void updateUserMoney(double money, String openId,FansTixian fansTixian){
		//用户金额减少，用户冻结金额增加
		entityDao.updateUserMoney(money, openId);
		//新增一条提现记录
		fansTixianSrevice.add(fansTixian);
	}
	public AccountFans getRandByLastUpdateTime(Date lastUpdateTime){
		return entityDao.getRandByLastUpdateTime(lastUpdateTime);
	}
	public void updateUserAddMoney(AccountFans fans, double money, long referUserId,MpAccount mpAccount,int times){
		if(times==0)
			entityDao.updateAddUserMoney(money, fans.getOpenId());//当前关注的用户获取的金额
		AccountFans referAccountFans=getById(referUserId + "");
		if((referAccountFans!=null)&&times<3){
			if(times==0)
				updateUserLevel1(1,referAccountFans.getId());
			else if(times==1)
				updateUserLevel2(1, referAccountFans.getId());
			else if(times==2)
				updateUserLevel3(1, referAccountFans.getId());

			Random random = new Random();
			double referMoney=0.5*money;
			if(times==0){
				referMoney=mpAccount.getReferMoneyMin()+(mpAccount.getReferMoneyMax()-mpAccount.getReferMoneyMin())*random.nextDouble();
			}
			//推广赠送金额写入到数据库
			String logAdd="";
			for (int i=0;i<times;i++){
				logAdd=logAdd+"的好友";
			}
			String log="您的好友#{friendName}扫描了您的二维码，您获取到了#{money}元红包";
			log=getContent(MsgType.SUBSCRIBE_REWARD_LEVEL.toString(),log);
			log=log.replace("#{friendName}",fans.getNicknameStr()+logAdd).replace("#{money}",String.format("%.2f",referMoney));
			Flow flow=new Flow();
			flow.setCreatetime(new Date());
			flow.setUserFlowMoney(referMoney);
			flow.setFansId(referUserId);
			flow.setFlowType(1);
			flow.setFromFansId(fans.getId());
			//flow.setUserFlowLog(log);
			try {
				flow.setUserFlowLogBinary(log.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			flowService.add(flow);
			entityDao.updateAddUserMoneyByUserId(referMoney,referUserId);
			//发送得到钱的客服消息(有效时间段内发消息，非有效时间就不发消息了。)
			//int intervalHours= MyServiceImpl.getIntervalHours(referAccountFans.getLastUpdateTime(), new Date());
			//if(intervalHours<48&&intervalHours>0)
			{
				JSONObject result = WxApiClient.sendCustomTextMessage(referAccountFans.getOpenId(), log, mpAccount);
			}
			//处理二级
			AccountFans referFans=getById(referUserId + "");
			if(null!=referFans){
				updateUserAddMoney(referFans,referMoney,referFans.getUserReferId(),mpAccount,times+1);
			}
		}
	}
	private String getContent(String code,String defalut){
		MsgText text = msgBaseDao.getMsgTextByInputCode(code);
		if(text != null){
			return  text.getContent();
		}
		return defalut;
	}
	public void updateAddUserMoneyByUserId(double money, long userId){
		entityDao.updateAddUserMoneyByUserId(money, userId);
	}
	public void updateUserLevel1( int userLevel1, long id){
		entityDao.updateUserLevel1(userLevel1, id);
	}
	public void updateUserLevel2( int userLevel2,long id){entityDao.updateUserLevel2(userLevel2, id);}
	public void updateUserLevel3(int userLevel3, long id){entityDao.updateUserLevel3(userLevel3, id);}

	public void updateUserMoneyFreezed(double userMoneyFreezedAdd, long  id){
		entityDao.updateUserMoneyFreezed(userMoneyFreezedAdd, id);
	}
	public void updateSubRecommendLevelMoney( double money, AccountFans accountFans, MpAccount mpAccount,int times) {
		AccountFans recommendAccountFans=this.getById(accountFans.getUserReferId() + "");
		if(null!=recommendAccountFans&&times<3){
			String logAdd="";
			if(times==0)
				this.updateUserLevel1(-1, recommendAccountFans.getId());
			else if(times==1)
				this.updateUserLevel2(-1, recommendAccountFans.getId());
			else if(times==2)
				this.updateUserLevel3(-1, recommendAccountFans.getId());
			for (int i=0;i<times;i++){
				logAdd=logAdd+"的好友";
			}
			String log="您的好友#{friendName}取消了关注，您被扣除#{money}元红包";
			log=getContent(MsgType.UNSUBSCRIBE_REWARD.toString(),log);
			log=log.replace("#{friendName}",accountFans.getNicknameStr()+logAdd).replace("#{money}",money+"");
			Flow flow=new Flow();
			flow.setCreatetime(new Date());
			flow.setUserFlowMoney(0 - money);
			flow.setFansId(accountFans.getUserReferId());
			flow.setFlowType(3);//取消关注减去的红包。
			flow.setFromFansId(accountFans.getId());
			//flow.setUserFlowLog(log);
			try {
				flow.setUserFlowLogBinary(log.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			flowService.add(flow);
			//int intervalHours=getIntervalHours(recommendAccountFans.getLastUpdateTime(),new Date());
			//if(intervalHours<48&&intervalHours>0)
			{
				JSONObject result = WxApiClient.sendCustomTextMessage(recommendAccountFans.getOpenId(), log, mpAccount);
			}
			this.updateAddUserMoneyByUserId(0 - money, recommendAccountFans.getId());//上级扣钱
			//获取推荐人
			if(recommendAccountFans.getUserReferId()!=0)
				updateSubRecommendLevelMoney(money*0.5,recommendAccountFans,mpAccount,times+1);
		}

	}
	private static int getIntervalHours(Date fDate, Date oDate) {
		if (null == fDate || null == oDate) {
			return -1;
		}
		long intervalMilli = oDate.getTime()-fDate.getTime();
		return (int) (intervalMilli / (  60 * 60 * 1000));
	}
	public void updateUserMoneyCheck(long id){
		entityDao.updateUserMoneyCheck(id);
	}

	public void updateHeadImgBlobToDb(String headImgSavePath,long id) {
		//读取图片文件为byte
		byte[] imageByte=ImageByteUtils.image2byte(headImgSavePath);
		entityDao.updateHeadImgBlob(imageByte,id);
	}
	public void updateRecommendImgBlob(String recommendImgBlob,long id) {
		//读取图片文件为byte
		byte[] imageByte=ImageByteUtils.image2byte(recommendImgBlob);
		entityDao.updateRecommendImgBlob(imageByte,id);
	}
}