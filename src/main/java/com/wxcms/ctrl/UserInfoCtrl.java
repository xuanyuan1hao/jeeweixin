package com.wxcms.ctrl;

import com.core.page.Pagination;
import com.core.spring.SpringFreemarkerContextPathUtil;
import com.core.util.AESUtil;
import com.core.util.Str2MD5;
import com.wxcms.domain.TaskCode;
import com.wxcms.domain.UserFlow;
import com.wxcms.domain.UserInfo;
import com.wxcms.service.TaskCodeService;
import com.wxcms.service.UserFlowService;
import com.wxcms.service.UserInfoService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2016-02-07.
 */

@Controller
@RequestMapping("/user")
public class UserInfoCtrl {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TaskCodeService taskCodeService;
    @Autowired
    private UserFlowService userFlowService;

    @RequestMapping(value = "/reg")
    public ModelAndView reg(){
        ModelAndView mv = new ModelAndView("user/reg");
        return mv;
    }
    @RequestMapping(value = "/login")
    public ModelAndView login(){
        ModelAndView mv = new ModelAndView("user/login");
        return mv;
    }
    @RequestMapping(value = "/index")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("user/user_index");
        return mv;
    }

    @RequestMapping(value = "/userInfo")
    public ModelAndView userInfo(ModelMap map,HttpSession httpSession){
        ModelAndView mv = new ModelAndView("user/userInfo");
        UserInfo sessionUserInfo=(UserInfo)httpSession.getAttribute("userInfo");
        if(sessionUserInfo==null){
            mv.addObject("msg", "请先登录!");
        }else{
            UserInfo userInfo= userInfoService.getById(sessionUserInfo.getId());
            mv.addObject("userInfo", userInfo);
        }
        return mv;
    }
    @RequestMapping(value = "/userMoney")
    public ModelAndView userMoney(ModelMap map,HttpSession httpSession,Pagination<UserFlow> pagination){
        ModelAndView mv = new ModelAndView("user/userMoney");
        UserInfo sessionUserInfo=(UserInfo)httpSession.getAttribute("userInfo");
        if(sessionUserInfo==null){
            mv.addObject("msg", "请先登录!");
        }else{
            UserFlow userFlow=new UserFlow();
            userFlow.setUserId(sessionUserInfo.getId());
            pagination=  userFlowService.paginationEntity(userFlow,pagination);
            mv.addObject("pagination", pagination);
        }
        return mv;
    }

    @RequestMapping(value = "/add_code_task")
    public ModelAndView addCodeTask(){
        ModelAndView mv = new ModelAndView("user/add_code_task");
        return mv;
    }
    @RequestMapping(value = "/edit_code_task")
    public ModelAndView editCodeTask(TaskCode searchEntity,ModelMap map,HttpSession httpSession){
        ModelAndView mv = new ModelAndView("user/edit_code_task");
        if(null!=searchEntity){
            UserInfo sessionUserInfo=(UserInfo)httpSession.getAttribute("userInfo");
            TaskCode searchEntityDb=taskCodeService.getById(searchEntity.getId());
            if(sessionUserInfo.getId()!=searchEntityDb.getUserId()){
                map.put("msg","有意思么？大家赚点小钱别JB瞎搞了!");
            }else{
                map.put("taskCode",searchEntityDb);
            }
        }
        return mv;
    }
    @RequestMapping(value = "edit_code_task_json")
    public @ResponseBody String editCodeTaskJson(HttpServletRequest request,
                                                 ModelMap map,@ModelAttribute("taskCode") TaskCode taskCode,HttpSession httpSession) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", false);
        jsonObject.put("msg", "系统错误，修改失败");
        try {
            String path = SpringFreemarkerContextPathUtil.getBasePath(request);
            String url = request.getScheme() + "://" + request.getServerName() + path + "/user_wxapi/" + taskCode.getAccount()+"/message.html";
            taskCode.setUrl(url);
            if(null!=taskCode.getWxRemarkText())
                taskCode.setWxRemark(taskCode.getWxRemarkText().getBytes("UTF-8"));
            taskCode.setValidateMenuUrl(getValidateMenuUrl(taskCode.getWxCodeImgHref(),taskCode.getUserId()));
            taskCodeService.update(taskCode);
            jsonObject.put("result", true);
            jsonObject.put("msg", "修改成功");
        }catch (Exception ex){

        }
        return jsonObject.toString();
    }
    @RequestMapping(value = "delete_code_task_json")
    public @ResponseBody String deleteCodeTaskJson(HttpServletRequest request,
                                                 ModelMap map,long id,HttpSession httpSession) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", false);
        jsonObject.put("msg", "系统错误，修改失败");
        try {
            TaskCode taskCode=new TaskCode();
            taskCode.setId(id);
            taskCodeService.delete(taskCode);
            jsonObject.put("result", true);
            jsonObject.put("msg", "删除成功");
        }catch (Exception ex){

        }
        return jsonObject.toString();
    }

    @RequestMapping(value = "/manage_code_task")
    public ModelAndView manageCodeTask(HttpServletRequest request,ModelMap map,TaskCode searchEntity,Pagination<TaskCode> pagination,HttpSession httpSession){
        ModelAndView mv = new ModelAndView("user/manage_code_task");
        if(null==searchEntity)searchEntity=new TaskCode();
        UserInfo sessionUserInfo=(UserInfo)httpSession.getAttribute("userInfo");
        searchEntity.setUserId(sessionUserInfo.getId());
        pagination=taskCodeService.paginationEntity(searchEntity,pagination);
        mv.addObject("pagination", pagination);
        //获取URL
        String webUrl = "http://" + request.getServerName().toString() + ((request.getLocalPort() == 80) ? "" : (":" + request.getLocalPort()));
        mv.addObject("webUrl", webUrl);
        return mv;
    }


    @RequestMapping(value = "add_code_task_json")
    public @ResponseBody String addCodeTaskJson(HttpServletRequest request,ModelMap map,
                                                @ModelAttribute("taskCode") TaskCode taskCode,HttpSession httpSession) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", false);
        jsonObject.put("msg", "系统错误，新增失败");
        try {
            String webRootPath = request.getServletContext().getRealPath("/");
            String headImgUrl= "http://open.weixin.qq.com/qr/code/?username="+taskCode.getWxCodeImgHref();
            String path = SpringFreemarkerContextPathUtil.getBasePath(request);
            String url = request.getScheme() + "://" + request.getServerName() + path + "/user_wxapi/" + taskCode.getAccount()+"/message.html";
            taskCode.setUrl(url);
            taskCode.setToken(UUID.randomUUID().toString().replace("-", ""));

            UserInfo userInfo=(UserInfo)httpSession.getAttribute("userInfo");
            if(null!=userInfo){
                taskCode.setUserId(userInfo.getId());
            }
            taskCode.setCreatetime(new Date());
            if(null!=taskCode.getWxRemarkText())
                taskCode.setWxRemark(taskCode.getWxRemarkText().getBytes("UTF-8"));
            taskCode.setValidateMenuUrl(getValidateMenuUrl(taskCode.getWxCodeImgHref(),taskCode.getUserId()));
            taskCodeService.add(taskCode);
            jsonObject.put("result", true);
            jsonObject.put("msg", "新增成功");
        }catch (Exception ex){

        }
        return jsonObject.toString();
    }


    @RequestMapping(value = "login_json")
    public @ResponseBody String loginJson(ModelMap map,@ModelAttribute("userInfo") UserInfo userInfo,HttpSession httpSession) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", false);
        jsonObject.put("msg", "系统错误，登录失败");
        UserInfo userInfoDb=userInfoService.getByEmail(userInfo);
        if(userInfoDb!=null){
            if(userInfo.getUserPassword().equals(userInfoDb.getUserPassword())){
                jsonObject.put("msg", "登录成功");
                httpSession.setAttribute("userInfo",userInfoDb);
                jsonObject.put("result", true);
            }else{
                jsonObject.put("msg", "登录失败，密码错误");
            }
        }else{
            jsonObject.put("msg", "登录失败，请检查用户名");
        }
        return jsonObject.toString();
    }



    @RequestMapping(value = "reg_json")
    public @ResponseBody String changePasswordJson(ModelMap map,@ModelAttribute("userInfo") UserInfo userInfo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", false);
        jsonObject.put("msg", "系统错误，注册失败");
        if(userInfoService.getByEmail(userInfo)==null){
            userInfo.setCreatetime(new Date());
            userInfoService.add(userInfo);
            jsonObject.put("msg", "注册成功，请登录");
            jsonObject.put("result", true);
        }else{
            jsonObject.put("msg", "注册失败，邮箱已经存在");
        }
        return jsonObject.toString();
    }

    public String getValidateMenuUrl(String source,long userId) {
        String password = Str2MD5.MD5("" + userId, 16);
        try {
            String encryptData = AESUtil.encryptStr(source, password);
            return "/"+userId+"/"+encryptData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/"+userId+"/"+source;
    }
}
