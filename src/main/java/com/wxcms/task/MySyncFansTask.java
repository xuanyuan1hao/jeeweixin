package com.wxcms.task;


import com.core.page.Pagination;
import com.wxapi.process.UserWxApiClient;
import com.wxcms.domain.TaskCode;
import com.wxcms.service.TaskCodeService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MySyncFansTask {

    @Autowired
    private TaskCodeService taskCodeService;
    private int pageNum=1;
    @Scheduled(cron = "0 0/10 * * * ? ") //间隔10分钟执行
    public void autoSyncFansTask() {
        TaskCode searchEntity=new TaskCode();
        Pagination<TaskCode> pagination=new Pagination<TaskCode>();
        pagination.setPageNum(pageNum);
        pagination=taskCodeService.paginationEntity(searchEntity,pagination );
        if (null==pagination||pagination.getItems()==null||pagination.getItems().size()==0){
            pageNum=0;//翻到了最后一页，再从第一页开始
        }else{
            for (int i=0;i<pagination.getItems().size();i++){
                TaskCode taskCode= pagination.getItems().get(i);
                JSONObject jsonObject= UserWxApiClient.syncFansGroups(taskCode);
                if (null!=jsonObject&&jsonObject.has("groups")){
                    JSONArray jsonArray=JSONArray.fromObject(jsonObject.get("groups"));
                    if (null!=jsonArray&&jsonArray.size()>0){
                        int fansNum=0;
                        for (int j=0;j<jsonArray.size();j++){
                            JSONObject group=  JSONObject.fromObject(jsonArray.get(j));
                            fansNum+=group.getLong("count");
                        }
                        System.out.println(fansNum);
                        taskCode.setFansNum(fansNum);
                        taskCodeService.updateFansNum(taskCode);
                    }
                }
            }
        }
        pageNum++;
    }
}
