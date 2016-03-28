package com.wxcms.ctrl;

import com.core.page.Pagination;
import com.wxcms.domain.VideoTask;
import com.wxcms.service.VideoTaskService;
import com.wxcms.service.WebConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/video")
public class VideoTaskCtrl {
    @Autowired
    private VideoTaskService videoTaskService;
    @Autowired
    private WebConfigService webConfigService;



    @RequestMapping(value = "/detail/{fansId}/{videoId}")
    public ModelAndView videoDetail(HttpServletRequest request,@PathVariable("fansId") String fansId,
                                       @PathVariable("videoId") long videoId){
        ModelAndView mv = new ModelAndView("h5/videoDetail");
        mv.addObject("userId", fansId);
        mv.addObject("videoId", videoId);
        VideoTask videoTask= videoTaskService.getById(videoId);
        mv.addObject("videoTask", videoTask);
        VideoTask searchEntity=new VideoTask();
        Pagination<VideoTask> pagination=new Pagination<VideoTask>();
        pagination=videoTaskService.paginationEntity(searchEntity,pagination);
        mv.addObject("pagination", pagination);

        //获取广告信息
        String webConfigVal= webConfigService.getWebConfigByKey("top_ads");
        mv.addObject("top_ads", webConfigVal);
        String wx_ads= webConfigService.getWebConfigByKey("wx_ads");
        mv.addObject("wx_ads", wx_ads);
        String mid_ads= webConfigService.getWebConfigByKey("mid_ads");
        mv.addObject("mid_ads", mid_ads);
        String bottom_ads= webConfigService.getWebConfigByKey("bottom_ads");
        mv.addObject("bottom_ads", bottom_ads);

        return mv;
    }
    @RequestMapping(value = "/list/{fansId}")
    public ModelAndView videoList(HttpServletRequest request,@PathVariable("fansId") String fansId, Pagination<VideoTask> pagination){
        ModelAndView mv = new ModelAndView("h5/list_article");
        mv.addObject("userId", fansId);
        VideoTask videoTask=new VideoTask();
        if(null==pagination)
            pagination=new Pagination<VideoTask>();
        pagination= videoTaskService.paginationEntity(videoTask,pagination);
        mv.addObject("pagination", pagination);
        return mv;
    }


    @RequestMapping(value = "/list_json/{userId}", method = RequestMethod.GET)
    public
    @ResponseBody
    String getArticleList(@PathVariable("userId") long userId,@RequestParam("page") int page){
        String retHtml="";
        String templete="<li><a href='/video/detail/${userId}/${row.id}.html'>"+
                "<dd><img width='100' height='100'"+
                " src='${row.thumbnailUrl}'/></dd>"+
                "${row.captionStr}"+
                " <dt>"+
                " <div class='ab1'><span>${row.createtime}</span>"+
                "&nbsp;&nbsp;<span>阅读 ${row.shareTimes}</span></div>"+
                " </dt></a>"+
                " </li>";
        Pagination<VideoTask>   pagination=new Pagination<VideoTask>();
        pagination.setPageNum(page);
        VideoTask videoTask=new VideoTask();
        pagination= videoTaskService.paginationEntity(videoTask,pagination);
        if (null!=pagination){
            for (int i=0;i<pagination.getItems().size();i++){
                String val=templete.replace("${row.captionStr}",pagination.getItems().get(i).getCaptionStr());
                val=val.replace("${row.id}",pagination.getItems().get(i).getId().toString());
                val=val.replace("${row.thumbnailUrl}",pagination.getItems().get(i).getThumbnailUrl().toString());
                val=val.replace("${row.createtime}",pagination.getItems().get(i).getCreatetime().toString());
                val=val.replace("${row.shareTimes}",pagination.getItems().get(i).getShareTimes()+"");
                retHtml+=val;
            }
        }
        return retHtml;
    }
}
