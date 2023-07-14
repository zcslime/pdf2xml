package com.quandashi.qz.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author zc
 * @PackageName pdf2xml
 * @Package com.quandashi.qz.utils
 * @Date 2023/4/3 18:07
 * @Version 1.0
 * @Describe 正则匹配
 */

public class RegexUtil {
    public static String getDate(String line){
        Pattern pattern = Pattern.compile("(\\d{4}\\.\\d{2}\\.\\d{2})");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            return matcher.group();
        }else{
            return "";
        }
    }
    public static String getAn(String line){
        Pattern pattern = Pattern.compile("([0-9]*\\.[0-9])|([0-9]*\\.X)");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            return matcher.group();
        }else{
            return "";
        }
    }
    public static String getPn(String line){
        Pattern pattern = Pattern.compile("(CN\\d*[A-Z][0-9])|(CN\\d*[A-Z])");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            return matcher.group();
        }else{
            return "";
        }
    }
    public static String getCountry(String line){
        return line.substring(0,2);
    }
    public static String getkind(String line){
        Pattern pattern = Pattern.compile("([A-Z][0-9]$)|([A-Z]$)");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            return matcher.group();
        }else{
            return "";
        }
    }
    public static String getNumber(String line){

        Pattern pattern = Pattern.compile("(CN\\d*)");
        Matcher matcher = pattern.matcher(line);
        if(matcher.find()){
            String pn = matcher.group();
            return pn.substring(2);
        }else{
            return "";
        }
    }
}
