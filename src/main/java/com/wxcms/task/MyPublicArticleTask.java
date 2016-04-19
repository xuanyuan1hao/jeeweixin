package com.wxcms.task;

import com.core.page.Pagination;
import com.wxapi.process.UserWxApiClient;
import com.wxcms.domain.*;
import com.wxcms.service.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    @Autowired
    private UserAutoNewsTaskService userAutoNewsTaskService;


    private int pageNum=1;
    //@Scheduled(cron = "0 0/10 * * * ? ") //间隔10分钟执行
    @Scheduled(cron = "0/30 * * * * ? ") //间隔10分钟执行
    public void autoCreateArticleTask() {
        //查找需要创建定时任务的所有微信号
        UserAutoNewsTask userAutoNewsTask=new UserAutoNewsTask();
        Pagination<UserAutoNewsTask> pagination=new Pagination<UserAutoNewsTask>();
        pagination.setPageSize(10);
        pagination.setPageNum(pageNum);
        pagination=userAutoNewsTaskService.paginationEntity(userAutoNewsTask,pagination);
        if (null==pagination||pagination.getItems().size()==0){
            pageNum=1;
        }else{
            for (int i=0;i<pagination.getItems().size();i++){
                UserAutoNewsTask userAutoNewsTaskTemp=  pagination.getItems().get(i);
                if(!userNewsTaskService.hasExistTaskByTaskRunTime(new Date(),userAutoNewsTaskTemp.getWxId())){
                    //创建任务
                    UserNewsTask userNewsTask=new UserNewsTask();
                    Calendar nowTime = Calendar.getInstance();
                    nowTime.add(Calendar.MINUTE, 5);//5分钟后的发布
                    userNewsTask.setTaskRunTime(nowTime.getTime());
                    userNewsTask.setWxId(userAutoNewsTaskTemp.getWxId());
                    userNewsTask.setArticleCount(userAutoNewsTaskTemp.getMaxArticleCount());
                    userNewsTask.setUserId(userAutoNewsTaskTemp.getUserId());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    userNewsTask.setNewsTaskName(sdf.format(nowTime.getTime()));
                    userNewsTask=  userNewsTaskService.add(userNewsTask);
                    //添加文章
                    Article article=new Article();
                    article.setClassfyId(userAutoNewsTaskTemp.getArticleClassifyId());
                    List<Article> list=articleService.getArticleNewsByRandom(article);
                    //添加文章到任务中
                    for (int j=0;(j<list.size()&&j<userAutoNewsTaskTemp.getMaxArticleCount());j++)
                    {
                        UserNewsTaskArticle userNewsTaskArticle=new UserNewsTaskArticle();
                        userNewsTaskArticle.setUserId(userAutoNewsTaskTemp.getUserId());
                        userNewsTaskArticle.setArticleId(list.get(j).getId());
                        userNewsTaskArticle.setNewsTaskId(userNewsTask.getId());
                        userNewsTaskArticleService.add(userNewsTaskArticle);
                    }
                }
            }
            pageNum++;
        }
    }

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
                    } else {
                        if(tmp.getTaskRunTimes()>=3)//上传3次失败就不再上传了。
                            tmp.setTaskRunStatus(-1);
                        tmp.setTaskRunResult(rstObj.getInt("errcode"));
                        tmp.setTaskRunResultMsg(rstObj.getString("errmsg"));
                    }
                    tmp.setTaskRunTimes(tmp.getTaskRunTimes()+1);
                    userNewsTaskService.update(tmp);
                }
            }
        }
    }
}
