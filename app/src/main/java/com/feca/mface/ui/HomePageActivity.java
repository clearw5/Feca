package com.feca.mface.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.feca.mface.R;
import com.feca.mface.ui.me_page.MeActivity;
import com.feca.mface.util.BottomNavigationViewHelper;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView moment_list;
    private List<Moment> mlist;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        moment_list = (RecyclerView) findViewById(R.id.moment_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        initDate();
        adapter = new RecyclerViewAdapter(mlist, HomePageActivity.this);
        moment_list.setHasFixedSize(true);
        moment_list.setLayoutManager(llm);
        moment_list.setAdapter(adapter);


        BottomNavigationView bnv_001 = (BottomNavigationView) findViewById(R.id.bnv_001);
        BottomNavigationViewHelper.disableShiftMode(bnv_001);
        //为底部导航设置条目选中监听
        bnv_001.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:

                        break;
                    case R.id.search:

                        break;
                    case R.id.edit:
                        startActivity(new Intent(getApplicationContext(), MainActivity_.class));
                        break;
                    case R.id.shopping_cart:
                        break;
                    case R.id.me:
                        startActivity(new Intent(getApplicationContext(), MeActivity.class));
                        break;
                }

                return true;
            }
        });

        bnv_001.getMenu().getItem(0).setChecked(true);



    }

    private void initDate() {
        mlist =new ArrayList<>();
        //添加新闻
        mlist.add(new Moment(getString(R.string.moment_one_title),getString(R.string.news_one_content),R.mipmap.img_4161));
        mlist.add(new Moment(getString(R.string.moment_two_title),getString(R.string.news_two_content),R.mipmap.img_4162));
        mlist.add(new Moment(getString(R.string.moment_three_title),getString(R.string.news_three_content),R.mipmap.img_4163));
        mlist.add(new Moment(getString(R.string.moment_four_title),getString(R.string.news_four_content),R.mipmap.img_4164));
    }
}
