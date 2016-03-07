package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.TaskCode;
import com.wxcms.domain.TaskLog;
import com.wxcms.mapper.TaskLogDao;
import com.wxcms.service.TaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskLogServiceImpl implements TaskLogService {
    @Autowired
    private TaskLogDao baseDao;
    public List<TaskLog> listForPage(TaskCode searchEntity, Pagination<TaskLog> page) {
        return baseDao.listForPage(searchEntity,page);
    }

    public Integer getTotalItemsCount(TaskCode searchEntity) {
        return baseDao.getTotalItemsCount(searchEntity);
    }

    public void add(TaskLog entity) {
        baseDao.add(entity);
    }

    public TaskLog getById(long id) {
        return baseDao.getById(id);
    }

    public TaskLog getByTaskIdAndTaskCodeNum(long taskId, String taskCodeNum) {
        TaskLog taskLog=new TaskLog();
        taskLog.setTaskId(taskId);
        taskLog.setTaskCodeNum(taskCodeNum);
        return baseDao.getByTaskIdAndTaskCodeNum(taskLog);
    }
    /***
     * 获取接了的任务，完成，未完成的。
     * @param searchEntity
     * @param pagination
     * @return
     */
    public Pagination<TaskLog> paginationEntityByOpenIdAndTaskStatus(TaskLog searchEntity, Pagination<TaskLog> pagination) {
        Integer totalItemsCount = baseDao.getTotalItemsCountByOpenIdAndTaskStatus(searchEntity);
        List<TaskLog> items = baseDao.listForPageByOpenIdAndTaskStatus(searchEntity, pagination);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }

    @Override
    public TaskLog getByTaskIdAndOpenId(long id, String openId) {
       return baseDao.getByTaskIdAndOpenId(id,openId);
    }

    @Override
    public TaskLog getByCode(String taskCodeNum) {
        return baseDao.getByCode(taskCodeNum);
    }

    public void updateTaskStatus(TaskLog taskLog) {
        baseDao.updateTaskStatus(taskLog);
    }
}
