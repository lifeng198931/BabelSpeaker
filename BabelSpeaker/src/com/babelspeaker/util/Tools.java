package com.babelspeaker.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

import com.babelspeaker.data.InitData;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;

/**
 * 工具类
 * 
 * @author mark
 * 
 */
public class Tools {

    private static Tools mInstance;

    public static synchronized Tools getInstance() {
        if (mInstance == null) {
            mInstance = new Tools();
        }
        return mInstance;
    }

    /**
     * Role:获取当前设置的电话号码 <BR>
     */
    public String getNativePhoneNumber(Context context) {
        String NativePhoneNumber = null;
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        NativePhoneNumber = telephonyManager.getLine1Number();
        return NativePhoneNumber;
    }

    /**
     * Role:Telecom service providers获取手机服务商信息 <BR>
     * 需要加入权限<uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/> <BR>
     * Date:2012-3-12 <BR>
     */
    public static String getProvidersName(Context context) {
        String ProvidersName = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String IMSI = telephonyManager.getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 02 07是中国移动，01 06是中国联通，03 05是中国电信。
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")) {
                ProvidersName = "YD";
            } else if (IMSI.startsWith("46001") || IMSI.startsWith("46006")) {
                ProvidersName = "LT";
            } else if (IMSI.startsWith("46003") || IMSI.startsWith("46005")) {
                ProvidersName = "DX";
            }
        } catch (Exception e) {
        }
        return ProvidersName;
    }

    /**
     * 获取ApiKey
     * 
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (NameNotFoundException e) {

        }
        return apiKey;
    }

    /**
     * check 是否有可用网络
     * 
     * @return
     */
    public static boolean CheckNetwork(Context context) {
        boolean flag = false;
        ConnectivityManager cManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cManager.getActiveNetworkInfo() != null)
            flag = cManager.getActiveNetworkInfo().isAvailable();
        return flag;
    }

    /**
     * 获取应用versioncode
     * 
     * @param context
     * @return
     */
    public static int getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取设备ID
     * 
     * @param context
     * @return
     */
    public static String getDeviceID(Context context) {
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = manager.getDeviceId();
        if (deviceId == null || deviceId.equals("0") || deviceId.equals("")) {
            deviceId = InitData.getInstance(context).getDeviceId();
            if (deviceId.equals("")) {
                DateUtil dateUtil = new DateUtil();
                String time = dateUtil.getCurrentDate("yyMMddHHmmss");
                deviceId = time
                        + (int) (Math.random() * (999999 - 100000) + 100000);
                InitData.getInstance(context).setDeviceId(deviceId);
            }
        }

        return deviceId;
    }

    /**
     * dip 转成 像素
     * 
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 像素 转成 dip
     * 
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px 转成 sp
     * 
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 从文件中读取数据
     * 
     * @param context
     * @param fileName
     * @return
     */
    public static String readFromFile(Context context, String fileName) {
        String content = "";
        FileInputStream is = null;
        try {
            is = context.openFileInput(fileName);
            byte buffer[] = new byte[is.available()];
            is.read(buffer);
            content = new String(buffer);
            // Log.i(tag, "read:"+content);
            // InputStreamReader inReader = new InputStreamReader(is);
        } catch (FileNotFoundException e) {
            // Log.e(tag, "createFile:",e);
        } catch (IOException e) {
            // Log.e(tag, "write file",e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // Log.e(tag, "close file",e);
                }
            }
        }
        return content;
    }

    /**
     * 将数据写入文件中
     * 
     * @param context
     * @param fileName
     * @param content
     */
    public static void write2File(Context context, String fileName,
            String content) {
        FileOutputStream os = null;
        int mode = Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE;
        try {
            os = context.openFileOutput(fileName, mode);
            os.write(content.getBytes());
            /*
             * OutputStreamWriter outWriter = new OutputStreamWriter (os);
             * outWriter.write(content);
             */
            // Log.i(tag, "write:"+content);
        } catch (FileNotFoundException e) {
            // Log.e(tag, "createFile:",e);
        } catch (IOException e) {
            // Log.e(tag, "write file",e);
        } finally {
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    // Log.e(tag, "close file",e);
                }
            }
        }
    }

    /**
     * 判断是否为数字
     * 
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否锁屏
     * 
     * @param c
     * @return
     */
    public final static boolean isScreenLocked(Context c) {
        android.app.KeyguardManager mKeyguardManager = (KeyguardManager) c
                .getSystemService(Context.KEYGUARD_SERVICE);
        return !mKeyguardManager.inKeyguardRestrictedInputMode();
    }

    // 比较第结束时间是否大于当前时间
    public boolean compare_date(String endTime, String currentTime) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(endTime);
            Date dt2 = df.parse(currentTime);
            if (dt1.getTime() > dt2.getTime()) {
                // System.out.println("dt1 在dt2前");
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * 字符串转换成日期
     * 
     * @param str
     * @return date
     */
    public String getDays(String str) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long diff = date.getTime() - (new Date()).getTime();
        long days = diff / (24 * 60 * 60 * 1000);
        String s_days = "0";
        if (days > 0) {
            s_days = (int) days + "";
        } else {
            s_days = "0";
        }
        return s_days;
    }

    /**
     * 小数2位 float
     * 
     * @param val
     * @return
     */
    public String formatFloat(float val) {
        DecimalFormat df = new DecimalFormat("########.00");
        String ret = "0";
        try {
            ret = String.valueOf(Float.parseFloat(df.format(val)));
            String[] ar = ret.split("\\.");
            if (ar.length == 1) {
                ret = ret + ".00";
            } else {
                if (ar[1].length() == 1) {
                    ret = ret + "0";
                }
            }
        } catch (Exception e) {
            // 不退出
        }
        return ret;
    }
    
    /**
     * 
     * @param context
     * @param url
     * @return
     */
    public String formatUrl(Context context,String url) {
        String tokenKey = InitData.getInstance(context).getTokenKey();
        url = url + "&apitoken=" + Tools.getInstance().getMD5Str(Tools.getInstance().getMD5Str(tokenKey, true), false);
        return url;
    }

    public String formatThreeFloat(float val) {
        DecimalFormat df = new DecimalFormat("########.000");
        String ret = "0";
        try {
            ret = String.valueOf(Float.parseFloat(df.format(val)));
            String[] ar = ret.split("\\.");
            if (ar.length == 1) {
                ret = ret + ".000";
            } else {
                int length = ar[1].length();
                if (length == 1) {
                    ret = ret + "00";
                } else if (length == 2) {
                    ret = ret + "0";
                }
            }
        } catch (Exception e) {
            // 不退出
        }
        return ret;
    }

    public String formatFloat_ex2(float val) {
        DecimalFormat df = new DecimalFormat("########.00");
        String ret = String.valueOf(Float.parseFloat(df.format(val / 100)));
        String[] ar = ret.split("\\.");
        if (ar.length == 1) {
            ret = ret + ".00";
        } else {
            if (ar[1].length() == 1) {
                ret = ret + "0";
            }
        }
        return ret;
    }

    public String formatFloat_ex0(float val) {
        DecimalFormat df = new DecimalFormat("########.00");
        float v = Float.parseFloat(df.format(val / 100));
        int iv = (new BigDecimal(v).setScale(0, BigDecimal.ROUND_HALF_UP))
                .intValue();
        String ret = String.valueOf(iv);
        return ret;
    }

    public String formatFloat_ex(float val) {
        DecimalFormat df = new DecimalFormat("########.000");
        String ret = String.valueOf(Float.parseFloat(df.format(val)));
        String[] ar = ret.split("\\.");
        if (ar[1].equals("0")) {
            ret = ret.replace(".0", "");
        }
        return ret;
    }

    public String formatFloat_ex4(float val) {
        DecimalFormat df = new DecimalFormat("########.0000");
        return String.valueOf(Float.parseFloat(df.format(val)));
    }

    public String formatMoneyToYuan(String val) {
        return (Integer.parseInt(val) / 100) + "";
    }

    public String formatMoney(String val) {
        float fMoney = Float.parseFloat(val) / 100;
        DecimalFormat df = new DecimalFormat("########.00");
        String ret = String.valueOf(Float.parseFloat(df.format(fMoney)));
        String[] ar = ret.split("\\.");
        if (ar.length == 1) {
            ret = ret + ".00";
        } else {
            if (ar[1].length() == 1) {
                ret = ret + "0";
            }
        }
        return ret;
    }

    public String formatMoney_ex(String val) {
        float fMoney = Float.parseFloat(val);
        DecimalFormat df = new DecimalFormat("########.000");
        String ret = String.valueOf(Float.parseFloat(df.format(fMoney)));
        String[] ar = ret.split("\\.");
        if (ar[1].equals("0")) {
            ret = ret.replace(".0", "");
        }
        return ret;
    }

    public String formatMoney_ex4(String val) {
        float fMoney = Float.parseFloat(val);
        DecimalFormat df = new DecimalFormat("########.0000");
        String ret = String.valueOf(Float.parseFloat(df.format(fMoney)));
        String[] ar = ret.split("\\.");
        if (ar[1].equals("0")) {
            ret = ret.replace(".0", "");
        }
        return ret;
    }

    public String formatMoney_ex2(String val) {
        String ret = "0";
        try {
            float fMoney = Float.parseFloat(val);
            DecimalFormat df = new DecimalFormat("########.00");
            ret = String.valueOf(Float.parseFloat(df.format(fMoney)));
            String[] ar = ret.split("\\.");
            if (ar.length == 1) {
                ret = ret + ".00";
            } else {
                if (ar[1].length() == 1) {
                    ret = ret + "0";
                }
            }
        } catch (Exception e) {
        }
        return ret;
    }

    public String formatFloat_r(float val) {
        DecimalFormat df = new DecimalFormat("########.00");
        return String.valueOf(Float.parseFloat(df.format(val))).replace(".0",
                "");
    }

    public String maxBuy(String money, String price) {
        int cnt = (int) (Float.parseFloat(money) / Float.parseFloat(price));
        return String.valueOf(cnt);
    }

    public String getCommState(String state) {
        // 0受理，1撤销，2完成
        String ret = "";
        switch (Integer.parseInt(state)) {
        case 0:
            ret = "受理";
            break;
        case 1:
            ret = "撤销";
            break;
        case 2:
            ret = "完成";
            break;
        }
        return ret;
    }

    public String getRandom() {
        Random random = new Random();
        return String.valueOf(random.nextInt());
    }

    static public String toMd5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return "";
        }
    }

    /*
     * 描述不同公司手机号码规则的正则表达式 cmcc-中国移动手机号码规则 cucc-中国联通手机号码规则 cnc--中国网通3G手机号码规则
     */
    private static String cmcc = "^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[012789]{1}))[0-9]{8}$";
    private static String cucc = "^[1]{1}(([3]{1}[0-3]{1})|([5]{1}[3456]{1}))[0-9]{8}$";
    private static String cnc = "^[1]{1}[8]{1}[0-9]{1}[0-9]{8}$";

    public int matchNum(String mobPhnNum) {
        int flag;// 存储匹配结果
        // 判断手机号码是否是11位
        if (mobPhnNum.length() == 11) {
            // 判断手机号码是否符合中国移动的号码规则
            if (mobPhnNum.matches(cmcc)) {
                flag = 1;
            }
            // 判断手机号码是否符合中国联通的号码规则
            else if (mobPhnNum.matches(cucc)) {
                flag = 2;
            }
            // 判断手机号码是否符合中国网通的号码规则
            else if (mobPhnNum.matches(cnc)) {
                flag = 3;
            }
            // 都不合适
            else {
                flag = 4;
            }
        }
        // 不是11位
        else {
            flag = 5;
        }
        return flag;
    }

    /*
     * MD5加密
     */
    public String getMD5Str(String str, boolean splite) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();
            // Log.d("str", str);
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        // 16位加密，从第9位到25位
        if (splite) {
            return md5StrBuff.substring(7, 19).toString().toLowerCase();
        } else {
            return md5StrBuff.toString().toLowerCase();
        }
    }

}
