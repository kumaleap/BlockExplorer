package com.tdp.blockexplorer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tdp.blockexplorer.R;
import com.tdp.blockexplorer.base.BaseActivity;
import com.tdp.blockexplorer.been.TdpBlock;
import com.tdp.blockexplorer.been.Transaction;
import com.tdp.blockexplorer.blockchain.BlockCallback;
import com.tdp.blockexplorer.blockchain.BlockError;
import com.tdp.blockexplorer.blockchain.BlockType;
import com.tdp.blockexplorer.blockchain.Tdp;
import com.tdp.blockexplorer.tool.AppLoading;
import com.tdp.blockexplorer.tool.BlockTool;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by LPC43 on 2018/4/12.
 */

@ContentView(R.layout.activity_transaction_detail)
public class TransactionDetailActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.layout_empty)
    private RelativeLayout layout_empty;
    @ViewInject(R.id.tv_type)
    private TextView tv_type;
    @ViewInject(R.id.btn_search)
    private Button btn_search;
    @ViewInject(R.id.tv_hash)
    private TextView tv_hash;
    @ViewInject(R.id.tv_from)
    private TextView tv_from;
    @ViewInject(R.id.tv_to)
    private TextView tv_to;
    @ViewInject(R.id.ethNum)
    private TextView ethNum;
    @ViewInject(R.id.tv_time)
    private TextView tv_time;
    @ViewInject(R.id.tv_number)
    private TextView tv_number;
    @ViewInject(R.id.tv_gasPrice)
    private TextView tv_gasPrice;
    @ViewInject(R.id.tv_count)
    private TextView tv_count;//暂时没有用
    @ViewInject(R.id.et_hash)
    private EditText et_hash;
    @ViewInject(R.id.tvTipTx)
    private TextView tvTipTx;
    @ViewInject(R.id.txCountRl)
    private RelativeLayout txCountRl;
    @ViewInject(R.id.ll_from)
    private LinearLayout ll_from;
    @ViewInject(R.id.ll_to)
    private LinearLayout ll_to;
    @ViewInject(R.id.valueOutRl)
    private View valueOutRl;
    @ViewInject(R.id.ethOutNum)
    private TextView ethOutNum;
    @ViewInject(R.id.tv_tag)
    private TextView tv_tag;

    private String transactionHash = "";
    private Transaction transaction;
    private ListPopupWindow mPopup;
    private Spanned spanned;
    private AppLoading loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {

        loading = new AppLoading(this, false, R.string.loading);
        tv_type.setText(Tdp.blockType + "");
        tv_type.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        tv_tag.setVisibility(View.VISIBLE);
        et_hash.setHint("请输入交易Hash");

        mPopup = new ListPopupWindow(this);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.layout_block_popup_item, Tdp.blockTypes);
        mPopup.setAdapter(adapter);
        mPopup.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopup.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopup.setModal(true);
        mPopup.setOnItemClickListener(this);

        tv_to.setOnClickListener(this);
        tv_from.setOnClickListener(this);
        transactionHash = "";
        if (getIntent() != null && getIntent().hasExtra("transactionHash")) {
            transactionHash = getIntent().getStringExtra("transactionHash");
            searchRequest(transactionHash);
        }
    }

    private void setView(Transaction transaction) {
        tv_hash.setText(transaction.hash);
        tv_tag.setVisibility(View.VISIBLE);
        //依据不同类型进行展示
        switch (Tdp.blockType) {
            case ETH:
                tv_from.setVisibility(View.VISIBLE);
                tv_to.setVisibility(View.VISIBLE);
                ll_from.setVisibility(View.GONE);
                ll_to.setVisibility(View.GONE);
                tv_from.setText(transaction.from);
                tv_to.setText(transaction.to);
                String eth = BlockTool.parseEth(transaction.value);
                spanned = BlockTool.fromHtml(eth, "#999999",BlockTool.getUnit(),"");
                ethNum.setText(spanned);
                tv_number.setText(Long.decode(transaction.blockNumber) + "");
                //烦死了 这大数计算
                BigInteger gas = new BigInteger(transaction.gas.substring(2), 16);
                BigInteger gasPrice = new BigInteger(transaction.gasPrice.substring(2), 16);
                BigInteger result = gas.multiply(gasPrice);
                String price = BlockTool.parseEth("0x" + result.toString(16));
                spanned = BlockTool.fromHtml(price, "#999999",BlockTool.getUnit(),"");
                tv_gasPrice.setText(spanned);
                break;
            case BTC:
                valueOutRl.setVisibility(View.VISIBLE);
                tv_from.setVisibility(View.GONE);
                tv_to.setVisibility(View.GONE);
                ll_from.setVisibility(View.VISIBLE);
                ll_to.setVisibility(View.VISIBLE);
                tvTipTx.setText("输入数量");
                txCountRl.setVisibility(View.VISIBLE);
                tv_number.setText(transaction.blockNumber);
                String value = transaction.valueIn;
                String vOut = transaction.valueOut;
                if (TextUtils.isEmpty(value)) value = "0.00000000";
                if (TextUtils.isEmpty(vOut)) vOut = "0.00000000";
                spanned = BlockTool.fromHtml(value, "#999999",BlockTool.getUnit(),"");
                ethNum.setText(spanned);
                spanned = BlockTool.fromHtml(vOut, "#999999",BlockTool.getUnit(),"");
                ethOutNum.setText(spanned);

                String fees = transaction.fees;
                if (TextUtils.isEmpty(fees)) fees = "0.00000000";
                spanned = BlockTool.fromHtml(fees, "#999999",BlockTool.getUnit(),"");
                tv_gasPrice.setText(spanned);
                int size = Integer.parseInt(transaction.size);
                spanned = BlockTool.fromHtml((size / 1000d)+"", "#999999"," KB","");
                tv_count.setText(spanned);//交易大小
                long timestamp = Long.decode(transaction.timestamp);
                String time = BlockTool.timestampToDate(timestamp);
                tv_time.setText(time);
                // addView
                addView(ll_from, transaction.fromList);
                addView(ll_to, transaction.toList);
                break;
            case BCH:
                valueOutRl.setVisibility(View.VISIBLE);
                tv_from.setVisibility(View.GONE);
                tv_to.setVisibility(View.GONE);
                ll_from.setVisibility(View.VISIBLE);
                ll_to.setVisibility(View.VISIBLE);
                tvTipTx.setText("输入数量");
                txCountRl.setVisibility(View.VISIBLE);
                tv_number.setText(transaction.blockNumber);
                String bchVin = transaction.valueIn;
                String bchVout = transaction.valueOut;
                if (TextUtils.isEmpty(bchVin)) bchVin = "0.00000000";
                if (TextUtils.isEmpty(bchVout)) bchVout = "0.00000000";
                spanned = BlockTool.fromHtml(bchVin, "#999999",BlockTool.getUnit(),"");
                ethNum.setText(spanned);
                spanned = BlockTool.fromHtml(bchVout, "#999999",BlockTool.getUnit(),"");
                ethOutNum.setText(spanned);

                String bchFees = transaction.fees;
                if (TextUtils.isEmpty(bchFees) && transaction.isCoinBase) bchFees = "0.00000000";
                spanned = BlockTool.fromHtml(bchFees, "#999999",BlockTool.getUnit(),"");
                tv_gasPrice.setText(spanned);
                spanned = BlockTool.fromHtml((Integer.parseInt(transaction.size) / 1000d)+"", "#999999"," KB","");
                tv_count.setText(spanned);//交易大小
                tv_time.setText(BlockTool.timestampToDate(Long.decode(transaction.timestamp)));
                // addView
                addView(ll_from, transaction.fromList);
                addView(ll_to, transaction.toList);
                break;
        }

    }


    private void addView(LinearLayout parent, List<String> list) {
        if (list == null || list.size() == 0) return;
        for (String str : list) {
            if (TextUtils.isEmpty(str)) {
                View coinBase = View.inflate(this, R.layout.tx_coinbase, null);
                parent.addView(coinBase);
                return;
            }
            String[] addressValue = str.split(",");
            if (addressValue != null && addressValue.length != 0) {
                final String address = addressValue[0];
                View item = View.inflate(this, R.layout.tx_item, null);
                TextView tv_address = (TextView) item.findViewById(R.id.tv_address);
                TextView tv_value = (TextView) item.findViewById(R.id.tv_value);
                tv_address.setText(address);
                tv_value.setText(addressValue[1] + BlockTool.getUnit());
                parent.addView(item);
                tv_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TransactionDetailActivity.this, TransactionListActivity.class);
                        intent.putExtra("address", address);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }
    }


    private void searchRequest(String transactionHash) {
        loading.show("数据查询中,请稍后…");
        Tdp.get(Tdp.blockType).getTransactionByHash(transactionHash, new BlockCallback() {
            @Override
            public void onSuccess(String result) {
                if (loading != null && loading.isShowing()) loading.dismiss();
                layout_empty.setVisibility(View.GONE);
                transaction = new Transaction().parse(Tdp.blockType, 0, result);
                setView(transaction);
                getBlock(transaction);

            }

            @Override
            public void onError(BlockError error) {
                if (loading != null && loading.isShowing()) loading.dismiss();
                layout_empty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getBlock(final Transaction transaction) {
        if (Tdp.blockType != BlockType.ETH) return;//只有以太坊需要获取block块  目前
        Tdp.get(Tdp.blockType).getBlockByHash(transaction.blockHash, new BlockCallback() {
            @Override
            public void onSuccess(String result) {
                TdpBlock block = new TdpBlock().parse(Tdp.blockType, result);
                transaction.timestamp = block.timestamp;
                long timestamp = Long.decode(transaction.timestamp);
                String time = BlockTool.timestampToDate(timestamp);
                tv_time.setText(time);
                BigInteger difficulty = new BigInteger(block.difficulty.substring(2), 16);
                LogUtil.e("d =" + difficulty.toString(10));
                BigInteger totalDifficulty = new BigInteger(block.totalDifficulty.substring(2), 16);
                LogUtil.e("d =" + totalDifficulty.toString(10));
                LogUtil.e("dive" + totalDifficulty.divide(difficulty).toString(10));
            }

            @Override
            public void onError(BlockError error) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_from:

                Intent intent = new Intent(this, TransactionListActivity.class);
                intent.putExtra("address", tv_from.getText().toString());
                startActivity(intent);
                finish();
                break;
            case R.id.tv_to:
                Intent intent1 = new Intent(this, TransactionListActivity.class);
                intent1.putExtra("address", tv_to.getText().toString());
                startActivity(intent1);
                finish();
                break;
            case R.id.btn_search:

                String hash = et_hash.getText().toString();
                if (TextUtils.isEmpty(hash)) {
                    Toast.makeText(this, "请输入交易Hash值", Toast.LENGTH_LONG).show();
                    return;
                }
                transactionHash = hash;
                searchRequest(transactionHash);
                break;
            case R.id.tv_type:

                mPopup.setAnchorView(tv_type);
                mPopup.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopup.setModal(true);
                mPopup.setDropDownGravity(Gravity.START | Gravity.BOTTOM);
                mPopup.show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Tdp.blockType = Tdp.blockTypes[position];
        tv_type.setText(Tdp.blockTypes[position] + "");
        mPopup.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loading != null && loading.isShowing()) loading.dismiss();
    }
}
