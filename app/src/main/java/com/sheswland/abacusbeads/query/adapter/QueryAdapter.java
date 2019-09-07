package com.sheswland.abacusbeads.query.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sheswland.abacusbeads.R;
import com.sheswland.abacusbeads.database.database_interface.Table;
import com.sheswland.abacusbeads.database.tables.AccountDayTable;
import com.sheswland.abacusbeads.database.tables.AccountMonthAndYearTable;
import com.sheswland.abacusbeads.query.QueryActivity;
import com.sheswland.abacusbeads.utils.DebugLog;

import java.util.ArrayList;
import java.util.List;

public class QueryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = "QueryAdapter";

    private int itemTypeTitle = 0;
    private int itemTypeContent = 1;

    private Activity mAcivity;
    private List<Table> dataList;

    private QueryActivity.accuracy accuracy = QueryActivity.accuracy.day;

    public QueryAdapter(Activity activity) {
        DebugLog.d(TAG, "init");
        mAcivity = activity;
        dataList = new ArrayList<>();
    }

    public void setAccuracy(QueryActivity.accuracy accuracy) {
        this.accuracy = accuracy;
    }

    public void setData(ArrayList<Table> list) {
        DebugLog.d(TAG, "setData " + list.size());
        this.dataList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {

        if (itemType == itemTypeTitle) {
            if (accuracy == QueryActivity.accuracy.day) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.query_title_day, viewGroup, false);
                return new TitleViewHolder(view);
            } else if (accuracy == QueryActivity.accuracy.month || accuracy == QueryActivity.accuracy.year) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.query_title_month_and_year, viewGroup, false);
                return new TitleViewHolder(view);
            }
        } else if (itemType == itemTypeContent) {
            if (accuracy == QueryActivity.accuracy.day) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.query_list_item_layout_day, viewGroup, false);
                return new DayViewHolder(view);
            } else if (accuracy == QueryActivity.accuracy.month || accuracy == QueryActivity.accuracy.year) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.query_list_item_layout_month_and_year, viewGroup, false);
                return new MonthAndYearViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder myViewHolder, int position) {

        int i = position - 1;

        if (myViewHolder instanceof DayViewHolder) {
            AccountDayTable table = (AccountDayTable) dataList.get(i);
            ((DayViewHolder)myViewHolder).date.setText(table.getDate());
            ((DayViewHolder)myViewHolder).content.setText(table.getContent());
            ((DayViewHolder)myViewHolder).type.setText(table.isIncome() ? "收入" : "支出");
            ((DayViewHolder)myViewHolder).spend.setText(String.valueOf(table.getSpend()));
            ((DayViewHolder)myViewHolder).remain.setText(String.valueOf(table.getRemain()));

        } else if (myViewHolder instanceof MonthAndYearViewHolder) {
            AccountMonthAndYearTable table = (AccountMonthAndYearTable) dataList.get(i);
            ((MonthAndYearViewHolder)myViewHolder).date.setText(table.getDate());
            ((MonthAndYearViewHolder)myViewHolder).income.setText(String.valueOf(table.getIncome()));
            ((MonthAndYearViewHolder)myViewHolder).spend.setText(String.valueOf(table.getSpend()));
            ((MonthAndYearViewHolder)myViewHolder).remain.setText(String.valueOf(table.getRemain()));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return itemTypeTitle;
        }
        return itemTypeContent;
    }

    class DayViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView content;
        TextView type;
        TextView spend;
        TextView remain;
        DayViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            content = itemView.findViewById(R.id.content);
            type = itemView.findViewById(R.id.type);
            spend = itemView.findViewById(R.id.spend);
            remain = itemView.findViewById(R.id.remain);
        }
    }

    class MonthAndYearViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView spend;
        TextView income;
        TextView remain;
        public MonthAndYearViewHolder(@NonNull View itemView) {
            super(itemView);
            this.date = itemView.findViewById(R.id.date);
            this.spend = itemView.findViewById(R.id.spend);
            this.income = itemView.findViewById(R.id.income);
            this.remain = itemView.findViewById(R.id.remain);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView type;
        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.bt_date);
            type = itemView.findViewById(R.id.bt_type);
        }
    }

}
