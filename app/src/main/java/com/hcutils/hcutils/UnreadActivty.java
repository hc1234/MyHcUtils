package com.hcutils.hcutils;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hcutils.hclibrary.views.Unreadview;

public class UnreadActivty extends Activity{
    Unreadview unreadview;
    Boolean ishsow=true;
    Boolean move=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myunread_layout);
        unreadview= (Unreadview) findViewById(R.id.unread_view);
        TextView show= (TextView) findViewById(R.id.show_unreadd);
        final TextView chege_back= (TextView) findViewById(R.id.chage_back);
        TextView start_move= (TextView) findViewById(R.id.start_move);
        EditText edit= (EditText) findViewById(R.id.get_unread);
        unreadview.getUnreadCallback(new Unreadview.unreadcallbck() {
            @Override
            public void unreadmove(TextView v) {
                unreadview.setUnreadVisibility(View.GONE);
            }
        });
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ishsow) {
                    unreadview.setUnreadVisibility(View.GONE);
                    ishsow=false;
                }else {
                    unreadview.setUnreadVisibility(View.VISIBLE);
                    ishsow=true;
                }
            }
        });
        chege_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        start_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!move) {
                    move=true;
                    unreadview.setUnreadmove(true);
                }else{
                    move=false;
                    unreadview.setUnreadmove(false);
                }
            }
        });
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                unreadview.setUnreadcount(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
    }
}
