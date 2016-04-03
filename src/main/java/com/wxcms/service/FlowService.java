package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.Flow;

import java.util.List;

/**
 * Created by Administrator on 2016-01-19.
 */
public interface FlowService {
    public Flow getById(String id);

    public List<Flow> listForPageTop100(Flow searchEntity);

    Pagination<Flow> paginationEntity(Flow searchEntity, Pagination<Flow> pagination);

    public void add(Flow entity);
}
