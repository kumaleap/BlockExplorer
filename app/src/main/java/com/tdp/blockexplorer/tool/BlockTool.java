package com.tdp.blockexplorer.tool;

import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.tdp.blockexplorer.blockchain.Tdp;

import org.xutils.common.util.LogUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wulijie on 2018/4/12.
 */

public class BlockTool {
    /**
     * 时间戳转为正常日期
     * d
     *
     * @return
     */
    public static String timestampToDate(long timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(timestamp * 1000));
    }

    /**
     * 解析Eth wei转换为eth的数量转换
     *
     * @param ethNum
     * @return
     */
    public static String parseEth(String ethNum) {
        ethNum = ethNum.substring(2);
        BigInteger bigInteger = new BigInteger(ethNum, 16);
        BigDecimal bigDecimal = new BigDecimal(bigInteger);
        LogUtil.e("wei=" + bigInteger.toString(10));
        BigDecimal result = bigDecimal.divide(new BigDecimal("1000000000000000000"));
        return result.toString();
    }

    public static String parseEth2(String ethNum) {
        BigDecimal bigDecimal = new BigDecimal(ethNum);
        BigDecimal result = bigDecimal.divide(new BigDecimal("1000000000000000000"));
        return result.toString();
    }

    public static String parseBtc(String btcNum) {
        if(TextUtils.isEmpty(btcNum))
            return "";
        BigDecimal bigDecimal = new BigDecimal(btcNum);
        BigDecimal result = bigDecimal.divide(new BigDecimal("100000000"));
        return result.toString().concat("");
    }

    /**
     * 获取货币单位
     *
     * @return
     */
    public static String getUnit() {
        switch (Tdp.blockType) {
            case BTC:
                return " BTC";
            case ETH:
                return " ETH";
            case BCH:
                return " BCH";
            default:
                return "";
        }
    }



    private void set(){
        CharSequence charSequence;
        String content = "<p>简介：</p><p>1.nickname:wildma！</p><p>2.职业：android攻城狮</p>";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            charSequence = Html.fromHtml(content,Html.FROM_HTML_MODE_LEGACY);
        } else {
            charSequence = Html.fromHtml(content);
        }
    }

    /**
     * 字体颜色突出显示
     * @param before
     *            前缀字符串
     * @param color
     *            要突出字符串颜色值
     * @param text
     *            要突出的字符串
     * @param after
     *            后缀字符串
     * @return 处理后字符串
     */
    public static Spanned fromHtml(String before, String color, String text, String after) {
        String[] strs = null;
        if (after.contains("\n")) {
            strs = null;
            strs = after.split("\n");
        }
        StringBuffer strB = new StringBuffer();
        if (strs != null && strs.length > 0) {
            for (int i = 0; i < strs.length; i++) {
                strB.append(strs[i]);
                if (i != strs.length - 1) {
                    strB.append("<br />");
                }
            }
        } else {
            strB.append(after);
        }
        return Html.fromHtml(before + "<font color='" + color + "'>" + text + "</font>" + strB.toString());
    }

}
