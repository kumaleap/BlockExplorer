package com.tdp.blockexplorer.btc.been;

import com.google.gson.Gson;
import com.tdp.blockexplorer.been.Transaction;
import com.tdp.blockexplorer.been.TransactionList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LPC43 on 2018/4/16.
 */

public class BchTransactionList implements BtcBlock<TransactionList>{

    public int totalItems;
    public String from;
    public String to;
    public List<BchTransaction> items;

    @Override
    public TransactionList parse(String json) {

        BchTransactionList btcTransactionList = new Gson().fromJson(json, BchTransactionList.class);
        //转换
        TransactionList transactionList = new TransactionList();
        transactionList.result = new ArrayList<>();
        for(BchTransaction info : btcTransactionList.items){

            Transaction transaction = new Transaction();
            transaction.hash = info.txid;
            transaction.blockHash = info.blockhash;
            transaction.blockNumber = info.blockheight;
            transaction.timestamp = info.time;
            transaction.value = info.valueOut;
            transaction.fees = info.size;
//            transaction.btcValue = info.value
            transactionList.result.add(transaction);
        }
        transactionList.totalItems = btcTransactionList.totalItems;

        return transactionList;
    }

    public TransactionList parse(String json,String address) {

        BchTransactionList btcTransactionList = new Gson().fromJson(json, BchTransactionList.class);
        //转换
        TransactionList transactionList = new TransactionList();
        transactionList.result = new ArrayList<>();
        for(BchTransaction info : btcTransactionList.items){

            Transaction transaction = new Transaction();
            transaction.hash = info.txid;
            transaction.blockHash = info.blockhash;
            transaction.blockNumber = info.blockheight;
            transaction.timestamp = info.time;
            transaction.value = info.valueOut;
            transaction.fees = "0.00000";

            transaction.btcValue = searchBchValue(info,address);
            transactionList.result.add(transaction);
        }
        transactionList.totalItems = btcTransactionList.totalItems;

        return transactionList;
    }

    public String searchBchValue(BchTransaction transaction,String address){

        String btcValue = "";

        for(BchTransaction.Vout vout: transaction.vout){
            for(String addresssOut: vout.scriptPubKey.addresses){
                if(address.equals(addresssOut)){
                    btcValue = vout.value;
                    return "+ " + btcValue;
                }
            }
        }
        return btcValue;
    }

}
