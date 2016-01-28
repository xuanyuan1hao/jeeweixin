package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.FansTixian;

/**
 * Created by Administrator on 2016-01-18.
 */
public interface FansTixianSrevice {
    public FansTixian getById(String id);

    public void add(FansTixian entity);

    public void update(FansTixian entity);
    public void updateStatus(FansTixian entity);

    public void delete(FansTixian entity);

    Pagination<FansTixian> paginationEntity(FansTixian searchEntity, Pagination<FansTixian> pagination);
}
