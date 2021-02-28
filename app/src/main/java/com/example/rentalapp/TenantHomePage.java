package com.example.rentalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class TenantHomePage extends AppCompatActivity {
    Toolbar toolbar;
    public String lang = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_home_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        NavController navController = Navigation.findNavController(this,  R.id.fragment2);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        toolbar = findViewById(R.id.tenant_home_page_toolbar);
        setSupportActionBar(toolbar);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_bar_EN:
                translate("en");
                lang = "en";
                recreate();
                break;
            case R.id.action_bar_FR:
                translate("fr");
                lang = "fr";
                recreate();
                break;
            case R.id.action_bar_About_US:

                break;
            case R.id.action_bar_About_Signout:
                FirebaseAuth.getInstance().signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}