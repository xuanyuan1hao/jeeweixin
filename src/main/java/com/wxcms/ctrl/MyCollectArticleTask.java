package com.wxcms.ctrl;

import com.core.util.Str2MD5;
import com.wxcms.domain.Article;
import com.wxcms.domain.ArticleClassify;
import com.wxcms.service.ArticleClassifyService;
import com.wxcms.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MyCollectArticleTask {
    private static int page=39;
    private static int maxPage=75;
    @Autowired
   private ArticleService articleService;
    @Autowired
    private ArticleClassifyService articleClassifyService;

    @Scheduled(cron = "0/5 * * * * ? ") //间隔5秒执行
    public void collectTaskCycle() {
       // String referer="http://fzn.cc/meiwen/";
        String targetUrl="http://fzn.cc/page/"+page+"/";
        if(page>maxPage)
            return;
        String source=getHTMLSrc(targetUrl);
        List<Article> listContent=getListCoutent(source);
        for (int i=0;i<listContent.size();i++)
        {
            long classId=0;
            String classfyMd5=Str2MD5.MD5(listContent.get(i).getClassfyName(),32);
            if(articleClassifyService.isExistMd5(classfyMd5)){
                ArticleClassify articleClassify= articleClassifyService.getByMd5(classfyMd5);
                classId=articleClassify.getId();
            }else{
                ArticleClassify articleClassify=new  ArticleClassify();
                articleClassify.setClassifyMd5(classfyMd5);
                articleClassify.setClassifyName(listContent.get(i).getClassfyName());
                articleClassifyService.add(articleClassify);
                classId=articleClassify.getId();
            }
            if(!articleService.isExistMd5(listContent.get(i).getMd5())){
                listContent.get(i).setClassfyId(classId);
                articleService.add(listContent.get(i));
            }
        }
        page++;
    }

    private List<Article> getListCoutent(String source) {
        List<Article> ret=new ArrayList<Article>();
        Pattern pattern = Pattern.compile("<article class=\"excerpt\".*?</article>");
        Matcher mt = pattern.matcher(source);
        while(mt.find())
        {
            Article article=new Article();
            System.out.println("===================");
            //System.out.println(mt.group());
            String s1="<header><a class=\"label label-important\".*?<i class=\"label-arrow\">";//分类部分
            String s2="title=\".*?\">";//标题部分
            String s3="<h2><a target=\"_blank\" href=\".*?\"";//文章网址
            String note="<span class=\"note\">.*?</span>";
            String thumb="<img class=\"thumb\" src=\".*?\"";
            String readTimes="<span class=\"muted\"><i class=\"fa fa-eye\"></i>.*?℃</span>";
            //获取分类名称
            Pattern pt1=Pattern.compile(s1);
            Matcher mt1=pt1.matcher(mt.group());
            while(mt1.find())
            {
                String cat=mt1.group().replaceAll("<header><a class=\"label label-important\"", "").replaceAll("<i class=\"label-arrow\">", "");
                if(cat.lastIndexOf(">")>0)
                {
                    cat=cat.substring(cat.lastIndexOf(">")+1,cat.length());
                }
                System.out.println("分类名称：" + cat);
                article.setClassfyName(cat);
            }


            Pattern pt2=Pattern.compile(s2);
            Matcher mt2=pt2.matcher(mt.group());
            while(mt2.find())
            {
                String title=mt2.group().replaceAll("title=\"", "").replaceAll("\">", "");
                System.out.println("标题："+title);
                article.setArticleTitle(title);
                article.setMd5(Str2MD5.MD5(title,32));
            }
            Pattern pt3=Pattern.compile(s3);
            Matcher mt3=pt3.matcher(mt.group());
            while(mt3.find())
            {
                String  articleUrl=mt3.group().replaceAll("<h2><a target=\"_blank\" href=\"", "").replaceAll("\"", "");
                System.out.println("网址："+articleUrl);
                article.setArticleUrl(articleUrl);
                //获取文章内容
                String content=getArticleContent(articleUrl);
                System.out.println("***************************");
                System.out.println(content);
                article.setArticleContent(content);
                System.out.println("***************************");
            }
            Pattern pt_thumb=Pattern.compile(thumb);
            Matcher mt_thumb=pt_thumb.matcher(mt.group());
            while(mt_thumb.find())
            {
                String thumbPic=mt_thumb.group().replaceAll("<img class=\"thumb\" src=\"", "").replaceAll("\"", "");
                System.out.println("缩略图："+thumbPic);
                article.setThumb(thumbPic);
            }
            Pattern pt_note=Pattern.compile(note);
            Matcher mt_note=pt_note.matcher(mt.group());
            while(mt_note.find())
            {
                String noteStr=mt_note.group().replaceAll("<span class=\"note\">", "").replaceAll("</span>", "");
                System.out.println("简介："+noteStr);
                article.setArticleNote(noteStr);
            }
            Pattern pt_readTimes=Pattern.compile(readTimes);
            Matcher mt_readTimes=pt_readTimes.matcher(mt.group());
            while(mt_readTimes.find())
            {
                String readTimesNum=mt_readTimes.group().replaceAll("<span class=\"muted\"><i class=\"fa fa-eye\"></i>", "").replaceAll("℃</span>", "").replace(" ","");
                System.out.println("阅读次数：" + readTimesNum);
                long num=new Long(readTimesNum);
                article.setReadTimes(num);
            }
            System.out.println("===================");
            article.setCreatetime(new Date());
            ret.add(article);
        }
        return ret;
    }

    private String getArticleContent(String articleUrl) {
        String articleContent="";
        String articleHtml=getHTMLSrc(articleUrl);
        //正则匹配文章内容
        Pattern pattern = Pattern.compile("<article class=\"article-content\">.*?<p>转载请注");
        Matcher mt = pattern.matcher(articleHtml);
        while(mt.find())
        {
            articleContent=mt.group();
        }

        articleContent=articleContent.replace("<article class=\"article-content\">","").replace("<p>转载请注","");
        return articleContent;
    }

    private String getHTMLSrc(String url){
        StringBuffer stringBuffer=new StringBuffer();
        InputStream openStream = null;
        BufferedReader buf = null;
        try {
            String line = null;
            URL theUrl= new URL(url);
            openStream = theUrl.openStream();
            //<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
            //构建输入流的的字符集必须和HTML源码中的	charset一致
            buf = new BufferedReader(new InputStreamReader(openStream,"utf-8"));
            while((line = buf.readLine()) != null){
               // System.out.println(line);
                stringBuffer.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                if(openStream!=null){
                    openStream.close();
                }
                if(buf!=null){
                    buf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }
    public static void main(String args[]){
        MyCollectArticleTask myCollectArticleTask=new MyCollectArticleTask();
        myCollectArticleTask.collectTaskCycle();
    }
}
