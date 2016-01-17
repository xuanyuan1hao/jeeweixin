package com.wxcms.ctrl;

import com.wxapi.service.impl.MyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping()
public class IndexController {

	@Autowired
	private MyServiceImpl myService;
	@RequestMapping(value = "/index")
	public ModelAndView index(){
		return new ModelAndView("index");
//		return new ModelAndView("redirect:/wxcms/urltoken.html");
	}
	@RequestMapping(value = "/top_tixian")
	public ModelAndView topTixian(){
		return new ModelAndView("top_tixian");
	}


}
