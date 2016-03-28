package com.wxcms.mapper;


import com.core.page.Pagination;
import com.wxcms.domain.VideoTask;

import java.util.List;

/**
 * Created by Administrator on 2016-03-24.
 */
public interface VideoTaskDao {
    public void add(VideoTask entity);

    public VideoTask getById(long id);

    public List<VideoTask> listForPage(VideoTask searchEntity);
    public List<VideoTask> paginationEntity(VideoTask searchEntity, Pagination<VideoTask> pagination);
    public Integer getTotalItemsCount(VideoTask searchEntity);
    
    public void update(VideoTask entity);//更新

    public void  updateShareTimes(VideoTask entity);//加

    public void delete(VideoTask entity);
}
