package com.core.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
/**
 * Created by kaka.li on 2016-03-09.
 */
public class FtpUtils {

    private  FTPClient ftp;
    /**
     *
     * @param path 上传到ftp服务器哪个路径下
     * @param addr 地址
     * @param port 端口号
     * @param username 用户名
     * @param password 密码
     * @return
     * @throws Exception
     */
    private  boolean connect(String path,String addr,int port,String username,String password) throws Exception {
        boolean result = false;
        ftp = new FTPClient();
        int reply;
        ftp.connect(addr, port);
        ftp.login(username, password);
        ftp.setControlEncoding("GBK");
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        //ftp.setFileType(FTP.BINARY_FILE_TYPE);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return result;
        }
        ftp.changeWorkingDirectory(path);
        result = true;
        return result;
    }
    /**
     *
     * @param file 上传的文件或文件夹
     * @param remotePath 远程服务器图片的存储路径。用左斜杠来分割
     * @throws Exception
     */
    private void upload(File file,String remotePath) throws Exception{
        if(null!=remotePath&&remotePath.contains("/")){
            String [] dir=remotePath.split("/");
            for (int i=0;i<dir.length;i++){
                File fileDictory = new File(dir[i]);
                ftp.makeDirectory(fileDictory.getName());
                ftp.changeWorkingDirectory(fileDictory.getName());
            }
        }
        if(file.isDirectory()){
            ftp.makeDirectory(file.getName());
            ftp.changeWorkingDirectory(file.getName());
            String[] files = file.list();
            for (int i = 0; i < files.length; i++) {
                File file1 = new File(file.getPath()+"\\"+files[i] );
                if(file1.isDirectory()){
                    upload(file1,remotePath);
                    ftp.changeToParentDirectory();
                }else{
                    File file2 = new File(file.getPath()+"\\"+files[i]);
                    FileInputStream input = new FileInputStream(file2);
                    ftp.storeFile(file2.getName(), input);
                    input.close();
                }
            }
        }else{
            File file2 = new File(file.getPath());
            FileInputStream input = new FileInputStream(file2);
            ftp.storeFile(file2.getName(), input);
            input.close();
        }
    }
    public static void main(String[] args) throws Exception{
        FtpUtils t = new FtpUtils();
        t.connect("", "localhost", 21, "a", "a");
        File file = new File("D:\\soft\\ftp\\jb51.net.txt");
        t.upload(file,"/abc/bb/");
    }
}