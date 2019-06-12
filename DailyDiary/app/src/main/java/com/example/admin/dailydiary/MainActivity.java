package com.example.admin.dailydiary;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    //작성 버튼
    Button insertButton;
    //Adapter
    ListAdapter listAdapter;
    //ListView
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //listAdapter
        listAdapter = new ListAdapter();

        //select
        DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        Cursor cursor = sqlDB.rawQuery("select * from diaryDB",null);
        while(cursor.moveToNext()){
            int dNo = cursor.getInt(0);
            String date = cursor.getString(1);
            String img = cursor.getString(2);
            String content = cursor.getString(3);
            Log.d("====",Integer.toString(dNo));
            ListViewItem item = new ListViewItem();
            item.setdNo(dNo);
            item.setDate(date);
            item.setImg(img);
            item.setContent(content);

            listAdapter.addItem(item);

        }
        //close
        cursor.close();
        sqlDB.close();

        //listView
        listView = findViewById(R.id.listView01);
        listView.setAdapter(listAdapter);
        //insert Button
        insertButton = findViewById(R.id.insertButton);
        //insert Button onClick
        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //insert Button Click -> insert Page
                Intent it = new Intent(getApplicationContext(),InsertActivity.class);
                startActivity(it);

                //refresh
                finish();
            }
        });

        //listView의 아이템을 클릭했을때.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //position = click한 index
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewItem clickItem = ListAdapter.diaryList.get(position);
                Intent it = new Intent(getApplicationContext(),ItemView.class);
                it.putExtra("clickItem", clickItem);
                startActivity(it);
                finish();
            }
        });
    }
}
