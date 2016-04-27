package com.wxcms.task;

import com.core.page.Pagination;
import com.wxapi.process.UserWxApiClient;
import com.wxcms.domain.*;
import com.wxcms.service.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Autowired
    private  MediaWxService mediaWxService;

    private int pageNum=1;
   // @Scheduled(cron = "0 0/10 * * * ? ") //间隔10分钟执行
    @Scheduled(cron = "0/30 * * * * ? ") //间隔10分钟执行
    public void autoCreateArticleTask() {
        //查找需要创建定时任务的所有微信号
        UserAutoNewsTask userAutoNewsTask=new UserAutoNewsTask();
        Pagination<UserAutoNewsTask> pagination=new Pagination<UserAutoNewsTask>();
        pagination.setPageSize(10);
        pagination.setPageNum(pageNum);
        userAutoNewsTask.setAutoStatus(1);//正在的才进行文章发布。
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
                        msgNews.setDescription(getContentAndUploadImage(listArticle.get(m).getArticleContent(),taskCode));
                        msgNews.setTitle(StringEscapeUtils.unescapeHtml(listArticle.get(m).getArticleTitle()));
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

    private String getContentAndUploadImage(String articleContent,TaskCode taskCode) {
        //正则出image
        Pattern p = Pattern.compile("<img[^>]+data-original\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher m = p.matcher(articleContent);
        System.out.println(m.find());
        System.out.println(m.groupCount());
        while(m.find()){
            System.out.println(m.group()+"-------------↓↓↓↓↓↓");
            System.out.println(m.group(0));
            System.out.println(m.group(1));
            String replaceStr="";
            replaceStr="<img src='"+m.group(1)+"'/>";

            //上传图片到微信服务器，并保存信息到数据库
          /*  MediaWx mediaWx=new MediaWx();
            mediaWx.setOldMedia(m.group(1));
            mediaWx.setOldMediaMd5(Str2MD5.MD5(m.group(1),32));
            if(!mediaWxService.isExistByMd5(mediaWx)){
                String rootPath=this.getClass().getResource("/").getFile().toString().replace("/WEB-INF/classes/","")+"/res/upload/";
                String picName= UUID.randomUUID().toString()+ ".jpg";
                //下载图片，上传图片
                UploadUtil.download(m.group(1), picName, rootPath);
                //File file=new File(rootPath+picName);
                JSONObject jsonObject=  UserWxApiClient.uploadImage(rootPath+picName,taskCode);
                if (null!=jsonObject&&jsonObject.has("url")){
                    replaceStr="<img src='"+jsonObject.getString("url")+"'/>";
                    mediaWx.setNewUrl(jsonObject.getString("url"));
                    mediaWxService.add(mediaWx);
                }
            }else
            {
                mediaWx=mediaWxService.getByMd5(mediaWx);
                if (null!=mediaWx){
                    replaceStr="<img src='"+mediaWx.getNewUrl()+"'/>";
                }
            }*/
            articleContent=articleContent.replace(m.group(0),replaceStr);
        }
        return articleContent;
    }
    public static void main(String args[]){

      String  articleContent="<p><img class=\"lazy aligncenter size-full wp-image-31989\" title=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" src=\"http://fzn.cc/wp-content/plugins/jquery-image-lazy-loading/images/grey.gif\" data-original=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-118.jpg\" alt=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" width=\"640\" height=\"439\" srcset=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-118-300x206.jpg 300w, http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-118.jpg 640w\" sizes=\"(max-width: 640px) 100vw, 640px\"/><noscript><img class=\"aligncenter size-full wp-image-31989\" title=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" src=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-118.jpg\" alt=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" width=\"640\" height=\"439\" srcset=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-118-300x206.jpg 300w, http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-118.jpg 640w\" sizes=\"(max-width: 640px) 100vw, 640px\"/></noscript></p><p>看过一个小故事：古代有个孝子叫韩伯俞。他的母亲在他犯错时，总是严厉地教导他，有时还会打他。待他长大成人后，当他犯错时，母亲的教训依然如故。</p><p>有一次母亲打他，他突然放声大哭。母亲很惊讶，几十年来打他从未哭过。于是就问他：“为什么要哭？”</p><p>伯俞回答说：“从小到大，母亲打我，我都觉得很痛。我能感受到母亲是为了教育我才这么做。但是今天母亲打我，我已经感觉不到痛了。这说明母亲的身体愈来愈虚弱，我奉养母亲的时间愈来愈短了。想到此我不禁悲从中来。”</p><p>这个小故事，让我感动不已。</p><p><strong>父母在，人生即有来处；父母去，人生只剩归途。</strong></p><p><img class=\"lazy aligncenter size-full wp-image-31990\" title=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" src=\"http://fzn.cc/wp-content/plugins/jquery-image-lazy-loading/images/grey.gif\" data-original=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-119.jpg\" alt=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" width=\"500\" height=\"332\" srcset=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-119-300x199.jpg 300w, http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-119.jpg 500w\" sizes=\"(max-width: 500px) 100vw, 500px\"/><noscript><img class=\"aligncenter size-full wp-image-31990\" title=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" src=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-119.jpg\" alt=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" width=\"500\" height=\"332\" srcset=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-119-300x199.jpg 300w, http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-119.jpg 500w\" sizes=\"(max-width: 500px) 100vw, 500px\"/></noscript></p><p>推荐一篇毕淑敏的小短文《孝心无价》，祝天下的父母平安喜乐，儿女的孝心都有着落。</p><p>我不喜欢一个苦孩子求学的故事。家庭十分困难，父亲逝去，弟妹嗷嗷待哺，可他大学毕业后，还要坚持读研究生，母亲只有去卖血……我以为那是一个自私的孩子。</p><p>求学的路很漫长，一生一世的事业，何必太在意几年蹉跎？况且这时间的分分秒秒都苦涩无比，需用母亲的鲜血灌溉！一个连母亲都无法挚爱的人，还能指望他会爱谁？把自己的利益放在至高无上位置的人，怎能成为人类的大师？</p><p><img class=\"lazy aligncenter size-full wp-image-31992\" title=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" src=\"http://fzn.cc/wp-content/plugins/jquery-image-lazy-loading/images/grey.gif\" data-original=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-121.jpg\" alt=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" width=\"640\" height=\"453\" srcset=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-121-300x212.jpg 300w, http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-121.jpg 640w\" sizes=\"(max-width: 640px) 100vw, 640px\"/><noscript><img class=\"aligncenter size-full wp-image-31992\" title=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" src=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-121.jpg\" alt=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" width=\"640\" height=\"453\" srcset=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-121-300x212.jpg 300w, http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-121.jpg 640w\" sizes=\"(max-width: 640px) 100vw, 640px\"/></noscript></p><p>我也不喜欢父母病重在床，断然离去的游子，无论你有多少理由。地球离了谁都照样转动，不必将个人的力量夸大到不可思议的程度。在一位老人行将就木的时候，将他对人世间最期冀的希望斩断，以绝望之心在寂寞中远行，那是对生命的大不敬。</p><p>我相信每个赤诚忠厚的孩子，都曾在心底向父母许下“孝”的宏愿，相信来日方长，相信水到渠成，相信自己必有功成名就衣锦还乡的那一天，可以从容尽孝。</p><p>可惜人们忘了，忘了时间的残酷，忘了人生的短暂，忘了世上有永远无法报答的恩情，忘了生命本身不堪一击的脆弱。</p><p><strong>父母走了，带着对我们深深的挂念；父母走了，留给我们永无偿还的心情。你就永远无以言孝。</strong></p><p><img class=\"lazy aligncenter size-full wp-image-31991\" title=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" src=\"http://fzn.cc/wp-content/plugins/jquery-image-lazy-loading/images/grey.gif\" data-original=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-120.jpg\" alt=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" width=\"640\" height=\"494\" srcset=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-120-300x232.jpg 300w, http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-120.jpg 640w\" sizes=\"(max-width: 640px) 100vw, 640px\"/><noscript><img class=\"aligncenter size-full wp-image-31991\" title=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" src=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-120.jpg\" alt=\"又到清明，父母在，人生即有来处；父母去，人生只剩归途\" width=\"640\" height=\"494\" srcset=\"http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-120-300x232.jpg 300w, http://fzn.cc/wp-content/uploads/2016/04/4ffce04d92a4d6cb21c1494cdfcd6dc1-120.jpg 640w\" sizes=\"(max-width: 640px) 100vw, 640px\"/></noscript></p><p><strong>有一些事情，当我们年轻的时候，无法懂得。当我们懂得的时候已不再年轻。</strong>世上有些东西可以弥补，有些东西永无弥补……</p><p>赶快为你的父母尽一份孝心。也许是一处豪宅，也许是一片砖瓦。也许是大洋彼岸的一只鸿雁，也许是近在咫尺的一个口信。也许是一顶纯黑的博士帽，也许是作业簿上的一个满分。也许是一桌山珍海味，也许是一只野果一朵山花。也许是花团锦簇的盛世华衣，也许是一双洁净的布鞋。也许是数以万计的金钱，也许只是含着体温的一枚硬币……但在“孝”的天平上，它们等值。</p>";
        //System.out.println(getContentAndUploadImage(""));
    }
}
