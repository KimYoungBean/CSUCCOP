package com.example.kimyoungbin.csuccop;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kim youngbin on 2017-08-23.
 */

public class BoardList extends Activity {
    Context context;
    DBTest db;
    DBTest.BoardCursor bc;
    TableLayout tbl;
    LinearLayout msgll;
    Spinner searchType;
    EditText searchVal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.search);

        //DB를 담당할 인스터스 객체 생성
        db = new DBTest(this);

        //SEARCH 버튼 이벤트 리스너
        Button btn_search = (Button)findViewById(R.id.btnShowList);

        btn_search.setOnClickListener(btn_search_listener);

        //검색 리스트에 넣을 내용
        searchType = (Spinner)findViewById(R.id.searchType);

        List searchTypeList = new ArrayList();
        searchTypeList.add("Title");
        searchTypeList.add("Content");
        searchTypeList.add("Title+Content");

        //검색 리스트 세팅
        ArrayAdapter AASearchType = new ArrayAdapter(this, android.R.layout.simple_spinner_item,searchTypeList);

        //세팅한 아이템과 설정 적용
        searchType.setAdapter(AASearchType);

        //다른 메소드에서도 사용할 수 있도록 객체 초기화
        tbl = (TableLayout)findViewById(R.id.tbl);
        msgll = (LinearLayout)findViewById(R.id.msgll);
        searchVal = (EditText)findViewById(R.id.searchVal);
    }

    /**
    * search 버튼 이벤트 리스너
     */
    private final View.OnClickListener btn_search_listener = new View.OnClickListener(){
        public void onClick(View v){
            boardList();
        }
    };

    private void boardList(){
        //검색어가 있을 경우 가져온다.
        String searchType = String.valueOf(this.searchType.getSelectedItemId()).trim();
        String searchVal = String.valueOf(this.searchVal.getText()).trim();

        //db에서 리스트를 조회해서 가져온다.
        bc = db.searchList(searchType,searchVal);
        msgll.removeAllViews();

        //리스트의 행을 초기화한다. 위에서 검색한 데이터로 그려야해서..
        tbl.removeAllViews();

        //조회한 리스트를 가져오는 부분
        for(int i=0; i<bc.getCount();i++){
            bc.moveToPosition(i);

            //TableLayout에 tr행을 추가한다.
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            //가운데 정렬
            tr.setGravity(Gravity.CENTER);

            //추가한 행의 각 열에 추가할 TextView
            TextView btn1 = new TextView(this);

            btn1.setText( String.valueOf(bc.getInt(bc.getColumnIndexOrThrow("SEQ"))));
            btn1.setPadding(5,5,5,5);
            btn1.setHeight(30);

            btn1.setGravity(Gravity.CENTER);
            btn1.setWidth(40);
            tr.addView(btn1);

            TextView btn2 = new TextView(this);

            btn2.setText(bc.getString(bc.getColumnIndexOrThrow("TITLE")));
            btn2.setPadding(5,5,5,5);
            btn2.setHeight(30);

            btn2.setGravity(Gravity.CENTER);
            btn2.setWidth(80);

            tr.addView(btn2);

            TextView btn3 = new TextView(this);

            btn3.setText(String.valueOf(bc.getInt(bc.getColumnIndexOrThrow("HIT"))));
            btn3.setPadding(5,5,5,5);
            btn3.setHeight(40);

            btn3.setGravity(Gravity.CENTER);
            btn3.setWidth(40);

            tr.addView(btn3);

            TextView btn4 = new TextView(this);

            btn4.setText(bc.getString(bc.getColumnIndexOrThrow("REG_NAME")));
            btn4.setPadding(5,5,5,5);
            btn4.setHeight(40);

            btn4.setGravity(Gravity.CENTER);
            btn4.setWidth(60);

            tr.addView(btn4);

            TextView btn5 = new TextView(this);

            btn5.setText(bc.getString(bc.getColumnIndexOrThrow("REG_DATE")));
            btn5.setPadding(5,5,5,5);
            btn5.setHeight(40);

            btn5.setGravity(Gravity.CENTER);
            btn5.setWidth(80);

            tr.addView(btn5);

            //TableLayout 에 생성한 tr열을 추가
            tbl.addView(tr);
        }

        //검색결과가 없을 경우
        if(bc.getCount()==0){
            TextView msg = new TextView(this);
            msg.setText("There are no correct searches.");
            msg.setHeight(30);

            msg.setGravity(Gravity.CENTER);
            msgll.addView(msg);
        }
    }
}
