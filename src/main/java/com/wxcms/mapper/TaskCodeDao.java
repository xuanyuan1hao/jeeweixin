package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.TaskCode;
import com.wxcms.domain.TaskLog;

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
    TaskCode getByWxCode(String wxCodeImgHref);
    //获取可以领取的任务
    public List<TaskCode> listForPageNotGet(TaskLog searchEntity,Pagination<TaskCode> page);
    public Integer getTotalItemsCountNotGet(TaskLog searchEntity);


    public void delete(TaskCode taskCode);

    TaskCode getByAccount(String account);
}
