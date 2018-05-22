package com.tdp.blockexplorer.btc;

import com.tdp.blockexplorer.blockchain.BlockCallback;
import com.tdp.blockexplorer.blockchain.BlockExplorer;
import com.tdp.blockexplorer.blockchain.BlockType;
import com.tdp.blockexplorer.blockchain.Tdp;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

/**
 * Created by wulijie on 2018/4/16.
 * 获取比特币区块链信息的实际操作类
 */
public class BtcBlockExplorer extends Btc implements BlockExplorer {
    //基于Insight开源框架 接入比特币
    String INSIGHT_URL = "https://insight.bitpay.com/api";
    String BCH_INSIGHT_URL = "https://bch-insight.bitpay.com/api";
    String URL = Tdp.blockType == BlockType.BTC ? INSIGHT_URL : BCH_INSIGHT_URL;
    @Override
    public void getTransactionByHash(String hash, BlockCallback callback) {
        RequestParams params = new RequestParams(URL+BtcConf.GET_TX.replace("{hash}", hash));
        LogUtil.e("BTC Url =" + params.getUri());
        get(params, callback);
    }

    @Override
    public void getBalance(String address, BlockCallback callback) {
        RequestParams params = new RequestParams(URL+BtcConf.GET_BALANCE.replace("{address}", address));
        LogUtil.e("BTC Url =" + params.getUri());
        get(params, callback);
    }

    @Override
    public void getTransactionCount(String address, BlockCallback callback) {
        //可以直接用 getTransactionsByAddress 接口返回参数中有交易总数
        RequestParams params = new RequestParams(URL+BtcConf.GET_TXS.replace("{address}", address));
        params.addQueryStringParameter("from", "0");//默认每次取10条
        params.addQueryStringParameter("to", "1");
        params.addQueryStringParameter("noAsm", "1");
        params.addQueryStringParameter("noScriptSig", "1");
        params.addQueryStringParameter("noSpent", "1");
        get(params, callback);
    }

    @Override
    public void getBlockByHash(String hash, BlockCallback callback) {
        RequestParams params = new RequestParams(URL+BtcConf.GET_BLOCK.replace("{hash}", hash));
        LogUtil.e("BTC Url =" + params.getUri());
        get(params, callback);
    }

    @Override
    public void getTransactionsByAddress(String address, int page, BlockCallback callback) {
        RequestParams params = new RequestParams(URL+BtcConf.GET_TXS.replace("{address}", address));
        int from = page * 10;
        int to = from + 10;//TODO:test
        params.addQueryStringParameter("from", String.valueOf(from));//默认每次取10条
        params.addQueryStringParameter("to", String.valueOf(to));
        params.addQueryStringParameter("noAsm", "1");
        params.addQueryStringParameter("noScriptSig", "1");
        params.addQueryStringParameter("noSpent", "1");
        LogUtil.e("BTC Url =" + params.getUri());
        get(params, callback);
    }

    /**
     * 获取某个地址下接收总量
     *
     * @param address
     * @param callback
     */
    public void getTransactionTotalReceived(String address, BlockCallback callback) {
        RequestParams params = new RequestParams(URL+BtcConf.GET_TOTALRECEIVED.replace("{address}", address));
        LogUtil.e("BTC Url =" + params.getUri());
        get(params, callback);
    }

    /**
     * 获取某个地址下的发送总量
     *
     * @param address
     * @param callback
     */
    public void getTransactionTotalSent(String address, BlockCallback callback) {
        RequestParams params = new RequestParams(URL+BtcConf.GET_TOTALSENT.replace("{address}", address));
        LogUtil.e("BTC Url =" + params.getUri());
        get(params, callback);
    }

}
