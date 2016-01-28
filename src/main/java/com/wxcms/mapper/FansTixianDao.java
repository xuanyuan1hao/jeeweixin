package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.FansTixian;

import java.util.List;

/**
 * Created by Administrator on 2016-01-18.
 */
public interface FansTixianDao {
    public FansTixian getById(String id);

    public List<FansTixian> paginationEntity(FansTixian searchEntity, Pagination<FansTixian> pagination);
    public Integer getTotalItemsCount(FansTixian searchEntity);

    public void add(FansTixian entity);

    public void update(FansTixian entity);
    public void updateStatus(FansTixian entity);

    public void delete(FansTixian entity);

}
