package com.tdp.blockexplorer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.tdp.blockexplorer.Conf;
import com.tdp.blockexplorer.R;
import com.tdp.blockexplorer.base.BaseActivity;
import com.tdp.blockexplorer.blockchain.Tdp;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

/**
 * Created by LPC43 on 2018/4/11.
 */
@ContentView(R.layout.activity_explorer)
public class ExplorerActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @ViewInject(R.id.tv_type)
    private TextView tv_type;
    @ViewInject(R.id.btn_search)
    private Button btn_search;
    @ViewInject(R.id.et_hash)
    private EditText et_address;

    private ListPopupWindow mPopup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_type.setText(Tdp.blockType + "");

    }

    private void initView() {
        tv_type.setOnClickListener(this);
        btn_search.setOnClickListener(this);

        mPopup = new ListPopupWindow(this);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.layout_block_popup_item, Tdp.blockTypes);
        mPopup.setAdapter(adapter);
        mPopup.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopup.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopup.setModal(true);
        mPopup.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_type:
                mPopup.setAnchorView(tv_type);
                mPopup.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopup.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopup.setModal(true);
                mPopup.setDropDownGravity(Gravity.START | Gravity.BOTTOM);
                mPopup.show();
                break;
            case R.id.btn_search:

                String transactionHash = et_address.getText().toString();
                if (TextUtils.isEmpty(transactionHash)) {
                    Toast.makeText(this, "请输入交易哈希", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(this, TransactionDetailActivity.class);
                intent.putExtra("transactionHash", transactionHash);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Tdp.blockType = Tdp.blockTypes[position];
        tv_type.setText(Tdp.blockTypes[position] + "");
        test();
        mPopup.dismiss();
    }

    private void test() {
        if (!Conf.isDebug) return;
        switch (Tdp.blockType) {
            case BTC:
                et_address.setText(Conf.test_btc_tx);
                break;
            case ETH:
                et_address.setText(Conf.test_eth_tx);
                break;
            case BCH:
                et_address.setText(Conf.test_bch_tx);
                break;
        }
    }
}
