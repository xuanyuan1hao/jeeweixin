package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.TaskLog;
import com.wxcms.domain.UserInfo;

import java.util.List;

/**
 * Created by Administrator on 2016-02-07.
 */
public interface UserInfoService {
    public UserInfo getById(long id);

    public UserInfo getByEmail(UserInfo searchEntity);

    public List<UserInfo> listForPage(UserInfo searchEntity);

    public Pagination<UserInfo> paginationEntity(UserInfo searchEntity, Pagination<UserInfo> pagination);

    public void add(UserInfo entity);

    public void update(UserInfo entity);//更新密码

    public void  updateUserMoney(UserInfo entity);//加钱或者减钱

    public void updateUserForzenedMoney(UserInfo entity);//加冻结的钱或者减掉冻结的钱（主要用于提现）

    public void delete(UserInfo entity);


    void updateUserMoneyByTask(long id, long userId, TaskLog taskLog);
}
