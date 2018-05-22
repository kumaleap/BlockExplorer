package com.tdp.blockexplorer.eth;

import com.google.gson.Gson;

import org.xutils.common.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wulijie on 2018/4/11.
 * Eth参数集
 */
public class EthParams {
    private Map<String, Object> params;

    private List paramsList;//参数集

    public EthParams(String method) {
        params = new HashMap<>();
        paramsList = new ArrayList();
        params.put("jsonrpc", "2.0");
        params.put("method", method);
        params.put("params", paramsList);
    }

    public EthParams setMethodId(int id) {
        params.put("id", id);
        return this;
    }


    public EthParams addParams(Object value) {
        paramsList.add(value);
        return this;
    }


    public String toJson() {
        String json = new Gson().toJson(params);
        LogUtil.e(json);
        return json;
    }

}
