package com.babelspeaker.data;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.DisplayMetrics;

import com.babelspeaker.R;
import com.babelspeaker.common.BaseSharedPreferences;
import com.babelspeaker.util.DateUtil;

/**
 * 初始数据
 * 
 * @author mark
 * 
 */
public class InitData extends BaseSharedPreferences {

    private static int WIDTH;
    private static int HEIGHT;
    public static String SITE = "http://apphome.sinaapp.com/dc/A";

    public static String VER;
    public static String DOWNLOADURL;
    public static String UPDATE_SAVENAME;
    public static int CLOCK; // 数据刷新时间间隔秒数
    public static int CLOCK_SHOW; // 间隔秒数是否显示�?不显示，1显示
    public static Date SERVER_TIME;// 登录时服务器时间
    public static Date LOCAL_TIME;// 本机时间
    public static boolean SCREEN_LIGHT; // 屏幕是否常亮�?否，1是，默认
    public static int MONITOR_SERVICE_I = 0; // 判断监控是否运行
    public static String DATA_RADIO_SHOW; // 设置数据界面的显示单选按钮项
    public static boolean DEBUG = false;
    public String LAST_DEVICE_ID = "";

    private final static String USER_ID = "MetalsUserId";
    private final static String WIDGET_REFRESH_CLOCK = "WidgetRefreshClock";
    private final static String REFRESH_CLOCK = "RefreshClock";
    private final static String DEFAULT_CHART_REFRESH_CLOCK = "DefaultChartRefreshClock";
    private final static String REFRESH_CLOCK_SHOW = "RefreshClockShow";
    private final static String SCREEN_KEEP_ON = "ScreenKeepOn";
    private final static String OPTIONAL_TIDS = "OptionalTIDs";
    private final static String DEFAULT_QUOTES_QUEUE = "DefaultQuotesQueue";
    private final static String CUSTOM_METALS_QUEUE = "CustomMetalsQueue";
    private final static String DEFAULT_METALS_SHOW = "DefaultMetalsShow";
    private final static String DEFAULT_CHART_PERIOD = "DefaultChartPeriod";
    private final static String DEFAULT_TIME_CHART = "DefaultTimeChart";
    private final static String DEFAULT_EMA_PERIOD = "DefaultEmaPeriod";
    private final static String DEFAULT_SMA_PERIOD = "DefaultSmaPeriod";
    private final static String DEFAULT_RSI_PERIOD = "DefaultRsiPeriod";
    private final static String USER_NAME = "UserName";
    private final static String PASSWORD = "Password";
    private final static String DEFAULT_AUTO_LOGIN = "DefaultAutoLogin";
    private final static String ANNOUNCEMENT_ID = "AnnouncementId";
    private final static String RISE_RING = "RiseRing";
    private final static String FALL_RING = "FallRing";
    private final static String DEVICE_ID = "DeviceId";
    private final static String RECEIVE_PUSH = "ReceivePush";
    private final static String AUTO_WIDGET_REFRESH = "AutoWidgetRefresh";

    private static InitData mInstance;

