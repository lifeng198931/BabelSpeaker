package com.babelspeaker.util;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.graphics.drawable.Drawable;
import android.os.Handler;

public class AsyncImageLoader3 {
    // 为了加快速度，在内存中开启缓存（主要应用于重复图片较多时，或者同�?��图片要多次被访问，比如在ListView时来回滚动）
    public Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
    private ExecutorService executorService = Executors.newFixedThreadPool(5); // 固定五个线程来执行任�?    private final Handler handler = new Handler();

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
        // 缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存�?        executorService.submit(new Runnable() {
            public void run() {
                try {
                    final Drawable drawable = Drawable.createFromStream(
                            new URL(imageUrl).openStream(), "image.png");

                    imageCache.put(imageUrl, new SoftReference<Drawable>(
                            drawable));

                    handler.post(new Runnable() {
                        public void run() {
                            callback.imageLoaded(drawable);
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return null;
    }

    // 从网络上取数据方�?    protected Drawable loadImageFromUrl(String imageUrl) {
        try {
            return Drawable.createFromStream(new URL(imageUrl).openStream(),
                    "image.png");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 对外界开放的回调接口
    public interface ImageCallback {
        // 注意 此方法是用来设置目标对象的图像资�?        public void imageLoaded(Drawable imageDrawable);
    }
}