package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.TaskCode;
import com.wxcms.domain.TaskRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016-03-22.
 */
public interface TaskRecordDao {
    public List<TaskRecord> listForPage(TaskCode searchEntity,Pagination<TaskRecord> page);
    public Integer getTotalItemsCount(TaskCode searchEntity);
    public void add(TaskRecord entity);
    public TaskRecord getById(long id);
    TaskRecord getByTaskIdAndTaskCodeNum(TaskRecord searchEntity);
    public List<TaskRecord>  listForPageByOpenId(TaskRecord searchEntity,Pagination<TaskRecord> page);
    TaskRecord getByTaskIdAndOpenId(@Param("taskId") long taskId,@Param("openId")  String openId);

    void delete(TaskRecord entity);
}
