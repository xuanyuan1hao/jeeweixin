package com.wxcms.task;

import com.wxapi.process.UserWxApiClient;
import com.wxcms.domain.*;
import com.wxcms.service.ArticleService;
import com.wxcms.service.TaskCodeService;
import com.wxcms.service.UserNewsTaskArticleService;
import com.wxcms.service.UserNewsTaskService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class MyPublicArticleTask {
    @Autowired
    private UserNewsTaskService userNewsTaskService;
    @Autowired
    private UserNewsTaskArticleService userNewsTaskArticleService;
@Autowired
private ArticleService articleService;
    @Autowired
    private TaskCodeService taskCodeService;

    @Scheduled(cron = "0/60 * * * * ? ") //间隔50秒执行
    public void publishArticleTaskCycle() {
        UserNewsTask userNewsTask=new UserNewsTask();
        userNewsTask.setTaskRunStatus(0);
        userNewsTask.setTaskRunTime(new Date());
        List<UserNewsTask> listUserNewsTask= userNewsTaskService.listForPage(userNewsTask);
        if(null!=listUserNewsTask)
        for (int i=0;i<listUserNewsTask.size();i++){
            UserNewsTask  tmp=listUserNewsTask.get(i);
            TaskCode taskCode= taskCodeService.getById(tmp.getWxId());
            if(tmp.getMediaId()!=null){
                //进行发送操作
                JSONObject rstObjMassSendall = UserWxApiClient.massSendall(tmp.getMediaId(),taskCode);
                if(rstObjMassSendall.getInt("errcode")==0){
                    //成功
                    tmp.setTaskRunStatus(1);
                    tmp.setTaskRunResult(1);
                    tmp.setTaskRunResultMsg("OK");
                }else{
                    //失败
                    tmp.setTaskRunStatus(-1);
                    tmp.setTaskRunResult(rstObjMassSendall.getInt("errcode"));
                    tmp.setTaskRunResultMsg(rstObjMassSendall.getString("errmsg"));
                }
                userNewsTaskService.update(tmp);
            }else{
                //获取当前任务及其文章，组合发送，并将结果保存到数据库
                UserNewsTaskArticle userNewsTaskArticle=new UserNewsTaskArticle();
                userNewsTaskArticle.setNewsTaskId(tmp.getId());
                List<UserNewsTaskArticle> listUserNewsTaskArticle=userNewsTaskArticleService.listForPage(userNewsTaskArticle);
                if(listUserNewsTaskArticle.size()>0){
                    long[] ids=new long[listUserNewsTaskArticle.size()];
                    for (int j=0;j<listUserNewsTaskArticle.size();j++){
                        ids[j]=listUserNewsTaskArticle.get(j).getArticleId();
                    }
                    List<Article> listArticle=  articleService.getArticleNewsByIds(ids);
                    //组合新闻
                    List<MsgNews> msgNewsList = new ArrayList<MsgNews>();
                    for (int m=0;m<listArticle.size();m++){
                        MsgNews msgNews=new MsgNews();
                        if(m==0)
                            msgNews.setShowpic(1);
                        msgNews.setPicpath(listArticle.get(m).getThumb());
                        msgNews.setCreatetime(listArticle.get(m).getCreatetime());
                        msgNews.setDescription(listArticle.get(m).getArticleContent());
                        msgNews.setTitle(listArticle.get(m).getArticleTitle());
                        msgNews.setBrief(listArticle.get(m).getArticleNote());
                        msgNewsList.add(msgNews);
                    }
                    JSONObject rstObj = UserWxApiClient.uploadNews(msgNewsList, taskCode);
                    if (rstObj.containsKey("media_id")) {
                        String mediaId= rstObj.getString("media_id");
                        tmp.setMediaId(mediaId);
                        tmp.setTaskRunTimes(tmp.getTaskRunTimes()+1);
                        userNewsTaskService.update(tmp);
                    } else {
                        //这里同步发布素材就失败了。//失败
                        tmp.setTaskRunStatus(-1);
                        tmp.setTaskRunResult(rstObj.getInt("errcode"));
                        tmp.setTaskRunResultMsg(rstObj.getString("errmsg"));
                        userNewsTaskService.update(tmp);
                    }
                }
            }
        }
    }
}
