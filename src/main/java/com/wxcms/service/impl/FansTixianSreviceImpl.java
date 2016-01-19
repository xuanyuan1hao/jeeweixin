package com.wxcms.service.impl;

import com.wxcms.domain.FansTixian;
import com.wxcms.mapper.FansTixianDao;
import com.wxcms.service.FansTixianSrevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FansTixianSreviceImpl implements FansTixianSrevice {

    @Autowired
    private FansTixianDao entityDao;

    public FansTixian getById(String id) {
        return entityDao.getById(id);
    }

    public List<FansTixian> listForPage(FansTixian searchEntity) {
        return entityDao.listForPage(searchEntity);
    }

    public void add(FansTixian entity) {
        entityDao.add(entity);
    }

    public void update(FansTixian entity) {
        entityDao.update(entity);
    }

    public void updateStatus(FansTixian entity) {
        entityDao.updateStatus(entity);
    }

    public void delete(FansTixian entity) {
        entityDao.delete(entity);
    }
}
