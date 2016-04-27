package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.MediaWx;

/**
 * Created by kaka.li on 2016-04-27.
 */
public interface MediaWxService {
    public MediaWx getById(String id);

    public Pagination<MediaWx> paginationEntity(MediaWx searchEntity, Pagination<MediaWx> pagination);

    public Integer getTotalItemsCount(MediaWx searchEntity);

    public MediaWx getByMd5(MediaWx searchEntity);//查找MD5值对应的文章shu
    public Boolean isExistByMd5(MediaWx searchEntity);
    public void add(MediaWx entity);

    public void update(MediaWx entity);

    public void delete(MediaWx entity);
}
