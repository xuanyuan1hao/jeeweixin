package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.TaskCode;
import com.wxcms.domain.TaskLog;

import java.util.List;

/**
 * Created by Administrator on 2016-02-07.
 */
public interface TaskCodeService {
    public List<TaskCode> listForPage(TaskCode searchEntity,Pagination<TaskCode> page);
    public Integer getTotalItemsCount(TaskCode searchEntity);
    public void add(TaskCode entity);
    public TaskCode getById(long id);

    Pagination<TaskCode> paginationEntity(TaskCode searchEntity, Pagination<TaskCode> pagination);

    void update(TaskCode taskCode);

    TaskCode getByWxCode(String wxCodeImgHref);

    Pagination<TaskCode> paginationEntityNotGet(TaskLog searchEntity, Pagination<TaskCode> pagination);

}
