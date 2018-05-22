package com.tdp.blockexplorer.eth.been;

import com.google.gson.Gson;
import com.tdp.blockexplorer.been.Transaction;


/**
 * Created by LPC43 on 2018/4/12.
 */
//EthScan API
public class EthTransactionInfo extends  EthBlock<Transaction>{

    // 解析  转换
    public ResultEntity result;

    @Override
    public Transaction parse(String json) {
        //Eth的type
        EthTransactionInfo ethTransaction = new Gson().fromJson(json, EthTransactionInfo.class);
        //转换
        Transaction transaction = new Transaction();
        if(ethTransaction.result ==null)
            return transaction;
        transaction.blockHash = null==ethTransaction.result.blockHash?"":ethTransaction.result.blockHash;//示例
        transaction.from = ethTransaction.result.from;//示例
        transaction.to = ethTransaction.result.to;//示例
        return transaction;
    }

    public class ResultEntity {

        public String blockNumber;
        public String timeStamp;
        public String hash;
        public String nonce;
        public String blockHash;
        public String transactionIndex;
        public String from;
        public String to;
        public String value;
        public String gas;
        public String gasPrice;
        public String isError;
        public String txreceipt_status;
        public String input;
        public String contractAddress;
        public String cumulativeGasUsed;
        public String gasUsed;
        public String confirmations;
    }

}
