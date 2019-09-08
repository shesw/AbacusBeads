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
import com.sheswland.abacusbeads.query.QueryDataManager;
import com.sheswland.abacusbeads.utils.DebugLog;

public class QueryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private final String TAG = "QueryAdapter";

    public enum Accuracy{
        day, month, year
    }

    private int itemTypeTitle = 0;
    private int itemTypeContent = 1;

    private Activity mActivity;
    private TitleBarListener mTitleBarListener;
    private TitleViewHolder titleViewHolder;
    public int currentAccuracy = 0;

    public void setAccuracy(int currentAccuracy) {
        this.currentAccuracy = currentAccuracy;
    }

    public QueryAdapter(Activity activity, TitleBarListener listener) {
        DebugLog.d(TAG, "init");
        mActivity = activity;
        this.mTitleBarListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int itemType) {

        if (itemType == itemTypeTitle) {
            if (currentAccuracy == Accuracy.day.ordinal()) {
                TitleViewHolder view = new TitleViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.query_title_day, viewGroup, false));
                titleViewHolder = view;
                return view;
            } else if (currentAccuracy == Accuracy.month.ordinal() || currentAccuracy == Accuracy.year.ordinal()) {
                TitleViewHolder view = new TitleViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.query_title_month_and_year, viewGroup, false));
                titleViewHolder = view;
                return view;
            }
        } else if (itemType == itemTypeContent) {
            if (currentAccuracy == Accuracy.day.ordinal()) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.query_list_item_layout_day, viewGroup, false);
                return new DayViewHolder(view);
            } else if (currentAccuracy == Accuracy.month.ordinal() || currentAccuracy == Accuracy.year.ordinal()) {
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
            AccountDayTable table = (AccountDayTable) QueryDataManager.getInstance().getmDayTableList().get(i);
            ((DayViewHolder)myViewHolder).date.setText(table.getDate());
            ((DayViewHolder)myViewHolder).content.setText(table.getContent());
            ((DayViewHolder)myViewHolder).type.setText(table.isIncome() ? "收入" : "支出");
            ((DayViewHolder)myViewHolder).spend.setText(String.valueOf(table.getSpend()));
            ((DayViewHolder)myViewHolder).remain.setText(String.valueOf(table.getRemain()));
        } else if (myViewHolder instanceof MonthAndYearViewHolder) {
            AccountMonthAndYearTable table;
            if (currentAccuracy == Accuracy.month.ordinal()) {
                table = (AccountMonthAndYearTable) QueryDataManager.getInstance().getmMonthTableList().get(i);
            } else {
                table = (AccountMonthAndYearTable) QueryDataManager.getInstance().getmYearTableList().get(i);
            }
            ((MonthAndYearViewHolder)myViewHolder).date.setText(table.getDate());
            ((MonthAndYearViewHolder)myViewHolder).income.setText(String.valueOf(table.getIncome()));
            ((MonthAndYearViewHolder)myViewHolder).spend.setText(String.valueOf(table.getSpend()));
            ((MonthAndYearViewHolder)myViewHolder).remain.setText(String.valueOf(table.getRemain()));
        } else if (myViewHolder instanceof TitleViewHolder) {
            ((TitleViewHolder) myViewHolder).date.setOnClickListener(this);
            ((TitleViewHolder) myViewHolder).type.setOnClickListener(this);
        }
    }


    @Override
    public int getItemCount() {
        int size = 0;
        if (currentAccuracy == Accuracy.day.ordinal()) {
            size = QueryDataManager.getInstance().getmDayTableList().size();
        } else if (currentAccuracy == Accuracy.month.ordinal()) {
            size = QueryDataManager.getInstance().getmMonthTableList().size();
        } else if (currentAccuracy == Accuracy.year.ordinal()) {
            size = QueryDataManager.getInstance().getmYearTableList().size();
        }
        return size + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return itemTypeTitle;
        }
        return itemTypeContent;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_date) {
            mTitleBarListener.onDateClick();
        } else if (id == R.id.bt_type) {
            mTitleBarListener.onTypeClick();
            titleViewHolder.type.setText(QueryActivity.types[((QueryActivity)mActivity).currentType]);
        }
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

    /**************** interface ***************/
    public interface TitleBarListener{
        void onDateClick();
        void onTypeClick();
    }
}
