package com.conceptodigital.fuentedevida;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.conceptodigital.fuentedevida.cobranza.CobranzaFragment;
import com.conceptodigital.fuentedevida.helpers.SessionManager;
import com.conceptodigital.fuentedevida.services.ItemServiceFragment;
import com.conceptodigital.fuentedevida.users.UsersFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager  =   new SessionManager(this);

        Button btnLogout    =   findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.setLogin(false,
                        null, null, null, null, null);
                Intent i    =   new Intent(HomeActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CobranzaFragment()).commit();
    }
    private BottomNavigationView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.item_cobranza)
                selectedFragment = new CobranzaFragment();
            else if (item.getItemId() == R.id.item_usuarios)
                selectedFragment = new UsersFragment();
            else if(item.getItemId() == R.id.item_servicio)
                selectedFragment = new ItemServiceFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };

}