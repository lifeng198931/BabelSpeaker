package com.babelspeaker.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.babelspeaker.data.InitData;

import android.content.Context;
import android.os.Environment;

public class DownloadUtil {
    private static DownloadUtil mInstance;
    private Context mContext;
    
    public static DownloadUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DownloadUtil(context);
        }
        return mInstance;
    }
    
    public DownloadUtil(Context context) {
        mContext = context;
    }
    
    
    public boolean download(String url, String saveName) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse response;
            response = client.execute(get);
            HttpEntity entity = response.getEntity();
            long length = entity.getContentLength();
            InputStream is = entity.getContent();
            FileOutputStream fileOutputStream = null;
            if (is != null) {

//                File file = new File(Environment.getExternalStorageDirectory(),
//                        saveName);
                File file = new File(InitData.getInstance(mContext).getFilePath(), saveName);
                fileOutputStream = new FileOutputStream(file);

                byte[] buf = new byte[1024];
                int ch = -1;
                int count = 0;
                while ((ch = is.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, ch);
                    count += ch;
                    if (length > 0) {
                    }
                }

            }
            fileOutputStream.flush();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (ClientProtocolException e) {
            // e.printStackTrace();
            return false;
        } catch (IOException e) {
            // e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean downloadImage(String url, String saveName) {

        String imgUrl = url;
        if (url.indexOf(InitData.SITE) < 0) {
            // 如果没找到站点，则说明是外部图片地址，直接下载就行了。
        } else {
            Net net = new Net();
            imgUrl = net.GetRemoteString(url);
        }

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(imgUrl);
        HttpResponse response;
        try {
            response = client.execute(get);
            HttpEntity entity = response.getEntity();
            long length = entity.getContentLength();
            InputStream is = entity.getContent();
            FileOutputStream fileOutputStream = null;
            if (is != null) {

                File file = new File(Environment.getExternalStorageDirectory(),
                        saveName);
                fileOutputStream = new FileOutputStream(file);

                byte[] buf = new byte[1024];
                int ch = -1;
                int count = 0;
                while ((ch = is.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, ch);
                    count += ch;
                    if (length > 0) {
                    }
                }

            }
            fileOutputStream.flush();
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (ClientProtocolException e) {
            // e.printStackTrace();
            return false;
        } catch (IOException e) {
            // e.printStackTrace();
            return false;
        }
        return true;
    }
}
