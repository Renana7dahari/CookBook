package com.example.newcookbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {



    public String NickName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NickName = getIntent().getStringExtra("Name");
    }

    public void menu(View view) {
        Intent intent = new Intent(this,Menu.class);
        intent.putExtra("Name",NickName);
        startActivity(intent);
    }

    public void upl(View view) {
        Intent intent = new Intent(this,Upload.class);
        intent.putExtra("Name",NickName);
        startActivity(intent);
    }

    public void uplLink(View view) {
        Intent intent = new Intent(this,UploadLink.class);
        intent.putExtra("Name",NickName);
        startActivity(intent);
    }

    public void logout(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
