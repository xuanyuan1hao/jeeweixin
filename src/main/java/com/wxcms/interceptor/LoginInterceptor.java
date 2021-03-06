package com.wxcms.interceptor;

import com.core.util.HttpUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by Administrator on 2016-01-20.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    /**
     * 开发者自行处理拦截逻辑，
     * 方便起见，此处只处理includes
     */
    private String[] excludes;//不需要拦截的
    private String[] includes;//需要拦截的


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        boolean oauthFlag = false;//为方便展示的参数，开发者自行处理
        for(String s : includes){
            if(uri.contains(s)){//如果包含，就拦截
                oauthFlag = true;
                break;
            }
        }
        if(!oauthFlag){//如果不需要oauth认证
            return true;
        }
        HttpSession session = request.getSession();
        if(null!=session&&(session.getAttribute("pwd")!=null)){
            return true;
        }
        HttpUtil.redirectUrl(request, response, "/index.html");
        return false;
    }


    public String[] getExcludes() {
        return excludes;
    }

    public void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }

    public String[] getIncludes() {
        return includes;
    }

    public void setIncludes(String[] includes) {
        this.includes = includes;
    }

}
