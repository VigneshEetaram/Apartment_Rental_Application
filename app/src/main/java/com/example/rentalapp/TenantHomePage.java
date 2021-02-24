package com.example.rentalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class TenantHomePage extends AppCompatActivity {

    public String lang = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_home_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        NavController navController = Navigation.findNavController(this,  R.id.fragment2);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        BottomNavigationView bottomNavigationView2 = (BottomNavigationView) findViewById(R.id.bottomNavigationView3);
        int size = bottomNavigationView2.getMenu().size();
        for (int i = 0; i < size; i++) {
            bottomNavigationView2.getMenu().getItem(i).setCheckable(false);
        }
        bottomNavigationView2.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.english:
                        Toast.makeText(TenantHomePage.this, "English", Toast.LENGTH_SHORT).show();
                        translate("en");
                        lang = "en";
                        recreate();
                        break;
                    case R.id.french:
                        Toast.makeText(TenantHomePage.this, "French", Toast.LENGTH_SHORT).show();
                        translate("fr");
                        lang = "fr";
                        recreate();
                        break;
                }
                return true;
            }
        });

    }


    private void translate(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang",lang);
        editor.apply();

    }

    private void loadlocale(){

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        translate(language);
    }

    public String language(){
        String langs = lang;
        return langs;
    }
}