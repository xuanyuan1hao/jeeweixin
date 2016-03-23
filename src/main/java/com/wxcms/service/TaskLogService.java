package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.TaskCode;
import com.wxcms.domain.TaskLog;

import java.util.List;

/**
 * Created by Administrator on 2016-02-18.
 */
public interface TaskLogService {
    public List<TaskLog> listForPage(TaskCode searchEntity,Pagination<TaskLog> page);
    public Integer getTotalItemsCount(TaskCode searchEntity);
    public void add(TaskLog entity);
    public TaskLog getById(long id);
    TaskLog getByTaskIdAndTaskCodeNum(long taskId,String taskCodeNum);
    void updateTaskStatus(TaskLog taskLog);
    Pagination<TaskLog> paginationEntityByOpenIdAndTaskStatus(TaskLog searchEntity, Pagination<TaskLog> pagination);
    List<TaskLog> listForPageByOpenId(TaskLog searchEntity, Pagination<TaskLog> pagination);

    TaskLog getByTaskIdAndOpenId(long id, String openId);

    TaskLog getByCode(String taskCodeNum);//状态为未完成，福利码为taskCodeNum的任务查询
}
