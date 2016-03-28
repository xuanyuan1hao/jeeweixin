package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.WebConfig;

import java.util.List;

/**
 * Created by Administrator on 2016-03-28.
 */
public interface WebConfigService {
    public void add(WebConfig entity);

    public void update(WebConfig entity);

    public void delete(WebConfig entity);

    public WebConfig getById(long id);

    public WebConfig getByKeyStr(String keyStr);

    public List<WebConfig> listForPage(WebConfig searchEntity);

    public Integer getTotalItemsCount(WebConfig searchEntity);

    public Pagination<WebConfig> paginationEntity(WebConfig searchEntity, Pagination<WebConfig> pagination);

    public void addWebConfigListToMemoryCache(List<WebConfig> webConfigList);

    public String getWebConfigByKey(String keyStr);
}
