package com.example.kimyoungbin.csuccop;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Kim youngbin on 2017-08-02.
 */

public class incidentActivity extends AppCompatActivity implements View.OnClickListener{

    int cnt=1;

    ImageView screen;

    ImageView capture;
    ImageView gallery;
    ImageView video;


    Button text_capture;
    Button text_gallery;
    Button text_video;

    File filePath;

    Button send;

    DBTest db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incident);

        setTitle("report an indident");

        screen = (ImageView)findViewById(R.id.screen);

        capture = (ImageView)findViewById(R.id.capture);
        text_capture = (Button)findViewById(R.id.text_capture);

        gallery = (ImageView)findViewById(R.id.gallery);
        text_gallery = (Button)findViewById(R.id.text_gallery);

        video = (ImageView)findViewById(R.id.video);
        text_video = (Button)findViewById(R.id.text_video);

        send = (Button) findViewById(R.id.send);

        capture.setOnClickListener(this);
        text_capture.setOnClickListener(this);

        gallery.setOnClickListener(this);
        text_gallery.setOnClickListener(this);

        video.setOnClickListener(this);
        text_video.setOnClickListener(this);

        send.setOnClickListener(this);

        //spinner
        String[] str = getResources().getStringArray(R.array.spinnerArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, str);
        Spinner spi = (Spinner)findViewById(R.id.spinner);
        spi.setAdapter(adapter);
        spi.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );



    }


    private void showToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
    }

    @Override
    public void onClick(View v) {
        if(v==capture || v==text_capture){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                try {
                    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myApp";
                    File dir = new File(dirPath);
                    if (!dir.exists())
                        dir.mkdir();

                    filePath = File.createTempFile("IMG", ".jpg", dir);
                    if(!filePath.exists())
                        filePath.createNewFile();

                    Uri photoURI = FileProvider.getUriForFile(incidentActivity.this, BuildConfig.APPLICATION_ID+".provider",filePath);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(intent,10);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }else if(v==gallery || v==text_gallery){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                        startActivityForResult(intent, 30);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 40);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
            }

        }else if(v==video || v==text_video){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                try {
                    String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/myApp";
                    File dir = new File(dirPath);
                    if (!dir.exists())
                        dir.mkdir();

                    filePath = File.createTempFile("VIDEO", ".mp4", dir);
                    if(!filePath.exists())
                        filePath.createNewFile();

                    Uri videoURI = FileProvider.getUriForFile(incidentActivity.this, BuildConfig.APPLICATION_ID+".provider",filePath);
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,0);
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 20);
                    intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024*1024*10);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                    startActivityForResult(intent,20);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }else if(v==send){
            Toast.makeText(this,"send", Toast.LENGTH_LONG).show();

            Spinner sp = (Spinner)findViewById(R.id.spinner);
            String sp_name = sp.getSelectedItem().toString();

            EditText et_name = (EditText)findViewById(R.id.information);
            String info_name = et_name.getText().toString();

            EditText op = (EditText)findViewById(R.id.optional);
            String op_name = op.getText().toString();

            /*
            StringBuffer sqlStr = new StringBuffer();
            sqlStr.append("\n INSERT INTO BOARD VALUE('"+sp_name+"','"+info_name+"','"+op_name+"');");
            SQLiteDatabase wd = db.getWritableDatabase();
            wd.execSQL(sqlStr.toString());
            */

            db.getWritableDatabase();


            //send시 intent 전환 및 토스트
            Toast.makeText(this, "transmission completed", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void insertImageView(String filePath){
        if(!filePath.equals("")){
            File file = new File(filePath);
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inJustDecodeBounds=true;
            try{
                InputStream in = new FileInputStream(filePath);
                BitmapFactory.decodeStream(in, null,options);
                in.close();
                in=null;
            }catch (Exception e){
                e.printStackTrace();
            }
            final int width = options.outWidth;
            int inSampleSize = 1;

            BitmapFactory.Options imgOptions=new BitmapFactory.Options();
            imgOptions.inSampleSize=inSampleSize;
            Bitmap bitmap = BitmapFactory.decodeFile(filePath, imgOptions);

            screen.setImageBitmap(bitmap);
        }
    }

    private String getFilePathFromDocumentUri(Context context, Uri uri){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            String docId= DocumentsContract.getDocumentId(uri);
            String[] split=docId.split(":");
            String type = split[0];
            Uri contentUri = null;
            if("image".equals(type)){
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            }
            String selection=MediaStore.Images.Media._ID+"=?";
            String[] selectionArg = new String[]{split[1]};

            String column="_data";
            String[] projection = {column};

            Cursor cursor = context.getContentResolver().query(contentUri, projection, selection, selectionArg, null);
            String filePath = null;
            if(cursor != null && cursor.moveToFirst()){
                int column_index = cursor.getColumnIndexOrThrow(column);
                filePath=cursor.getString(column_index);
            }
            cursor.close();
            return filePath;
        }else{
            return null;
        }
    }

    private String getFilePathFromUriSegment(Uri uri){
        String selection=MediaStore.Images.Media._ID+"=?";
        String[] selectionArgs = new String[]{uri.getLastPathSegment()};

        String column="_data";
        String[] projection = {column};

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection,selectionArgs, null);
        String filePath=null;
        if(cursor!=null && cursor.moveToFirst()){
            int column_index=cursor.getColumnIndexOrThrow(column);
            filePath=cursor.getString(column_index);
        }
        cursor.close();
        return filePath;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

            }else{
                showToast("no permission");
            }
        }else if(requestCode==200 && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

            }else{
                showToast("no permission");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==10 && resultCode==RESULT_OK){
            if(filePath != null){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                try{
                    InputStream in = new FileInputStream(filePath);
                    BitmapFactory.decodeStream(in, null, options);
                    in.close();
                    in = null;
                }catch (Exception e){
                    e.printStackTrace();
                }

                final int height = options.outHeight;
                final int width = options.outWidth;
                int inSampleSize = 1;

                BitmapFactory.Options imgOptions = new BitmapFactory.Options();
                imgOptions.inSampleSize = inSampleSize;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath(), imgOptions);

                screen.setImageBitmap(bitmap);
            }
        }else if(requestCode==20 && resultCode==RESULT_OK){
            if(filePath!=null){
                VideoView videoView = new VideoView(this);
                videoView.setMediaController(new MediaController(this));
                Uri videoUri = Uri.parse(filePath.getAbsolutePath());

                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                Bitmap bmp = null;

                retriever.setDataSource(filePath.getAbsolutePath());
                bmp=retriever.getFrameAtTime();
                int videoHeight = bmp.getHeight();
                int videoWidth = bmp.getWidth();

                videoView.setVideoURI(videoUri);
                screen.setImageBitmap(bmp);
                videoView.start();
            }
        }else if(requestCode==40 && resultCode==RESULT_OK){
            String[] projection={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(data.getData(),projection,null,null,null);
            cursor.moveToFirst();
            String filePath=cursor.getString(0);
            insertImageView(filePath);

        }else if(requestCode==30 && resultCode==RESULT_OK && Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            if(data.getClipData()!=null){
                ClipData clipData = data.getClipData();
                for(int i=0; i<clipData.getItemCount(); i++){
                    ClipData.Item item=clipData.getItemAt(i);
                    Uri uri = item.getUri();
                    if("com.android.provider.media.documents".equals(uri.getAuthority())&&Build.VERSION.SDK_INT>=19){
                        String filePath=getFilePathFromDocumentUri(this,uri);
                        if(filePath!=null){
                            insertImageView(filePath);
                        }
                    }else if("external".equals(uri.getPathSegments().get(0))){
                        String filePath=getFilePathFromUriSegment(uri);
                        if(filePath!=null){
                            insertImageView(filePath);
                        }
                    }
                }
            }else{
                Uri uri = data.getData();
                String filePath=getFilePathFromDocumentUri(this,uri);
                if(filePath != null){
                    insertImageView(filePath);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("filePath", filePath.getAbsolutePath());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            String path = savedInstanceState.getString("filePath");
            if (path != null) {
                filePath = new File(path);
            }
        }
    }


}
