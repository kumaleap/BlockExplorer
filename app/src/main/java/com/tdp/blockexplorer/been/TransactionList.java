package com.tdp.blockexplorer.been;

import com.tdp.blockexplorer.blockchain.BlockType;
import com.tdp.blockexplorer.btc.been.BchTransactionList;
import com.tdp.blockexplorer.btc.been.BtcTransactionList;
import com.tdp.blockexplorer.eth.been.EthTransaction;
import com.tdp.blockexplorer.eth.been.EthTransactionInfo;
import com.tdp.blockexplorer.eth.been.EthTransactionInfoList;

import java.util.List;


/**
 * Created by LPC43 on 2018/4/12.
 */

public class TransactionList  extends BaseBlock{

    public List<Transaction> result;

    public int totalItems;

    @Override
    public Object parse(BlockType type, int extra, String json) {
        return null;
    }

    @Override
    public Object parse(BlockType type, String json) {
        TransactionList transactionList = null;
        switch (type) {
            case ETH:
                transactionList = new EthTransactionInfoList().parse(json);
                break;
            case BTC:
                transactionList = new BtcTransactionList().parse(json);
                break;
            case BCH:
                transactionList = new BchTransactionList().parse(json);
                break;
        }
        return transactionList;
    }
}
