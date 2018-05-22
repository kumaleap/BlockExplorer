package com.tdp.blockexplorer.blockchain;

import com.tdp.blockexplorer.btc.BtcBlockExplorer;
import com.tdp.blockexplorer.eth.EthBlockExplorer;
import com.tdp.blockexplorer.ltc.LtcBlockExplorer;

/**
 * Created by wulijie on 2018/4/11.
 */

public class Tdp {
    public static BlockType blockType = BlockType.BTC;//当前的搜索类型

    public static BlockType[] blockTypes = {BlockType.BTC, BlockType.BCH, BlockType.ETH};

    private Tdp() {
    }

    public static BlockExplorer get(BlockType type) {
        blockType = type;
        BlockExplorer explorer = null;
        switch (type) {
            case BTC:
                explorer = new BtcBlockExplorer();
                break;
            case BCH:
                explorer = new BtcBlockExplorer();//因为只是改下接口地址 所以复用了btc的逻辑处理
                break;
            case ETH:
                explorer = new EthBlockExplorer();
                break;
            case LTC:
                explorer = new LtcBlockExplorer();
                break;
        }
        return explorer;
    }


}
