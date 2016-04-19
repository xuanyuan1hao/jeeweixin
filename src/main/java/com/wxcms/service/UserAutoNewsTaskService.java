package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.UserAutoNewsTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016-04-19.
 */
public interface UserAutoNewsTaskService {
    public void add(UserAutoNewsTask entity);

    public UserAutoNewsTask getById(long id);

    public List<UserAutoNewsTask> listForPage(UserAutoNewsTask searchEntity);

    public Pagination<UserAutoNewsTask> paginationEntity(UserAutoNewsTask searchEntity, Pagination<UserAutoNewsTask> pagination);

    public Integer getTotalItemsCount(UserAutoNewsTask searchEntity);

    public void update(UserAutoNewsTask entity);

    public void updateAutoStatus(UserAutoNewsTask entity);

    public void delete(UserAutoNewsTask entity);

    boolean hasExistTaskByWxId(@Param("wxId") long wxId);
}
