package com.example.kimyoungbin.csuccop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Kim youngbin on 2017-08-18.
 */

public class CustomAdapter extends BaseAdapter {
    private ArrayList<String> m_List;
    public CustomAdapter(){
        m_List = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return m_List.size();
    }

    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        //채우기
        TextView text = null;
        CustomHolder holder = null;
        //
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //채우기
            convertView = inflater.inflate(R.layout.custom_item, parent, false);

            text = (TextView) convertView.findViewById(R.id.tvCategory);
            //
            holder = new CustomHolder();
            holder.m_TextView = text;
            convertView.setTag(holder);
            //
        }else{
            //
            holder = (CustomHolder) convertView.getTag();
            text = holder.m_TextView;
            //
        }
        text.setText(m_List.get(position));


        return null;
    }

    private class CustomHolder{
        TextView m_TextView;
    }

    public void add(String _msg){
        m_List.add(_msg);
    }

    public void remove(int _position){
        m_List.remove(_position);
    }
}
