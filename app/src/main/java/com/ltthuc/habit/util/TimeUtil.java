package com.ltthuc.habit.util;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.api.client.util.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public abstract class TimeUtil {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());

    public static String format(Date date) {

        return simpleDateFormat.format(date.getTime());
    }





}