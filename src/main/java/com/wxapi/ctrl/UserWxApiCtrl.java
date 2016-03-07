package com.wxapi.ctrl;

import com.core.util.wx.SignUtil;
import com.wxapi.process.MsgXmlUtil;
import com.wxapi.service.UserFansService;
import com.wxapi.vo.MsgRequest;
import com.wxcms.domain.TaskCode;
import com.wxcms.service.TaskCodeService;
import com.wxcms.service.UserAccountFansService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Administrator on 2016-02-25.
 */
@Controller
@RequestMapping("/user_wxapi")
public class UserWxApiCtrl {
    private static final Logger logger = LoggerFactory.getLogger(WxApiCtrl.class);
    @Autowired
    private TaskCodeService taskCodeService;
    @Autowired
    private UserAccountFansService userAccountFansService;
    @Autowired
    private UserFansService userFansService;

    @RequestMapping(value = "/{account}/message", method = RequestMethod.GET)
    public
    @ResponseBody
    String doGet(HttpServletRequest request, @PathVariable String account) {
        TaskCode taskCode = taskCodeService.getByAccount(account);//获取缓存中的唯一账号
        if (taskCode != null) {
            if(account.equals(taskCode.getAccount())){
                String token = taskCode.getToken();//获取token，进行验证；
                String signature = request.getParameter("signature");// 微信加密签名
                String timestamp = request.getParameter("timestamp");// 时间戳
                String nonce = request.getParameter("nonce");// 随机数
                String echostr = request.getParameter("echostr");// 随机字符串
                // 校验成功返回  echostr，成功成为开发者；否则返回error，接入失败
                if (SignUtil.validSign(signature, token, timestamp, nonce)) {
                    return echostr;
                }
            }
        }
        return "error";
    }

    /**
     * POST 请求：进行消息处理；
     */
    @RequestMapping(value = "/{account}/message", method = RequestMethod.POST)
    public
    @ResponseBody
    String doPost(HttpServletRequest request, @PathVariable String account, HttpServletResponse response) {
        //处理用户和微信公众账号交互消息
        TaskCode taskCode = taskCodeService.getByAccount(account);//获取缓存中的唯一账号
        try {
            MsgRequest msgRequest = MsgXmlUtil.parseXml(request);//获取发送的消息
            System.out.println("=======================" + msgRequest.getMsgType());
            String openId = msgRequest.getFromUserName();
            userAccountFansService.updateLastUpdateTime(openId, new Date());//更新最后使用时间，用于客服群发消息
            String webUrl = "http://" + request.getServerName().toString() + ((request.getLocalPort() == 80) ? "" : (":" + request.getLocalPort()));
            String webRootPath = request.getServletContext().getRealPath("/");
            return userFansService.processMsg(msgRequest, taskCode, webRootPath, webUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
