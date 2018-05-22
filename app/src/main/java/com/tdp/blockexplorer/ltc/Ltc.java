package com.tdp.blockexplorer.ltc;

import com.tdp.blockexplorer.blockchain.BlockCallback;
import com.tdp.blockexplorer.blockchain.BlockError;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by wulijie on 2018/4/13.
 */
public class Ltc {
    public void get(RequestParams params, final BlockCallback callback) {
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("LTC onSuccess =" + result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("LTC onError =" + ex.toString());
                BlockError error = new BlockError();
                error.error = ex.toString();
                callback.onError(error);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
