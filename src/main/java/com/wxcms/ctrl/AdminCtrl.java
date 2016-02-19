package com.wxcms.ctrl;

import com.core.page.Pagination;
import com.wxcms.domain.UserFlow;
import com.wxcms.domain.UserInfo;
import com.wxcms.service.UserFlowService;
import com.wxcms.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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
    @RequestMapping(value = "/list_user_info")
    public ModelAndView listCodeTask(HttpServletRequest request,ModelMap map,UserInfo searchEntity,Pagination<UserInfo> pagination,
                                     String openId,String save){
        ModelAndView mv = new ModelAndView("admin/list_user_info");
        if(null==searchEntity)searchEntity=new UserInfo();
        pagination=userInfoService.paginationEntity(searchEntity, pagination);
        mv.addObject("pagination", pagination);
        mv.addObject("cur_nav", "list_user_info");
        if(save != null){
            mv.addObject("successflag",true);
        }
        return mv;
    }
    @RequestMapping(value = "/edit_user_info_input_money")
    public ModelAndView userInfoInputMoney(long id){
        ModelAndView mv = new ModelAndView("admin/edit_user_info_input_money");
        mv.addObject("cur_nav", "list_user_info");
        mv.addObject("id", id);
        UserInfo userInfo=userInfoService.getById(id);
        mv.addObject("userInfo", userInfo);
        return mv;
    }
    @RequestMapping(value = "/edit_user_info_input_money_post")
    public ModelAndView inputMoneyPost(HttpServletRequest request ,@ModelAttribute UserInfo userInfo){
        userInfoService.updateUserMoney(userInfo);
        return new ModelAndView("redirect:/admin/list_user_info.html?save=true");
    }


    @RequestMapping(value = "/edit_user_detail_money")
    public ModelAndView userDetailMoney(long id,Pagination<UserFlow> pagination){
        ModelAndView mv = new ModelAndView("admin/user_info_detail_money");
        mv.addObject("cur_nav", "list_user_info");
        mv.addObject("id", id);
        UserInfo userInfo=userInfoService.getById(id);
        mv.addObject("userInfo", userInfo);
        UserFlow userFlow=new UserFlow();
        userFlow.setUserId(id);
        pagination=  userFlowService.paginationEntity(userFlow,pagination);
        mv.addObject("pagination", pagination);
        return mv;
    }

}
