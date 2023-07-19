/*
 * Headwind Remote: Open Source Remote Access Software for Android
 * https://headwind-remote.com
 *
 * Copyright (C) 2022 headwind-remote.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hmdm.control;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

// A simple wrapper of Preferences
public class SettingsHelper {
    private static final String PREFERENCES = "com.hmdm.control.PREFERENCES";

    // Data keys
    public static final String KEY_USE_DEFAULT = "use_default";
    public static final String KEY_SERVER_URL = "server_url";
    public static final String KEY_SECRET = "secret";
    public static final String KEY_DEVICE_NAME = "device_name";
    public static final String KEY_TRANSLATE_AUDIO = "translate_audio";
    public static final String KEY_BITRATE = "bitrate";
    public static final String KEY_FRAME_RATE = "frame_rate";
    public static final String KEY_FORCE_REFRESH_SEC = "force_refresh_sec";
    public static final String KEY_TEST_DST_IP = "dst_ip";
    public static final String KEY_VIDEO_SCALE = "video_scale";
    public static final String KEY_NOTIFY_SHARING = "notify_sharing";
    public static final String KEY_IDLE_TIMEOUT = "idle_timeout";
    public static final String KEY_PING_TIMEOUT = "session_timeout";

    private SharedPreferences sharedPreferences;

    private static SettingsHelper instance;

    public static SettingsHelper getInstance( Context context ) {
        if ( instance == null ) {
            instance = new SettingsHelper( context );
        }
        return instance;
    }

    public SettingsHelper( Context context ) {
        sharedPreferences = context.getApplicationContext().getSharedPreferences( PREFERENCES, Context.MODE_PRIVATE );
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    public float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

    public Set<String> getStringSet(String key) {
        return sharedPreferences.getStringSet(key, null);
    }

    public void setString(String key, String value) {
        sharedPreferences.edit().putString(key, value).commit();
    }

    public void setBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
    }

    public void setInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public void setLong(String key, long value) {
        sharedPreferences.edit().putLong(key, value).commit();
    }

    public void setFloat(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).commit();
    }

    public void setStringSet(String key, Set<String> value) {
        // Sometimes StringSet fails to save!
        // https://stackoverflow.com/questions/51001328/shared-preferences-not-saving-stringset-when-application-is-killed-its-a-featu
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key).commit();
        editor.putStringSet(key, value).commit();
    }
}
