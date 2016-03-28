package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.WebConfig;
import com.wxcms.mapper.WebConfigDao;
import com.wxcms.service.WebConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kaka.li on 2016-03-28.
 */
@Service
public class WebConfigServiceImpl implements WebConfigService {

    private static Map<String, String> webConfigMap = new ConcurrentHashMap<String, String>();
    @Autowired
    private WebConfigDao baseDao;

    @Override
    public void add(WebConfig entity) {
        baseDao.add(entity);
        webConfigMap.put(entity.getKeyStr(), entity.getValStr());
    }

    @Override
    public void update(WebConfig entity) {
        baseDao.update(entity);
        webConfigMap.put(entity.getKeyStr(), entity.getValStr());
    }

    @Override
    public void delete(WebConfig entity) {
        baseDao.delete(entity);
        if(null!=entity.getKeyStr())
            webConfigMap.remove(entity.getKeyStr());
    }

    @Override
    public WebConfig getById(long id) {
        return baseDao.getById(id);
    }

    @Override
    public WebConfig getByKeyStr(String keyStr) {
        return baseDao.getByKeyStr(keyStr);
    }

    @Override
    public List<WebConfig> listForPage(WebConfig searchEntity) {
        return baseDao.listForPage(searchEntity);
    }

    @Override
    public Integer getTotalItemsCount(WebConfig searchEntity) {
        return baseDao.getTotalItemsCount(searchEntity);
    }

    @Override
    public Pagination<WebConfig> paginationEntity(WebConfig searchEntity, Pagination<WebConfig> pagination) {
        List<WebConfig> items = baseDao.paginationEntity(searchEntity, pagination);
        Integer totalItemsCount = baseDao.getTotalItemsCount(searchEntity);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }

    public void addWebConfigListToMemoryCache(List<WebConfig> webConfigList) {
        if (webConfigList != null && webConfigList.size() > 0) {
            for (int i = 0; i < webConfigList.size(); i++) {
                webConfigMap.put(webConfigList.get(i).getKeyStr(), webConfigList.get(i).getValStr());
            }
        }
    }

    //根据keyStr获取valStr值
    public String getWebConfigByKey(String keyStr) {
        return webConfigMap.get(keyStr);
    }
}
