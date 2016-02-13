package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.TaskCode;
import com.wxcms.mapper.TaskCodeDao;
import com.wxcms.service.TaskCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016-02-07.
 */
@Service
public class TaskCodeServiceImpl implements TaskCodeService {
    @Autowired
    private TaskCodeDao baseDao;

    public List<TaskCode> listForPage(TaskCode searchEntity, Pagination<TaskCode> page) {
        return baseDao.listForPage(searchEntity,page);
    }

    public Integer getTotalItemsCount(TaskCode searchEntity) {
        return baseDao.getTotalItemsCount(searchEntity);
    }

    public void add(TaskCode entity) {
        baseDao.add(entity);
    }

    public TaskCode getById(long id) {
        return baseDao.getById(id);
    }

    public Pagination<TaskCode> paginationEntity(TaskCode searchEntity, Pagination<TaskCode> pagination) {
        Integer totalItemsCount = baseDao.getTotalItemsCount(searchEntity);
        List<TaskCode> items = baseDao.listForPage(searchEntity, pagination);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }

    public void update(TaskCode taskCode) {
        baseDao.update(taskCode);
    }
}
