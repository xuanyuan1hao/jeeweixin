package com.core.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.UUID;
/**
 * 附件上传
 * 
 */
public class UploadUtil {
	
	public static String doUpload(String realPath ,MultipartFile file){
		return doUpload(realPath,"/res/upload/",file);
	}
	
	public static String doUpload(String realPath ,String modulePath ,MultipartFile file){
		String fileName = file.getOriginalFilename();  
		if(!StringUtils.isEmpty(fileName)){
			String ext = fileName.substring(fileName.indexOf("."));
			fileName = Calendar.getInstance().getTimeInMillis() + ext;
			
			File targetFile = new File(realPath + modulePath + fileName);  
	        if(!targetFile.exists()){
	            targetFile.mkdirs();
	        }  
	        //保存
	        try {
	            file.transferTo(targetFile);  
	        } catch (Exception e) {
	            e.printStackTrace();  
	        } 
	        return "/res/upload/" + fileName;
		}
		return null;
	}
	public static boolean download(String urlString, String filename,String savePath) {
		try {
			// 构造URL
			URL url = new URL(urlString);
			// 打开连接
			URLConnection con = url.openConnection();
			//设置请求超时为5s
			con.setConnectTimeout(5*1000);
			// 输入流
			InputStream is = con.getInputStream();

			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			File sf=new File(savePath);
			if(!sf.exists()){
				sf.mkdirs();
			}
			OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);
			boolean isZero=true;
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
				isZero=false;
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();
			if(!isZero)
				return true;
		}catch (Exception ex){

		}
		return false;
	}
	public static String byteToImg(String realPath ,byte[] bytes){
		if(bytes != null && bytes.length > 0){
			String imagePath = "/res/upload/" + UUID.randomUUID().toString() + ".jpg";
			FileImageOutputStream imageOutput;
			try {
				File file = new File(realPath + imagePath);
				file.createNewFile();
				imageOutput = new FileImageOutputStream(file);
				imageOutput.write(bytes, 0, bytes.length);  
			 	imageOutput.close(); 
				return imagePath;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} 
		}
		return null;
	}
	
}

