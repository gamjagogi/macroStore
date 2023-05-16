package com.hjson.macrostore.core.util;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MyDateUtil {
    public static String toStringFormat(LocalDate localDate){
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
