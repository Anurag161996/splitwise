package com.setu.splitwise.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static String convertEpochToFormattedDate(long epochTimestamp) {
        Instant instant = Instant.ofEpochSecond(epochTimestamp); // If the timestamp is in milliseconds, use Instant.ofEpochMilli(epochTimestamp);
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return localDate.format(formatter);
    }

    public static long getStartOfTheDayEpoch(String dateStr) {
        String pattern = "dd/MM/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            LocalDateTime dateTime = localDate.atStartOfDay();
            long epochTimestamp = dateTime.toEpochSecond(ZoneOffset.UTC);
            return epochTimestamp;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Please provide date in DD/MM/YYYY format.");
        }
    }

    public static long getEndOfTheDayEpoch(String dateStr) {
        String pattern = "dd/MM/yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            LocalDateTime dateTime = localDate.atTime(LocalTime.MAX);
            long epochTimestamp = dateTime.toEpochSecond(ZoneOffset.UTC);
            return epochTimestamp;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Please provide date in DD/MM/YYYY format.");
        }
    }
}
