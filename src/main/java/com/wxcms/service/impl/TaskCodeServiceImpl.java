package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.TaskCode;
import com.wxcms.domain.TaskLog;
import com.wxcms.mapper.TaskCodeDao;
import com.wxcms.service.TaskCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016-02-07.
 */
@Service
public class TaskCodeServiceImpl implements TaskCodeService{
    @Autowired
    private TaskCodeDao baseDao;

    public List<TaskCode> listForPage(TaskCode searchEntity, Pagination<TaskCode> page) {
        return baseDao.listForPage(searchEntity,page);
    }

    public Integer getTotalItemsCount(TaskCode searchEntity) {
        return baseDao.getTotalItemsCount(searchEntity);
    }

    @CachePut(value="TaskCodeItem",key="#entity.getId()")
    public TaskCode add(TaskCode entity) {
        baseDao.add(entity);
        return entity;
    }

    @Cacheable(value="TaskCodeItem", key="#id")
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
    @CacheEvict(value="TaskCodeItem",key="#taskCode.getId()")// 清空accountCache 缓存
    public void update(TaskCode taskCode) {
        baseDao.update(taskCode);
    }

    public TaskCode getByWxCode(String wxCodeImgHref) {
        return baseDao.getByWxCode(wxCodeImgHref);
    }

    public TaskCode getByAccount(String account) {
        return baseDao.getByAccount(account);
    }


    /***
     * 获取没有接的任务
     * @param searchEntity
     * @param pagination
     * @return
     */
    public Pagination<TaskCode> paginationEntityNotGet(TaskLog searchEntity, Pagination<TaskCode> pagination) {
        Integer totalItemsCount = baseDao.getTotalItemsCountNotGet(searchEntity);
        List<TaskCode> items = baseDao.listForPageNotGet(searchEntity, pagination);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }
    @CacheEvict(value="TaskCodeItem", allEntries=true)
    public void delete(TaskCode taskCode) {
        baseDao.delete(taskCode);
    }
}
