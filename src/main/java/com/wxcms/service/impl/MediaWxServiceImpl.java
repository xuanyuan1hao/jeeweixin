package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.MediaWx;
import com.wxcms.mapper.MediaWxDao;
import com.wxcms.service.MediaWxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediaWxServiceImpl implements MediaWxService {


    @Autowired
    private MediaWxDao baseDao;
    @Override
    public MediaWx getById(String id) {
        return baseDao.getById(id);
    }

    @Override
    public Pagination<MediaWx> paginationEntity(MediaWx searchEntity, Pagination<MediaWx> pagination) {
        Integer totalItemsCount = baseDao.getTotalItemsCount(searchEntity);
        List<MediaWx> items = baseDao.paginationEntity(searchEntity, pagination);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }

    @Override
    public Integer getTotalItemsCount(MediaWx searchEntity) {
        return baseDao.getTotalItemsCount(searchEntity);
    }

    @Override
    public MediaWx getByMd5(MediaWx searchEntity) {
        return baseDao.getByMd5(searchEntity);
    }

    @Override
    public Boolean isExistByMd5(MediaWx searchEntity) {
        return baseDao.isExistByMd5(searchEntity);
    }

    @Override
    public void add(MediaWx entity) {
        baseDao.add(entity);
    }

    @Override
    public void update(MediaWx entity) {
        baseDao.update(entity);
    }

    @Override
    public void delete(MediaWx entity) {
        baseDao.delete(entity);
    }
}
