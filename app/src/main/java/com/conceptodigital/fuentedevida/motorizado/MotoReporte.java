package com.conceptodigital.fuentedevida.motorizado;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.conceptodigital.fuentedevida.R;
import com.conceptodigital.fuentedevida.helpers.SessionManager;
import com.conceptodigital.fuentedevida.helpers.Url;

public class MotoReporte extends AppCompatActivity {

    private Url url     =   new Url();
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moto_reporte);

        sessionManager  =   new SessionManager(this);

        String link  =   url.get + "/motorizado/ver-pagos/"+sessionManager.getUserToken();
        WebView myWebView = new WebView(this);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setSupportMultipleWindows(true);
        myWebView.loadUrl(link);
        setContentView(myWebView);
    }
}