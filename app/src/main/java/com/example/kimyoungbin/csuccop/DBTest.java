package com.example.kimyoungbin.csuccop;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

/**
 * Created by Kim youngbin on 2017-08-23.
 */

public class DBTest extends SQLiteOpenHelper{
    private static final String DATABASE_NAME="DBTest";
    private static final int DATABASE_VERSION=1;

    private final Context mContext;

    public DBTest(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //사용할 테이블 생성
        StringBuffer sqlStr = new StringBuffer();

        sqlStr.append("\n CREATE TABLE BOARD( ");
        sqlStr.append("\n SEQ INTEGER PRIMARY KEY AUTOINCREMENT ");
        sqlStr.append("\n ,TITLE TEXT ");
        sqlStr.append("\n ,CONTENT TEXT ");
        sqlStr.append("\n ,HIT INTEGER ");
        sqlStr.append("\n ,REG_NAME TEXT ");
        sqlStr.append("\n ,REG_DATE TEXT ");
        sqlStr.append("\n ,MOD_DATE TEXT ");
        sqlStr.append("\n ); ");

        db.beginTransaction();

        try{
            //쿼리 실행
            db.execSQL(sqlStr.toString());
            db.setTransactionSuccessful();

            Log.d("DBTest.onCreate", "BOARD table was created");
        }catch (SQLException se){
            Log.e("DBTest.onCreate", se.toString());
        }catch (Exception e){
            Log.e("DBTest.onCreate",e.toString());
        }finally {
            db.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    //조회하기

    public BoardCursor searchList(String searchType, String SearchValue){
        StringBuffer sqlStr = new StringBuffer();

        sqlStr.append("\n SELECT * FROM BOARD WHERE 1=1");
        //사용자가 입력한 검색조건을 쿼리에 적용
        if(SearchValue != null && !SearchValue.equals("")){
            //제목 searchType은 리스트의 순서대로 인덱스가 설정되어 있다 0~2
            if(searchType.equals("0")){
                sqlStr.append("\n AND TITLE like '%"+SearchValue+"%'");
            //내용
            }else if(searchType.equals("1")){
                sqlStr.append("\n AND CONTENT like '%"+SearchValue+"%'");
            //제목 + 내용
            }else if(searchType.equals("2")){
                sqlStr.append("\n AND (TITLE like '%"+SearchValue+"%'");
                sqlStr.append("\n OR CONTENT like '%"+SearchValue+"%')");
            }
        }
        sqlStr.append("\n ORDER BY SEQ DESC;");
        SQLiteDatabase rd = getReadableDatabase();
         //커서로 조회
        BoardCursor bc = (BoardCursor)rd.rawQueryWithFactory(new BoardCursor.Factory(), sqlStr.toString(),null,null);

        //반드시 커서를 맨위로 올려야함
        bc.moveToFirst();

        return bc;
    }

    public static class BoardCursor extends SQLiteCursor{

        public BoardCursor(SQLiteDatabase db, SQLiteCursorDriver driver, String editTable, SQLiteQuery query) {
            super(db, driver, editTable, query);
        }

        public static class Factory implements SQLiteDatabase.CursorFactory{
            public Cursor newCursor(SQLiteDatabase db,SQLiteCursorDriver masterQuery,String editTable,SQLiteQuery query){
                return new BoardCursor(db, masterQuery, editTable,query);
            }
        }
    }
    //입력하기
    public void insertBoard(String category, String information, String optional){
        StringBuffer sqlStr = new StringBuffer();
        sqlStr.append("\n INSERT INTO BOARD('"+category+"','"+information+"','"+optional+"');");
        SQLiteDatabase wd = getWritableDatabase();
        wd.execSQL(sqlStr.toString());
    }
}
