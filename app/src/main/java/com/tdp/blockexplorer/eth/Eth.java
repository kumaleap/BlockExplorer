package com.tdp.blockexplorer.eth;

import com.tdp.blockexplorer.blockchain.BlockCallback;
import com.tdp.blockexplorer.blockchain.BlockError;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by wulijie on 2018/4/11.
 */
public class Eth {
    /**
     * 执行post请求
     *
     * @param params
     * @param blockCallback
     */
    public void post(EthParams params, final BlockCallback blockCallback) {
        RequestParams requestParams = new RequestParams(EthConf.url);
        String json = params.toJson();
        requestParams.setAsJsonContent(true);
        requestParams.setBodyContent(json);
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("class Eth onSuccess >>>>>" + result);
                blockCallback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                BlockError error = new BlockError();
                error.error = ex.toString();
                LogUtil.e("class Eth onError >>>>>" + error.error);
                blockCallback.onError(error);
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * get请求  去访问EtherScan的api 获取列表
     *
     * @param params
     * @param callback
     */
    public void get(RequestParams params, final BlockCallback callback) {
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("class Eth onSuccess >>>>>" + result);
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                BlockError error = new BlockError();
                error.error = ex.toString();
                LogUtil.e("class Eth onError >>>>>" + error.error);
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

