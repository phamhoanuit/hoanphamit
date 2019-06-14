package com.example.hometest.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.hometest.ListAdapter;
import com.example.hometest.R;
import com.example.hometest.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainView {

    private RecyclerView mRecycleView;
    List<String> dataReturn = new ArrayList();

    ListAdapter adapter;
    LinearLayoutManager manager;
    final int duration = 5000;
    final int pixelsToMove = 300;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMainPresenter();
        mainPresenter.loadData();
    }

    private void initRecyecleView(List<String> dataReturn){
        mRecycleView = findViewById(R.id.list_data);
        mRecycleView.setHasFixedSize(true);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter = new ListAdapter(dataReturn, this);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setItemAnimator(new DefaultItemAnimator());
        mRecycleView.setAdapter(adapter);
        autoScroll();

    }
    private void initMainPresenter(){
        mainPresenter = new MainPresenter(this);
    }

    private final Runnable SCROLLING_RUNNABLE = new Runnable() {
        @Override
        public void run() {
            mRecycleView.smoothScrollBy(pixelsToMove, 0);
            mHandler.postDelayed(this, duration);
        }
    };

    /**
     * auto scroll recyclerview
     */
    private void autoScroll(){
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = manager.findLastCompletelyVisibleItemPosition();
                if(lastItem == manager.getItemCount()-1){
                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                    Handler postHandler = new Handler();
                    postHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRecycleView.setAdapter(null);
                            mRecycleView.setAdapter(adapter);
                            mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
                        }
                    }, 2000);
                }
            }
        });
        mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
    }

    @Override
    public void displayDataToRecycleView(List<String> data) {
        initRecyecleView(data);
    }
}
