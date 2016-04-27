package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.UserAdsInfo;

import java.util.List;

/**
 * Created by Administrator on 2016-04-20.
 */
public interface UserAdsInfoService {
    public void add(UserAdsInfo entity);

    public UserAdsInfo getById(long id);

    public List<UserAdsInfo> listForPage(UserAdsInfo searchEntity);

    public Pagination<UserAdsInfo> paginationEntity(UserAdsInfo searchEntity, Pagination<UserAdsInfo> pagination);

    public Integer getTotalItemsCount(UserAdsInfo searchEntity);

    public void update(UserAdsInfo entity);

    public void delete(UserAdsInfo entity);
}
