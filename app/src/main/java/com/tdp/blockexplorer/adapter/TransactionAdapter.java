package com.tdp.blockexplorer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tdp.blockexplorer.R;
import com.tdp.blockexplorer.been.Transaction;
import com.tdp.blockexplorer.blockchain.BlockType;
import com.tdp.blockexplorer.tool.BlockTool;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LPC43 on 2018/4/12.
 */

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private final static int HEADER_TYPE = 0;//头布局
    private final static int DATA_TYPE = 1;//列表数据
    private final static int BOTTOM_TYPE = 2;//脚布局

    private String address,balance,count;
    private BlockType blockType;

    private String btcIn = "",btcOut = "";

    private boolean hasMore; //有没有更多

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
        if (!hasMore) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    public void setHeaderData(BlockType blockType,String address,String balance,String count){

        this.blockType = blockType;
        this.address = address;
        this.balance = balance;
        this.count = count;
        setHeaderView();
    }

    public void setBtcIn(String in){
        this.btcIn = in;
        headerViewHolder.layout_in.setVisibility(View.VISIBLE);
        Spanned spanned = BlockTool.fromHtml(BlockTool.parseBtc(btcIn), "#999999",BlockTool.getUnit(),"");
        headerViewHolder.tv_in.setText(spanned);
    }

    public void setBtcOut(String out){
        this.btcOut = out;
        headerViewHolder.layout_out.setVisibility(View.VISIBLE);
        Spanned spanned = BlockTool.fromHtml(BlockTool.parseBtc(btcOut), "#999999",BlockTool.getUnit(),"");
        headerViewHolder.tv_out.setText(spanned);
    }

    private List<Transaction> dataList = new ArrayList<>();

    public List<Transaction> getDataList() {
        return dataList;
    }

    public void setDataList(BlockType blockType,List<Transaction> dataList,String address,String count) {
        this.blockType = blockType;
        this.dataList = dataList;
        this.address = address;
        if(!TextUtils.isEmpty(count))
        this.count = count;
    }


    public TransactionAdapter(Context context,BlockType blockType) {
        this.mContext = context;
        this.blockType = blockType;
    }

    private View.OnClickListener mOnClickListener;


    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return dataList == null || dataList.isEmpty() ? 1: dataList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            return HEADER_TYPE;
        if(position == dataList.size() + 1)
            return BOTTOM_TYPE;
        return DATA_TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holer = null;
        switch (viewType) {

            case HEADER_TYPE://头步局
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_transaction_list_header, parent, false);
                holer = new HeaderViewHolder(view);
                break;
            case BOTTOM_TYPE://脚步局
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bottom_listitem, parent, false);
                holer = new FootViewHolder(view);
                break;
            case DATA_TYPE://列表数据
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_transaction_list_item, parent, false);
                holer = new DataViewHolder(view);
                break;
        }
        return holer;
    }

    HeaderViewHolder headerViewHolder;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {

            case DATA_TYPE:

                DataViewHolder dataViewHolder = (DataViewHolder) holder;
                setListView(dataViewHolder,dataList.get(position-1));

                break;
            case HEADER_TYPE:
                headerViewHolder = (HeaderViewHolder) holder;
                setHeaderView();
                break;

            case BOTTOM_TYPE:
                FootViewHolder footViewHolder = (FootViewHolder) holder;
                if (hasMore) {
                    footViewHolder.more_foot_view.setVisibility(View.VISIBLE);
                    footViewHolder.nothing_foot_view.setVisibility(View.GONE);
                } else {
                    footViewHolder.more_foot_view.setVisibility(View.GONE);
                    footViewHolder.nothing_foot_view.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void setHeaderView(){

        headerViewHolder.tv_type.setText(blockType+"");
        if(!TextUtils.isEmpty(address))
            headerViewHolder.tv_address.setText(address);
        Spanned spanned;
        switch (blockType) {
            case BTC:
            case BCH:
                headerViewHolder.layout_in.setVisibility(View.VISIBLE);
                headerViewHolder.layout_out.setVisibility(View.VISIBLE);
                spanned = BlockTool.fromHtml(BlockTool.parseBtc(btcOut), "#999999",BlockTool.getUnit(),"");
                headerViewHolder.tv_out.setText(spanned);
                spanned = BlockTool.fromHtml(BlockTool.parseBtc(btcIn), "#999999",BlockTool.getUnit(),"");
                headerViewHolder.tv_in.setText(spanned);
                if(!TextUtils.isEmpty(balance)){
                    spanned = BlockTool.fromHtml(BlockTool.parseBtc(balance), "#999999",BlockTool.getUnit(),"");
                    headerViewHolder.tv_balance.setText(spanned);
                }else {
                    headerViewHolder.tv_balance.setText("");
                }
                break;
            case ETH:

                headerViewHolder.layout_out.setVisibility(View.GONE);
                headerViewHolder.layout_in.setVisibility(View.GONE);
                if(!TextUtils.isEmpty(balance)){
                    if(balance.startsWith("0x")||balance.startsWith("0X")){
                        spanned = BlockTool.fromHtml(BlockTool.parseEth(balance), "#999999",BlockTool.getUnit(),"");
                        headerViewHolder.tv_balance.setText(spanned);
                    }else {
                        spanned = BlockTool.fromHtml(balance, "#999999",BlockTool.getUnit(),"");
                        headerViewHolder.tv_balance.setText(spanned);
                    }
                }else {
                    headerViewHolder.tv_balance.setText("");
                }
                break;
            default:
                headerViewHolder.layout_out.setVisibility(View.GONE);
                headerViewHolder.layout_in.setVisibility(View.GONE);
                break;
        }

        if(!TextUtils.isEmpty(count))
            headerViewHolder.tv_deal_count.setText(Long.decode(count)+"");
        else
            headerViewHolder.tv_deal_count.setText("");

        headerViewHolder.btn_search.setTag(headerViewHolder.et_hash);
        headerViewHolder.btn_search.setOnClickListener(mOnClickListener);
        headerViewHolder.tv_type.setOnClickListener(mOnClickListener);
    }


    private void setListView(final DataViewHolder holder, final Transaction transaction){
        if(transaction==null)
            return;
        holder.tv_hash.setText(transaction.hash);

        Spanned spanned = null;
        String price = "";
        StringBuilder value = new StringBuilder();

        switch (blockType) {
            case ETH:
                BigInteger gas = new BigInteger(transaction.gas);
                BigInteger gasPrice = new BigInteger(transaction.gasPrice);
                BigInteger result = gas.multiply(gasPrice);
                price = BlockTool.parseEth(result.toString(10));
                spanned = BlockTool.fromHtml(price, "#999999",BlockTool.getUnit(),"");
                holder.tv_fee.setText(spanned);
                if(transaction.from.equals(address))
                    value.append("- ");
                else
                    value.append("+ ");
                value.append(BlockTool.parseEth2(transaction.value));
                holder.tv_value.setText(value);
                break;
            case BTC:
                price = transaction.fees;
                spanned = BlockTool.fromHtml(price, "#999999",BlockTool.getUnit(),"");
                holder.tv_fee.setText(spanned);
                value.append(transaction.btcValue);
                holder.tv_value.setText(value + BlockTool.getUnit());
                break;
            case BCH:
                price = transaction.fees;
                spanned = BlockTool.fromHtml(price, "#999999",BlockTool.getUnit(),"");
                holder.tv_fee.setText(spanned);
                value.append("- ");
                value.append(transaction.value);
                holder.tv_value.setText(value  + BlockTool.getUnit());
                break;
        }

        if(!TextUtils.isEmpty(transaction.blockNumber)){

            holder.tv_height.setText(Long.decode(transaction.blockNumber)+ "");
        }
        long timestamp = Long.decode(transaction.timestamp);
        String time = BlockTool.timestampToDate(timestamp);
        holder.tv_time.setText(time);

        holder.tv_hash.setTag(transaction.hash);
        holder.tv_hash.setOnClickListener(mOnClickListener);

    }


    //头部布局的viewHolder
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private TextView tv_type,tv_address,tv_balance,tv_deal_count;
        private EditText et_hash;
        private Button btn_search;;
        private RelativeLayout layout_in,layout_out;
        private TextView tv_in,tv_out;
        public HeaderViewHolder(View view) {
            super(view);
            mView = view;
            tv_type = (TextView) view.findViewById(R.id.tv_type);
            tv_address = (TextView) view.findViewById(R.id.tv_address);
            tv_balance = (TextView) view.findViewById(R.id.tv_balance);
            tv_deal_count = (TextView) view.findViewById(R.id.tv_deal_count);
            et_hash = (EditText) view.findViewById(R.id.et_hash);
            btn_search = (Button) view.findViewById(R.id.btn_search);
            layout_in = (RelativeLayout) view.findViewById(R.id.layout_in);
            layout_out = (RelativeLayout) view.findViewById(R.id.layout_out);
            tv_in = (TextView) view.findViewById(R.id.tv_in);
            tv_out = (TextView) view.findViewById(R.id.tv_out);
        }
    }

    public TextView getTypeTextView(){
        if(headerViewHolder!=null)
        return  headerViewHolder.tv_type;
        else return null;
    }

    //底部布局的viewHolder
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        public final View mView, more_foot_view, nothing_foot_view;

        public FootViewHolder(View view) {
            super(view);
            mView = view;
            more_foot_view = view.findViewById(R.id.layout_load_more);
            nothing_foot_view = view.findViewById(R.id.layout_load_none);
        }
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        private TextView tv_hash,tv_height,tv_fee,tv_time,tv_value;

        public DataViewHolder(View view) {
            super(view);
            mView = view;
            tv_hash = (TextView) view.findViewById(R.id.tv_hash);
            tv_height = (TextView) view.findViewById(R.id.tv_height);
            tv_fee = (TextView) view.findViewById(R.id.tv_fee);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_value = (TextView) view.findViewById(R.id.tv_value);

        }
    }
}
