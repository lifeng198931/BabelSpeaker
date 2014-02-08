package com.babelspeaker.util;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class AsyncImageLoader {
    // 为了加快速度，加入了缓存（主要应用于重复图片较多时，或�?同一个图片要多次被访问，比如在ListView时来回滚动）
    private Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();

    /**
     * 
     * @param imageUrl
     *            图像url地址
     * @param callback
     *            回调接口
     * @return 返回内存中缓存的图像，第�?��加载返回null
     */
    public Drawable loadDrawable(final String imageUrl,
            final ImageCallback callback) {
        // 如果缓存过就从缓存中取出数据
        if (imageCache.containsKey(imageUrl)) {
            SoftReference<Drawable> softReference = imageCache.get(imageUrl);
            if (softReference.get() != null) {
                return softReference.get();
            }
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                callback.imageLoaded((Drawable) msg.obj);
            }
        };
        new Thread() {
            public void run() {
                Drawable drawable = loadImageFromUrl(imageUrl);
                imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
                handler.sendMessage(handler.obtainMessage(0, drawable));

            }

        }.start();
        /*
         * 下面注释的这段代码是Handler的一种代替方�?         */
        // new AsyncTask() {
        // @Override
        // protected Drawable doInBackground(Object... objects) {
        // Drawable drawable = loadImageFromUrl(imageUrl);
        // imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
        // return drawable;
        // }
        //
        // @Override
        // protected void onPostExecute(Object o) {
        // callback.imageLoaded((Drawable) o);
        // }
        // }.execute();
        return null;
    }

    protected Drawable loadImageFromUrl(String imageUrl) {
        try {
            return Drawable.createFromStream(new URL(imageUrl).openStream(),
                    "src");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 对外界开放的回调接口
    public interface ImageCallback {
        public void imageLoaded(Drawable imageDrawable);
    }
}