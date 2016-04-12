package com.wxcms.mapper;

import com.core.page.Pagination;
import com.wxcms.domain.UserNewsTaskArticle;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016-04-10.
 */
public interface UserNewsTaskArticleDao {
    public void add(UserNewsTaskArticle entity);

    public UserNewsTaskArticle getById(long id);

    public List<UserNewsTaskArticle> listForPage(UserNewsTaskArticle searchEntity);

    public List<UserNewsTaskArticle> paginationEntity(UserNewsTaskArticle searchEntity, Pagination<UserNewsTaskArticle> pagination);

    public Integer getTotalItemsCount(UserNewsTaskArticle searchEntity);

    public void update(UserNewsTaskArticle entity);

    public void delete(UserNewsTaskArticle entity);

    boolean isExistByArticleIdAndNewsTaskId(@Param("articleId") long articleId,@Param("newsTaskId") long newsTaskId);
}
