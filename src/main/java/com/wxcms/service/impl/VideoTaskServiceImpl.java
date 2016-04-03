package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.VideoTask;
import com.wxcms.mapper.VideoTaskDao;
import com.wxcms.service.VideoTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016-03-24.
 */
@Service
public class VideoTaskServiceImpl implements VideoTaskService {
    @Autowired
    private VideoTaskDao baseDao;
    @Override

    @CachePut(value="VideoTaskItem",key="#entity.getId()")
    public VideoTask add(VideoTask entity) {
        baseDao.add(entity);
        return entity;
    }

    @Override
    @Cacheable(value="VideoTaskItem", key="#id")
    public VideoTask getById(long id) {
        return baseDao.getById(id);
    }

    @Override
    public List<VideoTask> listForPage(VideoTask searchEntity) {
        return baseDao.listForPage(searchEntity);
    }

    @Override
    public Pagination<VideoTask> paginationEntity(VideoTask searchEntity, Pagination<VideoTask> pagination) {
        List<VideoTask> items= baseDao.paginationEntity(searchEntity,pagination);
        Integer totalItemsCount= baseDao.getTotalItemsCount(searchEntity);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }

    @Override
    public Integer getTotalItemsCount(VideoTask searchEntity) {
        return baseDao.getTotalItemsCount(searchEntity);
    }

    @Override
    @CacheEvict(value="VideoTaskItem",key="#entity.getId()")
    public void update(VideoTask entity) {
        baseDao.update(entity);
    }

    @Override
    @CacheEvict(value="VideoTaskItem",key="#entity.getId()")
    public void updateShareTimes(VideoTask entity) {
        baseDao.updateShareTimes(entity);
    }

    @Override
    @CacheEvict(value="VideoTaskItem",key="#entity.getId()")
    public void delete(VideoTask entity) {
        baseDao.delete(entity);
    }
}
