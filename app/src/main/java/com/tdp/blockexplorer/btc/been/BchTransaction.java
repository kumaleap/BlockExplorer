package com.tdp.blockexplorer.btc.been;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.tdp.blockexplorer.been.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LPC43 on 2018/4/16.
 */

public class BchTransaction implements BtcBlock {

    public String txid;
    public int version;
    public String locktime;
    public boolean isCoinBase;
    public String blockhash;
    public String blockheight;
    public String blocktime;
    public String confirmations;
    public String time;
    public String valueOut;
    public String valueIn;
    public String size;
    public List<Vin> vin;
    public List<Vout> vout;
    public String fees;


    public static class Vin {
        public String txid;
        public String vout;
        public String sequence;
        public String n;
        public String addr;
        public String valueSat;
        public String value;
        public String doubleSpentTxID;
    }

    public static class Vout {
        public String value;
        public String n;
        public ScriptPubKey scriptPubKey;
    }

    public static class ScriptPubKey {
        public String hex;
        public List<String> addresses;
        public String type;
    }

    @Override
    public Transaction parse(String json) {

        BchTransaction btcTransaction = new Gson().fromJson(json, BchTransaction.class);
        //转换
        Transaction transaction = new Transaction();
        transaction.blockHash = btcTransaction.blockhash;
        transaction.blockNumber = btcTransaction.blockheight;
        transaction.hash = btcTransaction.txid;
        transaction.timestamp = btcTransaction.blocktime;
        transaction.size = btcTransaction.size;
        transaction.isCoinBase = btcTransaction.isCoinBase;
        transaction.value = btcTransaction.valueIn;
        transaction.valueIn = btcTransaction.valueIn;
        transaction.valueOut = btcTransaction.valueOut;
        transaction.fees = btcTransaction.fees;

        if (btcTransaction.vin != null && btcTransaction.vin.size() != 0) {
            List<String> fromList = new ArrayList<>();
            for (Vin in : btcTransaction.vin) {
                if (TextUtils.isEmpty(in.addr) || TextUtils.isEmpty(in.value)) {
                    fromList.add("");
                } else {
                    fromList.add(in.addr + "," + in.value);
                }
            }
            transaction.fromList = fromList;
        }

        if (btcTransaction.vout != null && btcTransaction.vout.size() != 0) {
            List<String> toList = new ArrayList<>();
            for (Vout out : btcTransaction.vout) {
                ScriptPubKey sp = out.scriptPubKey;
                String outString = "";
                if (sp != null && sp.addresses != null && sp.addresses.size() != 0)
                    outString = sp.addresses.get(0) + "," + out.value;
                toList.add(outString);
            }
            transaction.toList = toList;
        }
        return transaction;

    }
}
