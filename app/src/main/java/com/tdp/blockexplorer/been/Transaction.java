package com.tdp.blockexplorer.been;

import com.tdp.blockexplorer.blockchain.BlockType;
import com.tdp.blockexplorer.btc.been.BchTransaction;
import com.tdp.blockexplorer.btc.been.BtcTransaction;
import com.tdp.blockexplorer.eth.been.EthTransaction;
import com.tdp.blockexplorer.eth.been.EthTransactionInfo;

import java.util.List;


/**
 * Created by LPC43 on 2018/4/12.
 */

public class Transaction extends BaseBlock {

    public String blockHash;//区块哈希值
    public String blockNumber;//区块高度
    public String from;//交易发送方
    public String to;//交易接收方
    public String gas;//交易发起者提供的gas
    public String gasPrice;//交易发起者配置的gas价格，单位是wei
    public String hash;//交易hash
    public String input;//交易附带的数据
    public String nonce;//交易的发起者在之前进行过的交易数量
    public String transactionIndex;//整数。交易在区块中的序号
    public String value;//交易附带的货币量，单位为Wei。
    public String timestamp;


    //BTC
    public String fees;//BTC 矿工费
    public List<String> fromList;
    public List<String> toList;
    public String size;
    public String btcValue;
    public boolean isCoinBase;//是否是挖币所得
    public String valueIn;
    public String valueOut;

    @Override
    public Transaction parse(BlockType type, int extra, String json) {
        Transaction transaction = null;
        switch (type) {
            case ETH:
                if (extra == 0)
                    transaction = new EthTransaction().parse(json);
                else
                    transaction = new EthTransactionInfo().parse(json);
                break;
            case BTC:
                transaction = new BtcTransaction().parse(json);
                break;
            case BCH:
                transaction = new BchTransaction().parse(json);
                break;
        }
        return transaction;
    }

    @Override
    public Transaction parse(BlockType type, String json) {
        return parse(type, 0, json);
    }


}
