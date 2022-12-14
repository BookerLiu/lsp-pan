package com.booker.lsp;

import java.io.*;
import java.nio.channels.*;
import java.util.UUID;

/**
 * @Author BookerLiu
 * @Date 2022/11/12 15:54
 * @Description
 **/
public class Test {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        spilt("C:\\Users\\Fei\\Videos\\Captures\\The Survivalists 2022-08-07 15-16-29.mp4", 35, "D:/文件分割");
        merge("D:/文件分割", "D:/文件分割.mp4");
    }

    public static void spilt(String from, int size, String to) throws IOException {
        File f = new File(from);
        FileInputStream in = new FileInputStream(f);
        FileOutputStream out = null;
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = null;

        // 将MB单位转为为字节B
        long m = size * 1024 * 1024;
        // 计算最终会分成几个文件
        int count = (int) (f.length() / m);
        // System.out.println(f.length() + " " + m + " " + count);
        for (int i = 0; i <= count; i++) {
            // 生成文件的路径
            String t = to + "/" + i + ".block";
            try {
                out = new FileOutputStream(new File(t));
                outChannel = out.getChannel();
                // 从inChannel的m*i处，读取固定长度的数据，写入outChannel
                if (i != count)
                    inChannel.transferTo(m * i, m, outChannel);
                else// 最后一个文件，大小不固定，所以需要重新计算长度
                    inChannel.transferTo(m * i, f.length() - m * count, outChannel);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                out.close();
                outChannel.close();
            }
        }
        in.close();
        inChannel.close();
    }

    public static void merge(String from, String to) throws IOException {
        File t = new File(to);
        FileInputStream in = null;
        FileChannel inChannel = null;

        FileOutputStream out = new FileOutputStream(t, true);
        FileChannel outChannel = out.getChannel();

        File f = new File(from);
        // 获取目录下的每一个文件名，再将每个文件一次写入目标文件
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            // 记录新文件最后一个数据的位置
            long start = 0;
            for (File file : files) {

                in = new FileInputStream(file);
                inChannel = in.getChannel();

                // 从inChannel中读取file.length()长度的数据，写入outChannel的start处
                outChannel.transferFrom(inChannel, start, file.length());
                start += file.length();
                in.close();
                inChannel.close();
            }
        }
        out.close();
        outChannel.close();
    }

}
