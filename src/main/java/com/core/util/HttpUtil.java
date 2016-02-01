package com.core.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

/**
 * http工具类
 */
public class HttpUtil  extends org.springframework.web.util.WebUtils{
	
	public static String getDomain(HttpServletRequest request){
		return request.getServerName();
	}
	
	public static String getHttpDomain(HttpServletRequest request){
		return request.getScheme() + "://" + request.getServerName();
	}
	
	public static String getContextHttpUri(HttpServletRequest request){
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
	
	public static String getRealPath(HttpServletRequest request){
		return request.getSession().getServletContext().getRealPath("/");
	}
	
	public static String getRequestFullUri(HttpServletRequest request){
		String port = "";
		if(request.getServerPort() != 80){
			port = ":" + request.getServerPort();
		}
		Random random = new Random(9999);
		String strBackUrl = "http://" + request.getServerName() //服务器地址
				+ port          //端口号
				+ request.getContextPath()      //项目名称
				+ request.getServletPath()      //请求页面或其他地址
				+ "?" + (request.getQueryString()) //参数
				+"&no="+random.nextInt();//随机参数
		return strBackUrl;
	}
	
	public static String getRequestFullUriNoContextPath(HttpServletRequest request){
		String port = "";
		if(request.getServerPort() != 80){
			port = ":" + request.getServerPort();
		}
		return request.getScheme() + "://" + request.getServerName() + port + request.getServletPath();
	}
	
	//获取ip地址；
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			if(ip.indexOf("::ffff:")!=-1) ip = ip.replace("::ffff:", "");
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}
	
	//判断当前请求是否为Ajax
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String header = request.getHeader("X-Requested-With");
		return !StringUtils.isEmpty(header) && "XMLHttpRequest".equals(header);
	}
	
	/**
	 * 重定向
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param url
	 */
	public static void redirectUrl(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,String url){
		try {
			httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 重定向到http://的url
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @param url
	 */
	public static void redirectHttpUrl(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,String url){
		try {
			httpServletResponse.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
