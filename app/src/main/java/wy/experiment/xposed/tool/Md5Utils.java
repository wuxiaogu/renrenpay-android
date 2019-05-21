package wy.experiment.xposed.tool;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by chenxinyou on 2019/3/7.
 */

public class Md5Utils {
    // 对字符串进行md5加密，比如对密码进行md5加密
    public static String encode(String password) {
        // 得到一个信息摘要器
        MessageDigest digest;
        StringBuffer buffer = new StringBuffer();
        try {
            digest = MessageDigest.getInstance("md5");

            byte[] result = digest.digest(password.getBytes());
            for (byte b : result) {
                // 数组中的每个byte都与11111111二进制位做与运算。
                // 11111111 表示为16进制就是0xff
                int number = b & 0xff;
                // 将number转为16进制
                String str = Integer.toHexString(number);
                // 将不足八位的在前面补一个0
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
                // System.out.println(str);
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }// 如果算法不存在，就会抛算法不存在异常
        // 得到用md5加密后的结果
        // System.out.println(buffer.toString());
        return buffer.toString();// 返回的是对参数加密后的字符串
    }

    // 用md5对文件进行提取特征码，前几年比较流行的杀毒软件的原理就是用到了这个
    public static String getFileMD5(File file) {
        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            StringBuffer buffer = new StringBuffer();
            @SuppressWarnings("resource")
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fis.read(bytes)) != -1) {
                // 用数组对信息摘要器进行更新
                digest.update(bytes, 0, len);
            }
            byte[] result = digest.digest();
            for (byte b : result) {
                int number = b & 0xff;
                String str = Integer.toHexString(number);
                // 如果是单位数，那么前面补零
                if (str.length() == 1) {
                    // 在前面加上一个0
                    buffer.append("0");
                }
                buffer.append(str);
            }
            return buffer.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
