package com.babelspeaker.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 信息通过SharedPreferences来存读取
 * 
 * @author mark
 * 
 */
public class BaseSharedPreferences {
    private Context mContext;
    protected SharedPreferences mSharedPreferences;
    protected Editor mEditor;

    public BaseSharedPreferences(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(getClass()
                .getSimpleName(), Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }
    
    public Context getContext() {
        return mContext;
    }
}
