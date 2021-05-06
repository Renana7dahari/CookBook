package com.example.newcookbook;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UploadLink extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    private static  final int PICK_IMAGE_REQUEST = 1;
    private Button mButtonChooseImage;
    private Uri mImageUri;
    private TextView nameFood; //שם
    private TextView linkFood; //URL
    private ImageView picFood; // תמונה
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference refToSave;
    private String NAME_USER;
    private String keyPic;
    private static String Url;
    private String TypeOfFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_link);

        NAME_USER = getIntent().getStringExtra("Name");

        Spinner spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.food, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        mButtonChooseImage =findViewById(R.id.btnChooseLink);
        nameFood = findViewById(R.id.txtLinkName);
        linkFood=findViewById(R.id.txtTheLink);
        picFood=findViewById(R.id.uploadImageLink);
        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        refToSave = FirebaseDatabase.getInstance().getReference("Foods");


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
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data!= null && data.getData()!=null){
            mImageUri = data.getData();
            Picasso.with(this).load(mImageUri).into(picFood);

        }
    }





    public void back(View view) {
        onBackPressed();

    }

    public void saveAndUpload(View view) {

        refToSave.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long child;
                Url = "";

                if (nameFood.getText().toString().equals("")) {
                    Toast.makeText(UploadLink.this, "נא לא להשאיר את השם של המתכון ריק.", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (long i = 0; i < dataSnapshot.child(NAME_USER).child("Link").child(TypeOfFood).getChildrenCount(); i++) {
                    if (dataSnapshot.child(NAME_USER).child("Link").child(TypeOfFood).child(i + "").child("name").getValue().equals(nameFood.getText().toString())) {

                        Toast.makeText(UploadLink.this, "נא להחליף שם של מתכון.", Toast.LENGTH_SHORT).show();
                        return;

                    }

                }
                child = dataSnapshot.child(NAME_USER).child("Link").child(TypeOfFood).getChildrenCount();
                DataOfFood iof = new DataOfFood(nameFood.getText().toString(),linkFood.getText().toString(), Url);

                putImage(refToSave, NAME_USER, child, iof);

                onBackPressed();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UploadLink.this, "failed", Toast.LENGTH_SHORT).show();

            }


        });
    }

    private void putImage(DatabaseReference refToSave, String name, long child, DataOfFood iof) {
        if (mImageUri != null) {

            StorageReference fileReference = mStorageRef.child(this.NAME_USER).child("Link").child(TypeOfFood).child(nameFood.getText().toString()
                    + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            iof.setUrl(uri.toString());
                            refToSave.child(name).child("Link").child(TypeOfFood).child(child + "").setValue(iof);
                            Toast.makeText(UploadLink.this, "image upload", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadLink.this, "fail to upload image", Toast.LENGTH_LONG).show();
                    refToSave.child(name).child("Link").child(TypeOfFood).child(child + "").setValue(iof);

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                }
            });
        }
        else{
            Toast.makeText(UploadLink.this, "not have a picture", Toast.LENGTH_LONG).show();
            refToSave.child(name).child("Link").child(TypeOfFood).child(child + "").setValue(iof);
        }

    }


    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TypeOfFood = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
