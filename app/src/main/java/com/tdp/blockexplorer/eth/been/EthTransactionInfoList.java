package com.tdp.blockexplorer.eth.been;

import com.google.gson.Gson;
import com.tdp.blockexplorer.been.BaseBlock;
import com.tdp.blockexplorer.been.Transaction;
import com.tdp.blockexplorer.been.TransactionList;
import com.tdp.blockexplorer.blockchain.BlockType;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by LPC43 on 2018/4/12.
 */

public class EthTransactionInfoList extends EthBlock<TransactionList>{

    public String status;
    public String message;
    public List<EthTransactionInfo.ResultEntity> result;

    @Override
    public TransactionList parse(String json) {

        EthTransactionInfoList ethTransactionInfoList = new Gson().fromJson(json, EthTransactionInfoList.class);
        //转换
        TransactionList transactionList = new TransactionList();
        transactionList.result = new ArrayList<>();
        for(EthTransactionInfo.ResultEntity info : ethTransactionInfoList.result){

            Transaction transaction = new Transaction();
            transaction.hash = info.hash;
            transaction.blockHash = null==info.blockHash?"":info.blockHash;
            transaction.from = info.from;//示例
            transaction.to = info.to;//示例
            transaction.blockNumber = info.blockNumber;
            transaction.timestamp = info.timeStamp;
            transaction.value = info.value;
            transaction.gasPrice = info.gasPrice;
            transaction.gas = info.gas;
            transactionList.result.add(transaction);
        }

        return transactionList;
    }
}
