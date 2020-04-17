package com.hcutils.hclibrary.FragmentDialog;

import android.view.View;

import androidx.fragment.app.FragmentManager;

public class HCSetFragmentDialog extends HC_FragmentDilog {
     FragmentManager fragmentManager;
    int layoutid;
    int gravity;
    int with;
    int height;
    Getview getview;
    public interface Getview{
        void getview(View view);
    }

    public static HCSetFragmentDialog Getinstace(FragmentManager fragmentManager){
        HCSetFragmentDialog hcSetFragmentDialog=new HCSetFragmentDialog();
        hcSetFragmentDialog.setFragmentManager(fragmentManager);
        return hcSetFragmentDialog;
    }

    public  HCSetFragmentDialog setFragmentManager(FragmentManager fragmentManager){
        this.fragmentManager=fragmentManager;
        return this;
    }
    @Override
    public int getlayout() {
        return layoutid;
    }

    @Override
    public int getGravity() {
        return gravity;
    }

    @Override
    public int getWith() {
        return with;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public View bindview(View view) {
        if(getview!=null) {
            getview.getview(view);
        }
        return super.bindview(view);
    }

    public HCSetFragmentDialog setlayout(int layoutid){
        this.layoutid=layoutid;
        return this;
    }
    public HCSetFragmentDialog setgravity(int gravity){
        this.gravity=gravity;
        return this;
    }
    public HCSetFragmentDialog setwith(int with){
        this.with=with;
        return this;
    }
    public HCSetFragmentDialog setheight(int height){
        this.height=height;
        return this;
    }

    public HCSetFragmentDialog show(){
        show(fragmentManager,"123");
        return this;
    }
    public HCSetFragmentDialog getview(Getview getview){
        this.getview=getview;
        return this;

    }

}
