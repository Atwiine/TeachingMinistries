package com.example.tushome.Preferences;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.tushome.R;
import com.example.tushome.Registrations.update;
import com.example.tushome.Urls.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class FacebookPage extends BottomSheetDialogFragment {

    SessionManager sessionManager;
    String url = "www.yitug.org";
//    https://www.facebook.com/Empower Youth in Technology
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        sessionManager = new SessionManager(getActivity());

        View v = inflater.inflate(R.layout.activity_facebook,
                container, false);


        WebView web_facebook = v.findViewById(R.id.web_facebook);
        web_facebook.setWebViewClient(new MyBrowser());
        web_facebook.getSettings().setLoadsImagesAutomatically(true);
        web_facebook.getSettings().setJavaScriptEnabled(true);
        web_facebook.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
web_facebook.loadUrl(url);

        return v;
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, request);
        }
    }
}