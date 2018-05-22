package com.tdp.blockexplorer.blockchain;

/**
 * Created by wulijie on 2018/4/11.
 * 区块浏览接口
 */
public interface BlockExplorer {
    /**
     * 根据交易哈希值获取交易数据
     *
     * @param hash
     * @param callback
     */
    void getTransactionByHash(String hash, BlockCallback callback);

    /**
     * 指定地址余额
     *
     * @param address
     * @param callback
     */
    void getBalance(String address, BlockCallback callback);

    /**
     * 指定地址的交易数量
     *
     * @param address
     * @param callback
     */
    void getTransactionCount(String address, BlockCallback callback);

    /**
     * 根据block 哈希值 获取 block对象 对象内包含所有交易信息哦
     *
     * @param hash
     * @param callback
     */
    void getBlockByHash(String hash, BlockCallback callback);

    /**
     * 根据账户地址 获取该地址下的所有交易记录
     *
     * @param address
     * @param callback
     * @param page
     */
    void getTransactionsByAddress(String address, int page, BlockCallback callback);

}
