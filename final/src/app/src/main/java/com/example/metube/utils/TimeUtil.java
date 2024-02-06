package com.example.metube.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtil {
    public static String getTimeDistance(Date publishDate) {
        LocalDateTime newPublishDate = publishDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        LocalDateTime current = LocalDateTime.now();

        Duration duration = Duration.between(newPublishDate, current);

        Period period = Period.between(newPublishDate.toLocalDate(), current.toLocalDate());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createdDateTimeFormatted = newPublishDate.format(formatter);

        if (duration.toMinutes() < 1) {
            return "Just now";
        } else if (duration.toHours() < 1) {
            long minutesAgo = duration.toMinutes();
            return minutesAgo + " minutes ago";
        } else if (duration.toDays() < 1) {
            long hoursAgo = duration.toHours();
            return hoursAgo + " hours ago";
        } else if (period.getMonths() < 1 && period.getYears() < 1) {
            long daysAgo = duration.toDays();
            return daysAgo + " days ago";
        } else if (period.getYears() < 1) {
            int monthsAgo = period.getMonths() + (period.getYears() * 12);
            return monthsAgo + " months ago";
        } else {
            return createdDateTimeFormatted;
        }
    }
}
