package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.UserFlow;
import com.wxcms.mapper.UserFlowDao;
import com.wxcms.service.UserFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016-02-19.
 */
@Service
public class UserFlowServiceImpl implements UserFlowService {
    @Autowired
    private UserFlowDao baseDao;
    public Pagination<UserFlow> paginationEntity(UserFlow searchEntity, Pagination<UserFlow> pagination) {
        Integer totalItemsCount = baseDao.getTotalItemsCount(searchEntity);
        List<UserFlow> items = baseDao.paginationEntity(searchEntity, pagination);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }
    public void add(UserFlow entity) {
        baseDao.add(entity);
    }
}
