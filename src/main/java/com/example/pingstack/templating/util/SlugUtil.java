package com.example.pingstack.templating.util;


import org.springframework.stereotype.Component;


public class SlugUtil {

    public static String toSlug(String text){
        if(text == null || text.isBlank()){
            return "";
        }

        return text
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-");
    }
}
