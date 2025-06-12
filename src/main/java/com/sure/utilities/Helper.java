package com.sure.utilities;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Log4j2
public final class Helper {
    private Helper() {
    }

    public static String generateRandomNumberByTimeAndDate() {
        String data = "" + java.time.LocalDate.now().getDayOfMonth() + java.time.LocalDate.now().getMonthValue()
                + java.time.LocalDate.now().getYear();
        String time = "" + java.time.LocalTime.now().getHour() + java.time.LocalTime.now().getMinute()
                + java.time.LocalTime.now().getSecond();
        return data + time;
    }

    public static String generateRandomDate() {
        String todayDate = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        log.info("Today'sDate :{}", todayDate);
        return todayDate;
    }

    public static String getSectorValueAfterSplitText(String text, String separator, int index) {

        String[] fullText = text.split(separator);
        return fullText[index].trim();

    }

    public static String addDays(String date, int days) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd 00:00:01");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(simpledateformat.parse(date));
        } catch (ParseException e) {
            log.error(e);
        }
        c.add(Calendar.DATE, days); // number of days to add
        date = simpledateformat.format(c.getTime()); // date is now the new date

        return date;
    }

    public static String getRandomString(int length) {
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }


}
