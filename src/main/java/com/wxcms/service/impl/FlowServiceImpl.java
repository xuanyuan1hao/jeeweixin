package com.wxcms.service.impl;

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

    public List<Flow> listForPage(Flow searchEntity) {
        return flowDao.listForPage(searchEntity);
    }

    public void add(Flow entity) {
        flowDao.add(entity);
    }
}
