package com.hankx.security.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static String getEcho(String password){

        String echo = "";

        if (password != null && !password.equals("")){

            int length = password.length();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i=0;i<length;i++){
                stringBuffer.append(Constant.PASS_WORD_ECHO);
            }

            echo = stringBuffer.toString();
        }

        return echo;
    }

    /**
     * 打乱字符串
     * @param str
     * @return
     */
    public static String shuffleString(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        if(isBlank(str)){
            return "";
        }
        List<String> charList = Arrays.asList(str.split(""));
        Collections.shuffle(charList);
        for (String s : charList) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    public static String getHostDomain(String url){
        if(isBlank(url)){
            throw new IllegalArgumentException("url 不能为空");
        }
        //定义好获取的域名后缀。如果还有要定义的	请添加 |(\\.域名的后缀) 。
        String regStr="[0-9a-zA-Z]+((\\.com)|(\\.cn)|(\\.org)|(\\.net)|(\\.edu)|(\\.com.cn)|(\\.xyz)|(\\.xin)|(\\.club)|(\\.shop)|(\\.site)|(\\.wang)" +
                "|(\\.top)|(\\.win)|(\\.online)|(\\.tech)|(\\.store)|(\\.bid)|(\\.cc)|(\\.ren)|(\\.lol)|(\\.pro)|(\\.red)|(\\.kim)|(\\.space)|(\\.link)|(\\.click)|(\\.news)|(\\.news)|(\\.ltd)|(\\.website)" +
                "|(\\.biz)|(\\.help)|(\\.mom)|(\\.work)|(\\.date)|(\\.loan)|(\\.mobi)|(\\.live)|(\\.studio)|(\\.info)|(\\.pics)|(\\.photo)|(\\.trade)|(\\.vc)|(\\.party)|(\\.game)|(\\.rocks)|(\\.band)" +
                "|(\\.gift)|(\\.wiki)|(\\.design)|(\\.software)|(\\.social)|(\\.lawyer)|(\\.engineer)|(\\.org)|(\\.net.cn)|(\\.org.cn)|(\\.gov.cn)|(\\.name)|(\\.tv)|(\\.me)|(\\.asia)|(\\.co)|(\\.press)|(\\.video)|(\\.market)" +
                "|(\\.games)|(\\.science)|(\\.中国)|(\\.公司)|(\\.网络)|(\\.pub)" +
                "|(\\.la)|(\\.auction)|(\\.email)|(\\.sex)|(\\.sexy)|(\\.one)|(\\.host)|(\\.rent)|(\\.fans)|(\\.cn.com)|(\\.life)|(\\.cool)|(\\.run)" +
                "|(\\.gold)|(\\.rip)|(\\.ceo)|(\\.sale)|(\\.hk)|(\\.io)|(\\.gg)|(\\.tm)|(\\.com.hk)|(\\.gs)|(\\.us))";
        Pattern p = Pattern.compile(regStr);
        Matcher m = p.matcher(url);
        String domain = "";
        //获取一级域名
        while(m.find()){
            domain = m.group();
        }
        return domain;  //substringBefore(domain, ".");
    }

    public static void main(String[] args) {
        String url = "http://anotherbug.blog.chinajavaworld.com/entry/4545/0/";
        url = "JavaDe";
        System.out.printf(getHostDomain(url));
    }

}
