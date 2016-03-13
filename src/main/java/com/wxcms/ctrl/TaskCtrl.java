package com.wxcms.ctrl;

import com.core.page.Pagination;
import com.core.util.AESUtil;
import com.core.util.Str2MD5;
import com.wxapi.process.MpAccount;
import com.wxapi.process.WxMemoryCacheClient;
import com.wxcms.domain.TaskCode;
import com.wxcms.domain.TaskLog;
import com.wxcms.domain.UserInfo;
import com.wxcms.service.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016-02-18.
 */
@Controller
@RequestMapping("/task")
public class TaskCtrl {
    @Autowired
    private AccountFansService accountFansService;
    @Autowired
    private TaskCodeService taskCodeService;
    @Autowired
    private TaskLogService taskLogService;
    @Autowired
    private AccountService accountService;


    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping(value = "/list_code_task")
    public ModelAndView listCodeTask(HttpServletRequest request,ModelMap map,TaskLog searchEntity,Pagination<TaskCode> pagination,
                                     String openId){
        ModelAndView mv = new ModelAndView("h5/list_code_task");
        if(null==searchEntity)searchEntity=new TaskLog();
        mv.addObject("openId", openId);
        //AccountFans accountFans= accountFansService.getByOpenId(openId);
        searchEntity.setOpenId(openId);
        pagination=taskCodeService.paginationEntityNotGet(searchEntity, pagination);
        mv.addObject("pagination", pagination);

        //待执行任务只取第一页10条即可
        searchEntity.setTaskStatus(0);//待执行任务
        Pagination<TaskLog> paginationTaskLog=new Pagination<TaskLog>();
        paginationTaskLog=taskLogService.paginationEntityByOpenIdAndTaskStatus(searchEntity, paginationTaskLog);
        mv.addObject("paginationTaskLog", paginationTaskLog);

        searchEntity.setTaskStatus(1);//已经完成的任务
        Pagination<TaskLog> paginationFinishedTaskLog=new Pagination<TaskLog>();
        paginationFinishedTaskLog=taskLogService.paginationEntityByOpenIdAndTaskStatus(searchEntity, paginationFinishedTaskLog);
        mv.addObject("paginationFinishedTaskLog", paginationFinishedTaskLog);

        //获取URL
        String webUrl = "http://" + request.getServerName().toString() + ((request.getLocalPort() == 80) ? "" : (":" + request.getLocalPort()));
        mv.addObject("webUrl", webUrl);
        //获取参数，分成比例
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        double taskProfit=mpAccount.getTaskProfit();
        mv.addObject("taskProfit",(1- taskProfit/100));
        return mv;
    }
    //菜单栏任务大厅
    @RequestMapping(value = "/my_task_list")
    public ModelAndView myCenter(HttpServletRequest request) {
        MpAccount mpAccount = WxMemoryCacheClient.getSingleMpAccount();//获取缓存中的唯一账号
        if (mpAccount != null) {
            //拦截器已经处理了缓存,这里直接取
            String openId = WxMemoryCacheClient.getOpenid(request.getSession().getId());
            if(null!=openId){
                return new ModelAndView("redirect:/task/list_code_task.html?openId="+openId);
            }
        }
        ModelAndView mv = new ModelAndView("common/failureMobile");
        mv.addObject("failureMsg", "OAuth获取openid失败");
        return mv;
    }

    @RequestMapping(value = "/my_list_code_task")
    public ModelAndView myListCodeTask(HttpServletRequest request,ModelMap map,TaskLog searchEntity,Pagination<TaskLog> pagination,
                                     String openId){
        ModelAndView mv = new ModelAndView("wxweb/list_code_task");
        if(null==searchEntity)searchEntity=new TaskLog();
        mv.addObject("openId",openId);
        searchEntity.setOpenId(openId);
        searchEntity.setTaskStatus(0);//待执行任务
        pagination=taskLogService.paginationEntityByOpenIdAndTaskStatus(searchEntity, pagination);
        mv.addObject("pagination", pagination);
        //获取URL
        String webUrl = "http://" + request.getServerName().toString() + ((request.getLocalPort() == 80) ? "" : (":" + request.getLocalPort()));
        mv.addObject("webUrl", webUrl);
        return mv;
    }

    /***
     * 验证用户关注成功与否
     * @param request
     * @return
     */
    @RequestMapping(value = "/validate/{userId}/{content}")
    public ModelAndView validateQrCode(HttpServletRequest request,@PathVariable("userId") String userId,@PathVariable("content") String content){
        ModelAndView mv = new ModelAndView("wxweb/validate");
        mv.addObject("userId", userId);
        mv.addObject("content", content);
        return mv;
    }

    /***
     *
     * @param request
     * @param map
     * @param userCode 领取人编码
     * @param content //领取二维码验证内容
     * @param userId //用户编号
     * @return
     */
    @RequestMapping(value = "validate_json")
    public @ResponseBody
    String validateJson(HttpServletRequest request,ModelMap map,
                        @ModelAttribute("userCode") String userCode,//福利码8位
                        @ModelAttribute("content") String content,
                        @ModelAttribute("userId") long userId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", false);
        jsonObject.put("msg", "领取失败");
        try {
            UserInfo userInfo=userInfoService.getById(userId);//获取任务发布人信息
            if (null==userInfo){
                jsonObject.put("msg", "领取失败，任务不存在");
                return jsonObject.toString();
            }
            String password = Str2MD5.MD5(userId+"", 16);
            String wxCodeImgHref = AESUtil.decryptStr(content, password);//获取到微信账号信息
            TaskCode taskCode= taskCodeService.getByWxCode(wxCodeImgHref);
            //根据任务ID，福利码，获取到领福利的用户
            TaskLog taskLog=  taskLogService.getByTaskIdAndTaskCodeNum(taskCode.getId(), userCode);
            if(null!=taskLog){
                //扣钱，加钱，修改状态
                userInfoService.updateUserMoneyByTask(taskCode.getId(), userId, taskLog);
                jsonObject.put("result", true);
                jsonObject.put("openId",taskLog.getOpenId());
                jsonObject.put("msg", "领取成功!");
            }else {
                jsonObject.put("msg", "任务已经完成或福利码错了!");
            }
        }catch (Exception ex){

        }
        return jsonObject.toString();
    }
}
