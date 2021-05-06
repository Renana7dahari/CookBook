package com.example.newcookbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class RecipesLink extends AppCompatActivity {

    private ArrayList<CardRecipes> mList;
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button buttonInsert;
    private Button buttonRemove;
    private EditText editTextInsert;
    private EditText editTextRemove;
    //private static int child;
    private String nameOfFood;
    private String NAME_USER;
    private String TYPE_FOOD;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private long child;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        NAME_USER = getIntent().getStringExtra("name");
        TYPE_FOOD = getIntent().getStringExtra("typeFood");
        nameOfFood = getIntent().getStringExtra("nameOfFood");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Foods");

        createList();

    }
    /* public void insertItem(int position) {
         mList.add(position, new CardRecipes(R.drawable.ic_launcher_background, "New Item At Position" + position, "This is Line 2"));
         mAdapter.notifyItemInserted(position);
     }*/
   /* public void removeItem(int position) {
        mList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }*/
    /*public void changeItem(int position, String text) {
        //mList.get(position).changeText1(text);
        mAdapter.notifyItemChanged(position);
    }*/
    public void createList() {
        mList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                child = dataSnapshot.child(NAME_USER).child("Link").child(TYPE_FOOD).getChildrenCount();
                for(long i = 0 ; i < child ; i++){

                    if(dataSnapshot.child(NAME_USER).child("Link").child(TYPE_FOOD).child(i+"").child("url").getValue().toString().equals("")) {
                        mList.add(new CardRecipes(
                                dataSnapshot.child(NAME_USER).child("Link").child(TYPE_FOOD).child(i + "").child("name").getValue().toString(),
                                dataSnapshot.child("url").getValue().toString()
                        ));
                    }
                    else{
                        mList.add(new CardRecipes(
                                dataSnapshot.child(NAME_USER).child("Link").child(TYPE_FOOD).child(i + "").child("name").getValue().toString(),
                                dataSnapshot.child(NAME_USER).child("Link").child(TYPE_FOOD).child(i + "").child("url").getValue().toString()
                        ));
                    }

                }

                Toast.makeText(RecipesLink.this, "successfully", Toast.LENGTH_SHORT).show();

                buildRecyclerView();
                //setButtons();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(RecipesLink.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void buildRecyclerView() {

        mRecyclerView = findViewById(R.id.RecycleView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Adapter(mList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                movToCard(position);
            }

        });
    }
    public void movToCard(int position){
        Intent intent = new Intent(this,Card.class);
        intent.putExtra("typeFood","Link");
        intent.putExtra("typeFood2",TYPE_FOOD);
        intent.putExtra("name",NAME_USER);
        nameOfFood = mList.get(position).getText1();
        intent.putExtra("nameOfRecipe",nameOfFood);
        startActivity(intent);
    }
    public void setButtons() {
        /*buttonInsert = findViewById(R.id.button_insert);
        buttonRemove = findViewById(R.id.button_remove);
        editTextInsert = findViewById(R.id.edittext_insert);
        editTextRemove = findViewById(R.id.edittext_remove);*/
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextInsert.getText().toString());
                //insertItem(position);
            }
        });
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(editTextRemove.getText().toString());
                //removeItem(position);
            }
        });
    }

    public void back(View view) {
        onBackPressed();
    }
}