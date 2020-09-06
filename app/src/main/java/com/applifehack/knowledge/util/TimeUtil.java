package com.applifehack.knowledge.util;

import android.content.Context;

import com.applifehack.knowledge.R;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import java.text.ParseException;
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

    public static String formatDate(Context context,Date date) {
        Calendar c1 = Calendar.getInstance(); // today

        Calendar c2 = Calendar.getInstance();
        c2.setTime(date); // your date
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
            return context.getString(R.string.today);
        }else {
            c1.add(Calendar.DAY_OF_YEAR, -1); // yesterday

            if(c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                    && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)) {
             return context.getString(R.string.yesterday);
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMMM ");


        return format.format(date);
    }




}