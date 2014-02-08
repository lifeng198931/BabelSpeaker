package com.babelspeaker.common;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.babelspeaker.bean.ResultBean;
import com.babelspeaker.data.InitData;
import com.babelspeaker.util.Des;
import com.babelspeaker.util.Net;

public class BaseLogic {
    public ResultBean handleResult(String result) {
        ResultBean bean = new ResultBean();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(result);
            bean.setStat(Integer.parseInt(jsonObject.getString("s")));
            bean.setData(jsonObject.getString("d"));
        } catch (JSONException e) {

        }
        return bean;
    }
    
    public String getStringFromUrl(String url, Context context, boolean isDes) {
        String result = "";
        try {
            Net net = Net.getInstance();
            result = net.getStringFromUrl(url, context);
            if (isDes) {
                result = Des.getInstance().de(result);
            }
        } catch (Exception e) {
        }
        return result;
    }

    public ResultBean getStringFromUrl(String url, Context context) {
        String result = "";
        try {
            Net net = Net.getInstance();
            result = net.getStringFromUrl(url, context);
            Des des = Des.getInstance();
            result = des.de(result);
        } catch (Exception e) {
            if(InitData.DEBUG) Log.v("error", e.toString());
        }
        return handleResult(result);
    }
    
    public List<String> handleData(String result) {
        List<String> list = new ArrayList<String>();
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                String string = array.getString(i);
                list.add(string);
            }
            
        } catch (JSONException e) {
        }
        
        return list;
    }

}
