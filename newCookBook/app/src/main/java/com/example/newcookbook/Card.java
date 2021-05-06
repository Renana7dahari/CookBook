package com.example.newcookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Card extends AppCompatActivity {

    private TextView nameOfFood; //שם
    private TextView preparationOfFood; //אופן ההכנה
    private TextView groceryOfFood; // מצרכים
    private ImageView picOfFood; // תמונה


    private FirebaseDatabase database;
    private DatabaseReference myRef;
    long child;

    private String nameFood;
    private String TypeFood;
    private String userName;
    private TextView Link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        picOfFood = findViewById(R.id.cardImage);
        nameOfFood = findViewById(R.id.cardNameOfRecipes);
        preparationOfFood = findViewById(R.id.cardOfenHachana);
        groceryOfFood = findViewById(R.id.cardMitzracim);
        Link = findViewById(R.id.infoTxtCredits);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Foods");

        userName = getIntent().getStringExtra("name");
        TypeFood = getIntent().getStringExtra("typeFood");
        nameFood = getIntent().getStringExtra("nameOfRecipe");


        if (TypeFood.equals("Link")) {

            TypeFood = getIntent().getStringExtra("typeFood2");


            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    child = dataSnapshot.child(userName).child("Link").child(TypeFood).getChildrenCount();
                    for (int i = 0; i < child; i++) {
                        if (dataSnapshot.child(userName).child("Link").child(TypeFood).child("" + i).child("name").getValue().toString().equals(nameFood)) {

                            nameOfFood.setText(nameFood);
                            groceryOfFood.setText("");
                            preparationOfFood.setText("");
                            Link.setText(dataSnapshot.child(userName).child("Link").child(TypeFood).child("" + i).child("link").getValue().toString());

                            String url;
                            if (dataSnapshot.child(userName).child("Link").child(TypeFood).child(i + "").child("url").getValue().toString().equals("")) {
                                url = dataSnapshot.child("url").getValue().toString();
                            } else {
                                url = dataSnapshot.child(userName).child("Link").child(TypeFood).child(i + "").child("url").getValue().toString();
                            }
                            Thread thread = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        //Your code goes here
                                        picOfFood.setImageBitmap(getBitmapFromURL(url));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            thread.start();

                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value

                }
            });

        }
        else {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    child = dataSnapshot.child(userName).child(TypeFood).getChildrenCount();
                    for (int i = 0; i < child; i++) {
                        if (dataSnapshot.child(userName).child(TypeFood).child("" + i).child("name").getValue().toString().equals(nameFood)) {

                            nameOfFood.setText(nameFood);
                            groceryOfFood.setText(dataSnapshot.child(userName).child(TypeFood).child("" + i).child("grocery").getValue().toString());
                            preparationOfFood.setText(dataSnapshot.child(userName).child(TypeFood).child("" + i).child("preparation").getValue().toString());
                            Link.setText("");

                            String url;
                            if (dataSnapshot.child(userName).child(TypeFood).child(i + "").child("url").getValue().toString().equals("")) {
                                url = dataSnapshot.child("url").getValue().toString();
                            } else {
                                url = dataSnapshot.child(userName).child(TypeFood).child(i + "").child("url").getValue().toString();
                            }
                            Thread thread = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    try {
                                        //Your code goes here
                                        picOfFood.setImageBitmap(getBitmapFromURL(url));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            thread.start();

                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value

                }
            });
        }
    }


    public static Bitmap getBitmapFromURL(String src) {

        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void back(View view) {
        onBackPressed();

    }
}