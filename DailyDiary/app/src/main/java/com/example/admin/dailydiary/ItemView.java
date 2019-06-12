package com.example.admin.dailydiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class ItemView extends AppCompatActivity {
    TextView viewDate,viewTodayWeater;
    ImageView viewWeather;
    TextView viewContent;
    Button updateBtn,deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        Intent intent = getIntent();
        final ListViewItem item = (ListViewItem) intent.getSerializableExtra("clickItem");

        viewDate = findViewById(R.id.viewDate);
        viewWeather = findViewById(R.id.viewWeather);
        viewTodayWeater = findViewById(R.id.viewTodayWeater);
        viewContent = findViewById(R.id.viewContent);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        viewContent.setEnabled(true);

        viewDate.setText(item.getDate());
        //img
        String wImg = item.getImg();

        if(wImg.equals("cloudy")){
            viewWeather.setImageResource(R.drawable.cloudy);
            viewTodayWeater.setText("구름많음/Cloudy");
        }else if(wImg.equals("sun")){
            viewWeather.setImageResource(R.drawable.sun);
            viewTodayWeater.setText("맑음/Sun");
        }else if(wImg.equals("rain")){
            viewWeather.setImageResource(R.drawable.rain);
            viewTodayWeater.setText("비/Rain");
        }else if(wImg.equals("blur")){
            viewWeather.setImageResource(R.drawable.blur);
            viewTodayWeater.setText("흐림/Blur");
        }

        viewContent.setText(item.getContent());

        //del Button Click Handler
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = View.inflate(ItemView.this,R.layout.dialog,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(ItemView.this);
                dlg.setTitle("삭제");
                dlg.setView(dialogView);
                TextView tf = dialogView.findViewById(R.id.dialogTv);
                tf.setText("삭제하시겠습니까?");
                dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
                        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
                        sqlDB.execSQL("delete from diaryDB where dNo='"+item.getdNo()+"'");
                        sqlDB.close();
                        Intent it = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(it);
                        ListAdapter.diaryList.clear();
                        finish();
                    }
                });
                dlg.show();
            }
        });

        //updateBtn Click Handler
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(),UpdateItemView.class);
                it.putExtra("item",item);
                startActivity(it);
                finish();
            }
        });



    }
}
