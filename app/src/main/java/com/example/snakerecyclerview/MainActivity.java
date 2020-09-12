package com.example.snakerecyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycler_view;


    /*设置横向数目*/
    private static final int COLUMN_COUNT = 6;
    /*设置总共Items*/
    private static final String[] ITEMS = {"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A"};
    /*设置数组*/
    private List<String> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, COLUMN_COUNT, LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(gridLayoutManager);
        recycler_view.addItemDecoration(new ConnectorDecoration(this));
        recycler_view.setAdapter(new MyAdapter(this));
    }


    /*初始化下划线类*/
    private class ConnectorDecoration extends RecyclerView.ItemDecoration {

        private Paint mLinePaint;
        private int mSpace;

        public ConnectorDecoration(Context context) {
            super();
            mSpace = context.getResources().getDimensionPixelOffset(R.dimen.space_margin);
            int connectorWidth = context.getResources().getDimensionPixelOffset(R.dimen.connector_width);
            mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLinePaint.setColor(context.getResources().getColor(R.color.colorRed));
            /*设置连接线宽度*/
            mLinePaint.setStrokeWidth(connectorWidth);
        }

        /*设置每个ITEM之间的边距*/

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.set(mSpace, mSpace, mSpace, mSpace);
        }

        @Override
        public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            RecyclerView.LayoutManager manager = parent.getLayoutManager();
            for (int i = 0; i < parent.getChildCount(); i++) {
                View childAt = parent.getChildAt(i);
                int childLeft = manager.getDecoratedLeft(childAt);
                int childRight = manager.getDecoratedRight(childAt);
                int childTop = manager.getDecoratedTop(childAt);
                int childBottom = manager.getDecoratedBottom(childAt);
                /*当前childView x轴方向的中间位置*/
                int x = childLeft + (110);
                /*当前ChildView y轴方向的中间位置*/
                int y = childBottom + (-80);
                int position = parent.getChildViewHolder(childAt).getLayoutPosition();
                if (!mItems.get(position).equals("placeholder")) {
                    /*过滤出第一列上所有item和position*/
                    if (position % COLUMN_COUNT == 0) {
                        /*第一列中奇数行item与下一行第一个item连接,并且当前不是最后一个ChildView,并且如果当前是最后一行也不画纵向连接线*/
                        if ((position / COLUMN_COUNT) % 2 == 1 && position < mItems.size() - COLUMN_COUNT) {
                            c.drawLine(x, childBottom - mSpace, x, childBottom + mSpace, mLinePaint);
                        }
                        /*过滤出最后一列上所有item的position*/
                    } else if (position % COLUMN_COUNT == (COLUMN_COUNT - 1)) {
                        /*偶数行，最后一列向下画连接线，并且当前不是最后一个ChildView*/
                        if ((position / COLUMN_COUNT) % 2 == 0 && (position != mItems.size() - 1)) {
                            c.drawLine(x, childBottom - mSpace, x, childBottom + mSpace, mLinePaint);
                        }
                    }
                    /*当前ChildView不是每一行的最后一列，并且不是最后一个，画横向的连接线*/
                    if ((position % COLUMN_COUNT != (COLUMN_COUNT - 1)) && (position != mItems.size() - 1)) {
                        c.drawLine(childRight - mSpace, y, childRight + mSpace, y, mLinePaint);
                    }
                }
            }
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<ItemHolder> {

        private LayoutInflater mLayoutInflater;
        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
            mItems = new ArrayList<>();
            List<String> temp = new ArrayList<>();
            if (ITEMS.length % COLUMN_COUNT == 0) {
                mItems.addAll(Arrays.asList(ITEMS));
            } else {
                if (ITEMS.length / COLUMN_COUNT % 2 == 0) {
                    mItems.addAll(Arrays.asList(ITEMS));
                } else {
                    mItems.addAll(Arrays.asList(ITEMS));
                    temp.addAll(mItems.subList(0, ITEMS.length - (ITEMS.length) % COLUMN_COUNT));
                    for (int i = 0; i < COLUMN_COUNT - (ITEMS.length) % COLUMN_COUNT; i++) {
                        temp.add("placeholder");
                    }
                    temp.addAll(mItems.subList(ITEMS.length - (ITEMS.length) % COLUMN_COUNT, mItems.size()));
                    mItems = temp;
                }
            }
            mLayoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = mLayoutInflater.inflate(R.layout.item, parent, false);
            return new ItemHolder(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            if (mItems.get(position).equals("placeholder")) {
                holder.setText("");
                holder.tv.setBackgroundColor(Color.TRANSPARENT);
            } else {
                holder.tv.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                holder.setText(mItems.get(position));
            }
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }

    /*private ArrayList sortData(List<String> items) {
        ArrayList<String> temItems = new ArrayList<>();
        int rowPosition = (items.size() - 1) / COLUMN_COUNT;
        if ((items.size() - 1) % COLUMN_COUNT != 0) {
            rowPosition = rowPosition + 1;
        }
        for (int i = 0; i < rowPosition - 1; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < COLUMN_COUNT - 1; j++) {
                    temItems.add(items.get((i + 1) * COLUMN_COUNT - j));
                }
            } else {
                for (int j = 0; j < COLUMN_COUNT - 1; j++) {
                    if (i != rowPosition) {
                        temItems.add(items.get((i + 1) * COLUMN_COUNT - j));
                    }
                }
            }
        }
        return temItems;
    }*/

    private class ItemHolder extends RecyclerView.ViewHolder {

        public TextView tv;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }

        public void setText(CharSequence text) {
            tv.setText(text);
        }
    }
}
