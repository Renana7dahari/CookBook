package com.example.newcookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuLink extends AppCompatActivity {

    private Button meatBtn;
    private Button milkBtn;
    private Button bakeBtn;
    private String NAME_USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_link);

        NAME_USER = getIntent().getStringExtra("name");

        meatBtn = findViewById(R.id.meatbtn);
        milkBtn = findViewById(R.id.milkbtn);
        bakeBtn = findViewById(R.id.bakingbtn);

        meatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRecipe("Meat and Fish");

            }
        });
        milkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRecipe("Milk");

            }
        });
        bakeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRecipe("Bake");

            }
        });

    }
    public void goToRecipe(String recipe) {
        Intent intent = new Intent(this, RecipesLink.class);
        intent.putExtra("typeFood", recipe);
        intent.putExtra("name", NAME_USER);
        startActivity(intent);
    }

    public void Back(View view) { onBackPressed();
    }
}