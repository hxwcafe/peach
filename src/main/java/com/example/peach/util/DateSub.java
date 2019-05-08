package com.example.peach.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/** 天数差(与系统时间相比)
 * Created by Administrator on 2018/12/5.
 */
public class DateSub {

    public static long getDaySub(Date date) {
        if(date==null){
            return 0;
        }
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String sysDate = format.format(new Date());
        try {
            Date dates = format.parse(sysDate);
            day = (date.getTime() - dates.getTime()) / (24 * 60 * 60 * 1000);
            //System.out.println("相隔的天数="+day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }
//    public static getHostSub(Timestamp data){
//
//    }
    /**
     *  分钟差(与当前时间相比)
     */
    public static long getMinSub(Timestamp date) {
        long day = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String systime=    format.format(new Date());
        try {
            Date sysdate = format.parse(systime);
            day=(date.getTime()-sysdate.getTime())/(1000*60);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return day;
    }

    public static long getHourSub(Date date) {
        long hour = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String systime= format.format(new Date());
        try {
            Date sysdate = format.parse(systime);
            hour=(sysdate.getTime()-date.getTime())/(1000*60*60);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return hour;
    }

    /**
     * 年份差(当前时间)
     * @param date
     * @return
     */
    public static int getYear(Date date){
        Calendar from  =  Calendar.getInstance();
        from.setTime(date);
        Calendar  to  =  Calendar.getInstance();
        to.setTime(new Date());
        int fromYear = from.get(Calendar.YEAR);
        int toYear = to.get(Calendar.YEAR);
        int year = toYear-fromYear;
        return  year;
    }


}
