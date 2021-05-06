package com.example.newcookbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Upload extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String myKey = "Name";
    private SharedPreferences pref;
    private DatabaseReference ref;
    private String NAME_USER;

    private TextView nameFood; //שם
    private TextView preparationFood; //אופן ההכנה
    private TextView groceryFood; // מצרכים
    private ImageView picFood; // תמונה
    private static String Url; //לינק לתמונה

    private DatabaseReference refToSave;
    private String TypeOfFood;

    //image upload
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage;
    private Button mButtonUploadImage;
    private TextView mTextViewShowUploads;
    private EditText mEditTextFileName;

    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseReference;
    private String keyPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        NAME_USER = getIntent().getStringExtra("Name");
        TypeOfFood = "Baking";
        refToSave = FirebaseDatabase.getInstance().getReference("Foods");

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.food, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        mButtonChooseImage = findViewById(R.id.btnChoose);
        picFood = findViewById(R.id.uploadImage);
        nameFood = findViewById(R.id.uploadName);
        preparationFood = findViewById(R.id.uploadPreparation);
        groceryFood = findViewById(R.id.uploadGrocery);

        // mButtonUploadImage=findViewById(R.id.btnUpload);
        //mProgressBar=findViewById(R.id.pro)

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");


        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("Image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(picFood);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TypeOfFood = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(),TypeOfFood,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void saveAndUpload(View view) {

        refToSave.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long child;
                Url = "";

                if (nameFood.getText().toString().equals("")) {
                    Toast.makeText(Upload.this, "נא לא להשאיר את השם של המתכון ריק.", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (long i = 0; i < dataSnapshot.child(NAME_USER).child(TypeOfFood).getChildrenCount(); i++) {
                    if (dataSnapshot.child(NAME_USER).child(TypeOfFood).child(i + "").child("name").getValue().equals(nameFood.getText().toString())) {

                        Toast.makeText(Upload.this, "נא להחליף שם של מתכון.", Toast.LENGTH_SHORT).show();
                        return;

                    }

                }
                child = dataSnapshot.child(NAME_USER).child(TypeOfFood).getChildrenCount();
                DataOfFood iof = new DataOfFood(nameFood.getText().toString(),
                        preparationFood.getText().toString(), groceryFood.getText().toString(), Url);


                //refToSave.child(NAME_USER).child(TypeOfFood).child(child + "").setValue(iof);

                //preparationFood.setText("");
                //groceryFood.setText("");


                //Toast.makeText(Upload.this, "success", Toast.LENGTH_SHORT).show();

                putImage(refToSave, NAME_USER, TypeOfFood, child, iof);

                onBackPressed();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Upload.this, "failed", Toast.LENGTH_SHORT).show();

            }


        });
    }


    public void putImage(DatabaseReference refToSave, String name, String type, long child,  DataOfFood iof) {

        if (mImageUri != null) {

            StorageReference fileReference = mStorageRef.child(this.NAME_USER).child(TypeOfFood).child(nameFood.getText().toString()
                    + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            iof.setUrl(uri.toString());
                            refToSave.child(name).child(type).child(child + "").setValue(iof);
                            Toast.makeText(Upload.this, "image upload", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Upload.this, "fail to upload image", Toast.LENGTH_LONG).show();
                    refToSave.child(name).child(type).child(child + "").setValue(iof);

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
        else{
            Toast.makeText(Upload.this, "not have a picture", Toast.LENGTH_LONG).show();
            refToSave.child(name).child(type).child(child + "").setValue(iof);
        }


    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    public void back(View view) {
        onBackPressed();

    }


}