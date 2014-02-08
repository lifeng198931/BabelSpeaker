package com.babelspeaker.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.babelspeaker.data.InitData;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class LoadImageUtil {
    final Handler handler = new Handler();
    
    private static LoadImageUtil mInstance;
    
    public static LoadImageUtil getInstance() {
        if (mInstance == null) {
            mInstance = new LoadImageUtil();
        }
        
        return mInstance;
    }
    
    /*
     * final Handler handler2 = new Handler() {
     * 
     * @Override public void handleMessage(Message msg) { ((ImageView)
     * LazyLoadImageActivity.this.findViewById(msg.arg1))
     * .setImageDrawable((Drawable) msg.obj); } };
     */
    private ExecutorService mExecutorService = Executors.newFixedThreadPool(5); // 固定五个线程来执行任务

    private AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
    private AsyncImageLoader3 asyncImageLoader3 = new AsyncImageLoader3();

    /*
     * @Override public void onCreate(Bundle savedInstanceState) {
     * super.onCreate(savedInstanceState); setContentView(R.layout.images);
     * 
     * // loadImage("http://www.chinatelecom.com.cn/images/logo_new.gif", //
     * R.id.image1); // loadImage("http://www.baidu.com/img/baidu_logo.gif",
     * R.id.image2); // loadImage("http://cache.soso.com/30d/img/web/logo.gif",
     * R.id.image3); // loadImage("http://www.baidu.com/img/baidu_logo.gif",
     * R.id.image4); // loadImage("http://cache.soso.com/30d/img/web/logo.gif",
     * R.id.image5);
     * 
     * loadImage2("http://www.chinatelecom.com.cn/images/logo_new.gif",R.id.image1
     * ); loadImage2("http://www.baidu.com/img/baidu_logo.gif", R.id.image2);
     * loadImage2("http://cache.soso.com/30d/img/web/logo.gif", R.id.image3);
     * loadImage2("http://www.baidu.com/img/baidu_logo.gif", R.id.image4);
     * loadImage2("http://cache.soso.com/30d/img/web/logo.gif", R.id.image5);
     * 
     * // loadImage3("http://www.chinatelecom.com.cn/images/logo_new.gif", //
     * R.id.image1); // loadImage3("http://www.baidu.com/img/baidu_logo.gif",
     * R.id.image2); // loadImage3("http://cache.soso.com/30d/img/web/logo.gif",
     * // R.id.image3); // loadImage3("http://www.baidu.com/img/baidu_logo.gif",
     * R.id.image4); // loadImage3("http://cache.soso.com/30d/img/web/logo.gif",
     * // R.id.image5);
     * 
     * // loadImage4("http://www.chinatelecom.com.cn/images/logo_new.gif", //
     * R.id.image1); // loadImage4("http://www.baidu.com/img/baidu_logo.gif",
     * R.id.image2); // loadImage4("http://cache.soso.com/30d/img/web/logo.gif",
     * // R.id.image3); // loadImage4("http://www.baidu.com/img/baidu_logo.gif",
     * R.id.image4); // loadImage4("http://cache.soso.com/30d/img/web/logo.gif",
     * // R.id.image5);
     * 
     * // loadImage5("http://www.chinatelecom.com.cn/images/logo_new.gif", //
     * R.id.image1); // //为了测试缓存而模拟的网络延时 // SystemClock.sleep(2000); //
     * loadImage5("http://www.baidu.com/img/baidu_logo.gif", R.id.image2); //
     * SystemClock.sleep(2000); //
     * loadImage5("http://cache.soso.com/30d/img/web/logo.gif", // R.id.image3);
     * // SystemClock.sleep(2000); //
     * loadImage5("http://www.baidu.com/img/baidu_logo.gif", R.id.image4); //
     * SystemClock.sleep(2000); //
     * loadImage5("http://cache.soso.com/30d/img/web/logo.gif", // R.id.image5);
     * // SystemClock.sleep(2000); //
     * loadImage5("http://www.baidu.com/img/baidu_logo.gif", R.id.image4); }
     * 
     * @Override protected void onDestroy() { mExecutorService.shutdown();
     * super.onDestroy(); }
     */
    // 线程加载图像基本原理
    public void loadImage(final String url, final ImageView iv) {
        handler.post(new Runnable() {
            public void run() {
                Drawable drawable = null;
                try {
                    drawable = Drawable.createFromStream(
                            new URL(url).openStream(), "image.png");
                } catch (IOException e) {
                }
                iv.setImageDrawable(drawable);
            }
        });
    }

    /*
     * // 采用handler+Thread模式实现多线程异步加载 public void loadImage2(final String url,
     * final int id) { Thread thread = new Thread() {
     * 
     * @Override public void run() { Drawable drawable = null; try { drawable =
     * Drawable.createFromStream( new URL(url).openStream(), "image.png"); }
     * catch (IOException e) { }
     * 
     * Message message = handler2.obtainMessage(); message.arg1 = id;
     * message.obj = drawable; handler2.sendMessage(message); } };
     * thread.start(); thread = null; }
     */

    // 引入线程池来管理多线程
    public void loadImage3(final String url, final ImageView iv) {
        mExecutorService.submit(new Runnable() {
            public void run() {
                try {
                    final Drawable drawable = Drawable.createFromStream(
                            new URL(url).openStream(), "image.png");
                    handler.post(new Runnable() {

                        public void run() {
                            iv.setImageDrawable(drawable);
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // 引入线程池来管理多线程
    public void loadImageFullScreen(final String url, final ImageView iv,
            final String saveName) {
        mExecutorService.submit(new Runnable() {
            public void run() {
                try {
                    String imgUrl = url;
                    if (url.indexOf(InitData.SITE) < 0) {
                        // 如果没找到站点，则说明是外部图片地址，直接下载就行了。
                    } else {
                        Net net = new Net();
                        imgUrl = net.GetRemoteString(url);
                    }
                    DownloadUtil df = DownloadUtil.getInstance(iv.getContext());
                    df.download(imgUrl, saveName);

                    handler.post(new Runnable() {

                        public void run() {
                            try {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 1;
                                Bitmap bm = BitmapFactory.decodeFile("/sdcard/"
                                        + saveName, options);

                                if (bm == null) {
                                    return;
                                }
                                // 横屏状态下宽高互换
                                int width = InitData.getHEIGHT();
                                int height = InitData.getWIDTH();

                                int bmW = bm.getWidth();// 740
                                int bmH = bm.getHeight();// 500

                                LayoutParams para;
                                para = iv.getLayoutParams();

                                int tempH = bmH * width / bmW;
                                int tempW = bmW * width / bmW;

                                if (tempH > height) {
                                    tempW = tempW * height / tempH;
                                    tempH = tempH * height / tempH;
                                }

                                para.height = tempH;
                                para.width = tempW;

                                iv.setLayoutParams(para);

                                iv.setImageBitmap(bm);

                                // ImageFullScreenSingleton.getInstance()
                                // .setChangePic(false);
                                // ImageFullScreenSingleton.getInstance()
                                // .setClock(60);

                            } catch (Exception e) {
                                // throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    // throw new RuntimeException(e);
                }
            }
        });
    }

    // 仅下载图片显示到容器中
    public void loadImage(final String url, final ImageView iv,
            final String saveName) {
        mExecutorService.submit(new Runnable() {
            public void run() {
                try {
                    DownloadUtil df = DownloadUtil.getInstance(iv.getContext());
                    df.download(url, saveName);

                    handler.post(new Runnable() {

                        public void run() {
                            try {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 1;
                                Bitmap bm = BitmapFactory.decodeFile("/sdcard/"
                                        + saveName, options);

                                if (bm == null) {
                                    return;
                                }
                                iv.setImageBitmap(bm);

                            } catch (Exception e) {
                                // throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // 引入线程池来管理多线程
    public void loadImage_ex(final String url, final ImageView iv,
            final String saveName) {
        mExecutorService.submit(new Runnable() {
            public void run() {
                try {
                    DownloadUtil df = DownloadUtil.getInstance(iv.getContext());
                    df.download(url, saveName);

                    handler.post(new Runnable() {

                        public void run() {
                            try {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 1;
                                Bitmap bm = BitmapFactory.decodeFile("/sdcard/"
                                        + saveName, options);

                                if (bm == null) {
                                    return;
                                }
                                int vW = iv.getWidth();
                                int bmW = bm.getWidth();
                                int bmH = bm.getHeight();

                                LayoutParams para;
                                para = iv.getLayoutParams();
                                para.height = bmH * vW / bmW;
                                para.width = vW;
                                iv.setLayoutParams(para);

                                iv.setImageBitmap(bm);

                            } catch (Exception e) {
                                // throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    // throw new RuntimeException(e);
                }
            }
        });
    }



    // 引入线程池来管理多线程
    public void loadImage_GUO(final String url, final ImageView iv,
            final String saveName) {
        mExecutorService.submit(new Runnable() {
            public void run() {
                try {
                    String imgUrl = url;
                    if (url.indexOf(InitData.SITE) < 0) {
                        // 如果没找到站点，则说明是外部图片地址，直接下载就行了。
                    } else {
                        Net net = new Net();
                        imgUrl = net.GetRemoteString(url);
                    }

                    // Log.d("imgUrl", imgUrl + "--" + saveName);
                    DownloadUtil df = DownloadUtil.getInstance(iv.getContext());
                    df.download(imgUrl, saveName);

                    handler.post(new Runnable() {

                        public void run() {
                            try {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 1;
                                Bitmap bm = BitmapFactory.decodeFile("/sdcard/"
                                        + saveName, options);

                                if (bm == null) {
                                    return;
                                }

                                int vW = InitData.getWIDTH();
                                int bmW = bm.getWidth();
                                int bmH = bm.getHeight();

                                LayoutParams para;
                                para = iv.getLayoutParams();
                                para.height = bmH * vW / bmW;
                                para.width = vW;
                                iv.setLayoutParams(para);

                                iv.setImageBitmap(bm);

                            } catch (Exception e) {
                                // throw new RuntimeException(e);
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public boolean fileIsExists(String file) {
        try {
            File f = new File(file);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void loadImage_Teacher(final String url, final ImageView iv,
            final String saveName) {
        mExecutorService.submit(new Runnable() {
            public void run() {
                try {
                    String imgUrl = url;
                    if (url.indexOf(InitData.SITE) < 0) {
                        // 如果没找到站点，则说明是外部图片地址，直接下载就行了。
                    } else {
                        Net net = new Net();
                        imgUrl = net.GetRemoteString(url);
                    }

                    if (fileIsExists("/sdcard/" + saveName)) {
                        // 文件已经存在,不再下载.
                        // Log.d("file", "yes");
                    } else {
                        // Log.d("imgUrl", imgUrl + "--" + saveName);
                        // Log.d("file", "no");
                        DownloadUtil df = DownloadUtil.getInstance(iv.getContext());
                        df.download(imgUrl, saveName);
                    }
                    handler.post(new Runnable() {

                        public void run() {
                            try {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 1;
                                Bitmap bm = BitmapFactory.decodeFile("/sdcard/"
                                        + saveName, options);

                                if (bm == null) {
                                    return;
                                }
                                iv.setImageBitmap(bm);

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void loadMetalsHistoryImage(final String url, final ImageView imageView,
            final String saveName, final Context context) {
        mExecutorService.submit(new Runnable() {
            public void run() {
                try {
                    DownloadUtil df = DownloadUtil.getInstance(imageView.getContext());
                    df.download(url, saveName);

                    handler.post(new Runnable() {

                        public void run() {
                            try {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 1;
                                Bitmap bm = BitmapFactory.decodeFile(InitData.getInstance(imageView.getContext()).getFilePath()
                                        + saveName, options);

                                if (bm == null) {
                                    return;
                                }
                                
                                DisplayMetrics displayMetrics = new DisplayMetrics();
                                displayMetrics = context.getResources().getDisplayMetrics();
                                
                                int vW = displayMetrics.widthPixels - 10;
                                int bmW = bm.getWidth();
                                int bmH = bm.getHeight();

                                LayoutParams params;
                                params = imageView.getLayoutParams();
                                params.height = bmH * vW / bmW;
                                params.width = vW;
                                imageView.setLayoutParams(params);

                                imageView.setImageBitmap(bm);

                            } catch (Exception e) {
                                // throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    // throw new RuntimeException(e);
                }
            }
        });
    }


    public void loadImageByUrl(final String url, final ImageView iv,
            final String saveName) {
        mExecutorService.submit(new Runnable() {
            public void run() {
                try {
                    String imgUrl = url;
                    if (fileIsExists(InitData.getInstance(iv.getContext()).getFilePath() + saveName)) {
                        // 文件已经存在,不再下载.
                         //Log.d("file", "yes");
                    } else {
                         //Log.d("imgUrl", imgUrl + "--" + saveName);
                         //Log.d("file", "no");
                        DownloadUtil df = DownloadUtil.getInstance(iv.getContext());
                        df.download(imgUrl, saveName);
                    }
                    handler.post(new Runnable() {

                        public void run() {
                            try {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 1;
                                Bitmap bm = BitmapFactory.decodeFile(InitData.getInstance(iv.getContext()).getFilePath()
                                        + saveName, options);

                                if (bm == null) {
                                    return;
                                }
                                iv.setImageBitmap(bm);

                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    // 引入线程池，并引入内存缓存功能,并对外部调用封装了接口，简化调用过程
    public void loadImage4(final String url, final ImageView iv) {
        // 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
        Drawable cacheImage = asyncImageLoader.loadDrawable(url,
                new AsyncImageLoader.ImageCallback() {
                    // 请参见实现：如果第一次加载url时下面方法会执行
                    public void imageLoaded(Drawable imageDrawable) {
                        iv.setImageDrawable(imageDrawable);
                    }
                });
        if (cacheImage != null) {
            iv.setImageDrawable(cacheImage);
        }
    }

    // 采用Handler+Thread+封装外部接口
    public void loadImage5(final String url, final ImageView iv) {
        // 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
        Drawable cacheImage = asyncImageLoader3.loadDrawable(url,
                new AsyncImageLoader3.ImageCallback() {
                    // 请参见实现：如果第一次加载url时下面方法会执行
                    public void imageLoaded(Drawable imageDrawable) {
                        iv.setImageDrawable(imageDrawable);
                    }
                });
        if (cacheImage != null) {
            iv.setImageDrawable(cacheImage);
        }
    }

}