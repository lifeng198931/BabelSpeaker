package com.babelspeaker.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.babelspeaker.data.InitData;

/**
 * 判断交易时间
 * 
 * @author mark
 * 
 */
public class Cal {

    private static Cal mInstance;

    public static Cal getInstance() {
        if (mInstance == null) {
            mInstance = new Cal();
        }
        return mInstance;
    }

    // 获取当天时间
    public String getDate(String dateFormat) {
        try {
            // Log.d("gtDate", dateFormat);
            Date now = new Date();
            long diff = now.getTime() - InitData.LOCAL_TIME.getTime();
            long currentTime = InitData.SERVER_TIME.getTime() + diff;
            Date current = new Date(currentTime);
            SimpleDateFormat date = new SimpleDateFormat(dateFormat,
                    Locale.SIMPLIFIED_CHINESE);// 可以方便地修改日期格式
            String dateStr = date.format(current);
            // Log.d("dateStr", dateStr);
            return dateStr;
        } catch (Exception e) {
            return getDate_ex(dateFormat);
        }
    }

    // 获取当天时间
    public String getDate_ex(String dateFormat) {
        Date now = new Date();
        SimpleDateFormat date = new SimpleDateFormat(dateFormat,
                Locale.SIMPLIFIED_CHINESE);// 可以方便地修改日期格式
        String dateStr = date.format(now);
        return dateStr;
    }

    // 获取当天时间
    public String getWeek(String dateFormat, String dat) {
        Date now = stringToDate(dat);
        SimpleDateFormat date = new SimpleDateFormat(dateFormat,
                Locale.SIMPLIFIED_CHINESE);// 可以方便地修改日期格式
        String dateStr = date.format(now);
        return dateStr;
    }

    public String lastDay(String dateFormat, String dat) {
        Date d = stringToDate(dat);
        SimpleDateFormat df = new SimpleDateFormat(dateFormat,
                Locale.SIMPLIFIED_CHINESE);// 可以方便地修改日期格式
        String dateStr = df.format(new Date(d.getTime() - 1 * 24 * 60 * 60
                * 1000));
        return dateStr;
    }

    public String nextDay(String dateFormat, String dat) {
        Date d = stringToDate(dat);
        SimpleDateFormat df = new SimpleDateFormat(dateFormat,
                Locale.SIMPLIFIED_CHINESE);// 可以方便地修改日期格式
        String dateStr = df.format(new Date(d.getTime() + 1 * 24 * 60 * 60
                * 1000));
        return dateStr;
    }

    public Date stringToDate(String str) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            // Fri Feb 24 00:00:00 CST 2012
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 2012-02-24
        date = java.sql.Date.valueOf(str);
        return date;
    }

    private int changeWeekToInt(String week) {

        int intWeek = 0;
        if (week.equals("周一") || week.equals("星期一")) {
            intWeek = 1;
        } else if (week.equals("周二") || week.equals("星期二")) {
            intWeek = 2;
        } else if (week.equals("周三") || week.equals("星期三")) {
            intWeek = 3;
        } else if (week.equals("周四") || week.equals("星期四")) {
            intWeek = 4;
        } else if (week.equals("周五") || week.equals("星期五")) {
            intWeek = 5;
        } else if (week.equals("周六") || week.equals("星期六")) {
            intWeek = 6;
        } else if (week.equals("周日") || week.equals("星期日")) {
            intWeek = 0;
        }
        return intWeek;
    }

    // 将秒数转换成时间字符串
    public String secToString(String inOnlineTime) {
        // TODO
        // long loginTime = MainSingleton.getInstance().getLoginTime();
        // long now = (new Date()).getTime();
        // long sec = Long.parseLong(inOnlineTime) + (now - loginTime) / 1000;

        String ret = "";
        // int days = (int) (sec / (24 * 60 * 60));
        // if (days > 0) {
        // ret = String.valueOf(days) + "天";
        // }
        // sec = sec % (24 * 60 * 60);
        // int hours = (int) (sec / (60 * 60));
        // ret += String.valueOf(hours) + "小时";
        //
        // sec = sec % (60 * 60);
        // int minutes = (int) (sec / 60);
        // ret += String.valueOf(minutes) + "分";
        //
        // sec = sec % 60;
        // ret += String.valueOf(sec) + "秒";

        return ret;
    }

    // 将秒数转换成分秒字符串
    public String secToMS(int sec) {

        String ret = "";

        int minutes = (int) (sec / 60);
        ret += String.valueOf(minutes) + "分";

        sec = sec % 60;
        ret += String.valueOf(sec) + "秒";

        return ret;
    }


    public String getEndTime(String startTime, String song, String packageId) {
        String ret = "";
        try {
            int pkgId = Integer.valueOf(packageId);
            int so = Integer.valueOf(song);
            DateUtil du = new DateUtil();
            // Date dt = du.get
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");

            Date start = formatter.parse(startTime);

            switch (pkgId) {
            case 0:
                // 1个月
                ret = du.getPreDate(start, "M", 1);
                ret = du.getPreDate(formatter.parse(ret), "d", so);
                break;
            case 1:
                // 1周
                ret = du.getPreDate(start, "d", 7 + so);
                break;
            case 2:
                // 1月
                ret = du.getPreDate(start, "M", 1);
                ret = du.getPreDate(formatter.parse(ret), "d", so);
                break;
            case 3:
                // 3月
                ret = du.getPreDate(start, "M", 3);
                ret = du.getPreDate(formatter.parse(ret), "d", so);
                break;
            case 4:
                // 6月
                ret = du.getPreDate(start, "M", 6);
                ret = du.getPreDate(formatter.parse(ret), "d", so);
                break;
            case 5:
                // 1年
                ret = du.getPreDate(start, "y", 1);
                ret = du.getPreDate(formatter.parse(ret), "d", so);
                break;
            case 6:
                // 1周
                ret = du.getPreDate(start, "d", 7 + so);
                break;
            case 7:
                // 1月
                ret = du.getPreDate(start, "M", 1);
                ret = du.getPreDate(formatter.parse(ret), "d", so);
                break;
            case 8:
                // 3月
                ret = du.getPreDate(start, "M", 3);
                ret = du.getPreDate(formatter.parse(ret), "d", so);
                break;
            case 9:
                // 6月
                ret = du.getPreDate(start, "M", 6);
                ret = du.getPreDate(formatter.parse(ret), "d", so);
                break;
            case 10:
                // 1年
                ret = du.getPreDate(start, "y", 1);
                ret = du.getPreDate(formatter.parse(ret), "d", so);
                break;
            }

        } catch (Exception e) {
            //
            e.printStackTrace();
        }
        return ret;
    }
}
