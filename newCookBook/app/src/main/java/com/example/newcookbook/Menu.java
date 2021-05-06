package com.example.newcookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    private Button meatBtn;
    private Button milkBtn;
    private Button bakeBtn;
    private Button linkBtn;
    private String NAME_USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        NAME_USER = getIntent().getStringExtra("Name");

        meatBtn = findViewById(R.id.meatbtn);
        milkBtn = findViewById(R.id.milkbtn);
        bakeBtn = findViewById(R.id.bakingbtn);
        linkBtn = findViewById(R.id.linkbtn);

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
        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRecipeLink("Links");
            }
        });

    }
    public void goToRecipe(String recipe) {
        Intent intent = new Intent(this, Recipes.class);
        intent.putExtra("typeFood",recipe);
        intent.putExtra("name",NAME_USER);
        startActivity(intent);
    }

    public void goToRecipeLink(String recipe) {
        Intent intent = new Intent(this, MenuLink.class);
        intent.putExtra("typeFood",recipe);
        intent.putExtra("name",NAME_USER);
        startActivity(intent);
    }


    public void Back(View view) {
        onBackPressed();
    }
}