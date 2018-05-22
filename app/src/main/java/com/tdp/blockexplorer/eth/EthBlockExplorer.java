package com.tdp.blockexplorer.eth;

import com.tdp.blockexplorer.blockchain.BlockCallback;
import com.tdp.blockexplorer.blockchain.BlockExplorer;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

/**
 * Created by wulijie on 2018/4/11.
 * 以太坊区块浏览方式
 * sort的排序规则
 * HEX String - an integer block number
 * String "earliest" for the earliest/genesis block
 * String "latest" - for the latest mined block
 * String "pending" - for the pending state/transactions
 */
public class EthBlockExplorer extends Eth implements BlockExplorer {
    @Override
    public void getTransactionByHash(String hash, BlockCallback callback) {
        //获取交易根据交易哈希值 示例
        EthParams params =
                new EthParams("eth_getTransactionByHash")
                        .setMethodId(1)
                        .addParams(hash);
        //执行请求
        post(params, callback);
    }


    @Override
    public void getBalance(String address, BlockCallback callback) {
        String sort = "latest";//默认指定吧
        EthParams params = new EthParams("eth_getBalance")
                .setMethodId(1)
                .addParams(address)
                .addParams(sort);//integer block number, or the string "latest", "earliest" or "pending"
        post(params, callback);
    }

    @Override
    public void getTransactionCount(String address, BlockCallback callback) {
        String sort = "latest";//默认指定吧
        EthParams params = new EthParams("eth_getTransactionCount")
                .setMethodId(1)
                .addParams(address)
                .addParams(sort);
        post(params, callback);
    }

    @Override
    public void getBlockByHash(String hash, BlockCallback callback) {
        EthParams params = new EthParams("eth_getBlockByHash")
                .setMethodId(1)
                .addParams(hash)
                .addParams(true);//可选 默认值为false。true会将区块包含的所有交易作为对象返回。否则只返回交易的哈希。
        post(params, callback);
    }


    @Override
    public void getTransactionsByAddress(String address, int page, BlockCallback callback) {
        String url = "http://api.etherscan.io/api";// 分页加载
        RequestParams params = new RequestParams(url);
        params.addQueryStringParameter("module", "account");
        params.addQueryStringParameter("action", "txlist");
        params.addQueryStringParameter("address", address);
        params.addQueryStringParameter("startblock", "0");
        params.addQueryStringParameter("endblock", "99999999");
        params.addQueryStringParameter("page", String.valueOf(page));
        params.addQueryStringParameter("offset", "10");//每页十条信息
        params.addQueryStringParameter("sort", "asc");
        params.addQueryStringParameter("apikey", "YourApiKeyToken");
        LogUtil.e("ETH Url =" + params.getUri());
        get(params, callback);
    }

//    @Override
//    public void getTransactionInfoByTxHash(String txHash, BlockCallback callback) {
//        //（EtherScan上提供的方法）建议配合Ether官方api 使用
//        String url = "http://api.etherscan.io/api";
//        RequestParams params = new RequestParams(url);
//        params.addQueryStringParameter("module", "account");
//        params.addQueryStringParameter("action", "txlistinternal");
//        params.addQueryStringParameter("txhash", txHash);
//        params.addQueryStringParameter("apikey", "YourApiKeyToken");
//        get(params, callback);
//        //http://www.qukuai.com/search/zh-CN/ETH/0x2cb58f20647c1944251b5213c193680537ec1821c572c34f26f93e10357f3ddf/1
//    }


}
