package com.rflpazini.sdf.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rflpazini on 11/1/16.
 */

public class UserLocalInfo {
    private static final String TAG = UserLocalInfo.class.getSimpleName();

    private SharedPreferences localInfo;
    private int uid;
    private String uName;
    private String token;

    public int getUid() {
        return uid;
    }

    public String getuName() {
        return uName;
    }

    public String getToken() {
        return token;
    }

    public UserLocalInfo() {

    }

    public UserLocalInfo(Context context) {
        localInfo = context.getSharedPreferences(Constants.USER_LOCAL_INFO, 0);
        uid = localInfo.getInt("id", 0);
        uName = localInfo.getString("userName", "");
        token = localInfo.getString("token", "");
    }
}
