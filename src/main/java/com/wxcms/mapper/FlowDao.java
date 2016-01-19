package com.wxcms.mapper;

import com.wxcms.domain.Flow;

import java.util.List;

/**
 * Created by Administrator on 2016-01-19.
 */
public interface FlowDao {
    public List<Flow> listForPage(Flow searchEntity);
    public void add(Flow entity);
    public Flow getById(String id);
}
