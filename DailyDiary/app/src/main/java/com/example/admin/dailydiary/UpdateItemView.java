package com.example.admin.dailydiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.w3c.dom.Text;

import java.util.Calendar;

public class UpdateItemView extends AppCompatActivity {

    TextView todayDate,todayWeather;
    // Calendar View
    CalendarView calendarView;
    // 날짜 설정을 위한 Calendar 객체
    Calendar calendar;
    // Button
    Button listBtn,insertBtn;
    // String Data
    String data,weatherData;
    // Image View
    ImageView weatherImg;
    // Linear Layout
    LinearLayout imgTextLinear;
    // EditText
    EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        todayDate = findViewById(R.id.todayDate);
        calendarView = findViewById(R.id.calendarView);
        listBtn = findViewById(R.id.listBtn);
        todayWeather = findViewById(R.id.todayWeather);
        weatherImg = findViewById(R.id.weatherImg);
        imgTextLinear = findViewById(R.id.imgTextLinear);
        insertBtn = findViewById(R.id.insertBtn);
        content = findViewById(R.id.content);

        calendarView.setVisibility(View.GONE);
        insertBtn.setText("수정");

        Intent it = getIntent();
        final ListViewItem item = (ListViewItem) it.getSerializableExtra("item");
        //init
        todayDate.setText(item.getDate());
        String uImg = item.getImg();
        if(uImg.equals("cloudy")){
            weatherImg.setImageResource(R.drawable.cloudy);
            todayWeather.setText("구름많음/Cloudy");
        }else if(uImg.equals("sun")){
            weatherImg.setImageResource(R.drawable.sun);
            todayWeather.setText("맑음/Sun");
        }else if(uImg.equals("rain")){
            weatherImg.setImageResource(R.drawable.rain);
            todayWeather.setText("비/Rain");
        }else if(uImg.equals("blur")){
            weatherImg.setImageResource(R.drawable.blur);
            todayWeather.setText("흐림/blur");
        }
        content.setText(item.getContent());

        //update btn click handler
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = View.inflate(UpdateItemView.this,R.layout.dialog,null);
                //ALertDialog
                AlertDialog.Builder dlg = new AlertDialog.Builder(UpdateItemView.this);
                dlg.setTitle("수정");
                dlg.setView(dialogView);
                TextView tf = dialogView.findViewById(R.id.dialogTv);
                tf.setText("수정하시겠습니까?");
                dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                //dialog -> postivieButton Click
                dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //update
                        DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
                        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
                        sqlDB.execSQL("update diaryDB set date='"+todayDate.getText().toString()+"', img='"+item.getImg()+"', contents='"+content.getText().toString()+"' where dNo='"+item.getdNo()+"'");
                        Intent it = new Intent(getApplicationContext(), ItemView.class);
                        ListViewItem upItem = new ListViewItem();
                        upItem.setdNo(item.getdNo());
                        upItem.setDate(todayDate.getText().toString());
                        upItem.setImg(item.getImg());
                        upItem.setContent(content.getText().toString());
                        it.putExtra("clickItem",upItem);
                        startActivity(it);
                        finish();
                    }
                });
                dlg.show();
            }
        });

        //list btn click handler
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(it);
                ListAdapter.diaryList.clear();
                finish();
            }
        });
    }
}
