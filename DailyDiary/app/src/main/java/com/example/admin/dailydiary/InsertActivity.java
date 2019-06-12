package com.example.admin.dailydiary;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Calendar;

public class InsertActivity extends AppCompatActivity {

    // TextView
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
    String selectImage;
    // Linear Layout
    LinearLayout imgTextLinear;
    // EditText
    EditText content;


    // SQLLiteDataBase
    SQLiteDatabase sqlDB;

    //String url
    String url = "https://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=4119077000";

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

        ListAdapter.diaryList.clear();

        //DB
        final DBHelper dbHelper = DBHelper.getInstance(getApplicationContext());


        //CalendarView default GONE
        calendarView.setVisibility(View.GONE);

        getXMLJsoup getXMLJsoup = new getXMLJsoup();
        getXMLJsoup.execute();

        // Calendar getInstance
        calendar = Calendar.getInstance();
        // textView default 오늘 날짜 설정
        todayDate.setText(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH));
        // date textView click -> CalendarView VISIBLE
        todayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarView.setVisibility(View.VISIBLE);
                imgTextLinear.setVisibility(View.GONE);
            }
        });

        //Calendar View Date Change
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Click한 날짜로 변경
                todayDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                // calendarView GONE
                calendarView.setVisibility(View.GONE);
                imgTextLinear.setVisibility(View.VISIBLE);

            }
        });

        //listBtn Click -> main
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(it);
                finish();
            }
        });

        // insertBtn Click -> DB store
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // date get
                String date = todayDate.getText().toString();

                // content get
                String contents = content.getText().toString();
                if(contents.length() == 0){
                    Toast.makeText(getApplicationContext(),"내용을 입력하셔야 저장이 가능합니다!.",Toast.LENGTH_SHORT).show();
                    return;
                }

                // DB Store
                sqlDB = dbHelper.getWritableDatabase();
                sqlDB.execSQL("insert into diaryDB(date,img,contents) values('"+date+"', '"+weatherData+"', '"+contents+"');");
                Toast.makeText(getApplicationContext(),"작성 완료!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                sqlDB.close();
                finish();
            }
        });

    }

    private class getXMLJsoup extends AsyncTask<Void,Void,Void> {

        //비동기 작업
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document doc = Jsoup.connect(url).get();
                Element items =  doc.select("data").first();
                data = items.select("wfKor").text()+"/";
                Log.d("=====",data);
                if(data.equals("비/")){
                    selectImage = "rain";
                    weatherData = "rain";
                }else if(data.equals("맑음/")){
                    weatherData = "sun";
                    selectImage = "sun";
                }else if(data.equals("흐림/")){
                    weatherData = "blur";
                    selectImage = "blur";
                }else if(data.equals("구름 많음/")){
                    weatherData = "cloudy";
                    selectImage = "cloudy";
                }
                data += items.select("wfEn").text();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        //메인 thread에게 요청(UI 변경)
        @Override
        protected void onPostExecute(Void result) {
            todayWeather.setText(data);
            if(selectImage.equals("cloudy")){
                weatherImg.setImageResource(R.drawable.cloudy);
            }else if(selectImage.equals("sun")){
                weatherImg.setImageResource(R.drawable.sun);
            }else if(selectImage.equals("rain")){
                weatherImg.setImageResource(R.drawable.rain);
            }else if(selectImage.equals("blur")){
                weatherImg.setImageResource(R.drawable.blur);
            }
        }
    }
}


