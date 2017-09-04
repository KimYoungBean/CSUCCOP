package com.example.kimyoungbin.csuccop;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    SQLiteDatabase sqliteDB;

    Button incident;
    Button sos;
    Button location;
    Button report;

    DBTest db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqliteDB = init_database();
        init_tables();

        incident = (Button)findViewById(R.id.incident);

        sos = (Button)findViewById(R.id.sos);

        location = (Button)findViewById(R.id.location);

        report = (Button)findViewById(R.id.report);

        incident.setOnClickListener(this);

        sos.setOnClickListener(this);

        location.setOnClickListener(this);

        report.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==incident ){
            Intent intent = new Intent(MainActivity.this, incidentActivity.class);
            startActivity(intent);
        }else if(v==sos){
            Intent intent = new Intent(MainActivity.this, sosActivity.class);
            startActivity(intent);
        }else if(v==location){
            Intent intent = new Intent(MainActivity.this, locationActivity.class);
            startActivity(intent);
        }else if(v==report){
            Intent intent = new Intent(MainActivity.this, BoardList.class);
            startActivity(intent);
        }
    }

    private SQLiteDatabase init_database(){
        SQLiteDatabase db = null;

        File file = new File(getFilesDir(), "contact.db");

        System.out.println("PATH : "+file.toString());
        try{
            db = SQLiteDatabase.openOrCreateDatabase(file, null);
        }catch (SQLiteException e){
            e.printStackTrace();
        }
        if(db == null){
            System.out.println("DB creation failed. " + file.getAbsolutePath());
        }
        return db;
    }

    private void init_tables(){
        if(sqliteDB !=null){
            String sqlCreateTbl = "CREATE TABLE IF NOT EXISTS CONTACT_T ("+
                    "NO " + "INTEGER NOT NULL, "+
                    "CATEGORY " + "TEXT, "+
                    "INFORMATION " + "TEXT "+
                    "OPTIONAL "+ "TEXT"+")";
            System.out.println(sqlCreateTbl);
            sqliteDB.execSQL(sqlCreateTbl);
        }
    }
}
