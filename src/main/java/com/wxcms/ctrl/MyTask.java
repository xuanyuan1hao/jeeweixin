package com.wxcms.ctrl;

import com.core.page.Pagination;
import com.wxapi.process.UserWxApiClient;
import com.wxapi.process.WxApiClient;
import com.wxcms.domain.CustomTextMessage;
import com.wxcms.service.CustomTextMessageService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyTask {
    @Autowired
    private CustomTextMessageService customTextMessageService;

    @Scheduled(cron = "0/1 * * * * ? ") //间隔5秒执行
    public void taskCycle() {
        CustomTextMessage searchEntity=new CustomTextMessage();
        searchEntity.setSendResult(-1);
        Pagination<CustomTextMessage> pagination=new Pagination<CustomTextMessage>();
        Pagination<CustomTextMessage> listCustomTextMessage = customTextMessageService.paginationEntity(searchEntity,pagination);
        if (null!=listCustomTextMessage&&listCustomTextMessage.getItems().size()>0){
            for (int i=0;i<listCustomTextMessage.getItems().size();i++){
                CustomTextMessage temp=listCustomTextMessage.getItems().get(i);
                String content=temp.getContent();
                JSONObject jsonObject=null;
                if(temp.getType()==0){
                    //服务号客服消息
                    jsonObject= WxApiClient.sendCustomTextMessage(temp.getOpenid(),
                            content, temp.getAccount(), temp.getAppId(), temp.getAppSecret());
                }else{
                    //客户客服消息
                    jsonObject= UserWxApiClient.sendCustomTextMessage(temp.getOpenid(),
                            content, temp.getAccount(),temp.getAppId(),temp.getAppSecret());

                }
                if(null!=jsonObject){
                    if (jsonObject.containsKey("errcode")) {//失败1次
                        int errorCode=jsonObject.getInt("errcode");
                        if (errorCode==0){
                            //发送完毕
                            temp.setSendResult(1);
                            customTextMessageService.update(temp);
                        }
                    }else{
                        continue;//跳过一次
                    }
                }
                customTextMessageService.updateSendTimes(temp);
                if (temp.getSendTimes()>10){
                    temp.setSendResult(-2);//以后不再发送，设置为发送失败
                    customTextMessageService.update(temp);
                }
            }
        }else{
            System.out.println("没有消息要发送，等待处理");
        }
    }
}
