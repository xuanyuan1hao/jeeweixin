package com.wxcms.ctrl;

import com.core.page.Pagination;
import com.wxcms.domain.AccountFans;
import com.wxcms.domain.TaskCode;
import com.wxcms.domain.TaskLog;
import com.wxcms.service.AccountFansService;
import com.wxcms.service.TaskCodeService;
import com.wxcms.service.TaskLogService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Random;

/**
 * 
 */

@Controller
@RequestMapping("/accountfans")
public class AccountFansCtrl{

	@Autowired
	private AccountFansService entityService;
	@Autowired
	private TaskCodeService taskCodeService;
	@Autowired
	private TaskLogService taskLogService;

	@RequestMapping(value = "/getById")
	public ModelAndView getById(String id){
		entityService.getById(id);
		return new ModelAndView();
	}

	@RequestMapping(value = "/list")
	public  ModelAndView list(AccountFans searchEntity){
		ModelAndView mv = new ModelAndView("wxcms/paginationEntity");
		return mv;
	}

	@RequestMapping(value = "/paginationEntity")
	public  ModelAndView paginationEntity(AccountFans searchEntity, Pagination<AccountFans> pagination){
		ModelAndView mv = new ModelAndView("wxcms/fansPagination");
		pagination = entityService.paginationEntity(searchEntity,pagination);
		mv.addObject("pagination",pagination);
		mv.addObject("cur_nav","fans");
		return mv;
	}
	
	@RequestMapping(value = "/toMerge")
	public ModelAndView toMerge(AccountFans entity){

		return new ModelAndView();
	}

	@RequestMapping(value = "/merge")
	public ModelAndView doMerge(AccountFans entity){
		entityService.add(entity);
		return new ModelAndView();
	}

	@RequestMapping(value = "/delete")
	public ModelAndView delete(AccountFans entity){
		entityService.delete(entity);
		return new ModelAndView();
	}

	@RequestMapping(value = "/accept_task_json", method = RequestMethod.GET)
	public @ResponseBody String acceptTaskJson(ModelMap map, @RequestParam("openId") String openId) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("result", false);
		jsonObject.put("msg", "接任务失败");
		AccountFans fans = entityService.getByOpenId(openId);
		if (null != fans) {
			TaskLog searchEntity=new TaskLog();
			searchEntity.setOpenId(openId);
			Pagination<TaskCode> pagination=new Pagination<TaskCode>() ;
			Pagination<TaskCode> taskCodes=taskCodeService.paginationEntityNotGet(searchEntity, pagination);
			if(null!=taskCodes&&taskCodes.getItems()!=null&&taskCodes.getItems().size()>0){
				for (int i=0;i<taskCodes.getItems().size();i++){
					TaskCode taskCode=taskCodes.getItems().get(i);
					if(null!=taskCode){
						TaskLog taskLog=new TaskLog();
						taskLog.setOpenId(openId);
						taskLog.setCreatetime(new Date());
						taskLog.setMoney(taskCode.getMoneyPer());
						taskLog.setTaskId(taskCode.getId());
						taskLog.setTaskStatus(0);//接收任务成功，等待处理
						taskLog.setTaskCodeNum(getRandomNum(8));
						String log="接收任务成功";
						try {
							taskLog.setLog(log.getBytes("UTF-8"));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						taskLogService.add(taskLog);
					}
				}
				jsonObject.put("result", true);
				jsonObject.put("msg", "接任务成功");
			}
		}else{
			jsonObject.put("result", false);
			jsonObject.put("msg", "没有任务可接了，请执行已接任务！");
		}
		return jsonObject.toString();
	}

	private String getRandomNum(int length) {
		//获取6位验证码数字
		while(true){
			int max=(int)Math.pow(10, length);
			Random random=new Random();
			String ret= String.valueOf(random.nextInt(max));
			if(taskLogService.getByCode(ret)==null)
				return ret;
		}
	}

}