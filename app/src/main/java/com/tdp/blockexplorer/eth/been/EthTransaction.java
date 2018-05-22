package com.tdp.blockexplorer.eth.been;

import com.google.gson.Gson;
import com.tdp.blockexplorer.been.Transaction;


/**
 * Created by LPC43 on 2018/4/12.
 */
public class EthTransaction extends EthBlock<Transaction> {
    // 解析  转换
    public ResultEntity result;

    @Override
    public Transaction parse(String json) {
        //Eth的type
        EthTransaction ethTransaction = new Gson().fromJson(json, EthTransaction.class);
        //转换
        Transaction transaction = new Transaction();
        transaction.blockHash = ethTransaction.result.blockHash;
        transaction.from = ethTransaction.result.from;
        transaction.to = ethTransaction.result.to;
        transaction.blockNumber = ethTransaction.result.blockNumber;
        transaction.gas = ethTransaction.result.gas;
        transaction.gasPrice = ethTransaction.result.gasPrice;
        transaction.hash = ethTransaction.result.hash;
        transaction.input = ethTransaction.result.input;
        transaction.nonce = ethTransaction.result.nonce;
        transaction.value = ethTransaction.result.value;
        return transaction;
    }


    public class ResultEntity {
        public String blockHash;
        public String blockNumber;
        public String from;
        public String gas;
        public String gasPrice;
        public String hash;
        public String input;
        public String nonce;
        public String to;
        public String transactionIndex;
        public String value;
        public String v;
        public String r;
        public String s;
    }
}
