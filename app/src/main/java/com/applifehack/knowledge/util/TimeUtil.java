package com.applifehack.knowledge.util;

import com.github.marlonlom.utilities.timeago.TimeAgo;

import java.text.SimpleDateFormat;
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


}