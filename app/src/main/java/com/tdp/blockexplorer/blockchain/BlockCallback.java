package com.tdp.blockexplorer.blockchain;

/**
 * Created by wulijie on 2018/4/11.
 */

public interface BlockCallback {
    void onSuccess(String result);

    void onError(BlockError error);
}
