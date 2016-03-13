package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.CustomTextMessage;

import java.util.List;

/**
 * Created by Administrator on 2016-03-12.
 */
public interface CustomTextMessageDao {
    public CustomTextMessage getById(String id);

    public List<CustomTextMessage> paginationEntity(CustomTextMessage searchEntity, Pagination<CustomTextMessage> pagination);
    public Integer getTotalItemsCount(CustomTextMessage searchEntity);

    public void add(CustomTextMessage entity);

    public void update(CustomTextMessage entity);

    public void updateSendTimes(CustomTextMessage entity);

    public void delete(CustomTextMessage entity);
}
