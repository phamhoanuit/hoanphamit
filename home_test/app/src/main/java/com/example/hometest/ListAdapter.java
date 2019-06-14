package com.example.hometest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    static  List<String> data;
    Context context;

    public ListAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_view, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            myViewHolder.txtValue.setText(data.get(i));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtValue;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtValue = itemView.findViewById(R.id.value_string);
        }
    }

//    public static  List<String> getValue(){
//        data = new ArrayList();
//        data.add("xiaomi");
//        data.add("bts");
//        data.add("balo");
//        data.add("bitis hunter x");
//        data.add("tai nghe");
//        data.add("harry potter");
//        data.add("anker");
//        data.add("iphone");
//        data.add("balo nữ");
//        data.add("nguyễn nhật ánh");
//        data.add("đắc nhân tâm");
//        data.add("ipad");
//        data.add("senka");
//        data.add("tai nghe bluetooth");
//        data.add("son");
//        data.add("maybelline");
//        data.add("laneige");
//        data.add("kem chống nắng");
//        data.add("anh chính là thanh xuân của em");
//        return data;
//    }
}
