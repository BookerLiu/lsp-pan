package com.booker.lsp.util;

import cn.hutool.core.codec.Base64;
import com.booker.lsp.constant.PropConfConst;
import lombok.extern.log4j.Log4j2;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.ScreenExtractor;
import ws.schild.jave.info.MultimediaInfo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

/**
 * @Author BookerLiu
 * @Date 2022/11/17 10:50
 * @Description 文件工具类
 **/

@Log4j2
@Component
public class FileUtil extends cn.hutool.core.io.FileUtil {


    private final static String PREFIX_VIDEO = "video/";


    /**
     * 关闭文件流
     *
     * @param closeables 文件流
     */
    public static void closeStream(Closeable... closeables) {
        if (closeables != null && closeables.length > 0) {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    try {
                        closeable.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 判断文件是否为音频/视频文件
     *
     * @param filePath 文件路径
     * @return MultimediaObject or null
     */
    public static MultimediaInfo isAVFile(String filePath) {
        try {
            MultimediaObject object = new MultimediaObject(new File(filePath));
            return object.getInfo();
        } catch (Exception e) {
//            log.error("判断av文件异常", e);
        }
        return null;
    }

    /**
     * 视频截取
     *
     * @param filePath 原视频绝对路径
     * @param millis   截取的毫秒
     * @throws EncoderException
     */
    public static File screenVideoImage(String filePath, int millis) throws EncoderException {
        MultimediaObject multimediaObject = new MultimediaObject(new File(filePath));
        ScreenExtractor screenExtractor = new ScreenExtractor();
        String tempFilePath = PropConfConst.TEMP_DIR + File.separator + UUID.randomUUID().toString() + ".png";
//        String tempFilePath = "D:" + File.separator + UUID.randomUUID().toString() + ".png";
        File file = new File(tempFilePath);
        screenExtractor.renderOneImage(multimediaObject, -1, -1, millis, file, 1);
        return file;
    }


    /**
     * 图片转base64
     *
     * @param file
     * @return
     */
    public static String getImgBase64(File file) {
        String head = "data:image/" + getSuffix(file) + ";base64,";
        return head + Base64.encode(file);

    }

    /**
     * 获取指定时间位置截屏的base64
     *
     * @param filePath 视频文件路径
     * @param millis   指定时间位置 毫秒
     * @return
     * @throws EncoderException
     */
    public static String getScreenBase64(String filePath, int millis) throws EncoderException {
        try {
            File file = screenVideoImage(filePath, millis);
            String imgBase64 = getImgBase64(file);
            del(file);
            return imgBase64;
        } catch (Exception e ) {
            log.info("获取截图失败", e);
        }
        return null;
    }


    /**
     * 判断文件是否是图片
     *
     * @param file in
     * @return
     */
    public static boolean isImage(File file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
            if (image == null || image.getWidth() <= 0 || image.getHeight() <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 获取无后缀文件名
     * @param filePath 文件路径
     * @return
     */
    public static String getNoSufName(String filePath) {
        String name = getName(filePath);
        if (name.contains(".")) {
            return name.substring(0, name.lastIndexOf("."));
        }
        return name;
    }

    public static BufferedImage resize(InputStream inputStream, int width, int height) {
        try {
            BufferedImage img = ImageIO.read(inputStream); // load image
            return resize(img, width, height);
        } catch (Exception e) {
            log.info("inputStream图片缩放失败", e);
            return null;
        }
    }

    public static BufferedImage resize(File file, int width, int height) {
        try {
            BufferedImage img = ImageIO.read(file); // load image
            return resize(img, width, height);
        } catch (Exception e) {
            log.info("file图片缩放失败", e);
            return null;
        }
    }

    public static BufferedImage resize(BufferedImage img, int width, int height) {
        try {
            return Scalr.resize(img, Scalr.Method.QUALITY,
                    width, height, Scalr.OP_ANTIALIAS);
        } catch (Exception e) {
            log.info("img图片缩放失败", e);
            return null;
        }
    }






    /**
     * 转base64
     * @param img BufferedImage
     * @param formatName 图片格式
     * @return
     */
    public static String imgToBase64String(BufferedImage img, final String formatName) {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            ImageIO.write(img, formatName, os);
            String head = "data:image/" + formatName + ";base64,";
            return head + Base64.encode(os.toByteArray());
        } catch (Exception e) {
            log.info("b img 转base64失败", e);
            return null;
        }
    }


    /**
     * 删除空目录及其子空目录
     * @param dirPath 目录路径
     */
    public static void deleteEmptyDirs(String dirPath) {
        File dirFile = new File(dirPath);
        if (!dirFile.exists() || !dirFile.isDirectory()) return;

        File[] files = dirFile.listFiles();
        if (files.length == 0) {
            dirFile.delete();
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteEmptyDirs(file.getAbsolutePath());
            }
        }
    }


    public static void main(String[] args) throws EncoderException {
        File file = new File("C:\\Users\\Fei\\Downloads\\下载.png");
//        MultimediaObject object = new MultimediaObject(file);
//        MultimediaInfo info = object.getInfo();

//        File file1 = screenVideoImage(file.getAbsolutePath(), 5000);

        System.out.println(Base64.encode(file));
//        System.out.println(getScreenBase64(file.getAbsolutePath(), 5000));
    }

}
