package com.tdp.blockexplorer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tdp.blockexplorer.R;
import com.tdp.blockexplorer.adapter.TransactionAdapter;
import com.tdp.blockexplorer.base.BaseActivity;
import com.tdp.blockexplorer.been.Transaction;
import com.tdp.blockexplorer.been.TransactionList;
import com.tdp.blockexplorer.blockchain.BlockCallback;
import com.tdp.blockexplorer.blockchain.BlockError;
import com.tdp.blockexplorer.blockchain.BlockType;
import com.tdp.blockexplorer.blockchain.Tdp;
import com.tdp.blockexplorer.btc.BtcBlockExplorer;
import com.tdp.blockexplorer.btc.been.BchTransactionList;
import com.tdp.blockexplorer.btc.been.BtcTransactionList;
import com.tdp.blockexplorer.eth.been.EthTransactionInfoList;
import com.tdp.blockexplorer.tool.AppLoading;
import com.tdp.blockexplorer.view.LoadMoreRecycleView;

import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LPC43 on 2018/4/12.
 */

@ContentView(R.layout.activity_transaction_list)
public class TransactionListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener,
        AdapterView.OnItemClickListener {

    @ViewInject(R.id.refreshLayout)
    private SwipeRefreshLayout refreshLayout;
    @ViewInject(R.id.recyclerView)
    private LoadMoreRecycleView recyclerView;
    private TransactionAdapter mAdapter;
    private TextView tv_type;

    private String address = "";
    private String balance = "";
    private String transaction_count = "";
    private int pageNum = 0;
    List<Transaction> transactionList = new ArrayList<>();
    private ListPopupWindow mPopup;
    private AppLoading loading;
    private BlockType searchBlockType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initView() {
        loading = new AppLoading(this, false, R.string.loading);
        mPopup = new ListPopupWindow(this);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.layout_block_popup_item, Tdp.blockTypes);
        mPopup.setAdapter(adapter);
        mPopup.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopup.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopup.setModal(true);
        mPopup.setOnItemClickListener(this);

        refreshLayout.setOnRefreshListener(this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        mAdapter = new TransactionAdapter(this, Tdp.blockType);
        tv_type = mAdapter.getTypeTextView();
        mAdapter.setOnClickListener(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setOnLoadMoreListener(mLayoutManager, mAdapter, new LoadMoreRecycleView.OnLoadMoreListener() {
            @Override
            public void loadMore() {
                if (mAdapter.isHasMore()) {
                    loadData();
                }
            }
        });

    }

    private void initData() {

        address = "";
        pageNum = 0;
        searchBlockType = Tdp.blockType;
        if (getIntent() != null && getIntent().hasExtra("address")) {
            address = getIntent().getStringExtra("address");
            initRequest();
        }
    }

    private void reloadData() {
        pageNum = 0;
        Tdp.blockType = searchBlockType;
        initRequest();
    }

    private void initRequest() {
        searchRequest();
        searchBalance();
        searchCount();
        searchReceived();
        searchSent();
    }

    private void loadData() {
        pageNum++;
        searchRequest();
    }

    @Override
    public void onRefresh() {
        pageNum = 0;
        initRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_hash:

                String transactionHash = v.getTag().toString();
                Intent intent = new Intent(this, TransactionDetailActivity.class);
                intent.putExtra("transactionHash", transactionHash);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_search:

                EditText et_hash = (EditText) v.getTag();
                String transactionAddress = et_hash.getText().toString();
                if (TextUtils.isEmpty(transactionAddress)) {
                    Toast.makeText(this, "请输入交易地址", Toast.LENGTH_LONG).show();
                    return;
                }
                address = transactionAddress;
                reloadData();
                break;
            case R.id.tv_type:

                mPopup.setAnchorView(v);
                mPopup.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopup.setModal(true);
                mPopup.setDropDownGravity(Gravity.START | Gravity.BOTTOM);
                mPopup.show();
                break;
        }
    }

    private void searchRequest() {
        if (pageNum == 0)
            loading.show("数据查询中,请稍后…");
        Tdp.get(Tdp.blockType).getTransactionsByAddress(address, pageNum, new BlockCallback() {
            @Override
            public void onSuccess(String result) {
                if (loading != null && loading.isShowing()) loading.dismiss();
                refreshLayout.setRefreshing(false);
                recyclerView.setIsLoadingMore(false);
                TransactionList transactions = null;
                switch (Tdp.blockType) {
                    case ETH:
                        transactions = new EthTransactionInfoList().parse(result);
                        break;
                    case BTC:
                        transactions = new BtcTransactionList().parse(result, address);
                        break;
                    case BCH:
                        transactions = new BchTransactionList().parse(result, address);
                        break;
                }

                if (pageNum == 0)
                    transactionList.clear();
                if (transactions.result != null && transactions.result.size() > 0) {
                    transactionList.addAll(transactions.result);
                    mAdapter.setDataList(Tdp.blockType, transactionList, address, transactions.totalItems + "");
                }
                mAdapter.notifyDataSetChanged();
                if (transactions.result == null || transactions.result.size() < 10)
                    mAdapter.setHasMore(false);
                else
                    mAdapter.setHasMore(true);

            }

            @Override
            public void onError(BlockError error) {
                if (loading != null && loading.isShowing()) loading.dismiss();
                Toast.makeText(TransactionListActivity.this, "查询交易数据失败", Toast.LENGTH_SHORT).show();
                if (pageNum == 0) {
                    transactionList.clear();
                    mAdapter.setHeaderData(Tdp.blockType, "", "", "");
                    mAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void searchBalance() {
        Tdp.get(Tdp.blockType).getBalance(address, new BlockCallback() {
            @Override
            public void onSuccess(String result) {
                try {

                    switch (Tdp.blockType) {
                        case ETH:
                            JSONObject jsonObject = new JSONObject(result);
                            balance = jsonObject.optString("result");
                            break;
                        case BTC:
                        case BCH:
                            balance = result;
                            break;
                    }
                    mAdapter.setHeaderData(Tdp.blockType, address, balance, transaction_count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(BlockError error) {
                balance = "";
                mAdapter.setHeaderData(Tdp.blockType, address, balance, transaction_count);
            }
        });
    }

    private void searchCount() {

        if (Tdp.blockType != BlockType.ETH)
            return;
        Tdp.get(BlockType.ETH).getTransactionCount(address, new BlockCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    transaction_count = jsonObject.optString("result");
                    mAdapter.setHeaderData(Tdp.blockType, address, balance, transaction_count);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(BlockError error) {
                transaction_count = "";
                mAdapter.setHeaderData(Tdp.blockType, address, balance, transaction_count);

            }
        });
    }

    //查询BTC转入总量
    private void searchReceived() {

        if (Tdp.blockType != BlockType.BTC && Tdp.blockType != BlockType.BCH)
            return;
        ((BtcBlockExplorer) Tdp.get(Tdp.blockType)).getTransactionTotalReceived(address, new BlockCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    mAdapter.setBtcIn(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(BlockError error) {
                mAdapter.setBtcIn("");
            }
        });
    }

    //查询BTC转出总量
    private void searchSent() {

        if (Tdp.blockType != BlockType.BTC && Tdp.blockType != BlockType.BCH)
            return;
        ((BtcBlockExplorer) Tdp.get(Tdp.blockType)).getTransactionTotalSent(address, new BlockCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    mAdapter.setBtcOut(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(BlockError error) {
                mAdapter.setBtcOut("");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        searchBlockType = Tdp.blockTypes[position];
        if (tv_type == null)
            tv_type = mAdapter.getTypeTextView();
        tv_type.setText(Tdp.blockTypes[position] + "");
        mPopup.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loading != null && loading.isShowing()) loading.dismiss();
    }
}
