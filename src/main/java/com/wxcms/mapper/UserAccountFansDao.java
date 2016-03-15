package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.UserAccountFans;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016-02-26.
 */
public interface UserAccountFansDao {
    public UserAccountFans getById(String id);

    public UserAccountFans getByOpenId(String openId);

    public Integer getTotalItemsCount(UserAccountFans searchEntity);

    public List<UserAccountFans> paginationEntity(UserAccountFans searchEntity ,Pagination<UserAccountFans> pagination);

    public UserAccountFans getLastOpenId();

    public void add(UserAccountFans entity);

    void updateLastUpdateTime(@Param("openId") String openId,@Param("lastUpdateTime") Date lastUpdateTime);

    void updateUserAccountFans(UserAccountFans userAccountFansWeb);

    void updateAccountFansOldOpenId(@Param("baseOpenId")String baseOpenId,@Param("openId") String openId);

    void deleteByOpenId(@Param("openId") String openId);

    List<UserAccountFans> getAllByLastUpdateTimePage(@Param("lastUpdateTime") Date lastUpdateTime,
                                                     @Param("id")   long id,@Param("accountOld") String accountOld);
}
