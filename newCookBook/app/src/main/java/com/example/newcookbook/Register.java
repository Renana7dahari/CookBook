package com.example.newcookbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView Password;
    private TextView Email;
    private TextView Nick;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("NickNames");

    }

    public void exitReg(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void Save(final View view) {

        Email=findViewById(R.id.Email_Reg);
        Password=findViewById(R.id.Pass_Reg);
        Nick=findViewById(R.id.nick_Reg);

        if(Email.getText().toString() != "" && Nick.getText().toString() != "" && Password.getText().toString() != "") {


            checkIfUserExists(view);

            // ...
        }
        else{
            Toast.makeText(Register.this, "The Email or Nickname is Empty",
                    Toast.LENGTH_SHORT).show();
            Email.setText("");
            Nick.setText("");
            Password.setText("");

        }

    }
    //add nickname and email to firebase!
    private void saveEmailAndNickname(){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long child;
                child=dataSnapshot.getChildrenCount();

                DataOfUser iou = new DataOfUser(Email.getText().toString(),Nick.getText().toString());
                ref.child(child+"").setValue(iou);

                Email.setText("");
                Nick.setText("");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void checkIfUserExists(final View view){

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean flag=true;
                long child = dataSnapshot.getChildrenCount();


                for (int i = 0; i < child; i++) {
                    if (Nick.getText().toString().equals(dataSnapshot.child("" + i).child("name").getValue().toString())) {
                        Toast.makeText(Register.this, "The Nickname exists.\nplease change Nickname",
                                Toast.LENGTH_SHORT).show();
                        Nick.setText("");
                        flag=false;
                    }
                }
                if(flag==true)
                    connectToUserAndPass(view);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void connectToUserAndPass(final View view) {
        mAuth.createUserWithEmailAndPassword(Email.getText().toString(), Password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            //FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Register.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();

                            saveEmailAndNickname();
                            exitReg(view);

                        }

                    }

                });
    }

    public void back(View view) {
        onBackPressed();

    }
}
