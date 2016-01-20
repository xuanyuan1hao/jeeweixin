package com.wxcms.ctrl;

import com.wxapi.service.impl.MyServiceImpl;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

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
	@RequestMapping(value = "/login_json", method = RequestMethod.POST)
	public @ResponseBody
	String loginJson(ModelMap map,@RequestParam("pwd") String pwd,HttpSession httpSession){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", false);
		jsonObject.put("msg", "登录失败");
		if("a@123456".equals(pwd)){
			httpSession.setAttribute("pwd",pwd);
			jsonObject.put("result", true);
			jsonObject.put("msg", "登录成功");
		}
		return  jsonObject.toString();
	}

}
