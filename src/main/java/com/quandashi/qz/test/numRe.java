package com.quandashi.qz.test;

import com.quandashi.qz.utils.RegexUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author zc
 * @PackageName pdf2xml
 * @Package com.quandashi.qz.test
 * @Date 2023/4/3 17:15
 * @Version 1.0
 * @Describe
 */

public class numRe {
    public static void main(String[] args) {
        String aa = "(21)申请号 201911035639.21Huang等.“MK558823.1”A，spergillus";
        String bb = "(22)申请日 2019.10.29cejpii isolate FS110 transcription factor";
        String cc = "CN110950940B";
        String number = RegexUtil.getNumber(cc);
        System.out.println(number);
        Pattern pattern = Pattern.compile("(CN\\d*)");
        Matcher matcher = pattern.matcher(cc);
        if(matcher.find()){
            System.out.println(matcher.group());
        }else{
        }

    }
}
