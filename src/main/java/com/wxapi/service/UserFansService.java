package com.wxapi.service;

import com.wxapi.vo.MsgRequest;
import com.wxcms.domain.TaskCode;

/**
 * Created by Administrator on 2016-02-27.
 */
public interface UserFansService {
    String processMsg(MsgRequest msgRequest, TaskCode taskCode, String webRootPath, String webUrl);
}
