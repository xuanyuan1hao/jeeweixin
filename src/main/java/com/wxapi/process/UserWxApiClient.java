package com.wxapi.process;

import com.wxcms.domain.TaskCode;
import com.wxcms.domain.UserAccountFans;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Created by Administrator on 2016-02-28.
 */
public class UserWxApiClient {

    private static final Logger logger = LoggerFactory.getLogger(UserWxApiClient.class);
    public static String getAccessToken( TaskCode taskCode){
        //获取唯一的accessToken，如果是多账号，请自行处理
        AccessToken token = WxMemoryCacheClient.getAccessTokenByAccount(taskCode.getAccount());
        if(token != null && !token.isExpires()){//不为空，并且没有过期
            return token.getAccessToken();
        }else{
            token = WxApi.getAccessToken(taskCode.getAppid(), taskCode.getAppsecret());
            if(token != null){
                if(token.getErrcode() != null){//获取失败
                    System.out.println("## getAccessToken Error = " + token.getErrmsg());
                    logger.error("## getAccessToken Error = " + token.getErrmsg());
                }else{
                    WxMemoryCacheClient.addUserAccessToken(taskCode.getAccount(), token);
                    return token.getAccessToken();
                }
            }
            return null;
        }
    }

    public static UserAccountFans syncAccountFans(String openId, TaskCode taskCode){
        String accessToken = getAccessToken(taskCode);
        String url = WxApi.getFansInfoUrl(accessToken, openId);
        JSONObject jsonObj = WxApi.httpsRequest(url, "GET", null);
        if (null != jsonObj) {
            if(jsonObj.containsKey("errcode")){
                int errorCode = jsonObj.getInt("errcode");
                System.out.println(String.format("获取用户信息失败 errcode:{%d} errmsg:{%s}", errorCode, ErrCode.errMsg(errorCode)));
                logger.error(String.format("获取用户信息失败 errcode:{%d} errmsg:{%s}", errorCode, ErrCode.errMsg(errorCode)));
                //这里进行自救活动
                if(40001==errorCode){
                    System.out.println("token无效自救");
                    flushAccessToken(taskCode);
                }
                return null;
            }else{
                UserAccountFans fans = new UserAccountFans();
                fans.setOpenId(jsonObj.getString("openid"));// 用户的标识
                fans.setSubscribeStatus(new Integer(jsonObj.getInt("subscribe")));// 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
                if(jsonObj.containsKey("subscribe_time")){
                    fans.setSubscribeTime(jsonObj.getString("subscribe_time"));// 用户关注时间
                }
                if(jsonObj.containsKey("nickname")){// 昵称
                    try {
                        String nickname = jsonObj.getString("nickname");
                        fans.setNickname(nickname.getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObj.containsKey("sex")){// 用户的性别（1是男性，2是女性，0是未知）
                    fans.setGender(jsonObj.getInt("sex"));
                }
                if(jsonObj.containsKey("language")){// 用户的语言，简体中文为zh_CN
                    fans.setLanguage(jsonObj.getString("language"));
                }
                if(jsonObj.containsKey("country")){// 用户所在国家
                    fans.setCountry(jsonObj.getString("country"));
                }
                if(jsonObj.containsKey("province")){// 用户所在省份
                    fans.setProvince(jsonObj.getString("province"));
                }
                if(jsonObj.containsKey("city")){// 用户所在城市
                    fans.setCity(jsonObj.getString("city"));
                }
                if(jsonObj.containsKey("headimgurl")){// 用户头像
                    fans.setHeadimgurl(jsonObj.getString("headimgurl"));
                }
                if(jsonObj.containsKey("remark")){
                    fans.setRemark(jsonObj.getString("remark"));
                }
                fans.setStatus(1);
                fans.setCreatetime(new Date());
                return fans;
            }
        }
        return null;
    }
    private static void flushAccessToken( TaskCode taskCode) {
        AccessToken token = WxApi.getAccessToken(taskCode.getAppid(), taskCode.getAppsecret());
        if(token != null){
            if(token.getErrcode() != null){//获取失败
                System.out.println("## getAccessToken Error = " + token.getErrmsg());
                logger.error("## getAccessToken Error = " + token.getErrmsg());
            }else{
                WxMemoryCacheClient.addAccessToken(taskCode.getAccount(), token);
            }
        }else{
            System.out.println("自救失败 " );
        }
    }

    public static JSONObject sendCustomTextMessage(String openid, String content, TaskCode taskCodeThread) {
        if(!StringUtils.isBlank(openid) && !StringUtils.isBlank(content)){
            final String accessToken = getAccessToken(taskCodeThread);
            final String content2 = WxMessageBuilder.prepareCustomText(openid, content);
            new Thread(new Runnable(){
                public void run(){
                    WxApi.httpsRequest(WxApi.getSendCustomMessageUrl(accessToken), HttpMethod.POST, content2);
                }
            }).start();
            return null;
        }
        return null;
    }
}
