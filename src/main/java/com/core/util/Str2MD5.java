package com.core.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Str2MD5 {

    public static void main(String[] args) {
        System.out.println(MD5("sunzn"));
        System.out.println(MD5("sunzn", 16));
    }

    /***
     * 默认32位加密。length传递16则为16位加密
     * @param sourceStr
     * @param length
     * @return
     */
    public static String MD5(String sourceStr,int length) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            if(length==16){
                result=buf.toString().substring(8, 24);
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }
    private static String MD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            System.out.println("MD5(" + sourceStr + ",32) = " + result);
            System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return result;
    }
}