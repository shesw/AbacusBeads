package com.sheswland.abacusbeads.query.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sheswland.abacusbeads.R;
import com.sheswland.abacusbeads.database.tables.OperateDataTable;
import com.sheswland.abacusbeads.utils.DebugLog;

import java.util.ArrayList;
import java.util.List;

public class QueryAdapter extends RecyclerView.Adapter<QueryAdapter.MyViewHolder> {
    private final String TAG = "QueryAdapter";

    private Activity mAcivity;
    private List<OperateDataTable> dataList;

    public QueryAdapter(Activity activity) {
        DebugLog.d(TAG, "init");
        mAcivity = activity;
        dataList = new ArrayList<>();
    }

    public void setData(ArrayList<OperateDataTable> list) {
        DebugLog.d(TAG, "setData " + list.size());
        this.dataList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.query_list_item_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        OperateDataTable table = dataList.get(i);
        myViewHolder.date.setText(table.getYear() + "" + table.getMonth() + "" + table.getDay());
        myViewHolder.content.setText(table.getContent());
        myViewHolder.type.setText(table.isIncome() ? "收入" : "支出");
        myViewHolder.spend.setText(String.valueOf(table.getSpend()));
        myViewHolder.spend.setText(String.valueOf(table.getRemain()));
        DebugLog.d(TAG, "bind view " + table.getYear());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView content;
        TextView type;
        TextView spend;
        TextView remain;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            content = itemView.findViewById(R.id.content);
            type = itemView.findViewById(R.id.type);
            spend = itemView.findViewById(R.id.spend);
            remain = itemView.findViewById(R.id.remain);
        }
    }


}
