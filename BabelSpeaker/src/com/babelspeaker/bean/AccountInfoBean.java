package com.babelspeaker.bean;

import com.babelspeaker.util.Tools;

/**
 * 账号信息
 * 
 * @author mark
 * 
 */
public class AccountInfoBean {

    private String mId;
    private String mUserName;
    private String mNickName;
    private String mName;
    private PhoneStatus mPhone;
    private QQStatus mQQ;
    private String mBalance;
    private String mBonus;
    private String mFreeze;
    private String mCash;
    private String mSafetyCode;
    private int mLevel;
    private int mCredits;
    private String mUserToken;
    
    public AccountInfoBean() {
        clear();
    }
    
    public void clear() {
        mId = "";
        mUserName = "";
        mNickName = "";
        mName = "";
        mPhone = new PhoneStatus();
        mQQ = new QQStatus();
        mBalance = "";
        mBonus = "";
        mFreeze = "";
        mSafetyCode = "";
        mLevel = 0;
        mCash = "";
        mCredits = 0;
        mUserToken = "";
    }

    private class PhoneStatus {
        private String mPhoneNum;
        private int mIsValid;
        
        public PhoneStatus() {
            clear();
        }
        
        public void clear() {
            mPhoneNum = "";
            mIsValid = 0;
        }

        public String getPhoneNum() {
            return mPhoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            mPhoneNum = phoneNum;
        }

        public int getIsValid() {
            return mIsValid;
        }

        public void setIsValid(int isValid) {
            mIsValid = isValid;
        }
    }

    private class QQStatus {
        private String mQQ;
        private int mIsValid;
        
        public QQStatus() {
            clear();
        }
        
        public void clear() {
            mQQ = "";
            mIsValid = 0;
        }

        public String getQQ() {
            return mQQ;
        }

        public void setQQ(String qQ) {
            mQQ = qQ;
        }

        public int getIsValid() {
            return mIsValid;
        }

        public void setIsValid(int isValid) {
            mIsValid = isValid;
        }
    }
    
    private String escapeString(String str) {
        if (str.equals("null")) {
            str = "";
        }
        return str;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = escapeString(userName);
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        mNickName = escapeString(nickName);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = escapeString(name);
    }

    public String getPhone() {
        return mPhone.getPhoneNum();
    }

    public void setPhone(String phone) {
        mPhone.setPhoneNum(escapeString(phone));
    }
    
    public int getPhoneIsValid(){
        return mPhone.getIsValid();
    }
    
    public void setPhoneIsValid(int isValid) {
        mPhone.setIsValid(isValid);
    }

    public String getQQ() {
        return mQQ.getQQ();
    }

    public void setQQNum(String qqNum) {
        mQQ.setQQ(escapeString(qqNum));
    }
    
    public int getQQIsValid() {
       return mQQ.getIsValid();
    }
    
    public void setQQIsValid(int isValid) {
        mQQ.setIsValid(isValid);
    }

    public String getBalance() {
        return mBalance;
    }

    public void setBalance(int balance) {
        mBalance = Tools.getInstance().formatFloat_ex2(balance);
    }

    public String getBonus() {
        return mBonus;
    }

    public void setBonus(int bonus) {
        mBonus = Tools.getInstance().formatFloat_ex2(bonus);
    }
    
    public String getCash() {
        return mCash;
    }

    public void setCash(int cash) {
        mCash = Tools.getInstance().formatFloat_ex2(cash);
    }

    public String getFreeze() {
        return mFreeze;
    }

    public void setFreeze(int freeze) {
        mFreeze = Tools.getInstance().formatFloat_ex2(freeze);
    }

    public String getSafetyCode() {
        return mSafetyCode;
    }

    public void setSafetyCode(String safetyCode) {
        mSafetyCode = safetyCode;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public int getLevel() {
        return mLevel;
    }

    public void setLevel(int level) {
        mLevel = level;
    }

    public int getCredits() {
        return mCredits;
    }

    public void setCredits(int credits) {
        mCredits = credits;
    }

    public String getUserToken() {
        return mUserToken;
    }

    public void setUserToken(String userToken) {
        mUserToken = userToken;
    }

}
