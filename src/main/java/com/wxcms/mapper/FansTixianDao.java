package com.wxcms.mapper;

import com.wxcms.domain.FansTixian;

import java.util.List;

/**
 * Created by Administrator on 2016-01-18.
 */
public interface FansTixianDao {
    public FansTixian getById(String id);

    public List<FansTixian> listForPage(FansTixian searchEntity);

    public void add(FansTixian entity);

    public void update(FansTixian entity);
    public void updateStatus(FansTixian entity);

    public void delete(FansTixian entity);

}
