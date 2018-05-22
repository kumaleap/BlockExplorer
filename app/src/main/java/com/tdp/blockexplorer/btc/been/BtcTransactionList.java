package com.tdp.blockexplorer.btc.been;

import com.google.gson.Gson;
import com.tdp.blockexplorer.been.Transaction;
import com.tdp.blockexplorer.been.TransactionList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LPC43 on 2018/4/16.
 */

public class BtcTransactionList implements BtcBlock<TransactionList>{

    public int totalItems;
    public String from;
    public String to;
    public List<BtcTransaction> items;

    @Override
    public TransactionList parse(String json) {

        BtcTransactionList btcTransactionList = new Gson().fromJson(json, BtcTransactionList.class);
        //转换
        TransactionList transactionList = new TransactionList();
        transactionList.result = new ArrayList<>();
        for(BtcTransaction info : btcTransactionList.items){

            Transaction transaction = new Transaction();
            transaction.hash = info.txid;
            transaction.blockHash = info.blockhash;
//            transaction.from = info.from;//示例
//            transaction.to = info.to;//示例
            transaction.blockNumber = info.blockheight;
            transaction.timestamp = info.time;
            //TODO
            transaction.value = info.valueIn;
            transaction.fees = info.fees;
//            transaction.btcValue = info.value
            transactionList.result.add(transaction);
        }
        transactionList.totalItems = btcTransactionList.totalItems;

        return transactionList;
    }

    public TransactionList parse(String json,String address) {

        BtcTransactionList btcTransactionList = new Gson().fromJson(json, BtcTransactionList.class);
        //转换
        TransactionList transactionList = new TransactionList();
        transactionList.result = new ArrayList<>();
        for(BtcTransaction info : btcTransactionList.items){

            Transaction transaction = new Transaction();
            transaction.hash = info.txid;
            transaction.blockHash = info.blockhash;
//            transaction.from = info.from;//示例
//            transaction.to = info.to;//示例
            transaction.blockNumber = info.blockheight;
            transaction.timestamp = info.time;
            //TODO
            transaction.value = info.valueIn;
            transaction.fees = info.fees;

            transaction.btcValue = searchBtcValue(info,address);
            transactionList.result.add(transaction);
        }
        transactionList.totalItems = btcTransactionList.totalItems;

        return transactionList;
    }

    public String searchBtcValue(BtcTransaction transaction,String address){

        String btcValue = "";
        for(BtcTransaction.Vin vin: transaction.vin){
            if(address.equals(vin.addr)){

                btcValue = vin.value;
                return "- " + btcValue;
            }
        }
        for(BtcTransaction.Vout vout: transaction.vout){
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
