package com.core.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-01-31.
 */
public class QRCodeUtils {

    public static boolean createQrEncode( String filePath, String fileName, String content,int width,int height){
        String format = "png";// 图像类型
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = null;// 生成矩阵
        try {
            bitMatrix = new MultiFormatWriter().encode(content,
                    BarcodeFormat.QR_CODE, width, height, hints);
            mkDir(new File(filePath));
            Path path = FileSystems.getDefault().getPath(filePath, fileName);
            MatrixToImageWriter.writeToPath(bitMatrix, format, path);// 输出图像
            System.out.println("输出成功.");
            return  true;
        } catch (Exception e) {
            e.printStackTrace();
        }
       return false;
    }
    private static void mkDir(File file){
        if(file.getParentFile().exists()){
            file.mkdir();
        }else{
            mkDir(file.getParentFile());
            file.mkdir();
        }
    }
    public static String qrImgDecode(String filePath) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码
            return result.getText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args){
        /*createQrEncode("D://","zxing.png","http://kesun-shy.iteye.com/blog/2154169",200,200);
        String content=qrImgDecode("D://"+"zxing.png");
        System.out.println(content);*/


        //正则表达式测试
        //获取上级信息
        String tmpContent="领福利234324";
        Pattern p=Pattern.compile("(\\d+)");
        Matcher m=p.matcher(tmpContent);
        if(m.find()){
            System.out.println(m.group(1));
        }
    }
}
