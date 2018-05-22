package com.tdp.blockexplorer.been;

import com.tdp.blockexplorer.blockchain.BlockType;

import org.json.JSONObject;

/**
 * Created by wulijie on 2018/4/12.
 */

public class TdpBlock extends BaseBlock {


    public String timestamp;
    public String difficulty;
    public String totalDifficulty;


    @Override
    public TdpBlock parse(BlockType type, int extra, String json) {
        switch (type) {
            case BTC:
                break;
            case ETH:
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject result = jsonObject.getJSONObject("result");
                    this.timestamp = result.optString("timestamp");
                    this.difficulty = result.optString("difficulty");
                    this.totalDifficulty = result.optString("totalDifficulty");
                } catch (Exception e) {
                }
                break;
        }
        return this;
    }

    @Override
    public TdpBlock parse(BlockType type, String json) {
        return parse(type, 0, json);
    }
}
