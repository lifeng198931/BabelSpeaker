package com.babelspeaker.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间帮助�?
 * 
 * @version $Id: DateUtil.java,v 1.1 2008/05/28 04:29:52 linan Exp $
 * @author LiNan
 */
public class DateUtil {

    private Calendar calendar = Calendar.getInstance();

    /**
     * 得到当前的时间，时间格式yyyy-MM-dd
     * 
     * @return
     */
    public String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    /**
     * 得到当前的时�?自定义时间格�?y �?M �?d �?H �?m �?s �?
     * 
     * @param dateFormat
     *            输出显示的时间格�?
     * @return
     */
    public String getCurrentDate(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(new Date());
    }

    /**
     * 日期格式化，默认日期格式yyyy-MM-dd
     * 
     * @param date
     * @return
     */
    public String getFormatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 日期格式化，自定义输出日期格�?
     * 
     * @param date
     * @return
     */
    public String getFormatDate(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(date);
    }

    /**
     * 返回当前日期的前�?��时间日期，amount为正�?当前时间后的时间 为负�?当前时间前的时间 默认日期格式yyyy-MM-dd
     * 
     * @param field
     *            日历字段 y �?M �?d �?H �?m �?s �?
     * @param amount
     *            数量
     * @return �?��日期
     */
    public String getPreDate(String field, int amount) {
        calendar.setTime(new Date());
        if (field != null && !field.equals("")) {
            if (field.equals("y")) {
                calendar.add(calendar.YEAR, amount);
            } else if (field.equals("M")) {
                calendar.add(calendar.MONTH, amount);
            } else if (field.equals("d")) {
                calendar.add(calendar.DAY_OF_MONTH, amount);
            } else if (field.equals("H")) {
                calendar.add(calendar.HOUR, amount);
            }
        } else {
            return null;
        }
        return getFormatDate(calendar.getTime());
    }

    /**
     * 某一个日期的前一个日�?
     * 
     * @param d
     *            ,某一个日�?
     * @param field
     *            日历字段 y �?M �?d �?H �?m �?s �?
     * @param amount
     *            数量
     * @return �?��日期
     */
    public String getPreDate(Date d, String field, int amount) {
        calendar.setTime(d);
        if (field != null && !field.equals("")) {
            if (field.equals("y")) {
                calendar.add(calendar.YEAR, amount);
            } else if (field.equals("M")) {
                calendar.add(calendar.MONTH, amount);
            } else if (field.equals("d")) {
                calendar.add(calendar.DAY_OF_MONTH, amount);
            } else if (field.equals("H")) {
                calendar.add(calendar.HOUR, amount);
            }
        } else {
            return null;
        }
        return getFormatDate(calendar.getTime());
    }

    /**
     * 某一个时间的前一个时�?
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public String getPreDate(String date) throws ParseException {
        Date d = new SimpleDateFormat().parse(date);
        String preD = getPreDate(d, "d", 1);
        Date preDate = new SimpleDateFormat().parse(preD);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(preDate);
    }

}