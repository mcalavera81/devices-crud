package com.example.demo.device.utils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static String formatDate(OffsetDateTime offsetDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX").withZone(ZoneOffset.UTC);
        return offsetDateTime.format(formatter);


    }
}
