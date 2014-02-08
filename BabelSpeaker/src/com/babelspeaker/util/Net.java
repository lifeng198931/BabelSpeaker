package com.babelspeaker.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.babelspeaker.data.Api;
import com.babelspeaker.data.InitData;
import com.babelspeaker.data.ReceiverList;
import com.babelspeaker.logic.AccountLogic;

public class Net {

    private static Net mInstance;

    public static Net getInstance() {
        if (mInstance == null) {
            mInstance = new Net();
        }
        return mInstance;
    }

    public String GetRemoteString(String url) {
        // Log.d("url", url);
        // if(InitData.NET_AGENT_ON==1){
        // // 采用代理方式访问
        // url = "http://a.zsgjs.com/a.php?p=" +
        // java.net.URLEncoder.encode(url);
        // }
        // Log.d("url", url);
        String remoteString = "";

        DefaultHttpClient httpclient = new DefaultHttpClient();

        HttpGet httpget = new HttpGet(url);

        ResponseHandler<String> responseHandler = new BasicResponseHandler();

        try {
            remoteString = httpclient.execute(httpget, responseHandler);
        } catch (Exception e) {
            // e.printStackTrace();
            // System.out.println("出错了");
            remoteString = "";
        }
        httpclient.getConnectionManager().shutdown();
        return remoteString.toString();
    }

    public String getStringFromUrl(String url, Context context) {
        String tokenKey = InitData.getInstance(context).getTokenKey();
        url = url.replaceAll(" ", "%20");
        url = url + "&apitoken=" + Tools.getInstance().getMD5Str(Tools.getInstance().getMD5Str(tokenKey, true), false);
        if (AccountLogic.getInstance().isHasLogin()) {
            url = url.replace("@safety", AccountLogic.getInstance().getAccountInfoBean().getSafetyCode());
            url = url.replace("@uid", AccountLogic.getInstance().getAccountInfoBean().getId());
        }
        
        HttpPost httpRequest = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("param", ""));
        try {
            String result = "";
            HttpEntity httpEntity = new UrlEncodedFormEntity(params, "UTF-8");
            httpRequest.setEntity(httpEntity);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils
                        .toString(httpResponse.getEntity(), "UTF-8");
            } else {
                result = "";
            }
            
            
            if (result.equals("5050")) {
                Intent intent = new Intent(ReceiverList.BASE_RECEIVER);
                intent.putExtra("flag", 100);
                context.sendBroadcast(intent);
                
//                Intent loadService = new Intent(context, LoadInitService.class);
//                context.startService(loadService);
            }
            
            
            return result;

        } catch (ClientProtocolException e) {
            return "";
        } catch (IOException e) {
            return "";
        } catch (Exception e) {
            return "";
        }
    }

    public void saveBug(String info, Context context) {
        // Log.d("info", info);
        String model = Build.MODEL; // 手机型号
        String sdk = Build.VERSION.SDK;// SDK版本号

        if (info.length() > 2500) {
            info = info.substring(0, 2500);
        }

        String p = model + "@" + sdk + "@" + info;

        Des des = new Des();

        try {
            // Log.d("bug", p);
            p = des.en(p);
            // Log.d("bug", p);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Net net = Net.getInstance();
//        String url = "http://2.zsgjs.sinaapp.com/Mobi/A/TiBug/index?os=android&key=" + p;
        String url = Api.SITE + "/TiBug/index?os=android&key=" + p;
//        Log.v("error", url);
        // String url = "http://zsgjs.sinaapp.com/Mobi/Log/bug/p" + p;

        net.getStringFromUrl(url, context);
    }

    public String HtmlToTextGb2312(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        Pattern p_houhtml;
        Matcher m_houhtml;
        Pattern p_spe;
        Matcher m_spe;
        Pattern p_blank;
        Matcher m_blank;
        Pattern p_table;
        Matcher m_table;
        Pattern p_enter;
        Matcher m_enter;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            // 定义script的正则表达式.
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            // 定义style的正则表达式.
            String regEx_html = "<[^>]+>";
            // 定义HTML标签的正则表达式
            String regEx_houhtml = "/[^>]+>";
            // 定义HTML标签的正则表达式
            String regEx_spe = "\\&[^;]+;";
            // 定义特殊符号的正则表达式
            String regEx_blank = " +";
            // 定义多个空格的正则表达式
            String regEx_table = "\t+";
            // 定义多个制表符的正则表达式
            String regEx_enter = "\n+";
            // 定义多个回车的正则表达式

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            p_houhtml = Pattern
                    .compile(regEx_houhtml, Pattern.CASE_INSENSITIVE);
            m_houhtml = p_houhtml.matcher(htmlStr);
            htmlStr = m_houhtml.replaceAll(""); // 过滤html标签

            p_spe = Pattern.compile(regEx_spe, Pattern.CASE_INSENSITIVE);
            m_spe = p_spe.matcher(htmlStr);
            htmlStr = m_spe.replaceAll(""); // 过滤特殊符号

            p_blank = Pattern.compile(regEx_blank, Pattern.CASE_INSENSITIVE);
            m_blank = p_blank.matcher(htmlStr);
            htmlStr = m_blank.replaceAll(" "); // 过滤过多的空格

            p_table = Pattern.compile(regEx_table, Pattern.CASE_INSENSITIVE);
            m_table = p_table.matcher(htmlStr);
            htmlStr = m_table.replaceAll(" "); // 过滤过多的制表符

            p_enter = Pattern.compile(regEx_enter, Pattern.CASE_INSENSITIVE);
            m_enter = p_enter.matcher(htmlStr);
            htmlStr = m_enter.replaceAll(" "); // 过滤过多的制表符

            textStr = htmlStr.replaceAll(" ", "").replaceAll("\r", "@");

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return textStr;// 返回文本字符串
    }

}
