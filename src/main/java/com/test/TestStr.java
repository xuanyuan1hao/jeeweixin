package com.test;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * Created by Administrator on 2016-04-26.
 */
public class TestStr {
    public static void main(String args[]){
       System.out.println(StringEscapeUtils.unescapeHtml("♬性格测试： &#8220;日”字加一笔，你最先想到什么字你就是什么人？"));
    }
}
