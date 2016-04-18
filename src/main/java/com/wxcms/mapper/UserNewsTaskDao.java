package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.UserNewsTask;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-04-09.
 */
public interface UserNewsTaskDao {
    public void add(UserNewsTask entity);

    public UserNewsTask getById(long id);

    public List<UserNewsTask> listForPage(UserNewsTask searchEntity);

    public List<UserNewsTask> paginationEntity(UserNewsTask searchEntity, Pagination<UserNewsTask> pagination);

    public Integer getTotalItemsCount(UserNewsTask searchEntity);

    public void update(UserNewsTask entity);

    public void updateArticleCount(UserNewsTask entity);

    public void delete(UserNewsTask entity);

    boolean hasExistTaskByTaskRunTime(@Param("taskRunTime") Date taskRunTime,@Param("wxId") long wxId);
}
