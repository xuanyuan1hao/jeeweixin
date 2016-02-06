package com.wxapi.ctrl;

import com.core.page.Pagination;
import com.core.spring.JsonView;
import com.core.spring.SpringFreemarkerContextPathUtil;
import com.core.util.DateUtil;
import com.core.util.ImageByteUtils;
import com.core.util.UploadUtil;
import com.core.util.wx.SignUtil;
import com.wxapi.process.*;
import com.wxapi.service.impl.MyServiceImpl;
import com.wxapi.vo.*;
import com.wxcms.domain.AccountFans;
import com.wxcms.domain.FansTixian;
import com.wxcms.domain.Flow;
import com.wxcms.domain.MsgNews;
import com.wxcms.service.AccountFansService;
import com.wxcms.service.FlowService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;


/**
 * 微信与开发者服务器交互接口
 */
@Controller
@RequestMapping("/wxapi")
public class WxApiCtrl {

    private static final Logger logger = LoggerFactory.getLogger(WxApiCtrl.class);
    @Autowired
    private MyServiceImpl myService;

    @Autowired
    private AccountFansService accountFansService;
    @Autowired
    private FlowService flowService;


    @InitBinder("fansTixian")
    public void initBinderFansTixian(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("fansTixian.");
    }

    /**
     * GET请求：进行URL、Tocken 认证；
     * 1. 将token、timestamp、nonce三个参数进行字典序排序
     * 2. 将三个参数字符串拼接成一个字符串进行sha1加密
     * 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
     */
    @RequestMapping(value = "/{account}/message", method = RequestMethod.GET)
    public
    @ResponseBody
    String doGet(HttpServletRequest request, @PathVariable String account) {
        //如果是多账号，根据url中的account参数获取对应的MpAccount处理即可
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        if (mpAccount != null) {
            String token = mpAccount.getToken();//获取token，进行验证；
            String signature = request.getParameter("signature");// 微信加密签名
            String timestamp = request.getParameter("timestamp");// 时间戳
            String nonce = request.getParameter("nonce");// 随机数
            String echostr = request.getParameter("echostr");// 随机字符串
            // 校验成功返回  echostr，成功成为开发者；否则返回error，接入失败
            if (SignUtil.validSign(signature, token, timestamp, nonce)) {
                return echostr;
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
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();
        try {
            MsgRequest msgRequest = MsgXmlUtil.parseXml(request);//获取发送的消息
            System.out.println("=======================" + msgRequest.getMsgType());
            String openId = msgRequest.getFromUserName();
            accountFansService.updateLastUpdateTime(openId, new Date());//更新最后使用时间，用于客服群发消息
            String path = SpringFreemarkerContextPathUtil.getBasePath(request);
            String webUrl = request.getScheme() + "://" + request.getServerName() + path;
            String webRootPath = request.getServletContext().getRealPath("/");
            return myService.processMsg(msgRequest, mpAccount, webRootPath, webUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    //创建微信公众账号菜单
    @RequestMapping(value = "/publishMenu")
    public ModelAndView publishMenu(HttpServletRequest request, String gid) {
        JSONObject rstObj = null;
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();
        if (mpAccount != null) {
            rstObj = myService.publishMenu(gid, mpAccount);
            if (rstObj != null) {//成功，更新菜单组
                if (rstObj.containsKey("menu_id")) {
                    ModelAndView mv = new ModelAndView("common/success");
                    mv.addObject("successMsg", "创建菜单成功");
                    return mv;
                } else if (rstObj.containsKey("errcode") && rstObj.getInt("errcode") == 0) {
                    ModelAndView mv = new ModelAndView("common/success");
                    mv.addObject("successMsg", "创建菜单成功");
                    return mv;
                }
            }
        }

        ModelAndView mv = new ModelAndView("common/failure");
        String failureMsg = "创建菜单失败，请检查菜单：可创建最多3个一级菜单，每个一级菜单下可创建最多5个二级菜单。";
        if (rstObj != null) {
            failureMsg += ErrCode.errMsg(rstObj.getInt("errcode"));
        }
        mv.addObject("failureMsg", failureMsg);
        return mv;
    }

    //删除微信公众账号菜单
    @RequestMapping(value = "/deleteMenu")
    public ModelAndView deleteMenu(HttpServletRequest request) {
        JSONObject rstObj = null;
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        if (mpAccount != null) {
            rstObj = myService.deleteMenu(mpAccount);
            if (rstObj != null && rstObj.getInt("errcode") == 0) {
                ModelAndView mv = new ModelAndView("common/success");
                mv.addObject("successMsg", "删除菜单成功");
                return mv;
            }
        }
        ModelAndView mv = new ModelAndView("common/failure");
        String failureMsg = "删除菜单失败";
        if (rstObj != null) {
            failureMsg += ErrCode.errMsg(rstObj.getInt("errcode"));
        }
        mv.addObject("failureMsg", failureMsg);
        return mv;
    }

    //获取用户列表
    @RequestMapping(value = "/syncAccountFansList")
    public ModelAndView syncAccountFansList() {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        if (mpAccount != null) {
            boolean flag = myService.syncAccountFansList(mpAccount);
            if (flag) {
                return new ModelAndView("redirect:/accountfans/paginationEntity.html");
            }
        }
        ModelAndView mv = new ModelAndView("common/failure");
        mv.addObject("failureMsg", "获取用户列表失败");
        return mv;
    }

    //根据用户的ID更新用户信息
    @RequestMapping(value = "/syncAccountFans")
    public ModelAndView syncAccountFans(String openId) {
        ModelAndView mv = new ModelAndView("common/failure");
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        if (mpAccount != null) {
            AccountFans fans = myService.syncAccountFans(openId, mpAccount, true);//同时更新数据库
            if (fans != null) {
                mv.setViewName("wxcms/fansInfo");
                mv.addObject("fans", fans);
                mv.addObject("cur_nav", "fans");
                return mv;
            }
        }
        mv.addObject("failureMsg", "获取用户信息失败,公众号信息或openid信息错误");
        return mv;
    }

    //获取永久素材
    @RequestMapping(value = "/syncMaterials")
    public ModelAndView syncMaterials(Pagination<MaterialArticle> pagination) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号

        ModelAndView mv = new ModelAndView("wxcms/materialPagination");
        Integer offset = pagination.getStart();
        Integer count = pagination.getPageSize();
        Material material = WxApiClient.syncBatchMaterial(MediaType.News, offset, count, mpAccount);
        if (material != null) {
            List<MaterialArticle> materialList = new ArrayList<MaterialArticle>();
            List<MaterialItem> itemList = material.getItems();
            if (itemList != null) {
                for (MaterialItem item : itemList) {
                    MaterialArticle m = new MaterialArticle();
                    if (item.getNewsItems() != null && item.getNewsItems().size() > 0) {
                        MaterialArticle ma = item.getNewsItems().get(0);//用第一个图文的简介、标题、作者、url
                        m.setAuthor(ma.getAuthor());
                        m.setTitle(ma.getTitle());
                        m.setUrl(ma.getUrl());
                    }
                    materialList.add(m);
                }
            }
            pagination.setTotalItemsCount(material.getTotalCount());
            pagination.setItems(materialList);
        }
        mv.addObject("page", pagination);
        mv.addObject("cur_nav", "material");
        return mv;
    }


    //上传图文素材
    @RequestMapping(value = "/doUploadMaterial")
    public ModelAndView doUploadMaterial(MsgNews msgNews) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        String rstMsg = "上传图文消息素材";
        List<MsgNews> msgNewsList = new ArrayList<MsgNews>();
        msgNewsList.add(msgNews);
        JSONObject rstObj = WxApiClient.uploadNews(msgNewsList, mpAccount);
        if (rstObj.containsKey("media_id")) {
            ModelAndView mv = new ModelAndView("common/success");
            mv.addObject("successMsg", "上传图文素材成功,素材 media_id : " + rstObj.getString("media_id"));
            return mv;
        } else {
            rstMsg = ErrCode.errMsg(rstObj.getInt("errcode"));
        }
        ModelAndView mv = new ModelAndView("common/failure");
        mv.addObject("failureMsg", rstMsg);
        return mv;
    }

    //获取openid
    @RequestMapping(value = "/oauthOpenid")
    public ModelAndView oauthOpenid(HttpServletRequest request) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        if (mpAccount != null) {
            ModelAndView mv = new ModelAndView("wxweb/oauthOpenid");
            //拦截器已经处理了缓存,这里直接取
            String openid = WxMemoryCacheClient.getOpenid(request.getSession().getId());
            AccountFans fans = myService.syncAccountFans(openid, mpAccount, false);//同时更新数据库
            mv.addObject("openid", openid);
            mv.addObject("fans", fans);
            return mv;
        } else {
            ModelAndView mv = new ModelAndView("common/failureMobile");
            mv.addObject("message", "OAuth获取openid失败");
            return mv;
        }
    }

    @RequestMapping(value = "/my_center_sub")
    public ModelAndView myCenter(HttpServletRequest request,@RequestParam("openId") String openId,@RequestParam("id") long id) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        if (mpAccount != null) {
            ModelAndView mv = new ModelAndView("wxweb/my_center");
            AccountFans fans = accountFansService.getByOpenId(openId);//同时更新数据库
            if (null != fans && fans.getId()==id) {
                accountFansService.updateUserMoneyCheck(fans.getId());
                mv.addObject("openid", openId);
                mv.addObject("fans", fans);
            }else {
                mv = new ModelAndView("common/failureMobile");
                mv.addObject("message", "读取用户信息失败");
                return mv;
            }
            return mv;
        } else {
            ModelAndView mv = new ModelAndView("common/failureMobile");
            mv.addObject("message", "OAuth获取openid失败");
            return mv;
        }
    }
    @RequestMapping(value = "/my_center")
    public ModelAndView myCenter(HttpServletRequest request) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        if (mpAccount != null) {
            ModelAndView mv = new ModelAndView("wxweb/my_center");
            //拦截器已经处理了缓存,这里直接取
            String openid = WxMemoryCacheClient.getOpenid(request.getSession().getId());
            AccountFans fans = accountFansService.getByOpenId(openid);//同时更新数据库
            if (null != fans) {
                accountFansService.updateUserMoneyCheck(fans.getId());
                mv.addObject("openid", openid);
                mv.addObject("fans", fans);
            }
            return mv;
        } else {
            ModelAndView mv = new ModelAndView("common/failureMobile");
            mv.addObject("message", "OAuth获取openid失败");
            return mv;
        }
    }
    @RequestMapping(value = "/tixian")
    public ModelAndView tixian(HttpServletRequest request, @RequestParam("openId") String openId) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        if (mpAccount != null) {
            ModelAndView mv = new ModelAndView("wxweb/tixian");
            AccountFans fans = accountFansService.getByOpenId(openId);//同时更新数据库
            mv.addObject("openid", openId);
            mv.addObject("fans", fans);
            mv.addObject("account", mpAccount);
            return mv;
        } else {
            ModelAndView mv = new ModelAndView("common/failureMobile");
            mv.addObject("message", "OAuth获取openid失败");
            return mv;
        }
    }

    @RequestMapping(value = "/tixian_top")
    public ModelAndView tixianTop(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("wxweb/top_tixian");
        List<AccountFans> listAccountFans = accountFansService.listByUserMoneyTixian(null);
        mv.addObject("listAccountFans", listAccountFans);
        return mv;
    }

    @RequestMapping(value = "/referer")
    public ModelAndView referer(HttpServletRequest request, @RequestParam("id") String id) {
        ModelAndView mv = new ModelAndView("wxweb/referer");
        AccountFans accountFans = accountFansService.getById(id);
        String webRootPath = request.getServletContext().getRealPath("/");
        String headImg = webRootPath + "/res/upload/" + accountFans.getOpenId() + ".jpg";
        if (!hasCreateRecommendPic(headImg + ".text.jpg")) {
            //创建图片
            //创建图片
            if(null!=accountFans.getRecommendImgBlob()){
                try {
                    //写图片到磁盘
                    ImageByteUtils.byte2image(accountFans.getRecommendImgBlob(),headImg+ ".text.jpg");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mv.addObject("fans", accountFans);
            }
            else{
                mv.addObject("msg", "暂无推广图，请点击我的海报生成推广图片！");
            }
        } else {
            mv.addObject("fans", accountFans);
        }
        return mv;
    }

    @RequestMapping(value = "/referer_detail")
    public ModelAndView refererDetail(HttpServletRequest request, @RequestParam("id") long id) {
        ModelAndView mv = new ModelAndView("wxweb/referer_detail");
        Flow flow = new Flow();
        flow.setFansId(id);
        List<Flow> list = flowService.listForPage(flow);
        mv.addObject("listFlow", list);
        AccountFans fans = accountFansService.getById(id+"");//同时更新数据库
        mv.addObject("openid", fans.getOpenId());
        mv.addObject("fans", fans);
        return mv;
    }


    @RequestMapping(value = "/change_password")
    public ModelAndView changePassword(HttpServletRequest request, @RequestParam("openId") String openId) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        if (mpAccount != null) {
            ModelAndView mv = new ModelAndView("wxweb/change_password");
            AccountFans fans = accountFansService.getByOpenId(openId);//同时更新数据库
            mv.addObject("openid", openId);
            mv.addObject("fans", fans);
            return mv;
        } else {
            ModelAndView mv = new ModelAndView("common/failureMobile");
            mv.addObject("message", "OAuth获取openid失败");
            return mv;
        }
    }

    @RequestMapping(value = "/change_password_json", method = RequestMethod.POST)
    public
    @ResponseBody
    String changePasswordJson(ModelMap map, @RequestParam("openId") String openId
            , @RequestParam(value = "oldPwd", defaultValue = "") String oldPwd,
                              @RequestParam("newPwd") String newPwd) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", false);
        jsonObject.put("msg", "修改失败");
        AccountFans fans = accountFansService.getByOpenId(openId);
        if (null != fans) {
            if (fans.getUserMoneyPassword() == null || "".equals(fans.getUserMoneyPassword())) {
                accountFansService.updateUserMoneyPassword(openId, newPwd);
            } else {
                if (fans.getUserMoneyPassword().equals(oldPwd)) {
                    accountFansService.updateUserMoneyPassword(openId, newPwd);
                } else {
                    jsonObject.put("msg", "旧密码不正确");
                    return jsonObject.toString();
                }
            }
            jsonObject.put("result", true);
            jsonObject.put("msg", "修改成功");
        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/tixian_json", method = RequestMethod.POST)
    public
    @ResponseBody
    String tixianJson(ModelMap map, @RequestParam("openId") String openId,
                      @RequestParam("money") double money,
                      @RequestParam("userMoneyPassword") String userMoneyPassword,
                      @ModelAttribute("fansTixian") FansTixian fansTixian) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", false);
        jsonObject.put("msg", "提现失败");
        AccountFans fans = accountFansService.getByOpenId(openId);
        if (null != fans) {
            MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
            if (null != mpAccount) {
                if (mpAccount.getTixianMinMoney() > money) {
                    jsonObject.put("msg", "最少提现" + mpAccount.getTixianMinMoney() + "元");
                    return jsonObject.toString();
                }
            }
            if (money <= fans.getUserMoney() && (userMoneyPassword.equals(fans.getUserMoneyPassword()))) {
                fansTixian.setCreatetime(new Date());
                fansTixian.setFansId(fans.getId());
                fansTixian.setTixianStatus(0);
                fansTixian.setTixianMoney(money);
                accountFansService.updateUserMoney(money, openId, fansTixian);
                //提交提现之后用户金额要减少
                Flow flow = new Flow();
                flow.setCreatetime(new Date());
                flow.setFromFansId(fans.getId());
                flow.setFansId(fans.getId());
                String log = "成功提交提现" + money + "元，等待收款";
                try {
                    flow.setUserFlowLogBinary(log.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                flow.setUserFlowMoney(0 - money);//成功提交，用户金额减少
                flow.setFlowType(4);
                flowService.add(flow);
                jsonObject.put("msg", "提现提交成功，等待收款");
            } else {
                jsonObject.put("msg", "提现密码不正确或提现金额不足");
            }
        }
        return jsonObject.toString();
    }

    /**
     * 生成二维码
     *
     * @param request
     * @param num     二维码参数
     * @return
     */
    @RequestMapping(value = "/createQrcode", method = RequestMethod.POST)
    public ModelAndView createQrcode(HttpServletRequest request, Integer num) {
        ModelAndView mv = new ModelAndView("wxcms/qrcode");
        mv.addObject("cur_nav", "qrcode");
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        if (num != null) {
            byte[] qrcode = WxApiClient.createQRCode(60, num, mpAccount);//有效期60s
            String url = UploadUtil.byteToImg(request.getServletContext().getRealPath("/"), qrcode);
            mv.addObject("qrcode", url);
        }
        mv.addObject("num", num);
        return mv;
    }

    //以根据openid群发文本消息为例
    @RequestMapping(value = "/massSendTextMsg", method = RequestMethod.POST)
    public void massSendTextMsg(HttpServletResponse response, String openid, String content) {
        content = "群发文本消息";
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        String rstMsg = "根据openid群发文本消息失败";
        if (mpAccount != null && !StringUtils.isBlank(openid)) {
            List<String> openidList = new ArrayList<String>();
            openidList.add(openid);
            //根据openid群发文本消息
            JSONObject result = WxApiClient.massSendTextByOpenIds(openidList, content, mpAccount);

            try {
                if (result.getInt("errcode") != 0) {
                    response.getWriter().write("send failure");
                } else {
                    response.getWriter().write("send success");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ModelAndView mv = new ModelAndView("common/failure");
        mv.addObject("failureMsg", rstMsg);
    }

    /**
     * 发送客服消息
     *
     * @param request  ： 粉丝的openid
     * @param response ： 消息内容
     * @return
     */
    @RequestMapping(value = "/sendCustomTextMsg", method = RequestMethod.POST)
    public void sendCustomTextMsg(HttpServletRequest request, HttpServletResponse response, String openid) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        String content = "微信派官方测试客服消息";
        JSONObject result = WxApiClient.sendCustomTextMessage(openid, content, mpAccount);
        try {
            if (result.getInt("errcode") != 0) {
                response.getWriter().write("send failure");
            } else {
                response.getWriter().write("send success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送模板消息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/sendTemplateMessage", method = RequestMethod.POST)
    public void sendTemplateMessage(HttpServletRequest request, HttpServletResponse response, String openid) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        TemplateMessage tplMsg = new TemplateMessage();

        tplMsg.setOpenid(openid);
        //微信公众号号的template id，开发者自行处理参数
        tplMsg.setTemplateId("Wyme6_kKUqv4iq7P4d2NVldw3YxZIql4sL2q8CUES_Y");

        tplMsg.setUrl("http://www.weixinpy.com");
        Map<String, String> dataMap = new HashMap<String, String>();
        dataMap.put("first", "微信派官方微信模板消息测试");
        dataMap.put("keyword1", "时间：" + DateUtil.COMMON.getDateText(new Date()));
        dataMap.put("keyword2", "关键字二：你好");
        dataMap.put("remark", "备注：感谢您的来访");
        tplMsg.setDataMap(dataMap);

        JSONObject result = WxApiClient.sendTemplateMessage(tplMsg, mpAccount);
        try {
            if (result.getInt("errcode") != 0) {
                response.getWriter().write("send failure");
            } else {
                response.getWriter().write("send success");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取js ticket
     *
     * @param request
     * @param url
     * @return
     */
    @RequestMapping(value = "/jsTicket")
    @ResponseBody
    public String jsTicket(HttpServletRequest request, String url) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        String jsTicket = WxApiClient.getJSTicket(mpAccount);
        WxSign sign = new WxSign(mpAccount.getAppid(), jsTicket, url);

		/*System.out.println("jsTicket = " + jsTicket);
		System.out.println("appId = " + sign.getAppId());
		System.out.println("nonceStr = "+sign.getNonceStr());
		System.out.println("timestamp = " + sign.getTimestamp());
		System.out.println("url = " + url);
		System.out.println("signature = " + sign.getSignature());*/

        JsonView jv = new JsonView();
        jv.setData(sign);

        return jv.toString();
    }
    @RequestMapping(value = "/getImageByAccountFansId/{id}.jpg.text.jpg.html", method = RequestMethod.GET)
    public void getImageByAccountFansId(HttpServletRequest request, HttpServletResponse response,@PathVariable  String id) {
        AccountFans accountFans= accountFansService.getById(id);
        String webRootPath = request.getServletContext().getRealPath("/");
        String headImg = webRootPath + "/res/upload/" + accountFans.getOpenId() + ".jpg";
        if (!hasCreateRecommendPic(headImg + ".text.jpg")) {
            //创建图片
            if(null!=accountFans.getRecommendImgBlob()){
                response.setContentType("image/gif");
                try {
                    OutputStream out = response.getOutputStream();
                    out.write(accountFans.getRecommendImgBlob());
                    out.flush();
                    //写图片到磁盘
                    ImageByteUtils.byte2image(accountFans.getRecommendImgBlob(),headImg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            FileInputStream fis = null;
            response.setContentType("image/gif");
            try {
                OutputStream out = response.getOutputStream();
                File file = new File(headImg + ".text.jpg");
                fis = new FileInputStream(file);
                byte[] b = new byte[fis.available()];
                fis.read(b);
                out.write(b);
                out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    //打开网页授权的网页
    @RequestMapping(value = "/my_referer")
    public ModelAndView myReferer(HttpServletRequest request,@RequestParam(value="referId", defaultValue="0")  String referId) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        ModelAndView mv = new ModelAndView("wxweb/my_referer");
        AccountFans fans = accountFansService.getById(referId);//同时更新数据库
        mv.addObject("fans", fans);
        mv.addObject("account",mpAccount.getAccount());
        mv.addObject("referId",referId);
        return mv;
    }
    @RequestMapping(value = "/my_yaoyiyao")
    public ModelAndView myYaoYiYao(HttpServletRequest request,@RequestParam(value = "openId", defaultValue = "") String openId,@RequestParam("id") long id) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        if (mpAccount != null) {
            ModelAndView mv = new ModelAndView("wxweb/my_yaoyiyao");
            AccountFans fans = accountFansService.getByOpenId(openId);//同时更新数据库
            if (null != fans && fans.getId()==id) {
                accountFansService.updateUserMoneyCheck(fans.getId());
            }
            mv.addObject("openId", openId);
            mv.addObject("fans", fans);
            mv.addObject("id", id);
            /*}else {
                mv = new ModelAndView("common/failureMobile");
                mv.addObject("message", "读取用户信息失败");
                return mv;
            }*/
            return mv;
        } else {
            ModelAndView mv = new ModelAndView("common/failureMobile");
            mv.addObject("message", "OAuth获取openid失败");
            return mv;
        }
    }
    @RequestMapping(value = "/my_yaoyiyao_json", method = RequestMethod.GET)
    public
    @ResponseBody
    String myYaoyiyaoJson(ModelMap map, @RequestParam("id") String id, @RequestParam(value = "openId", defaultValue = "") String openId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", "-1");
        jsonObject.put("msg", "系统错误");
        if(openId==null||"".equals(openId)){
            jsonObject.put("result", "-3");
            jsonObject.put("msg", "请先关注再摇一摇");
            jsonObject.put("lastChances", 3);
            return jsonObject.toString();
        }
        AccountFans fans = accountFansService.getById(id);
        if (null != fans) {
            if(fans.getUserGiveYaoyiyaoTimes()<fans.getUserYaoyiyaoTimesUesd())
            {
                jsonObject.put("result", "-2");
                jsonObject.put("msg", "摇奖次数不够");
                return jsonObject.toString();
            }
            double yaoyiyaoGoldCoin=accountFansService.updateUserYaoyiyao(fans);
            jsonObject.put("userYaoyiyaoGoldCoin", yaoyiyaoGoldCoin);
            if(yaoyiyaoGoldCoin<=0){
                jsonObject.put("result", "1");
                jsonObject.put("msg", "未中奖");
                jsonObject.put("lastChances", fans.getUserGiveYaoyiyaoTimes()-fans.getUserYaoyiyaoTimesUesd()+1);
                return jsonObject.toString();
            }
            jsonObject.put("result", "2");
            jsonObject.put("msg", "中奖了");
            jsonObject.put("lastChances", fans.getUserGiveYaoyiyaoTimes() - fans.getUserYaoyiyaoTimesUesd() + 1);
        }
        return jsonObject.toString();
    }
    @RequestMapping(value = "/charge_gold_to_money")
    public ModelAndView chargeGoldToMoney(HttpServletRequest request, @RequestParam("openId") String openId) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        if (mpAccount != null) {
            ModelAndView mv = new ModelAndView("wxweb/charge_gold_to_money");
            AccountFans fans = accountFansService.getByOpenId(openId);//同时更新数据库
            mv.addObject("openid", openId);
            mv.addObject("fans", fans);
            return mv;
        } else {
            ModelAndView mv = new ModelAndView("common/failureMobile");
            mv.addObject("message", "OAuth获取openid失败");
            return mv;
        }
    }
    @RequestMapping(value = "/charge_gold_to_money_json", method = RequestMethod.POST)
    public
    @ResponseBody
    String chargeGoldToMoneyJson(ModelMap map, @RequestParam("openId") String openId, @RequestParam("userGoldCoin") double userGoldCoin, @RequestParam("userMoneyPassword") String userMoneyPassword) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", false);
        jsonObject.put("msg", "系统错误");
        AccountFans fans = accountFansService.getByOpenId(openId);
        if (null != fans) {
            if(!userMoneyPassword.equals(fans.getUserMoneyPassword())){
                jsonObject.put("msg", "提现密码不正确");
                return jsonObject.toString();
            }
            if(userGoldCoin>fans.getUserGoldCoin()){
                jsonObject.put("msg", "金币不足兑换");
                return jsonObject.toString();
            }
            accountFansService.updateUserGoldCoinToMoney(fans);
            jsonObject.put("result", true);
            jsonObject.put("msg", "金币兑换成功");
        }
        return jsonObject.toString();
    }
    @RequestMapping(value = "/my_input_refer_url")
    public ModelAndView myInputReferUrl(HttpServletRequest request, @RequestParam("openId") String openId) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        if (mpAccount != null) {
            ModelAndView mv = new ModelAndView("wxweb/my_input_refer_url");
            AccountFans fans = accountFansService.getByOpenId(openId);//同时更新数据库
            mv.addObject("openid", openId);
            mv.addObject("fans", fans);
            return mv;
        } else {
            ModelAndView mv = new ModelAndView("common/failureMobile");
            mv.addObject("message", "OAuth获取openid失败");
            return mv;
        }
    }
    @RequestMapping(value = "/my_input_refer_url_json", method = RequestMethod.POST)
    @ResponseBody
    String myInputReferUrlJson(ModelMap map,  @RequestParam("awardCode") long referId, @RequestParam("openId") String openId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", false);
        String content="系统错误";
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        AccountFans accountFans = accountFansService.getByOpenId(openId);
        if (null != accountFans) {
            if(referId>0){
                if (accountFans.getUserReferId()!=0){
                    content="已经领取了奖励，请生成海报转发推广获取更多奖励";
                }else{
                    accountFansService.updateUserReferId(openId,referId);
                    accountFans.setUserReferId(referId);
                    Random random = new Random();
                    double money = mpAccount.getInitSendMoneyMin() + (mpAccount.getInitSendMoneyMax() - mpAccount.getInitSendMoneyMin()) * random.nextDouble();
                    accountFansService.updateUserAddMoney(accountFans, money, accountFans.getUserReferId(), mpAccount, 0);
                    content="领取了#{money}元奖励，请生成海报转发推广获取更多奖励";
                    content = content.replace("#{money}", String.format("%.2f", money));
                    Flow flow = new Flow();
                    flow.setCreatetime(new Date());
                    flow.setUserFlowMoney(money);
                    flow.setFansId(accountFans.getId());
                    flow.setFlowType(2);//二次关注获取的红包。
                    flow.setFromFansId(0);
                    try {
                        flow.setUserFlowLogBinary(content.getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    flowService.add(flow);
                }
                jsonObject.put("result", true);
            }
        }
        jsonObject.put("msg", content);
        return jsonObject.toString();
    }



    @RequestMapping(value = "/my_referer_jump")
    public String myRefererJump(HttpServletRequest request) {
        return "redirect:weixin://weixin.qq.com/r/WEjTy_zEqbHWraio9x1K";
    }
    private boolean hasCreateRecommendPic(String recommendPicPath) {
        return new File(recommendPicPath).exists();
    }
}