    public static synchronized InitData getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new InitData(context);
        }

        return mInstance;
    }

    private InitData(Context context) {
        super(context);
        CLOCK = getRefreshClock();
        SCREEN_LIGHT = getScreenLight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics = context.getResources().getDisplayMetrics();
        setWIDTH(displayMetrics.widthPixels);
        setHEIGHT(displayMetrics.heightPixels);
        if (!isFileExist()) {
            createSDDir();
        }
        //修复魅族bug
        if (!isMzFileExist()) {
            createMzSDDir();
        }
    }

    public File createSDDir() {
        File dir = new File("/mnt/sdcard/metals/");
        if (dir.mkdir()) {
            setFilePath("/mnt/sdcard/metals/");
        }
        return dir;
    }

    public boolean isFileExist() {
        File file = new File("/mnt/sdcard/metals/");
        if (file.exists()) {
            setFilePath("/mnt/sdcard/metals/");
        }
        return file.exists();
    }
    
    public File createMzSDDir() {
        File dir = new File("/sdcard/metals/");
        if (dir.mkdir()) {
            setFilePath("/sdcard/metals/");
        }
        return dir;
    }

    public boolean isMzFileExist() {
        File file = new File("/sdcard/metals/");
        if (file.exists()) {
            setFilePath("/sdcard/metals/");
        }
        return file.exists();
    }
    
    /**
     * 设置启动时间 
     * 毫秒为单�?
     */
    public void setLoadDate() {
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        setPerferenceLong("LoadStartTime", time);
    }
    
    /**
     * 获取启动时间
     * @return
     */
    public long getLoadDate() {
        return mSharedPreferences.getLong("LoadStartTime", 0);
    }
    
    /**
     * 设置启动图片地址
     * @param path
     */
    public void setLoadImagePath(String path) {
        setPerferenceString("LoadImagePath", path);
    }
    
    /**
     * 获取启动图片地址
     * @return
     */
    public String getLoadImagePath() {
        return mSharedPreferences.getString("LoadImagePath", "");
    }
    
    /**
     * 设置启动图片显示时间
     * @param showTime
     */
    public void setLoadImageShowTime(int showTime) {
        setPerferenceInt("LoadImageShowTime", showTime);
    }
    
    /**
     * 获取启动图片显示时间
     * @return
     */
    public int getLoadImageShowTime() {
        return mSharedPreferences.getInt("LoadImageShowTime", 0);
    }
    
    /**
     * 设置文件保存目录
     * @param metalsFilePath
     */
    public void setFilePath(String metalsFilePath) {
        setPerferenceString("MetalsFilePath", metalsFilePath);
    }
    
    /**
     * 获取文件保存目录
     * @return str
     */
    public String getFilePath() {
        return mSharedPreferences.getString("MetalsFilePath", "/sdcard/");
    }
    
    /**
     * 设置快讯top显示文字
     * @param daily
     */
    public void setDailyExpressString(String daily) {
        setPerferenceString("DailyExpressString", daily);
    }
    
    /**
     * 获取快讯top显示文字
     * @return
     */
    public String getDailyExpressString() {
        return mSharedPreferences.getString("DailyExpressString", "快讯直播�?..");
    }
    
    /**
     * 设置MACD指标参数�?
     * @param macd
     */
    public void setMacdParam(String macd) {
        setPerferenceString("MacdParam", macd);
    }
    
    /**
     * 获取MACD指标参数�?
     * @return
     */
    public String getMacdParam() {
        return mSharedPreferences.getString("MacdParam", "12,26,9");
    }
    
    /**
     * 设置boll指标参数�?
     * @param boll
     */
    public void setBollParam(String boll) {
        setPerferenceString("BollParam", boll);
    }
    
    /**
     * 获取boll指标参数�?
     * @return
     */
    public String getBollParam() {
        return mSharedPreferences.getString("BollParam", "20");
    }
    
    /**
     * 设置kd指标参数�?
     * @param kd
     */
    public void setKDParam(String kd) {
        setPerferenceString("KDParam", kd);
    }
    
    /**
     * 获取KD指标参数�?
     * @return
     */
    public String getKDParam() {
        return mSharedPreferences.getString("KDParam", "9");
    }
    
    /**
     * 设置env指标参数�?
     * @param env
     */
    public void setEnvParam(String env) {
        setPerferenceString("EnvParam", env);
    }
    
    /**
     * 获取env指标参数�?
     * @return
     */
    public String getEnvParam() {
        return mSharedPreferences.getString("EnvParam", "26");
    }
    
    /**
     * 设置提醒是否震动
     * @param shock
     */
    public void setRemindShock(boolean shock) {
        setPerferenceBoolean("RemindShock", shock);
    }
    
    /**
     * 获取提醒是否震动
     * @return
     */
    public boolean getRemindShock() {
        return mSharedPreferences.getBoolean("RemindShock", true);
    }
    
    /**
     * 设置刷新时间
     */
    public void setRefreshTime() {
        DateUtil dateUtil = new DateUtil();
        String time = dateUtil.getCurrentDate("yyyyMMdd");
        setPerferenceString("QuotesRefreshTime", time);
    }
    
    /**
     * 获取是否�?��获取新数�?
     * @return
     */
    public boolean getRefreshTime() {
        String time = mSharedPreferences.getString("QuotesRefreshTime", "");
        DateUtil dateUtil = new DateUtil();
        String currentDate = dateUtil.getCurrentDate("yyyyMMdd");
        
        if (!time.equals(currentDate)) {
            return true;
        } else {
            return false;
        }
        
    }
    
    /**
     * 设置TD盈亏计算器数�?
     * @param tdGainLossData
     */
    public void setTDGainLossData(String direction, String tdGainLossData) {
        setPerferenceString("TDGainLossData" + direction, tdGainLossData);
    }
    
    /**
     * 获取TD盈亏计算器数�?
     * @return
     */
    public String getTDGainLossData(String direction) {
        return mSharedPreferences.getString("TDGainLossData" + direction, "");
    }
    
    /**
     * 设置提醒铃声的提醒周�?
     * @param clock
     */
    public void setRemindClock(int clock) {
        setPerferenceInt("RemindClock", clock);
    }
    
    /**
     * 获取提醒铃声的提醒周�?
     * @return
     */
    public int getRemindClock() {
        return mSharedPreferences.getInt("RemindClock", 2);
    }
    
    /**
     * 设置提醒铃声的提醒周期（秒）
     * @param secClock
     */
    public void setRemindSecClock(int secClock) {
        setPerferenceInt("RemindSecondClock", secClock);
    }
    
    /**
     * 获取提醒铃声的提醒周期（秒）
     * @return
     */
    public int getRemindSecClock() {
        return mSharedPreferences.getInt("RemindSecondClock", 60);
    }
    
    /**
     * 保存 桌面部件ID
     * @param widgetIds
     */
    public void setWidgetIds(String widgetIds) {
        setPerferenceString("WidgetIds", widgetIds);
    }
    
    /**
     * 获取 桌面部件ID 以刷�?
     * @return
     */
    public String getWidgetIds() {
        return mSharedPreferences.getString("WidgetIds", "");
    }
    
    /**
     * 是否保存�?token id
     * @param isBind
     */
    public void setBindTokenId(boolean isBind) {
        setPerferenceBoolean("BindTokenId", isBind);
    }
    
    /**
     * 是否保存过token id
     * @return
     */
    public boolean getBindTokenId() {
        return mSharedPreferences.getBoolean("BindTokenId", false);
    }
    
    /**
     * 设置版本�?
     * @param ver
     */
    public void setVersion(int ver) {
        setPerferenceInt("VersionId", ver);
    }
    
    /**
     * 获取版本�?
     * @return
     */
    public int getVersion() {
        return mSharedPreferences.getInt("VersionId", 0);
    }
    
    /**
     * 设置 是否强制升级
     * @param forceUpdate
     */
    public void setForceUpdate(int forceUpdate) {
        setPerferenceBoolean("ForceUpdate", (forceUpdate == 1 ? true : false));
    }
    
    /**
     * 是否升级
     * @return
     */
    public boolean getForceUpdate() {
        return mSharedPreferences.getBoolean("ForceUpdate", false);
    }
    
    /**
     * 设置是否强制升级
     * @param forceUpdate
     */
    public void setForceUpdateInt(int forceUpdate) {
        setPerferenceInt("ForceUpdateInt", forceUpdate);
    }
    
    /**
     * 获取升级模式
     * @return
     */
    public int getForceUpdateInt() {
        return mSharedPreferences.getInt("ForceUpdateInt", 2);
    }
    
    /**
     * 设置 新版下载地址
     * @param downloadUrl
     */
    public void setDownloadUrl(String downloadUrl) {
        setPerferenceString("DownloadUrl", downloadUrl);
    }
    
    /**
     * 获取新版下载地址
     * @return
     */
    public String getDownloadUrl() {
        return mSharedPreferences.getString("DownloadUrl", "");
    }
    
    /**
     * 设置 是否 黑名�?
     * @param isBlack
     */
    public void setIsBlack(int isBlack) {
        setPerferenceInt("IsBlack", isBlack);
    }
    
    /**
     * 获取是否是黑名单
     * @return
     */
    public int getIsBlack() {
        return mSharedPreferences.getInt("IsBlack", 0);
    }
    
    /**
     * 设置  服务器时�?
     * @param time
     */
    public void setServerTime(String time) {
        setPerferenceString("ServerTime", time);
    }
    
    public String getServerTime() {
        return mSharedPreferences.getString("ServerTime", "");
    }
    
    public void setLTPhone(String phone) {
        setPerferenceString("LianTong", phone);
    }
    
    public String getLTPhone() {
        return mSharedPreferences.getString("LianTong", "");
    }
    
    public void setDXPhone(String phone) {
        setPerferenceString("DianXin", phone);
    }
    
    public String getDXPhone() {
        return mSharedPreferences.getString("DianXin", "");
    }
    
    public void setYDPhone(String phone) {
        setPerferenceString("YiDong", phone);
    }
    
    public String getYDPhone() {
        return mSharedPreferences.getString("YiDong", "");
    }
    
    /**
     * 设置 设备ID
     * @param deviceId
     */
    public void setDeviceId(String deviceId) {
        setPerferenceString(DEVICE_ID, deviceId);
    }
    
    public String getDeviceId() {
        String deviceId = mSharedPreferences.getString(DEVICE_ID, "");
        return deviceId;
    }
    
    /**
     * 设置 桌面部件 是否自动刷新
     * @param autoRefresh
     */
    public void setAuotWidgetRefresh(boolean autoRefresh) {
        setPerferenceBoolean(AUTO_WIDGET_REFRESH, autoRefresh);
    }
    
    /**
     * 桌面部件是否自动刷新
     * @return
     */
    public boolean getAutoWidgetRefresh() {
        return mSharedPreferences.getBoolean(AUTO_WIDGET_REFRESH, true);
    }
    
    /**
     * 设置接收见闻快讯推�?
     * @param receive
     */
    public void setReceiveDailyExpress(boolean receive) {
        setPerferenceBoolean("ReceiveDailyExpress", receive);
    }
    
    /**
     * 获取是否接收见闻快讯推�?
     * @return
     */
    public boolean getReceiveDailyExpress() {
        return mSharedPreferences.getBoolean("ReceiveDailyExpress", true);
    }
    
    /**
     * 设置 是否接收推�?
     * @param receive
     */
    public void setReceivePush(boolean receive) {
        setPerferenceBoolean(RECEIVE_PUSH, receive);
    }
    
    /**
     * 是否接收推�?
     * @return
     */
    public boolean getReceivePush() {
        return mSharedPreferences.getBoolean(RECEIVE_PUSH, true);
    }
    
    /**
     * 设置用户ID
     * @param id
     */
    public void setUserId(int id) {
        setPerferenceInt(USER_ID, id);
    }
    
    public int getUserId() {
        return mSharedPreferences.getInt(USER_ID, -1);
    }
    
    public void setSendVerificationTime(long time) {
        setPerferenceLong("SendVerificationTime", time);
    }
    
    public long getSendVerificationTime() {
        return mSharedPreferences.getLong("SendVerificationTime", 0);
    }
    
    
    public void setWidgetData(int widgetId, String content) {
        setPerferenceString("MetalWidget" + widgetId, content);
    }
    
    public String getWidgetData(int widgetId) {
        return mSharedPreferences.getString("MetalWidget" + widgetId, "");
    }
    
    public void setTokenKey(String content) {
        setPerferenceString("TokenKey", content);
    }
    
    public String getTokenKey() {
        return mSharedPreferences.getString("TokenKey", "");
    }
    
    /**
     * 设置小部件刷新周�?
     * @param clock
     */
    public void setWidgetRefreshClock(int clock) {
        setPerferenceInt(WIDGET_REFRESH_CLOCK, clock);
    }
    
    /**
     * 获取小部件刷新周�?
     * @return
     */
    public int getWidgetRefreshClock() {
        return mSharedPreferences.getInt(WIDGET_REFRESH_CLOCK, 60);
    }
    
    /**
     * 设置上涨铃声
     * @param riseRing
     */
    public void setRiseRing(String riseRing) {
        setPerferenceString(RISE_RING, riseRing);
    }
    
    /**
     * 获取上涨铃声
     * @return
     */
    public String getRiseRing() {
        return mSharedPreferences.getString(RISE_RING, getContext().getString(R.string.mute));
    }
    
    /**
     * 设置下跌铃声
     * @param fallRing
     */
    public void setFallRing(String fallRing) {
        setPerferenceString(FALL_RING, fallRing);
    }
    
    /**
     * 获取下跌铃声
     * @return
     */
    public String getFallRing() {
        return mSharedPreferences.getString(FALL_RING, getContext().getString(R.string.mute));
    }
    
    /**
     * 设置公告id
     * @param id
     */
    public void setAnnouncementId(int id){
        setPerferenceInt(ANNOUNCEMENT_ID, id);
    }
    
    /**
     * 获取公告id
     * @return
     */
    public int getAnnouncementId() {
        return mSharedPreferences.getInt(ANNOUNCEMENT_ID, 1);
    }
    
    /**
     * 设置是否自动登录
     * @param autoLogin
     */
    public void setDefaultAutoLogin(boolean autoLogin) {
        setPerferenceBoolean(DEFAULT_AUTO_LOGIN, autoLogin);
    }
    
    /**
     * 获取是否自动登录
     * @return
     */
    public boolean getDefaultAutoLogin() {
        return mSharedPreferences.getBoolean(DEFAULT_AUTO_LOGIN, true);
    }
    
    /**
     * 
     * @param username
     * @param password
     */
    public void setUserNameAndPassword(String username, String password) {
        setUserName(username);
        setPassword(password);
    }
    
    /**
     * 设置用户�?
     * @param username
     */
    public void setUserName(String username) {
        setPerferenceString(USER_NAME, username);
    }
    
    /**
     * 获取用户�?
     * @return
     */
    public String getUserName() {
        return mSharedPreferences.getString(USER_NAME, "");
    }
    
    /**
     * 设置密码
     * @param password
     */
    public void setPassword(String password) {
        setPerferenceString(PASSWORD, password);
    }
    
    /**
     * 获取密码
     * @return
     */
    public String getPassword() {
        return mSharedPreferences.getString(PASSWORD, "");
    }

    /**
     * 获取行情刷新周期
     * 
     * @return
     */
    public int getRefreshClock() {
        return mSharedPreferences.getInt(REFRESH_CLOCK, 5);
    }

    /**
     * 设置行情刷新周期
     * @param clock
     */
    public void setRefreshClock(int clock) {
        setPerferenceInt(REFRESH_CLOCK, clock);
    }
    
    /**
     * 获取K线自动刷新周期（默认1分钟�?
     * @return
     */
    public int getDefaultChartRefreshClock() {
        return mSharedPreferences.getInt(DEFAULT_CHART_REFRESH_CLOCK, 60);
    }
    
    /**
     * 设置K线刷新周�?
     * @param clock
     */
    public void setDefaultChartRefreshClock(int clock) {
        setPerferenceInt(DEFAULT_CHART_REFRESH_CLOCK, clock);
    }

    /**
     * 是否显示倒计�?0不显示，1显示
     * 
     * @return
     */
    public boolean getRefreshClockShow() {
        return mSharedPreferences.getBoolean(REFRESH_CLOCK_SHOW, true);
    }

    /**
     * 是否显示倒计�?0不显示，1显示
     * 
     */
    public void setRefreshClockShow(boolean clockShow) {
        setPerferenceBoolean(REFRESH_CLOCK_SHOW, clockShow);
    }

    /**
     * 屏幕是否常亮�?否，1�?默认)
     * 
     * @return
     */
    public boolean getScreenLight() {
        return mSharedPreferences.getBoolean(SCREEN_KEEP_ON, true);
    }

    /**
     * 屏幕是否常亮�?否，1是，默认
     * 
     * @param screenLight
     */
    public void setScreenLight(boolean screenLight) {
        setPerferenceBoolean(SCREEN_KEEP_ON, screenLight);
    }

    /**
     * 获取自定义金属种�?
     * 
     * @return
     */
    public String getOptionalTIDs() {
        return mSharedPreferences.getString(OPTIONAL_TIDS,
                "XHAU,XHAG,TTAG,RMBAG,USDAG");
    }

    /**
     * 设置自定义金属种�?
     * 
     * @param tids
     */
    public void setOptionalTIDs(String tids) {
        setPerferenceString(OPTIONAL_TIDS, tids);
    }
    
    /**
     * 获取报价排序
     * @return
     */
    public String getDefaultQuotesQueue() {
        return mSharedPreferences.getString(DEFAULT_QUOTES_QUEUE, "");
    }
    
    /**
     * 设置报价排序
     * @param gids
     */
    public void setDefaultQuotesQueue(String gids) {
        setPerferenceString(DEFAULT_QUOTES_QUEUE, gids);
    }

    /**
     * 获取金属种类排序
     * 
     * @return
     */
    public String getCustomMetalsQueue() {
        return mSharedPreferences.getString(CUSTOM_METALS_QUEUE, "");
    }

    /**
     * 设置金属种类排序
     * 
     * @param queue
     */
    public void setCustomMetalsQueue(String queue) {
        setPerferenceString(CUSTOM_METALS_QUEUE, queue);
    }

    /**
     * 获取默认显示的金属种�?
     * 
     * @return
     */
    public int getDefaultMetalsShow() {
        return mSharedPreferences.getInt(DEFAULT_METALS_SHOW, 0);
    }

    /**
     * 设置默认显示的金属种�?
     * 
     * @param showNum
     */
    public void setDefaultMetalsShow(int showNum) {
        setPerferenceInt(DEFAULT_METALS_SHOW, showNum);
    }

    /**
     * 设置RSI 周期
     * @param rsi
     */
    public void setDefaultRsiPeriod(String rsi) {
        setPerferenceString(DEFAULT_RSI_PERIOD, rsi);
    }

    /**
     * 获取RSI周期
     * @return
     */
    public String getDefaultRsiPeriod() {
        return mSharedPreferences.getString(DEFAULT_RSI_PERIOD, "6,12,24");
    }

    /**
     * 设置sma周期
     * @param sma
     */
    public void setDefaultSmaPeriod(String sma) {
        setPerferenceString(DEFAULT_SMA_PERIOD, sma);
    }

    /**
     * 获取sma周期
     * @return
     */
    public String getDefaultSmaPeriod() {
        return mSharedPreferences.getString(DEFAULT_SMA_PERIOD, "5,10,20");
    }

    /**
     * 设置ema 周期
     * @param ema
     */
    public void setDefaultEmaPeriod(String ema) {
        setPerferenceString(DEFAULT_EMA_PERIOD, ema);
    }

    /**
     * 获取ema周期
     * @return
     */
    public String getDefaultEmaPeriod() {
        return mSharedPreferences.getString(DEFAULT_EMA_PERIOD, "5,10,20");
    }

    /**
     * 设置time走势图的周期
     * @param def
     */
    public void setDefaultTimeChart(String def) {
        setPerferenceString(DEFAULT_TIME_CHART, def);
    }

    /**
     * 获取time走势图的周期
     * @return
     */
    public String getDefaultTimeChart() {
        return mSharedPreferences.getString(DEFAULT_TIME_CHART, "3");
    }

    /**
     * 设置K�?周期
     * @param defaultPeriod
     */
    public void setDefaultChartPeriod(int defaultPeriod) {
        setPerferenceInt(DEFAULT_CHART_PERIOD, defaultPeriod);
    }

    /**
     * 获取K线周期默认显�?
     * @return
     */
    public int getDefaultChartPeriod() {
        return mSharedPreferences.getInt(DEFAULT_CHART_PERIOD, 2);
    }

    private void setPerferenceInt(String name, int content) {
        mEditor.putInt(name, content);
        mEditor.commit();
    }

    private void setPerferenceString(String name, String content) {
        mEditor.putString(name, content);
        mEditor.commit();
    }
    
    private void setPerferenceBoolean(String name, boolean content) {
        mEditor.putBoolean(name, content);
        mEditor.commit();
    }
    
    private void setPerferenceLong(String name, long content) {
        mEditor.putLong(name, content);
        mEditor.commit();
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static void setWIDTH(int wIDTH) {
        WIDTH = wIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }

    public static void setHEIGHT(int hEIGHT) {
        HEIGHT = hEIGHT;
    }

}
