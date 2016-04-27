package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.MediaWx;

import java.util.List;

/**
 * Created by Administrator on 2016-04-26.
 */
public interface MediaWxDao {
    public MediaWx getById(String id);

    public List<MediaWx> paginationEntity(MediaWx searchEntity, Pagination<MediaWx> pagination);

    public Integer getTotalItemsCount(MediaWx searchEntity);

    public MediaWx getByMd5(MediaWx searchEntity);//查找MD5值对应的文章shu

    public void add(MediaWx entity);

    public void update(MediaWx entity);

    public void delete(MediaWx entity);

    Boolean isExistByMd5(MediaWx searchEntity);
}
