package com.babelspeaker.common;

import java.util.ArrayList;
import java.util.List;

import com.babelspeaker.R;
import com.babelspeaker.data.InitData;
import com.babelspeaker.data.ReceiverList;
import com.babelspeaker.logic.AccountLogic;
import com.babelspeaker.util.CrashHandler;
import com.babelspeaker.util.Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Toast;

public class BaseActivity extends Activity {
    public static List<Activity> mActivities = new ArrayList<Activity>();
    private ProgressDialog mProgressDialog;
    private Boolean mNeedLogin;

    private Intent mLoadService;
    private IntentFilter mIntentFilter = new IntentFilter(ReceiverList.BASE_RECEIVER);
    private BaseReceiver mBaseReceiver = new BaseReceiver();;
    
    private OnLoadInitListener mOnLoadInitListener;
    
    public interface OnLoadInitListener {
        void onLoadInitListener();
    }
    
    public OnLoadInitListener getOnLoadInitListener() {
        return mOnLoadInitListener;
    }

    public void setOnLoadInitListener(OnLoadInitListener onLoadInitListener) {
        mOnLoadInitListener = onLoadInitListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivities.add(this);
        InitData initData = InitData.getInstance(getApplicationContext());
        if (initData.getScreenLight()) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        checkLogin();

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mBaseReceiver, mIntentFilter);
        checkLogin();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mBaseReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivities.remove(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPress();
    }
    
    
    private class BaseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();
            int flag = bundle.getInt("flag");
            switch (flag) {
            case 4:
                dismissProgressDialog();
                if (mOnLoadInitListener != null) {
                    if (InitData.DEBUG) Log.v("initdata", "ababababaabbaab");
                    mOnLoadInitListener.onLoadInitListener();
                } else {
                    if (InitData.DEBUG) Log.v("initdata", "oooooooooooo");
                }
                break;
            case 100:
                break;
            }
        
        }
        
    }

    private void checkLogin() {

        if (mNeedLogin && !AccountLogic.getInstance().isHasLogin()) {
//            showLoginDialog();
        }
        InitData initData = InitData.getInstance(getApplicationContext());
        if (!AccountLogic.getInstance().isHasLogin() && initData.getDefaultAutoLogin() && !initData.getUserName().equals("")) {
            if (InitData.DEBUG) Log.v("initdata", "autoLogin");
//            Intent userLoginService = new Intent(getApplicationContext(), AccountLoginService.class);
//            startService(userLoginService);
        }
    }

    private void backPress() {
        if (mNeedLogin && !AccountLogic.getInstance().isHasLogin()) {
            this.finish();
        }
    }

    
    public void doNewVersionUpdate() {
        StringBuffer sb = new StringBuffer();
        String name = "";
        if (InitData.getInstance(this).getForceUpdateInt() == 1) {
            sb.append("发现新版本，建议您立即更新，否则将不能使用本软件，直到您更新到新版本，\n感谢您的支持");
            name = "关闭";
        } else if (InitData.getInstance(this).getForceUpdateInt() == 0) {
            sb.append("发现新版本，建议您立即更新，否则某些功能将暂时屏蔽，直到您更新到新版本，\n感谢您的支持");
            name = "暂不更新";
        } else {
            return;
        }
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("软件更新")
                .setMessage(sb.toString())
                // 设置内容
                .setPositiveButton("更新",// 设置确定按钮
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                                try {
                                    
                                    Uri uri = Uri.parse(InitData.getInstance(BaseActivity.this).getDownloadUrl());
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                }
                            }

                        })
                .setNegativeButton(name,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int whichButton) {
                                // 点击"取消"按钮之后�?��程序
                                dialog.dismiss();
                                if (InitData.getInstance(BaseActivity.this).getForceUpdateInt() == 1) {
                                    killMyProcess(BaseActivity.this);
                                } 
                            }
                        }).create();// 创建
        // 显示对话�?
        dialog.show();
    }

    public static void killMyProcess(Context context) {
        for (Activity activity : mActivities) {
            activity.finish();
        }

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
    
    public void dismissProgressDialog() {
        getProgressDialog().dismiss();
    }

    public void showProgressDialog(String content) {
        getProgressDialog().setMessage(content);
        getProgressDialog().show();
    }

    public void showProgressDialog(int id) {
        showProgressDialog(getResources().getString(id));
    }

    public void showPrompt(String content) {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show();
    }
    
    public void showPrompt(int id) {
        showPrompt(getResources().getString(id));
    }

    public boolean isNeedLogin() {
        return mNeedLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        mNeedLogin = needLogin;
    }
    

    public ProgressDialog getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        return mProgressDialog;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        mProgressDialog = progressDialog;
    }

}
