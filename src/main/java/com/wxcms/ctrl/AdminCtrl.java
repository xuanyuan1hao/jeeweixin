package com.wxcms.ctrl;

import com.core.page.Pagination;
import com.wxcms.domain.TaskCode;
import com.wxcms.domain.UserAccountFans;
import com.wxcms.domain.UserFlow;
import com.wxcms.domain.UserInfo;
import com.wxcms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-02-19.
 */
@Controller
@RequestMapping("/admin")
public class AdminCtrl {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserFlowService userFlowService;
    @Autowired
    private TaskCodeService taskCodeService;
    @Autowired
    private UserAccountFansService userAccountFansService;
    @Autowired
    private CustomTextMessageService customTextMessageService;

    @RequestMapping(value = "/list_user_info")
    public ModelAndView listCodeTask(HttpServletRequest request, ModelMap map, UserInfo searchEntity, Pagination<UserInfo> pagination,
                                     String openId, String save) {
        ModelAndView mv = new ModelAndView("admin/list_user_info");
        if (null == searchEntity) searchEntity = new UserInfo();
        pagination = userInfoService.paginationEntity(searchEntity, pagination);
        mv.addObject("pagination", pagination);
        mv.addObject("cur_nav", "list_user_info");
        if (save != null) {
            mv.addObject("successflag", true);
        }
        return mv;
    }

    @RequestMapping(value = "/edit_user_info_input_money")
    public ModelAndView userInfoInputMoney(long id) {
        ModelAndView mv = new ModelAndView("admin/edit_user_info_input_money");
        mv.addObject("cur_nav", "list_user_info");
        mv.addObject("id", id);
        UserInfo userInfo = userInfoService.getById(id);
        mv.addObject("userInfo", userInfo);
        return mv;
    }

    @RequestMapping(value = "/edit_user_info_input_money_post")
    public ModelAndView inputMoneyPost(HttpServletRequest request, @ModelAttribute UserInfo userInfo) {
        userInfoService.updateUserMoney(userInfo);
        return new ModelAndView("redirect:/admin/list_user_info.html?save=true");
    }


    @RequestMapping(value = "/edit_user_detail_money")
    public ModelAndView userDetailMoney(long id, Pagination<UserFlow> pagination) {
        ModelAndView mv = new ModelAndView("admin/user_info_detail_money");
        mv.addObject("cur_nav", "list_user_info");
        mv.addObject("id", id);
        UserInfo userInfo = userInfoService.getById(id);
        mv.addObject("userInfo", userInfo);
        UserFlow userFlow = new UserFlow();
        userFlow.setUserId(id);
        pagination = userFlowService.paginationEntity(userFlow, pagination);
        mv.addObject("pagination", pagination);
        return mv;
    }

    //显示参加大联盟的所有公众号
    @RequestMapping(value = "/manage_code_task")
    public ModelAndView manageCodeTask(TaskCode searchEntity, Pagination<TaskCode> pagination) {
        ModelAndView mv = new ModelAndView("admin/manage_code_task");
        if (null == searchEntity)
            searchEntity = new TaskCode();
        pagination = taskCodeService.paginationEntity(searchEntity, pagination);
        mv.addObject("pagination", pagination);
        mv.addObject("cur_nav", "manage_code_task");
        return mv;
    }
    @RequestMapping(value = "/send_msg_code_task")
    public ModelAndView sendCustomTextMsg(HttpServletRequest request,long id){
        ModelAndView mv = new ModelAndView("admin/send_msg_code_task");
        mv.addObject("cur_nav", "manage_code_task");
        TaskCode taskCode= taskCodeService.getById(id);
        mv.addObject("taskCode",taskCode);
        return mv;
    }

    @RequestMapping(value = "/sendCustomTextMsgByTaskCodeJson", method = RequestMethod.POST)
    public void sendCustomTextMsg(HttpServletRequest request, HttpServletResponse response,
                                  @RequestParam(value = "content",defaultValue = "") String content,
                                  @RequestParam(value = "id",defaultValue = "0") long taskId) {
        int count=0;
        if(!"".equals(content))
        {
            long maxId=0;
            Date nowTime=new Date();
            Date endTime=new Date((nowTime.getTime() - 2 * 24 * 60 * 60 * 1000));
            TaskCode taskCode= taskCodeService.getById(taskId);
            if(null!=taskCode)
            while (true){
                List<UserAccountFans> list= userAccountFansService.getAllByLastUpdateTimePage(endTime,maxId, taskCode.getAccount());
                count+=list.size();
                if (null!=list&&list.size()>0){
                    for (int i=0;i<list.size();i++){
                        UserAccountFans temp=list.get(i);
                        customTextMessageService.addByTaskCode(temp.getOpenId(), content, taskCode);
                        maxId=list.get(i).getId();
                    }
                }else
                {
                    break;
                }
            }
        }
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write("发送成功"+count+"条");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
