package com.example.tyjk.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by tyjk on 2017/10/5.
 */
public class FragmentAdapter extends FragmentPagerAdapter{

    private Fragment[] fragmentArray;
    public FragmentAdapter(FragmentManager fm,Fragment[] fragmentArray){
        super(fm);
        if(fragmentArray==null){
            this.fragmentArray=new Fragment[]{};
        }else {
            this.fragmentArray=fragmentArray;
        }

    }
    @Override
    public Fragment getItem(int position) {

        return fragmentArray[position];
    }

    @Override
    public int getCount() {
        return fragmentArray.length;
    }
}
