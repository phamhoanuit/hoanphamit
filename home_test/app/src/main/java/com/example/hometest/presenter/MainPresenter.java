package com.example.hometest.presenter;

import com.example.hometest.model.DataModel;
import com.example.hometest.model.ILoadData;
import com.example.hometest.view.MainView;

import java.util.List;

public class MainPresenter implements ILoadData {

    DataModel dataModel;
    MainView mainView;

    public MainPresenter(MainView mainView) {
        this.mainView = mainView;
        dataModel = new DataModel(this);
    }

    @Override
    public void getData(List<String> data) {
         mainView.displayDataToRecycleView(data);
    }

    public void loadData(){
        dataModel.createData();
    }
}
