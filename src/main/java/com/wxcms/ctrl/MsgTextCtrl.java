package com.wxcms.ctrl;

import com.core.page.Pagination;
import com.wxcms.domain.*;
import com.wxcms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 
 */

@Controller
@RequestMapping("/msgtext")
public class MsgTextCtrl{

	@Autowired
	private MsgTextService entityService;
	
	@Autowired
	private MsgBaseService baseService;
	@Autowired
	private FansTixianSrevice fansTixianSrevice;
	@Autowired
	private FlowService flowService;
	@Autowired
	private AccountFansService accountFansService;


	@RequestMapping(value = "/getById")
	public ModelAndView getById(String id){
		entityService.getById(id);
		return new ModelAndView();
	}

	@RequestMapping(value = "/list")
	public  ModelAndView list(@ModelAttribute MsgText searchEntity){
		ModelAndView modelAndView = new ModelAndView("wxcms/msgtextList");
		List<MsgText> pageList = entityService.listForPage(searchEntity);
		modelAndView.addObject("pageList", pageList);
		modelAndView.addObject("cur_nav", "text");
		return modelAndView;
	}

	/*public  ModelAndView tixianList(@ModelAttribute MsgText searchEntity){
		ModelAndView modelAndView = new ModelAndView("wxweb/admin_tixian_list");
		FansTixian fansTixian=new FansTixian();
		List<FansTixian> pageList = fansTixianSrevice.listForPage(fansTixian);
		modelAndView.addObject("pageList", pageList);
		modelAndView.addObject("cur_nav", "tixian");
		return modelAndView;
	}*/
	@RequestMapping(value = "/tixian_list")
	public  ModelAndView paginationEntity(FansTixian searchEntity, Pagination<FansTixian> pagination){
		ModelAndView mv = new ModelAndView("wxweb/admin_tixian_list");
		pagination = fansTixianSrevice.paginationEntity(searchEntity,pagination);
		mv.addObject("pagination",pagination);
		mv.addObject("cur_nav","tixian_list");
		return mv;
	}
	@RequestMapping(value = "/admin_flow_list")
	public  ModelAndView flowList(@RequestParam("fansId") long fansId){
		ModelAndView modelAndView = new ModelAndView("wxweb/admin_flow_list");
		//获取粉丝情况
		AccountFans accountFans=accountFansService.getById(fansId + "");
		modelAndView.addObject("accountFans", accountFans);

		Flow flow=new Flow();
		flow.setFansId(fansId);
		List<Flow> pageList=flowService.listForPage(flow);
		modelAndView.addObject("pageList", pageList);
		modelAndView.addObject("cur_nav", "tixian_list");
		return modelAndView;
	}

	@RequestMapping(value = "/admin_edit_tixian")
	public ModelAndView adminEditTixian(String id){
		ModelAndView mv = new ModelAndView("wxcms/admin_edit_tixian");
		mv.addObject("cur_nav", "tixian_list");
		FansTixian fansTixian=fansTixianSrevice.getById(id);
		if(fansTixian != null){
			mv.addObject("entity",fansTixian);
		}
		return mv;
	}
	@RequestMapping(value = "/admin_edit_tixian_merge")
	public ModelAndView adminEditTixianToMerge(FansTixian entity){
		if(entity.getId() != null){
			FansTixian entityOld=fansTixianSrevice.getById(entity.getId()+"");
			entityOld.setTixianStatus(entity.getTixianStatus());
			fansTixianSrevice.update(entityOld);
		}
		return new ModelAndView("redirect:/msgtext/tixian_list.html");
	}

	@RequestMapping(value = "/toMerge")
	public ModelAndView toMerge(MsgText entity){
		ModelAndView mv = new ModelAndView("wxcms/msgtextMerge");
		mv.addObject("cur_nav", "text");
		if(entity.getId() != null){
			MsgText text = entityService.getById(entity.getId().toString());
			mv.addObject("entity",text);
			mv.addObject("baseEntity", baseService.getById(text.getBaseId().toString()));
		}else{
			mv.addObject("entity",new MsgText());
			mv.addObject("baseEntity", new MsgBase());
		}
		return mv;
	}
	
	@RequestMapping(value = "/doMerge")
	public ModelAndView doMerge(MsgText entity){
		if(entity.getId() != null){
			entityService.update(entity);
		}else{
			entityService.add(entity);
		}
		return new ModelAndView("redirect:/msgtext/list.html");
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete(MsgText entity){
		entityService.delete(entity);
		return new ModelAndView("redirect:/msgtext/list.html");
	}



}

