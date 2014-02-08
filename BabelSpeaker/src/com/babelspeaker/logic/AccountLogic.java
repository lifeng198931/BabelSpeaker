package com.babelspeaker.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.babelspeaker.bean.AccountInfoBean;
import com.babelspeaker.bean.ResultBean;
import com.babelspeaker.common.BaseLogic;
import com.babelspeaker.data.Api;
import com.babelspeaker.data.InitData;
import com.babelspeaker.util.Tools;

public class AccountLogic extends BaseLogic {

    private static AccountLogic mInstance;
    private String mNewUserName;
    private String mNewUserPwd;
    private String mUserName;
    private String mUserPwd;
    private String mUserPhone;
    private int mReturnState;
    private int mDefaultRegisterMode = 0;
    private String mPhoneCode;

    private int mModifyMode = 0;
    private String mModifyName;

    private ResultBean mSendVerificationResult;
    private ResultBean mPhoneRegisterResult;
    private ResultBean mQQRegisterResult;
    private ResultBean mLoginResult;
    private ResultBean mModifyAccountInfoResult;
    private ResultBean mModifyPhoneResult;
    private ResultBean mModifyQQResult;
    private ResultBean mModifyPasswordResult;
    private ResultBean mRechargeListResult;
    private ResultBean mRechargeOrderResult;
    private ResultBean mRechargeUpdateResult;
    private ResultBean mRefreshAccountResult;

    private String mOldPassword;
    private String mNewPassword;

    private AccountInfoBean mAccountInfoBean;

    private boolean mHasLogin = false;

    private String mRechargeID;

    private int mFindPasswordType = 0;
    private ResultBean mFindPasswordResult;
    private String mFindPasswordName;

    private String mFindPasswordCode;
    private ResultBean mModifyPasswordByPhoneResult;
    private String mModifyPassword;

    private ResultBean mRechargeHistoryResult;
    private ResultBean mConsumeHistoryResult;

    private int mRechargeId = 1;
    private int mConsumeId = 1;

    public static synchronized AccountLogic getInstance() {
        if (mInstance == null) {
            mInstance = new AccountLogic();
        }
        return mInstance;
    }

