package com.wxcms.service.impl;

import com.wxcms.domain.TaskRecord;
import com.wxcms.mapper.TaskRecordDao;
import com.wxcms.service.TaskRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016-03-23.
 */
@Service
public class TaskRecordServiceImpl  implements TaskRecordService {
    @Autowired
    private TaskRecordDao baseDao;

    @Override
    public void add(TaskRecord entity) {
        baseDao.add(entity);
    }

    @Override
    public void delete(TaskRecord entity) {
        baseDao.delete(entity);
    }

    @Override
    public TaskRecord getByTaskIdAndOpenId(long id, String openId) {
        return baseDao.getByTaskIdAndOpenId(id,openId);
    }
}
