package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.TaskCode;

import java.util.List;

/**
 * Created by Administrator on 2016-02-07.
 */
public interface TaskCodeDao {
    public List<TaskCode> listForPage(TaskCode searchEntity,Pagination<TaskCode> page);
    public Integer getTotalItemsCount(TaskCode searchEntity);
    public void add(TaskCode entity);
    public TaskCode getById(long id);

    void update(TaskCode taskCode);
}
