package com.hankx.security.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordHelper {

    private static final Random RANDOM = new Random();

    // 因为伪随机数random在nextInt()的时候会出现向更小的数偏离的情况，所以用一个randomMax来修正
    private static final int randomMax = 100000000;
    // 一共26个字母
    private static final int numberOfLetter = 26;
    // ascii码表数字从48开始
    private static final int nunberIndex = 48;
    // 数组下标0-9代表10个数字
    private static final int numberMax = 9;
    // 大写字母ascii码表从65开始
    private static final int capitalIndex = 65;
    // 小写字母ascii码表从65开始
    private static final int lowercaseIndex = 97;
    // 特殊字符的起始ascii码表序号取第一个数字
    private static final int special = 0;
    // 特殊字符集，第一个数字代表ascii码表序号，第二个代表从这里开始一共使用多少个字符
    private static final int[][] specialCharacter = {{33, 14}, {58, 6}, {91, 5}, {123, 3}};

    // 取随机密码
    public static String generatePassword(int length, String... charTypes) throws Exception {
        StringBuffer buffer = new StringBuffer();
        if (length <= 0)
            throw new Exception("length can not <= 0");
        if (length <= charTypes.length) {
            for (int i = 0; i < length; i++) {
                buffer.append((char) getNextChar(charTypes[i]));
            }
        } else {
            for (String charType : charTypes) {
                buffer.append((char) getNextChar(charType));
            }
            for (int i = 0; i < length - charTypes.length; i++) {
                buffer.append((char) getNextChar(charTypes[RANDOM.nextInt(randomMax) % charTypes.length]));
            }
        }

        return StringUtils.shuffleString(buffer.toString());
    }

    // 取得下一个ascii编码
    private static int getNextChar(String charType) throws Exception {
        switch (charType) {
            case Constant.CHAR_NUMBERIC:
                return RANDOM.nextInt(randomMax) % numberMax + nunberIndex;
            case Constant.CHAR_LOWER:
                return RANDOM.nextInt(randomMax) % numberOfLetter + lowercaseIndex;
            case Constant.CHAR_CAPITAL:
                return RANDOM.nextInt(randomMax) % numberOfLetter + capitalIndex;
            case Constant.CHAR_SPECHARS:
                // 特殊字符的随机集合下标（数组第一维）
                int specialType = RANDOM.nextInt(randomMax) % specialCharacter.length;
                return RANDOM.nextInt(randomMax) % specialCharacter[specialType][1] + specialCharacter[specialType][special];
            default:
                return 48;// assic 0对应10进制
        }
    }

    public static void main(String[] args) {

        System.out.println(System.currentTimeMillis());
        PasswordHelper password = new PasswordHelper();
        for (int i = 0; i < 10000; i++)
            try {
                System.out.println(password.generatePassword(3, "U", "N", "C", "S"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        System.out.println(System.currentTimeMillis());
    }
}