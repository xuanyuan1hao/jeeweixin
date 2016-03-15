package com.wxapi.service.impl;

import com.wxapi.process.MsgType;
import com.wxapi.process.MsgXmlUtil;
import com.wxapi.process.UserWxApiClient;
import com.wxapi.process.WxMessageBuilder;
import com.wxapi.service.UserFansService;
import com.wxapi.vo.MsgRequest;
import com.wxcms.domain.MsgText;
import com.wxcms.domain.TaskCode;
import com.wxcms.domain.TaskLog;
import com.wxcms.domain.UserAccountFans;
import com.wxcms.mapper.MsgBaseDao;
import com.wxcms.service.CustomTextMessageService;
import com.wxcms.service.TaskCodeService;
import com.wxcms.service.TaskLogService;
import com.wxcms.service.UserAccountFansService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-02-27.
 */
@Service
public class UserFansServiceImpl implements UserFansService {
    @Autowired
    private UserAccountFansService userAccountFansService;
    @Autowired
    private  TaskLogService taskLogService;
    @Autowired
    private  TaskCodeService taskCodeService;
    @Autowired
    private MsgBaseDao msgBaseDao;

    @Autowired
    private CustomTextMessageService customTextMessageService;
    public String processMsg(MsgRequest msgRequest, TaskCode taskCode, String webRootPath, String webUrl) {
        String msgtype = msgRequest.getMsgType();//接收到的消息类型
        String respXml = null;//返回的内容；
        if (msgtype.equals(MsgType.Text.toString())) {
            /**
             * 文本消息，一般公众号接收到的都是此类型消息
             */
            respXml = this.processTextMsg(msgRequest, taskCode);
        } else if (msgtype.equals(MsgType.Event.toString())) {//事件消息
            /**
             * 用户订阅公众账号、点击菜单按钮的时候，会触发事件消息
             */
            respXml = this.processEventMsg(msgRequest, taskCode, webRootPath, webUrl);

            //其他消息类型，开发者自行处理
        } else if (msgtype.equals(MsgType.Image.toString())) {//图片消息

        } else if (msgtype.equals(MsgType.Location.toString())) {//地理位置消息

        }

        //如果没有对应的消息，默认返回订阅消息；
        if (StringUtils.isEmpty(respXml)) {
            MsgText text = msgBaseDao.getMsgTextByInputCode(MsgType.Default.toString());
            if (text != null) {
                respXml = MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, text));
            }
        }
        return respXml;
    }

    private String processEventMsg(MsgRequest msgRequest, final TaskCode taskCode, String webRootPath, String webUrl) {
        String key = msgRequest.getEventKey();
        if (MsgType.SUBSCRIBE.toString().equals(msgRequest.getEvent())) {//订阅消息
            //订阅消息，获取订阅扫描的二维码信息
            String openId = msgRequest.getFromUserName();
            UserAccountFans fans = userAccountFansService.getByOpenId(openId);
            if (null == fans) {
                fans = new UserAccountFans();
                fans.setOpenId(openId);
                fans.setAccountOld(taskCode.getAccount());
                userAccountFansService.add(fans);
            }
            if (fans.getNickname() == null) {
                UserAccountFans fansThread = userAccountFansService.getByOpenId(openId);
                UserAccountFans userAccountFansWeb = UserWxApiClient.syncAccountFans(openId, taskCode);
                if (null != fansThread&&null!=userAccountFansWeb) {
                    userAccountFansWeb.setId(fansThread.getId());
                    userAccountFansService.updateUserAccountFans(userAccountFansWeb, taskCode,"");
                    //发送客服消息
                    String log = "您在易米网的账号已经获得福利金，请到易米网公众号查看详情！";
                    log = getContent(MsgType.SUBSCRIBE_USER_ACCOUNT_ADD_MONEY.toString(), log);
                    //UserWxApiClient.sendCustomTextMessage(openId, log, taskCode);
                    customTextMessageService.addByTaskCode(openId, log, taskCode);
                }
            }
            String log = "回复福利码即可获得福利。例如：1234567";
            log = getContent(MsgType.SUBSCRIBE_USER_ACCOUNT_SUB_ACCOUNT.toString(), log);
            MsgText msgResponseText = new MsgText();
            msgResponseText.setContent(log);
            return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, msgResponseText));
        } else if (MsgType.UNSUBSCRIBE.toString().equals(msgRequest.getEvent())) {
            //取消订阅消息
            String openId = msgRequest.getFromUserName();//获取取消关注的用户openId
            UserAccountFans fans = userAccountFansService.getByOpenId(openId);
            if (null != fans && fans.getBaseOpenId() != null) {
                userAccountFansService.updateSubUserAccountFans(fans, taskCode);
            }
            userAccountFansService.deleteByOpenId(openId);
        } else
        {//点击事件消息
            if (!StringUtils.isEmpty(key)) {
                if (key.startsWith("_fix_")) {
                    String baseIds = key.substring("_fix_".length());
                }
            }
        }
        return null;
    }

    //处理文本消息
    private String processTextMsg(MsgRequest msgRequest, TaskCode taskCode) {
        String content = msgRequest.getContent();
        if (!StringUtils.isEmpty(content)) {//文本消息
            MsgText msgResponseText = new MsgText();
            String tmpContent = content.trim();
            String openId=msgRequest.getFromUserName();
            UserAccountFans userAccountFans= userAccountFansService.getByOpenId(openId);
            if(null==userAccountFans){
                userAccountFans=new UserAccountFans();
                userAccountFans.setOpenId(openId);
                userAccountFans.setAccountOld(taskCode.getAccount());
                userAccountFansService.add(userAccountFans);
            }
            Pattern p= Pattern.compile("(\\d+)");
            Matcher m = p.matcher(tmpContent);
            String log="欢迎关注，想要做任务赚点零花钱请关注易米网公众号了解详情。";
            log=getContent(MsgType.DEFAULT_TEXT_RESPONSE_MSG.toString(), log);
            if(m.find()){
                String taskCodeNum=m.group(1);
                System.out.println(m.group(1));
                TaskLog taskLog= taskLogService.getByCode(taskCodeNum);
                if(null!=taskLog){
                    //福利码正确，查找任务接任务的原始粉丝
                    taskCode= taskCodeService.getById(taskLog.getTaskId());
                    if(null!=taskCode){
                        userAccountFansService.updateUserAccountFans(userAccountFans, taskCode,taskLog.getOpenId());
                        log="您在易米网的账号已经获得福利金，请到易米网公众号查看详情！";
                        log=getContent(MsgType.SUCCESS_GET_MONEY_TEXT_RESPONSE_MSG.toString(), log);
                    }else{
                        log="任务已经下架，请到关注易米网公众号执行其他任务。";
                        log=getContent(MsgType.TASK_IS_NOT_EXIST_TEXT_RESPONSE_MSG.toString(), log);
                    }
                }else{
                    //福利码错误或者是已经领取完了福利
                    log = "福利码错误或者是已经领取完了福利，请检查福利码";
                    log=getContent(MsgType.TASK_CODE_ERROR_TEXT_RESPONSE_MSG.toString(), log);
                }
            }else{
                //输入的福利码格式不对。福利码为数字，请到易米网查看。
                log = "输入的福利码格式不对，请输入正确福利码。例如：1234567";
                log=getContent(MsgType.TASK_CODE_PATTEN_ERROR_TEXT_RESPONSE_MSG.toString(), log);
            }
            msgResponseText.setContent(log);
            return MsgXmlUtil.textToXml(WxMessageBuilder.getMsgResponseText(msgRequest, msgResponseText));
        }
        return null;
    }

    private String getContent(String code, String defalut) {
        MsgText text = msgBaseDao.getMsgTextByInputCode(code);
        if (text != null) {
            return text.getContent();
        }
        return defalut;
    }
}
