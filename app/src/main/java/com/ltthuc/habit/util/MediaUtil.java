package com.ltthuc.habit.util;

import android.app.Activity;
import android.content.Context;


import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeIntents;
import com.google.android.youtube.player.YouTubeStandalonePlayer;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MediaUtil {
//    public static void a(Context context, String str) {
//        MediaViewerActivity.b(context, str);
//    }

    public static void a(Activity activity, String str, String str2) {
        b(activity, a(str), str2);
    }

    public static void b(Activity activity, String str, String str2) {
        activity.startActivity(YouTubeStandalonePlayer.createVideoIntent(activity, str2, str));
    }

    public static void openYoutube(Activity activity, String developeKey, String videoId, int i, boolean z, boolean z2) {
        activity.startActivity(YouTubeStandalonePlayer.createVideoIntent(activity, developeKey, videoId, i, z, z2));
    }

    public static boolean isYoutubeInstalled(Activity activity) {
        YouTubeInitializationResult a = YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(activity);
        if (a == YouTubeInitializationResult.SERVICE_DISABLED) {
            return true;
        }
        a.getErrorDialog(activity, 0).show();
        return false;
    }

    public static void b(Context context, String str) {
        if (YouTubeIntents.isYouTubeInstalled(context)) {
            context.startActivity(YouTubeIntents.createPlayVideoIntentWithOptions(context, str, true, true));
        }
    }

    private static String a(String str) {
        if (str != null) {
            if (str.trim().length() > 0) {
               Matcher matcher = Pattern.compile("(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%‌​2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*").matcher(str);
                try {
                    if (matcher.find()) {
                        return matcher.group();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        }
        return null;
    }

}
