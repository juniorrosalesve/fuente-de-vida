package com.conceptodigital.fuentedevida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.conceptodigital.fuentedevida.helpers.SessionManager;
import com.conceptodigital.fuentedevida.helpers.Url;
import com.conceptodigital.fuentedevida.motorizado.AgregarPago;
import com.conceptodigital.fuentedevida.motorizado.MotoReporte;

public class MotorActivity extends AppCompatActivity {

    public String TAG   =   "MotorActivity";

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor);

        sessionManager  =   new SessionManager(this);

        Button btnConsultar =   findViewById(R.id.btnConsultar);
        Button btnPagar     =   findViewById(R.id.btnPagar);

        Button btnLogout    =   findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setLogin(false,
                        null, null, null, null, null);
                Intent i    =   new Intent(MotorActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i    =   new Intent(MotorActivity.this, MotoReporte.class);
                startActivity(i);
            }
        });
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i    =   new Intent(MotorActivity.this, AgregarPago.class);
                startActivity(i);
            }
        });
    }
}