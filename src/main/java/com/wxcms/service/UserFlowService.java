package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.UserFlow;

/**
 * Created by Administrator on 2016-02-19.
 */
public interface UserFlowService {
    Pagination<UserFlow> paginationEntity(UserFlow searchEntity, Pagination<UserFlow> pagination);
    public void add(UserFlow entity);

}
