package com.atguigu.srb.core.hfb;

import java.util.Iterator;
import java.util.Map;

/**
 * projectName: gitclone
 *
 * @author: GOD伟
 * time: 2022/6/29 12:33 周三
 * description:
 */
public class FormHelper {
    public static String buildForm(String url, Map<String ,Object> paramMap){
        StringBuffer inputStr = new StringBuffer();
        Iterator<Map.Entry<String, Object>> entries = paramMap.entrySet().iterator();
        while (entries.hasNext()){
            Map.Entry<String,Object> entry=entries.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            inputStr.append("<input type='hidden' name='"+key+"' value='"+value+"'/>");
        }

        String formStr = "<!DOCTYPE html>\n" +
                "<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "<form name=\"form\" action=\""+url+"\" method=\"post\">\n"+

                inputStr +

                "</form>\n" +
                "<script>\n" +
                "\tdocument.form.submit();\n" +
                "</script>\n" +
                "</body>\n" +
                "</html>";
        return formStr;
    }
}