    /**
     * 修改密码
     */
    public void modifyPassword(Context context) {
        try {
            String url = Api.Account.MODIFY_PASSWORD;

            url = url.replace("@id", mAccountInfoBean.getId());
            url = url.replace("@old", mOldPassword);
            url = url.replace("@new", mNewPassword);
            url = url.replace("@safety", mAccountInfoBean.getSafetyCode());

            // Log.v("msg", url);
            //
            // String result = net.getStringFromUrl(url);
            // Log.v("msg", result);
            // getModifyPasswordResult();
            mModifyPasswordResult = getStringFromUrl(url, context);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 发送code
     */
    public void sendVerification(Context context) {
        try {
            String url = Api.Account.SEND_VERIFICATION + mUserPhone;
            // String ret = net.getStringFromUrl(url);
            // getSendVerificationResult();
            // mSendVerificationResult.clear();
            mSendVerificationResult = getStringFromUrl(url, context);// handleResult(ret);

        } catch (Exception e) {
        }
    }

    /**
     * 用户注册
     */
    public void register(Context context) {
        try {
            mReturnState = -1;
            String url = Api.Account.REGISTER_BY_QQ;

            if (mDefaultRegisterMode == 0) {
                url = Api.Account.REGISTER_BY_PHONE;
                url = url.replace("@c", mPhoneCode);
            }

            url = url.replace("@nickName", getUserName());
            url = url.replace("@p", mUserPhone);
            url = url.replace("@wd", mNewUserPwd);

            if (mDefaultRegisterMode == 0) {
                mPhoneRegisterResult = getStringFromUrl(url, context);// handleResult(result);
            } else {
                mQQRegisterResult = getStringFromUrl(url, context);// handleResult(result);
            }

        } catch (Exception e) {
            // 异常不处理
        }
    }

    /**
     * 登录
     */
    public void login(Context context) {
        try {
            String deviceid = Tools.getDeviceID(context);
            String p = AccountLogic.getInstance().getUserName() + "@" + AccountLogic.getInstance().getUserPwd();
            String url = Api.Account.LOGIN;

            InitData initData = InitData.getInstance(context);
            url = url.replace("@key", initData.getUserName());
            url = url.replace("@pwd", initData.getPassword());
            url = url.replace("@dev", deviceid);

            if (InitData.DEBUG)
                Log.v("msg", url);
            mLoginResult = getStringFromUrl(url, context);// handleResult(result);
            if (mLoginResult.getStat() == 200) {
                mHasLogin = true;
                handleUserInfo(mLoginResult.getData());
                // Intent savePushInfoService = new Intent(context,
                // RemindInfoSaveService.class);
                // context.startService(savePushInfoService);
                // if (!initData.getDefaultAutoLogin()) {
                // InitData.getInstance(context).setUserNameAndPassword("", "");
                // }
            } else if (mLoginResult.getStat() == 402) {
                InitData.getInstance(context).setUserNameAndPassword("", "");
            }

            // Log.v("msg", result);
        } catch (Exception e) {
            // 异常不处理
        }

    }

    public void refreshAccountInfo(Context context) {
        try {
            String url = Api.Account.GET_MESSAGE;

            url = url.replace("@id", getAccountInfoBean().getId());
            url = url.replace("@safety", getAccountInfoBean().getSafetyCode());

            if (InitData.DEBUG)
                Log.v("msg", url);
            mRefreshAccountResult = getStringFromUrl(url, context);// handleResult(result);
            if (mRefreshAccountResult.getStat() == 200) {
                handleUserInfo(mRefreshAccountResult.getData());
            }
        } catch (Exception e) {
        }

    }

    /**
     * 修改账号信息
     */
    // public void modifyUserInfo(Context context) {
    // try {
    // String url = Api.Account.MODIFY_USER_MESSAGE;
    //
    // url = url.replace("@safety", mAccountInfoBean.getSafetyCode());
    // url = url.replace("@id", mAccountInfoBean.getId());
    // if (mModifyMode == 0) {
    // // 修改昵称
    // url = url.replace("@nickName", mModifyName);
    // url = url.replace("@realName", mAccountInfoBean.getName());
    //
    // } else {
    // url = url.replace("@nickName", mAccountInfoBean.getNickName());
    // url = url.replace("@realName", mModifyName);
    // }
    //
    // mModifyAccountInfoResult = getStringFromUrl(url,
    // context);//handleResult(result);
    // if (mModifyAccountInfoResult.getStat() == 200) {
    // if (mModifyMode == 0) {
    // mAccountInfoBean.setNickName(mModifyName);
    // } else {
    // mAccountInfoBean.setName(mModifyName);
    // }
    // }
    //
    // } catch (Exception e) {
    //
    // }
    //
    // }

    /**
     * 修改手机
     */
    // public void modifyPhone(Context context) {
    // try {
    // String url = Api.Account.MODIFY_MOBI;
    //
    // url = url.replace("@id", mAccountInfoBean.getId());
    // url = url.replace("@mobi", mUserPhone);
    // url = url.replace("@code", mPhoneCode);
    // url = url.replace("@safety", mAccountInfoBean.getSafetyCode());
    //
    // mModifyPhoneResult = getStringFromUrl(url, context);
    //
    // if (mModifyPhoneResult.getStat() == 200) {
    // mAccountInfoBean.setPhone(mUserPhone);
    // }
    //
    // } catch (Exception e) {
    // // TODO: handle exception
    // }
    // }

    /**
     * 修改QQ
     */
    // public void modifyQQ(Context context) {
    //
    // try {
    // String url = Api.Account.MODIFY_QQ;
    //
    // url = url.replace("@id", mAccountInfoBean.getId());
    // url = url.replace("@qq", mUserPhone);
    // url = url.replace("@safety", mAccountInfoBean.getSafetyCode());
    //
    // mModifyQQResult = getStringFromUrl(url, context);
    //
    // } catch (Exception e) {
    // // TODO: handle exception
    // }
    // }

    /**
     * 获取账号等级列表数据
     */
    // public void getAccountLevelList(Context context) {
    // try {
    // String url = Api.Account.ACCOUNT_LEVEL_LIST;
    //
    // ResultBean bean = getStringFromUrl(url, context);
    // if (bean.getStat() == 200) {
    // getLevelBeans();
    // mLevelBeans.clear();
    // JSONArray array = new JSONArray(bean.getData());
    //
    // for (int i = 0; i < array.length(); i++) {
    // AccountLevelBean levelBean = new AccountLevelBean();
    // JSONObject object = array.getJSONObject(i);
    // int id = object.getInt("id");
    // String name = object.getString("name");
    // String upgrade = object.getString("upgrad");
    // String service = object.getString("service");
    //
    // levelBean.setID(id);
    // levelBean.setName(name);
    // levelBean.setUpgrade(upgrade);
    // levelBean.setService(service);
    //
    // mLevelBeans.add(i, levelBean);
    //
    // }
    //
    // }
    //
    // } catch (Exception e) {
    // // TODO: handle exception
    // }
    // }

    private void handleUserInfo(String data) {
        getAccountInfoBean();
        try {
            // JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = new JSONObject(data);// jsonArray.getJSONObject(0);

            String id = jsonObject.getString("id");
            String userName = jsonObject.getString("userName");
            String name = jsonObject.getString("realName");
            int cash = jsonObject.getInt("cash");
            int freezeCash = jsonObject.getInt("freezeCash");
            int bonus = jsonObject.getInt("bonus");
            String phone = jsonObject.getString("phone");
            // phone = Des.getInstance().de(phone);
            String qq = jsonObject.getString("qq");
            int validPhone = jsonObject.getInt("validPhone");
            int validQQ = jsonObject.getInt("validQq");
            String safetyCode = jsonObject.getString("safetyCode");
            int levelInt = 0;
            try {
                levelInt = jsonObject.getInt("level");
            } catch (Exception e) {
            }
            String nickName = jsonObject.getString("nickName");
            String cuid = jsonObject.getString("cuid");
            int credits = jsonObject.getInt("credits");

            if (InitData.DEBUG)
                Log.v("freezeCash", freezeCash + "");

            mAccountInfoBean.setUserToken(cuid);
            mAccountInfoBean.setNickName(nickName);
            mAccountInfoBean.setId(id);
            mAccountInfoBean.setUserName(userName);
            mAccountInfoBean.setPhone(phone);
            mAccountInfoBean.setPhoneIsValid(validPhone);
            mAccountInfoBean.setQQNum(qq);
            mAccountInfoBean.setQQIsValid(validQQ);
            mAccountInfoBean.setSafetyCode(safetyCode);
            mAccountInfoBean.setCash(cash);
            mAccountInfoBean.setFreeze(freezeCash);
            mAccountInfoBean.setBonus(bonus);
            mAccountInfoBean.setName(name);
            mAccountInfoBean.setLevel(levelInt);
            mAccountInfoBean.setCredits(credits);
        } catch (JSONException e) {
            Log.v("mAccountInfoBean", e.toString());
        }

    }

    /**
     * 获取充值列表
     */
    // public void getRechargeList(Context context) {
    // try {
    //
    // mRechargeListResult = getStringFromUrl(Api.Account.RECHARGE_LIST,
    // context);
    //
    // if (mRechargeListResult.getStat() == 200) {
    // JSONArray data = new JSONArray(mRechargeListResult.getData());
    // getRechargeItemBeans();
    // mRechargeItemBeans.clear();
    // for (int i = 0; i < data.length(); i++) {
    // JSONObject item = data.getJSONObject(i);
    // // {"pi":"1","pv":"0.01","s":"充值","b":"充值1元","p":"0.01"}
    //
    // String Id = item.getString("payid");
    // String title = item.getString("subject");
    // String content = item.getString("body");
    // String price = item.getString("price");
    //
    // AccountRechargeItemBean itemBean = new AccountRechargeItemBean();
    //
    // itemBean.setTitle(title);
    // itemBean.setContent(content);
    // itemBean.setID(Id);
    // itemBean.setPrice(price);
    //
    // mRechargeItemBeans.add(itemBean);
    // }
    // }
    //
    // } catch (Exception e) {
    // // 异常不处理
    // Log.v("error", e.toString());
    // }
    // }

    /**
     * 获取order id
     */
    public void getRechargeOrder(Context context) {
        try {
            String url = Api.Account.RECHARGE_ORDER;

            url = url.replace("@uid", getAccountInfoBean().getId());
            url = url.replace("@safety", getAccountInfoBean().getSafetyCode());
            url = url.replace("@amount", getRechargeID());

            mRechargeOrderResult = getStringFromUrl(url, context);

        } catch (Exception e) {

        }
    }

    public void updateRechargeOrder(Context context) {
        try {
            String url = Api.Account.RECHARGE_UPDATE;

            url = url.replace("@uid", getAccountInfoBean().getId());
            url = url.replace("@trade_no", getRechargeOrderResult().getData());
            url = url.replace("@safety", mAccountInfoBean.getSafetyCode());
            mRechargeUpdateResult = getStringFromUrl(url, context);
            if (mRechargeUpdateResult.getStat() == 200) {
                if (!mRechargeUpdateResult.getData().equals("0")) {
                    mAccountInfoBean.setCash(Integer.getInteger(mRechargeUpdateResult.getData()));
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // public void getRechargeHistoryData(Context context) {
    // try {
    // String url = Api.Account.RECHARGE_HISTORY;
    // AccountInfoBean accountInfoBean = AccountLogic.getInstance()
    // .getAccountInfoBean();
    //
    // url = url.replace("@uid", accountInfoBean.getId());
    // url = url.replace("@safety", accountInfoBean.getSafetyCode());
    //
    // mRechargeHistoryResult = getStringFromUrl(url, context);
    // if (mRechargeHistoryResult.getStat() == 200) {
    // getRechargeHistoryBeans();
    // JSONArray array = new JSONArray(mRechargeHistoryResult.getData());
    // mRechargeHistoryBeans.clear();
    // for (int i = 0; i < array.length(); i++) {
    // JSONObject object = array.getJSONObject(i);
    // String create = object.getString("c");
    // String id = object.getString("o");
    // String price = object.getString("p");
    // String end = object.getString("e");
    //
    // RechargeHistoryBean historyBean = new RechargeHistoryBean();
    // historyBean.setCreate(create);
    // historyBean.setOrderId(id);
    // historyBean.setPrice(price);
    // historyBean.setEnd(end);
    // getRechargeHistoryBeans().add(historyBean);
    //
    // }
    //
    // }
    //
    // } catch (Exception e) {
    // // TODO: handle exception
    // }
    // }

    // public void getConsumeHistoryData(Context context) {
    // try {
    // String url = Api.Account.CONSUME_HISTORY;
    // AccountInfoBean accountInfoBean = AccountLogic.getInstance()
    // .getAccountInfoBean();
    // url = url.replace("@uid", accountInfoBean.getId());
    // url = url.replace("@safety", accountInfoBean.getSafetyCode());
    //
    // mConsumeHistoryResult = getStringFromUrl(url, context);
    // if (mConsumeHistoryResult.getStat() == 200) {
    // getConsumeHistoryBeans();
    // JSONArray array = new JSONArray(mConsumeHistoryResult.getData());
    // mConsumeHistoryBeans.clear();
    // for (int i = 0; i < array.length(); i++) {
    // JSONObject object = array.getJSONObject(i);
    // String date = object.getString("dt");
    // String price = object.getString("p");
    // String content = object.getString("ct");
    //
    // ConsumeHistoryBean historyBean = new ConsumeHistoryBean();
    // historyBean.setSendTime(date);
    // historyBean.setContent(content);
    // historyBean.setPrice(price);
    // getConsumeHistoryBeans().add(historyBean);
    //
    // }
    //
    // }
    //
    // } catch (Exception e) {
    // }
    // }

    public void findPassword(Context context) {
        try {
            String url = Api.Account.FIND_PASSWORD;
            String type;
            if (mFindPasswordType == 0) {
                type = "s";
            } else {
                type = "q";
            }

            url = url.replace("@type", type);
            url = url.replace("@key", mFindPasswordName);
            mFindPasswordResult = getStringFromUrl(url, context);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void modifyPasswordByPhone(Context context) {
        try {
            String url = Api.Account.MODIFY_PASSWORD_BY_PHONE;
            getFindPasswordCode();
            getModifyPassword();
            url = url.replace("@mobi", mFindPasswordName);
            url = url.replace("@code", mFindPasswordCode);
            url = url.replace("@password", mModifyPassword);

            mModifyPasswordByPhoneResult = getStringFromUrl(url, context);

        } catch (Exception e) {

        }
    }

    public void logout(Context context) {
        mHasLogin = false;
        getAccountInfoBean().clear();
        InitData.getInstance(context).setUserNameAndPassword("", "");
    }

    public String getNewUserName() {
        return mNewUserName;
    }

    public void setNewUserName(String newUserName) {
        this.mNewUserName = newUserName;
    }

    public String getNewUserPwd() {
        return mNewUserPwd;
    }

    public void setNewUserPwd(String newUserPwd) {
        this.mNewUserPwd = newUserPwd;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getUserPwd() {
        return mUserPwd;
    }

    public void setUserPwd(String userPwd) {
        mUserPwd = userPwd;
    }

    public boolean isHasLogin() {
        return mHasLogin;
    }

    public void setHasLogin(boolean hasLogin) {
        mHasLogin = hasLogin;
    }

    public String getUserPhone() {
        return mUserPhone;
    }

    public void setUserPhone(String userPhone) {
        mUserPhone = userPhone;
    }

    public int getReturnState() {
        return mReturnState;
    }

    public void setReturnState(int returnState) {
        mReturnState = returnState;
    }

    public int getDefaultRegisterMode() {
        return mDefaultRegisterMode;
    }

    public void setDefaultRegisterMode(int defaultRegisterMode) {
        mDefaultRegisterMode = defaultRegisterMode;
    }

    public String getPhoneCode() {
        return mPhoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        mPhoneCode = phoneCode;
    }

    public ResultBean getSendVerificationResult() {
        if (mSendVerificationResult == null) {
            mSendVerificationResult = new ResultBean();
        }
        return mSendVerificationResult;
    }

    public void setSendVerificationResult(ResultBean sendVerificationResult) {
        mSendVerificationResult = sendVerificationResult;
    }

    public ResultBean getPhoneRegisterResult() {
        if (mPhoneRegisterResult == null) {
            mPhoneRegisterResult = new ResultBean();
        }
        return mPhoneRegisterResult;
    }

    public void setPhoneRegisterResult(ResultBean phoneRegisterResult) {
        mPhoneRegisterResult = phoneRegisterResult;
    }

    public ResultBean getQQRegisterResult() {
        if (mQQRegisterResult == null) {
            mQQRegisterResult = new ResultBean();
        }
        return mQQRegisterResult;
    }

    public void setQQRegisterResult(ResultBean qQRegisterResult) {
        mQQRegisterResult = qQRegisterResult;
    }

    public ResultBean getLoginResult() {
        if (mLoginResult == null) {
            mLoginResult = new ResultBean();
        }
        return mLoginResult;
    }

    public void setLoginResult(ResultBean loginResult) {
        mLoginResult = loginResult;
    }

    public AccountInfoBean getAccountInfoBean() {
        if (mAccountInfoBean == null) {
            mAccountInfoBean = new AccountInfoBean();
        }
        return mAccountInfoBean;
    }

    public ResultBean getModifyAccountInfoResult() {
        if (mModifyAccountInfoResult == null) {
            mModifyAccountInfoResult = new ResultBean();
        }
        return mModifyAccountInfoResult;
    }

    public void setModifyAccountInfoResult(ResultBean modifyAccountInfoResult) {
        mModifyAccountInfoResult = modifyAccountInfoResult;
    }

    public ResultBean getModifyPhoneResult() {
        if (mModifyPhoneResult == null) {
            mModifyPhoneResult = new ResultBean();
        }
        return mModifyPhoneResult;
    }

    public void setModifyPhoneResult(ResultBean modifyPhoneResult) {
        mModifyPhoneResult = modifyPhoneResult;
    }

    public ResultBean getModifyQQResult() {
        if (mModifyQQResult == null) {
            mModifyQQResult = new ResultBean();
        }
        return mModifyQQResult;
    }

    public void setModifyQQResult(ResultBean modifyQQResult) {
        mModifyQQResult = modifyQQResult;
    }

    public void setAccountInfoBean(AccountInfoBean accountInfoBean) {
        mAccountInfoBean = accountInfoBean;
    }

    public int getModifyMode() {
        return mModifyMode;
    }

    public void setModifyMode(int modifyMode) {
        mModifyMode = modifyMode;
    }

    public String getModifyName() {
        return mModifyName;
    }

    public void setModifyName(String modifyName) {
        mModifyName = modifyName;
    }

    public ResultBean getModifyPasswordResult() {
        if (mModifyPasswordResult == null) {
            mModifyPasswordResult = new ResultBean();
        }
        return mModifyPasswordResult;
    }

    public void setModifyPasswordResult(ResultBean modifyPasswordResult) {
        mModifyPasswordResult = modifyPasswordResult;
    }

    public String getOldPassword() {
        return mOldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.mOldPassword = oldPassword;
    }

    public String getNewPassword() {
        return mNewPassword;
    }

    public void setNewPassword(String newPassword) {
        this.mNewPassword = newPassword;
    }

    public ResultBean getRechargeListResult() {
        if (mRechargeListResult == null) {
            mRechargeListResult = new ResultBean();
        }
        return mRechargeListResult;
    }

    public void setRechargeListResult(ResultBean rechargeListResult) {
        mRechargeListResult = rechargeListResult;
    }

    public ResultBean getRechargeOrderResult() {
        if (mRechargeOrderResult == null) {
            mRechargeOrderResult = new ResultBean();
        }
        return mRechargeOrderResult;
    }

    public void setRechargeOrderResult(ResultBean rechargeOrderResult) {
        mRechargeOrderResult = rechargeOrderResult;
    }

    public ResultBean getRechargeUpdateResult() {
        if (mRechargeUpdateResult == null) {
            mRechargeUpdateResult = new ResultBean();
        }
        return mRechargeUpdateResult;
    }

    public void setRechargeUpdateResult(ResultBean rechargeUpdateResult) {
        mRechargeUpdateResult = rechargeUpdateResult;
    }

    public String getRechargeID() {
        return mRechargeID;
    }

    public void setRechargeID(String rechargeID) {
        mRechargeID = rechargeID;
    }

    public int getFindPasswordType() {
        return mFindPasswordType;
    }

    public void setFindPasswordType(int findPasswordType) {
        mFindPasswordType = findPasswordType;
    }

    public ResultBean getFindPasswordResult() {
        if (mFindPasswordResult == null) {
            mFindPasswordResult = new ResultBean();
        }
        return mFindPasswordResult;
    }

    public void setFindPasswordResult(ResultBean findPasswordResult) {
        mFindPasswordResult = findPasswordResult;
    }

    public String getFindPasswordName() {
        return mFindPasswordName;
    }

    public void setFindPasswordName(String findPasswordName) {
        mFindPasswordName = findPasswordName;
    }

    public String getFindPasswordCode() {
        if (mFindPasswordCode == null) {
            mFindPasswordCode = "";
        }
        return mFindPasswordCode;
    }

    public void setFindPasswordCode(String findPasswordCode) {
        mFindPasswordCode = findPasswordCode;
    }

    public ResultBean getModifyPasswordByPhoneResult() {
        if (mModifyPasswordByPhoneResult == null) {
            mModifyPasswordByPhoneResult = new ResultBean();
        }
        return mModifyPasswordByPhoneResult;
    }

    public void setModifyPasswordByPhoneResult(ResultBean modifyPasswordByPhoneResult) {
        mModifyPasswordByPhoneResult = modifyPasswordByPhoneResult;
    }

    public String getModifyPassword() {
        if (mModifyPassword == null) {
            mModifyPassword = "";
        }
        return mModifyPassword;
    }

    public void setModifyPassword(String modifyPassword) {
        mModifyPassword = modifyPassword;
    }

    public ResultBean getRefreshAccountResult() {
        if (mRefreshAccountResult == null) {
            mRefreshAccountResult = new ResultBean();
        }
        return mRefreshAccountResult;
    }

    public void setRefreshAccountResult(ResultBean refreshAccountResult) {
        mRefreshAccountResult = refreshAccountResult;
    }

    public ResultBean getRechargeHistoryResult() {
        if (mRechargeHistoryResult == null) {
            mRechargeHistoryResult = new ResultBean();
        }
        return mRechargeHistoryResult;
    }

    public void setRechargeHistoryResult(ResultBean rechargeHistoryResult) {
        mRechargeHistoryResult = rechargeHistoryResult;
    }

    public ResultBean getConsumeHistoryResult() {
        if (mConsumeHistoryResult == null) {
            mConsumeHistoryResult = new ResultBean();
        }
        return mConsumeHistoryResult;
    }

    public void setConsumeHistoryResult(ResultBean consumeHistoryResult) {
        mConsumeHistoryResult = consumeHistoryResult;
    }

    public int getRechargeId() {
        return mRechargeId;
    }

    public void setRechargeId(int rechargeId) {
        mRechargeId = rechargeId;
    }

    public int getConsumeId() {
        return mConsumeId;
    }

    public void setConsumeId(int consumeId) {
        mConsumeId = consumeId;
    }

}
