package com.bell.arc.usageaggregator.mongotest.tdragg.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by maxchlam on 7/26/17.
 */
public class DateUtil {

    public static Long getFirstDateOfMonth(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return new Long(simpleDateFormat.format(cal.getTime()));
    }

    public static Long getLastDateOfMonth(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date firstDayOfMonth = cal.getTime();
        Date firstDayOfMonthZeroTime = setTime(firstDayOfMonth, 0, 0, 0, 0);
        return new Long(simpleDateFormat.format(firstDayOfMonthZeroTime));
    }

    public static Date setTime( final Date date, final int hourOfDay, final int minute, final int second, final int ms )
    {
        final GregorianCalendar gc = new GregorianCalendar();
        gc.setTime( date );
        gc.set( Calendar.HOUR_OF_DAY, hourOfDay );
        gc.set( Calendar.MINUTE, minute );
        gc.set( Calendar.SECOND, second );
        gc.set( Calendar.MILLISECOND, ms );
        return gc.getTime();
    }
}
