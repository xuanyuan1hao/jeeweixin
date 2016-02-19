package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.TaskCode;
import com.wxcms.domain.TaskLog;

import java.util.List;

/**
 * Created by Administrator on 2016-02-18.
 */
public interface TaskLogDao {
    public List<TaskLog> listForPage(TaskCode searchEntity,Pagination<TaskLog> page);
    public Integer getTotalItemsCount(TaskCode searchEntity);
    public void add(TaskLog entity);
    public TaskLog getById(long id);
    TaskLog getByTaskIdAndTaskCodeNum(TaskLog searchEntity);
    void updateTaskStatus(TaskLog taskLog);
    //根据状态和OpenId获取任务（已接到的任务，已经完成的任务。）
    public List<TaskLog> listForPageByOpenIdAndTaskStatus(TaskLog searchEntity,Pagination<TaskLog> page);
    public Integer getTotalItemsCountByOpenIdAndTaskStatus(TaskLog searchEntity);
}
