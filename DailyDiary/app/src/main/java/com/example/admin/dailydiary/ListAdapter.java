package com.example.admin.dailydiary;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    //데이터베이스에서 꺼내온 데이터를 저장하기 위한 리스트
    public static ArrayList<ListViewItem> diaryList = new ArrayList<>();

    @Override
    public int getCount() {
        return diaryList.size();
    }

    @Override
    public Object getItem(int i) {
        return diaryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final int pos = i;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView dnoView = convertView.findViewById(R.id.dNoView);
        TextView contentView =  convertView.findViewById(R.id.contentView);
        TextView dateView = convertView.findViewById(R.id.dateView);
        ImageView imgView = convertView.findViewById(R.id.imgView);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = diaryList.get(i);

        // 아이템 내 각 위젯에 데이터 반영
        contentView.setText(listViewItem.getContent());
        dateView.setText(listViewItem.getDate());
        String weather = listViewItem.getImg();
        Log.d("weatehr ===",weather);
        switch(weather){
            case "rain":
                imgView.setImageResource(R.drawable.rain);
                break;
            case "sun":
                imgView.setImageResource(R.drawable.sun);
                break;
            case "cloudy":
                imgView.setImageResource(R.drawable.cloudy);
                break;
            case "blur":
                imgView.setImageResource(R.drawable.blur);
        }
        return convertView;
    }

    public void addItem(ListViewItem item){
        diaryList.add(item);
    }
}
