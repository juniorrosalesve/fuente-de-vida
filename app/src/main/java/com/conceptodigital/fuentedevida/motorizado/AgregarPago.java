package com.conceptodigital.fuentedevida.motorizado;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.webkit.WebView;
import android.Manifest;
import android.widget.Toast;

import com.conceptodigital.fuentedevida.R;
import com.conceptodigital.fuentedevida.helpers.SessionManager;
import com.conceptodigital.fuentedevida.helpers.Url;

public class AgregarPago extends AppCompatActivity {

    private Url url     =   new Url();
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_pago);

        sessionManager  =   new SessionManager(this);

        loadWebView();
    }


    private void loadWebView() {
        String link  =   url.get + "/motorizado/agregar-pago/"+sessionManager.getUserToken();
        WebView myWebView = new WebView(this);
        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setSupportMultipleWindows(true);
        myWebView.loadUrl(link);
        setContentView(myWebView);
    }
}