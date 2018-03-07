package com.example.tyjk.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tyjk.adapter.FragmentAdapter;
import com.example.tyjk.fragment.FilmFragment;
import com.example.tyjk.fragment.MineFragment;
import com.example.tyjk.fragment.MusicFragment;
import com.example.tyjk.fragment.NewsFragment;
import com.example.tyjk.fragment.VideoFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends FragmentActivity {

    @Bind(R.id.viewpager_navigation)
    ViewPager viewpagerNavigation;
    @Bind(R.id.linearLayout_tabTitle)
    LinearLayout linearLayoutTabTitle;
    @Bind(R.id.linearLayout_tabIndication)
    LinearLayout linearLayoutTabIndication;

    private TextView[] tabTitle = null;
    private TextView[] tabIndication = null;
    private Fragment[] fragmentArray = null;

    private Fragment fragment1, fragment2, fragment3, fragment4, fragment5;
    private FragmentAdapter adapter;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fragment1 = new MusicFragment();
        fragment2 = new FilmFragment();
        fragment3 = new NewsFragment();
        fragment4 = new VideoFragment();
        fragment5 = new MineFragment();
        fragmentArray = new Fragment[]{fragment1, fragment2, fragment3, fragment4, fragment5};
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragmentArray);

        viewpagerNavigation.setAdapter(adapter);
        viewpagerNavigation.setOffscreenPageLimit(5);
        viewpagerNavigation.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 5; i++) {
                    tabIndication[i].setBackgroundColor(Color.parseColor("#ebebeb"));
                    tabTitle[i].setTextColor(Color.parseColor("#808080"));
                    tabTitle[i].setEnabled(true);
                }
                tabIndication[position].setBackgroundColor(Color.parseColor("#18b4ed"));
                tabTitle[position].setTextColor(Color.parseColor("#18b4ed"));
                tabTitle[position].setEnabled(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initTab();

    }

    private void initTab() {
        tabTitle = new TextView[5];
        tabIndication = new TextView[5];
        for (int i = 0; i < 5; i++) {
            TextView textView1 = (TextView) linearLayoutTabIndication
                    .getChildAt(i);
            tabIndication[i] = textView1;

            tabIndication[i].setBackgroundColor(Color.parseColor("#ebebeb"));

            TextView textView = (TextView) linearLayoutTabTitle.getChildAt(i);
            tabTitle[i] = textView;
            tabTitle[i].setTag(i);

            tabTitle[i].setTextColor(Color.parseColor("#808080"));
            tabTitle[i].setEnabled(true);
            tabTitle[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    viewpagerNavigation.setCurrentItem((Integer) v.getTag());

                    tabIndication[(Integer) v.getTag()]
                            .setBackgroundColor(Color.parseColor("#18b4ed"));
                }
            });

        }
        tabIndication[0].setBackgroundColor(Color.parseColor("#18b4ed"));
        tabTitle[0].setTextColor(Color.parseColor("#18b4ed"));
        tabTitle[0].setEnabled(false);
    }
}
