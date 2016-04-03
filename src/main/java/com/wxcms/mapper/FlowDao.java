package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.Flow;

import java.util.List;

/**
 * Created by Administrator on 2016-01-19.
 */
public interface FlowDao {
    public List<Flow> listForPageTop100(Flow searchEntity);

    public List<Flow> listForPage(Flow searchEntity,Pagination<Flow> page);

    public Integer getTotalItemsCount(Flow searchEntity);

    public void add(Flow entity);
    public Flow getById(String id);
}
