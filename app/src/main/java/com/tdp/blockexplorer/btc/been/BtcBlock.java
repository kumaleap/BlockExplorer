package com.tdp.blockexplorer.btc.been;

/**
 * Created by wulijie on 2018/4/12.
 */

public interface BtcBlock<T> {

    public abstract T parse(String json);

}
