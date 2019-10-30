package com.dqn.bottomSheet;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        MyInterRelativeLayout myInterRelativeLayout = findViewById(R.id.myInterRelativeLayout);
        RecyclerView rv = findViewById(R.id.rv);

        //布局顶部距离中间需要停留的高度
        int middleDis = 630;
        //底部冒出的高度
        int peekHeight = 200;
        myInterRelativeLayout.setDis_m_ph(middleDis, peekHeight);


        //布局管理器
        rv.setLayoutManager(new MySwitchScrollLinearManager(this));

        list = new ArrayList<>();
        for(int i=0;i<60;i++){
            list.add("");
        }
        RecycleAdapter adapter = new RecycleAdapter(this);
        rv.setAdapter(adapter);

        //初始状态
        myInterRelativeLayout.initFirstPosition();
    }

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {


        private LayoutInflater layoutInflater;


        public RecycleAdapter(Context context) {

            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.item_tv, parent, false);
            return new RecycleAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecycleAdapter.ViewHolder holder, final int position) {
           // holder.textView.setText();

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }
}
