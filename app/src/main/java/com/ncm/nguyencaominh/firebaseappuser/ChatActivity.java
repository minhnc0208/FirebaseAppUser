package com.ncm.nguyencaominh.firebaseappuser;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class ChatActivity extends AppCompatActivity {

    // views from xml

    Toolbar toolbar;
    RecyclerView recyclerView;
    ImageView profileIv;
    TextView nameTv, userStatusTv;
    EditText messageEt;
    ImageButton sendBtn;

    // firebase auth
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference usersDbRef;

    String hisUid;
    String myUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // init views

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        recyclerView = findViewById(R.id.chat_recyclerView);
        profileIv = findViewById(R.id.profileIv);
        nameTv = findViewById(R.id.nameTv);
        userStatusTv = findViewById(R.id.userStatusTv);

        messageEt = findViewById(R.id.messageEt);
        sendBtn = findViewById(R.id.sendBtn);

        Intent intent = getIntent();

        hisUid = intent.getStringExtra("hisUid");

        // firebase auth instance

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();

        usersDbRef = firebaseDatabase.getReference("Users");

        // search users to get that user's info

        Query userQuery = usersDbRef.orderByChild("uid").equalTo(hisUid);

        // get user picture and name

        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // check until required info is received

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    // get data

                    String name = "" + ds.child("name").getValue();
                    String image = "" + ds.child("image").getValue();

                    // set data

                    nameTv.setText(name);

                    try {

                        // image recevied, set it to imageview in toolbbar

                        Picasso.get().load(image).placeholder(R.drawable.ic_default_img_white).into(profileIv);

                    }catch (Exception e){

                        // there is exception getting picture, set default picture

                        Picasso.get().load(R.drawable.ic_default_img_white).into(profileIv);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // click button to send message

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get text from edit text

                String message = messageEt.getText().toString().trim();

                // check if text is empty or not

                if (TextUtils.isEmpty(message)){

                    // text empty

                    Toast.makeText(ChatActivity.this, "Cannot send the empty message...", Toast.LENGTH_SHORT).show();

                }
                else{

                    // text not empty

                    sendMessage(message);
                }
            }
        });

    }

    private void sendMessage(String message) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>() ;
        hashMap.put("sender",myUid);
        hashMap.put("receiver",hisUid);
        hashMap.put("message",message);

        databaseReference.child("Chats").push().setValue(hashMap);

        // reset edit text after sending message

        messageEt.setText("");

    }


    private void checkUserStatus() {

        // get current user

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {

            // user is singed in stay here
            // set email of logged in user

            //mProfileTv.setText(user.getEmail());

            myUid = user.getUid(); // currently signed in user's uid


        } else {

            // user not singed in, go to Main Activity
            startActivity(new Intent(this, MainActivity.class));
            finish();

        }
    }

    @Override
    protected void onStart() {

        checkUserStatus();
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        // hide search view, as we don't need it here

        menu.findItem(R.id.action_search).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }
}
