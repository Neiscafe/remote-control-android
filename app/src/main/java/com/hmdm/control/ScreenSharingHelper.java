package com.hmdm.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Helper API for interaction between MainActivity and ScreenSharingService
 */
public class ScreenSharingHelper {
    // Scale down screen size to reduce the video traffic
    public static float adjustScreenMetrics(DisplayMetrics metrics) {
        int srcWidth = metrics.widthPixels;
        // Adjust translated screencast size for phones with high screen resolutions
        if (metrics.widthPixels > Const.MAX_SHARED_SCREEN_WIDTH || metrics.heightPixels > Const.MAX_SHARED_SCREEN_HEIGHT) {
            float widthScale = (float)metrics.widthPixels / Const.MAX_SHARED_SCREEN_WIDTH;
            float heightScale = (float)metrics.heightPixels / Const.MAX_SHARED_SCREEN_HEIGHT;
            float maxScale = widthScale > heightScale ? widthScale : heightScale;
            metrics.widthPixels /= maxScale;
            metrics.heightPixels /= maxScale;
        }

        float videoScale = (float)metrics.widthPixels / srcWidth;
        Log.i(Const.LOG_TAG, "screenWidth=" + metrics.widthPixels + ", screenHeight=" + metrics.heightPixels + ", scale=" + videoScale);
        // Workaround against the codec bug: https://stackoverflow.com/questions/36915383/what-does-error-code-1010-in-android-mediacodec-mean
        // Making height and width divisible by 2
        metrics.heightPixels = metrics.heightPixels & 0xFFFE;
        metrics.widthPixels = metrics.widthPixels & 0xFFFE;
        return videoScale;
    }

    public static void setScreenMetrics(Activity activity, int screenWidth, int screenHeight, int screenDensity) {
        Intent intent = new Intent(activity, ScreenSharingService.class);
        intent.setAction(ScreenSharingService.ACTION_SET_METRICS);
        intent.putExtra(ScreenSharingService.ATTR_SCREEN_WIDTH, screenWidth);
        intent.putExtra(ScreenSharingService.ATTR_SCREEN_HEIGHT, screenHeight);
        intent.putExtra(ScreenSharingService.ATTR_SCREEN_DENSITY, screenDensity);
        executeCommand(activity, intent);
    }

    public static void configure(Activity activity, boolean audio, int videoFrameRate, int videoBitRate, String host, int audioPort, int videoPort) {
        Intent intent = new Intent(activity, ScreenSharingService.class);
        intent.setAction(ScreenSharingService.ACTION_CONFIGURE);
        intent.putExtra(ScreenSharingService.ATTR_AUDIO, audio);
        intent.putExtra(ScreenSharingService.ATTR_FRAME_RATE, videoFrameRate);
        intent.putExtra(ScreenSharingService.ATTR_BITRATE, videoBitRate);
        intent.putExtra(ScreenSharingService.ATTR_HOST, host);
        intent.putExtra(ScreenSharingService.ATTR_AUDIO_PORT, audioPort);
        intent.putExtra(ScreenSharingService.ATTR_VIDEO_PORT, videoPort);
        executeCommand(activity, intent);
    }

    public static void requestSharing(Activity activity) {
        Intent intent = new Intent(activity, ScreenSharingService.class);
        intent.setAction(ScreenSharingService.ACTION_REQUEST_SHARING);
        executeCommand(activity, intent);
    }

    public static void startSharing(Activity activity, int resultCode, Intent data) {
        Intent intent = new Intent(activity, ScreenSharingService.class);
        intent.setAction(ScreenSharingService.ACTION_START_SHARING);
        intent.putExtra(ScreenSharingService.ATTR_RESULT_CODE, resultCode);
        intent.putExtra(ScreenSharingService.ATTR_DATA, data);
        executeCommand(activity, intent);
    }

    public static void stopSharing(Activity activity) {
        Intent intent = new Intent(activity, ScreenSharingService.class);
        intent.setAction(ScreenSharingService.ACTION_STOP_SHARING);
        executeCommand(activity, intent);
    }

    private static void executeCommand(Activity activity, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(intent);
        } else {
            activity.startService(intent);
        }
    }
}
