package com.tdp.blockexplorer.tool;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.tdp.blockexplorer.R;


/**
 * Created by wulijie on 2018/1/12.
 */
public class AppLoading extends Dialog {
    private TextView mTips;

    private Context mContext;

    private boolean isCancel;

    public AppLoading(Context context) {
        super(context, R.style.AppLoadingStyle);
        mContext = context;
        this.isCancel = true;
        setCancelable(isCancel);
        init();
    }


    public AppLoading(Context context, boolean isCancel, int resId) {
        super(context, R.style.AppLoadingStyle);
        mContext = context;
        init();
        setCancelable(isCancel);
        this.isCancel = isCancel;
        setTips(resId);
    }

    public AppLoading(Context context, int theme) {
        super(context, R.style.AppLoadingStyle);
        mContext = context;
        init();
    }

    //设置提示文字
    public void setTips(int resId) {
        String tip = mContext.getResources().getString(resId);
        setTips(tip);
    }

    //设置提示文字
    public void setTips(String str) {
        mTips.setText(str);
    }

    @Override
    public void onBackPressed() {
//        if (isCancel) {
//            if (mContext instanceof Activity) {
//                Activity act = (Activity) mContext;
//                act.finish();
//            }
//            return;
//        }
        super.onBackPressed();
    }

    /**
     * 初始化
     **/
    protected void init() {
        setContentView(R.layout.dialog_loading);
        mTips = (TextView) findViewById(R.id.loading_tips);
    }

    /**
     * show dialog with the tips
     *
     * @param tips
     * @return
     */
    public AppLoading show(String tips) {
        setTips(tips);
        show();
        return this;
    }
}
