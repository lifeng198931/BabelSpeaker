package com.babelspeaker.util;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.metals.data.InitData;

import android.util.Base64;

/**
 * DES 加解�?
 * 
 * @author mark
 * 
 */
public class Des {

    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    private static String KEY = "JiwLYG=-";
    private static Des mInstance;

    public static Des getInstance() {
        if (mInstance == null) {
            mInstance = new Des();
        }
        return mInstance;
    }

    public Des() {
//         KEY = InitData.DESKEY;
    }

    /**
     * DES加密
     * 
     * @param data
     * @return
     * @throws Exception
     */
    public String en(String data) throws Exception {
        String en = encode(KEY, data);
        en = en.replace("+", "$$");
        en = en.replace("/", "@@");
        return en;
    }

    /**
     * DES解密
     * 
     * @param data
     * @return
     */
    public String de(String data) {
        data = data.replace("@@", "/");
        data = data.replace("$$", "+");
        return decodeValue(KEY, data);
    }

    /**
     * DES算法，加�?
     * 
     * @param data
     *            待加密字符串
     * @param key
     *            加密私钥，长度不能够小于8�?
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws CryptException
     *             异常
     */
    private static String encode(String key, String data) throws Exception {
        return encode(key, data.getBytes());
    }

    /**
     * DES算法，加�?
     * 
     * @param data
     *            待加密字符串
     * @param key
     *            加密私钥，长度不能够小于8�?
     * @return 加密后的字节数组，一般结合Base64编码使用
     * @throws CryptException
     *             异常
     */
    private static String encode(String key, byte[] data) throws Exception {
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字�?
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);

            byte[] bytes = cipher.doFinal(data);

            return Base64.encodeToString(bytes, 3);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * DES算法，解�?
     * 
     * @param data
     *            待解密字符串
     * @param key
     *            解密私钥，长度不能够小于8�?
     * @return 解密后的字节数组
     * @throws Exception
     *             异常
     */
    private static byte[] decode(String key, byte[] data) throws Exception {
        try {
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字�?
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    /**
     * 获取编码后的�?
     * 
     * @param key
     * @param data
     * @return
     * @throws Exception
     */
    private static String decodeValue(String key, String data) {
        byte[] datas;
        String value = null;
        try {
            datas = decode(key, Base64.decode(data, 3));
            value = new String(datas);
        } catch (Exception e) {
            value = "";
        }
        return value;
    }

}
