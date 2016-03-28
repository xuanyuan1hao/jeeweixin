package com.wxcms;

import com.core.spring.SpringBeanDefineService;
import com.wxapi.process.WxMemoryCacheClient;
import com.wxcms.domain.Account;
import com.wxcms.domain.WebConfig;
import com.wxcms.mapper.AccountDao;
import com.wxcms.service.WebConfigService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 系统启动时自动加载，把公众号信息加入到缓存中
 */
public class AppDefineInitService implements SpringBeanDefineService {

	@Autowired
	private AccountDao accountDao;
	@Autowired
	private WebConfigService webConfigService;

	public void initApplicationCacheData() {
		Account account = accountDao.getSingleAccount();
		WxMemoryCacheClient.addMpAccount(account);
		WebConfig webConfig=new WebConfig();
		List<WebConfig> webConfigs= webConfigService.listForPage(webConfig);
		webConfigService.addWebConfigListToMemoryCache(webConfigs);
	}
	
}
