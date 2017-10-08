package com.feca.mface.ui.forum;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.feca.mface.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import butterknife.ButterKnife;

/**
 * Created by Bob on 2017/10/7.
 */
@EActivity(R.layout.activity_forumdetail)
public class DetailActivity extends AppCompatActivity {
    @ViewById(R.id.webView)
    WebView web_view;
    private int number = this.getIntent().getExtras().getInt("number",0);
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        initUrl(number);
//        web_view.loadUrl(url);
        web_view.loadUrl(url);
        web_view.setWebViewClient(new WebViewClient());
    }

    private void initUrl(int number) {
        if(number == 0){
            url = "https://post.smzdm.com/p/519254/";
        }
        else if(number == 1){
            url = "https://post.smzdm.com/p/573704/";
        }
        else if(number == 2){
            url = "https://post.smzdm.com/p/549579/";
        }
        else if(number == 3){
            url = "https://www.baidu.com/link?url=42IOjfR37KdYTajQNIgE2gV58stfAyljH6a1jljCwhm3gMdFSPNTw8xiuktlYi4E&wd=&eqid=b2318877000332160000000659d883d4";
        }
    }

    private void initView() {
        WebSettings setting = web_view.getSettings();
        setting.setJavaScriptEnabled(true);//支持Js
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);//缓存模式
        //是否支持画面缩放，默认不支持
        setting.setBuiltInZoomControls(true);
        setting.setSupportZoom(true);
        //是否显示缩放图标，默认显示
        setting.setDisplayZoomControls(false);
        //设置网页内容自适应屏幕大小
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (web_view.canGoBack()) {
                web_view.goBack();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
