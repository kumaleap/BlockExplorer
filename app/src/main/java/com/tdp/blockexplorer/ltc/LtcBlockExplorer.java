package com.tdp.blockexplorer.ltc;

import com.tdp.blockexplorer.blockchain.BlockCallback;
import com.tdp.blockexplorer.blockchain.BlockExplorer;

import org.xutils.http.RequestParams;

/**
 * Created by wulijie on 2018/4/13.
 * 莱特币
 */
public class LtcBlockExplorer extends Ltc implements BlockExplorer {
    @Override
    public void getTransactionByHash(String hash, BlockCallback callback) {
        String url = LtcConf.url.replace("{address}", hash);
        RequestParams params = new RequestParams(url);
        get(params, callback);
    }

    @Override
    public void getBalance(String address, BlockCallback callback) {

    }

    @Override
    public void getTransactionCount(String address, BlockCallback callback) {

    }

    @Override
    public void getBlockByHash(String hash, BlockCallback callback) {

    }

    @Override
    public void getTransactionsByAddress(String address, int page, BlockCallback callback) {

    }
}
