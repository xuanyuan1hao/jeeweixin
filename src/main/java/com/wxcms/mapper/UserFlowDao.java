package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.UserFlow;

import java.util.List;

/**
 * Created by Administrator on 2016-02-19.
 */
public interface UserFlowDao {
    public List<UserFlow> paginationEntity(UserFlow searchEntity, Pagination<UserFlow> pagination);
    public Integer getTotalItemsCount(UserFlow searchEntity);
    public void add(UserFlow entity);
}
