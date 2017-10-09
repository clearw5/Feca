package com.feca.mface.ui.forum;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.feca.mface.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Bob on 2017/10/7.
 */
@EActivity(R.layout.activity_forumdetail)
public class DetailActivity extends AppCompatActivity {

    @ViewById(R.id.web_view)
    WebView mWebView;

    @ViewById(R.id.progress_bar)
    ProgressBar mProgressBar;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    @AfterViews
    void setupViews() {
        initUrl(getIntent().getExtras().getInt("number", 0));
        setupWebView();
        setupToolbar();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initUrl(int number) {
        String url = "";
        if (number == 0) {
            url = "https://post.smzdm.com/p/519254/";
        } else if (number == 1) {
            url = "https://post.smzdm.com/p/573704/";
        } else if (number == 2) {
            url = "https://post.smzdm.com/p/549579/";
        } else if (number == 3) {
            url = "https://www.baidu.com/link?url=42IOjfR37KdYTajQNIgE2gV58stfAyljH6a1jljCwhm3gMdFSPNTw8xiuktlYi4E&wd=&eqid=b2318877000332160000000659d883d4";
        }
        mWebView.loadUrl(url);
    }

    private void setupWebView() {
        WebSettings setting = mWebView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);
        setting.setBuiltInZoomControls(true);
        setting.setSupportZoom(true);
        setting.setDisplayZoomControls(false);
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
