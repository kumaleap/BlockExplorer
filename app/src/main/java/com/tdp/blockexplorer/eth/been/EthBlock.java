package com.tdp.blockexplorer.eth.been;

/**
 * Created by wulijie on 2018/4/12.
 */

public abstract class EthBlock<T> {
    public String jsonrpc;
    public int id;

    public abstract T parse(String json);

}
