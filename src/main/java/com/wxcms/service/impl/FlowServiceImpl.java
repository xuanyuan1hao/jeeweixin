package com.wxcms.service.impl;

import com.core.page.Pagination;
import com.wxcms.domain.Flow;
import com.wxcms.mapper.FlowDao;
import com.wxcms.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016-01-19.
 */
@Service
public class FlowServiceImpl implements FlowService {
    @Autowired
    private FlowDao flowDao;
    public Flow getById(String id) {
        return flowDao.getById(id);
    }
    public List<Flow> listForPageTop100(Flow searchEntity) {
        return flowDao.listForPageTop100(searchEntity);
    }

    @Override
    public Pagination<Flow> paginationEntity(Flow searchEntity, Pagination<Flow> pagination) {
        Integer totalItemsCount = flowDao.getTotalItemsCount(searchEntity);
        List<Flow> items = flowDao.listForPage(searchEntity, pagination);
        pagination.setTotalItemsCount(totalItemsCount);
        pagination.setItems(items);
        return pagination;
    }

    public void add(Flow entity) {
        flowDao.add(entity);
    }
}
