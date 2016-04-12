package com.wxcms.service;

import com.core.page.Pagination;
import com.wxcms.domain.UserNewsTaskArticle;

import java.util.List;

public interface UserNewsTaskArticleService {

    public UserNewsTaskArticle add(UserNewsTaskArticle entity);

    public UserNewsTaskArticle getById(long id);

    public List<UserNewsTaskArticle> listForPage(UserNewsTaskArticle searchEntity);

    public Pagination<UserNewsTaskArticle> paginationEntity(UserNewsTaskArticle searchEntity, Pagination<UserNewsTaskArticle> pagination);

    public Integer getTotalItemsCount(UserNewsTaskArticle searchEntity);

    public void update(UserNewsTaskArticle entity);//更新

    public void delete(UserNewsTaskArticle entity);

    boolean isExist(long articleId, long newsTaskId);
}
