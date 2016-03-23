package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.TaskCode;
import com.wxcms.domain.TaskLog;
import com.wxcms.domain.UserAccountFans;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-02-27.
 */
public interface UserAccountFansService {

    void updateLastUpdateTime(String openId, Date date);
    public UserAccountFans getById(String id);

    public UserAccountFans getByOpenId(String openId);

    public Integer getTotalItemsCount(UserAccountFans searchEntity);

    public List<UserAccountFans> paginationEntity(UserAccountFans searchEntity ,Pagination<UserAccountFans> pagination);

    public UserAccountFans getLastOpenId();

    public void add(UserAccountFans entity);

    public List<UserAccountFans> getAllByLastUpdateTimePage(Date lastUpdateTime,long  id,String accountOld);

    void updateUserAccountFans(UserAccountFans userAccountFansWeb,TaskCode taskCode,TaskLog acceptTaskFansopenId);
    void updateSubUserAccountFans(UserAccountFans userAccountFans,TaskCode taskCode);

    void deleteByOpenId(String openId);
}
