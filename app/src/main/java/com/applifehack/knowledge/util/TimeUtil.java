package com.applifehack.knowledge.util;

import com.github.marlonlom.utilities.timeago.TimeAgo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class TimeUtil {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

    public static String format(Date date) {

        return simpleDateFormat.format(date.getTime());
    }


    public static String formatTimeAgo(long j) {
        return TimeAgo.using(j);
    }

    public static Date[] getFirsAndLastDateOfLastWeek(){
        Date[] array = new Date[2];
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
         cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.add(Calendar.DATE, -7);

        array[0] = cal.getTime();
        cal.add(Calendar.DATE, 6);
        array[1] = cal.getTime();
        return array;
    }


}