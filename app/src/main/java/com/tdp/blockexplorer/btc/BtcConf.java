package com.tdp.blockexplorer.btc;

/**
 * Created by wulijie on 2018/4/16.
 */
public interface BtcConf {

    //基于BlockHash 获取block
    String GET_BLOCK = "/block/{hash}";
    //基于交易Hash 获取交易信息
    String GET_TX = "/tx/{hash}";
    //获取交易列表
    String GET_TXS = "/addrs/{address}/txs";//?addr
    //获取地址余额
    String GET_BALANCE = "/addr/{address}/balance";
    //获取地址下的接收总量
    String GET_TOTALRECEIVED = "/addr/{address}/totalReceived";
    //获取地址下的发送总量
    String GET_TOTALSENT = "/addr/{address}/totalSent";


}
