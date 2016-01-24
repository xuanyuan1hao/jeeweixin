package com.core.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2016-01-17.
 */
public final class ImageUtils {
    public ImageUtils() {
    }
    /**//*
     * public final static String getPressImgPath() { return ApplicationContext
     * .getRealPath("/template/data/util/shuiyin.gif"); }
     */
    /** *//**
     * 把图片印刷到图片上
     *
     * @param pressImg --
     *            水印文件
     * @param targetImg --
     *            目标文件
     * @param x
     *            --x坐标
     * @param y
     *            --y坐标
     */
    public final static void pressImage(String pressImg, String targetImg,String outPutImg,
                                        int x, int y,boolean isCenter,int pressImgWidth,int pressImgHigh) {
        try {
            //目标文件
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, null);
            //水印文件
            File _filebiao = new File(pressImg);
            Image src_biao = ImageIO.read(_filebiao);
            int wideth_biao = src_biao.getWidth(null);
            int height_biao = src_biao.getHeight(null);
            if(isCenter){
                x=Math.abs(wideth - pressImgWidth) / 2;
                y=Math.abs(height - pressImgHigh) / 2;
            }

            g.drawImage(src_biao.getScaledInstance(pressImgWidth, pressImgHigh,
                            java.awt.Image.SCALE_SMOOTH), x,
                    y, pressImgWidth, pressImgHigh, null);
            //水印文件结束
            g.dispose();
            File outPutImgFile = new File(outPutImg);
            FileOutputStream out = new FileOutputStream(outPutImg);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(image);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /** *//**
     * 打印文字水印图片
     *
     * @param pressText
     *            --文字
     * @param targetImg --
     *            目标图片
     * @param fontName --
     *            字体名
     * @param fontStyle --
     *            字体样式
     * @param color --
     *            字体颜色
     * @param fontSize --
     *            字体大小
     * @param x --
     *            偏移量
     * @param y
     */
    public static void pressText(String markContent, String targetImg,String outPutImg,
                                 String fontName, int fontStyle, int color, int fontSize, int x,
                                 int y) {
        ImageIcon imgIcon = new ImageIcon(targetImg);
        Image theImg = imgIcon.getImage();
        int width = theImg.getWidth(null);
        int height = theImg.getHeight(null);
        BufferedImage bimage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bimage.createGraphics();
        g.setColor(Color.red);
        g.setFont(new Font(fontName, fontStyle, fontSize));
        //g.setBackground(Color.yellow);
        g.drawImage(theImg, 0, 0, null);
        g.drawString(markContent, x, y); // 添加水印的文字和设置水印文字出现的内容
        g.dispose();
        try {
            FileOutputStream out = new FileOutputStream(outPutImg);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
            param.setQuality(0.9f, true);
            encoder.encode(bimage, param);
            out.close();
        } catch (Exception e) {

        }
    }
    public static void main(String[] args) throws Exception {
        String text = "http://www.yihaomen.com";
        //图片叠加
        pressImage("C:/111.jpg", "C:/222.jpg", "C:/outPutImg.jpg", 0, 0,true,100,100);
        //添加文字
        pressText("我是疯子", "C:/outPutImg.jpg", "C:/outPutImg_font.jpg", "宋体", Font.BOLD, 0, 36, 0, 0);
    }
}
