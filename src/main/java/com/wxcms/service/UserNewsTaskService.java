package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.UserNewsTask;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-04-09.
 */
public interface UserNewsTaskService {
    public UserNewsTask add(UserNewsTask entity);

    public UserNewsTask getById(long id);

    public List<UserNewsTask> listForPage(UserNewsTask searchEntity);

    public Pagination<UserNewsTask> paginationEntity(UserNewsTask searchEntity, Pagination<UserNewsTask> pagination);

    public Integer getTotalItemsCount(UserNewsTask searchEntity);

    public void update(UserNewsTask entity);//更新

    public void updateArticleCount(UserNewsTask entity);

    public void delete(UserNewsTask entity);

    boolean hasExistTaskByTaskRunTime(Date taskRunTime,long wxId);
}
