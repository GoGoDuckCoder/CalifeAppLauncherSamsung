package com.banqu.samsung.music.carlifeapplauncher.adapter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encoder {

    private static String toMd5(String str) {

        // 实例化一个指定摘要算法为MD5的MessageDigest对象
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
            // 重置摘要以供再次使用
            algorithm.reset();
            // 使用bytes更新摘要
            algorithm.update(str.getBytes());
            // 使用指定的byte数组对摘要进行最后更新,然后完成摘要计算
            return toHexString(algorithm.digest(), "");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    // SHA加密
    private static String toSHA(String str) {
        // 实例化一个指定摘要算法为SHA的MessageDigest对象
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("SHA");
            // 重置摘要以供再次使用
            algorithm.reset();
            // 使用bytes更新摘要
            algorithm.update(str.getBytes());
            // 使用指定的byte数组对摘要进行最后更新,然后完成摘要计算
            return toHexString(algorithm.digest(), "");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 将字符串中的每个字符转换为十六进制
    private static String toHexString(byte[] bytes, String separtor) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append("0");
            }
            hexString.append(hex).append(separtor);
        }
        return hexString.toString();
    }
}
