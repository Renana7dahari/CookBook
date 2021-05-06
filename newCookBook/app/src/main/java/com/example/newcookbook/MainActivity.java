package com.example.newcookbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static Context mContext = null;

    private FirebaseAuth mAuth;

    //private EditText data_user;
    private TextView email;
    private TextView password;

    private SharedPreferences.Editor editorEmail;
    public static SharedPreferences prefEmail;
    public static final String KeyEmail = "Email";

    private SharedPreferences.Editor editorName;
    public static SharedPreferences prefName;
    public static final String KeyName = "Name";

    private DatabaseReference ref;
    private long backPressedTime;
    private Toast backToast;


    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.EmailText);
        password = findViewById(R.id.PassText);

        email.setText("elior2031@gmail.com");
        password.setText("123456");

        ref = FirebaseDatabase.getInstance().getReference("NickNames");

        prefEmail = mContext.getSharedPreferences(KeyEmail, 0);
        editorEmail = prefEmail.edit();

        prefName = mContext.getSharedPreferences(KeyName, 0);
        editorName = prefEmail.edit();

        /***
         ***** check
         */

     /***   final TextView tv = findViewById(R.id.textView);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
                StorageReference fileReference1 = mStorageRef.child("elior2031").child("נאשף.jpg");
                fileReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Bitmap bmp = null;
                        try {
                            Utility.downloadImgFromUrl(uri.toString(), new DownloadCallBack() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void done(Bitmap bitmap) {
                                    if (bitmap == null)
                                        return;
                                    Drawable d = new BitmapDrawable(getResources(), bitmap);
                                    tv.setBackground(d);
                                }
                            });
                            //bmp = BitmapFactory.decodeStream(blob);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });**/


    }

    public void Login(View view) {
        if (email.getText().toString().equals("") && email.getText().toString().equals(""))
            Toast.makeText(MainActivity.this, "The email and password is empty. please try again",
                    Toast.LENGTH_SHORT).show();


        else if (email.getText().toString().equals(""))
            Toast.makeText(MainActivity.this, "The email is empty. please try again",
                    Toast.LENGTH_SHORT).show();
        else if (password.getText().toString().equals(""))
            Toast.makeText(MainActivity.this, "The password is empty. please try again",
                    Toast.LENGTH_SHORT).show();

        else {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                // FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this, "Authentication success.",
                                        Toast.LENGTH_SHORT).show();

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        long child = dataSnapshot.getChildrenCount();
                                        for (int i = 0; i < child; i++) {
                                            if (email.getText().toString().equals(dataSnapshot.child("" + i).child("email").getValue().toString())) {

                                                // editorEmail.putString(KeyEmail, dataSnapshot.child("" + i).child("email").getValue().toString());
                                                // editorEmail.commit();

                                                // editorName.putString(KeyName,dataSnapshot.child("" +i).child("name").getValue().toString());
                                                //editorName.commit();


                                                Intent intent = new Intent(MainActivity.this, Home.class);
                                                intent.putExtra("Name", dataSnapshot.child("" + i).child("name").getValue().toString());
                                                startActivity(intent);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }

                                });

                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void register(View view) {

        // Intent intent = new Intent(this , Login.class);
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        //............
        if (backPressedTime + 2000 > System.currentTimeMillis()) {

            finish();
            finishAffinity();
            System.exit(0);

        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();

    }
}
