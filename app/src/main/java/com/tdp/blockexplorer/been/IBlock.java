package com.tdp.blockexplorer.been;

import com.tdp.blockexplorer.blockchain.BlockType;

/**
 * Created by wulijie on 2018/4/12.
 * 定义实体类的转换行为
 */
public interface IBlock<T> {
    /**
     * 解析Json 根据货币类型
     *
     * @param json
     */
    T parse(BlockType type, int extra, String json);

    /**
     * 解析Json 根据货币类型
     *
     * @param json
     */
    T parse(BlockType type, String json);


}
