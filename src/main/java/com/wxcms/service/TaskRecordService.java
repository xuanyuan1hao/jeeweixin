package com.wxcms.service;

import com.wxcms.domain.TaskRecord;

/**
 * Created by Administrator on 2016-03-23.
 */
public interface TaskRecordService {
    public void add(TaskRecord entity);
    public void delete(TaskRecord entity);
    TaskRecord getByTaskIdAndOpenId(long id, String openId);
}
