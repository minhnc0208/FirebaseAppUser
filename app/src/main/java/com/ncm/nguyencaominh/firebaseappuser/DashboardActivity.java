package com.ncm.nguyencaominh.firebaseappuser;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {
    //    Toolbar toolbar ;
    FirebaseAuth firebaseAuth;

    // views

//    TextView mProfileTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        // init

        firebaseAuth = FirebaseAuth.getInstance();

        // bottom navigation
        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        // home fragment transaction (default, on start)
        HomeFragment fragment1 = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, fragment1, "");
        ft1.commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    // handle item clicks

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            HomeFragment fragment1 = new HomeFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content, fragment1, "");
                            ft1.commit();
                            // home fragment transaction
                            return true;
                        case R.id.nav_profile:
                            ProfileFragment fragment2 = new ProfileFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content, fragment2, "");
                            ft2.commit();
                            // profile fragment transaction
                            return true;
                        case R.id.nav_users:
                            UsersFragment fragment3 = new UsersFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content, fragment3, "");
                            ft3.commit();
                            // users fragment transaction
                            return true;
                    }
                    return false;
                }
            };

    private void checkUserStatus() {

        // get current user

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {

            // user is singed in stay here
            // set email of logged in user

            //mProfileTv.setText(user.getEmail());


        } else {

            // user not singed in, go to Main Activity
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            finish();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {

        // check on start of app
        checkUserStatus();
        super.onStart();
    }

    // infalte options menu


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // infalting menu

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    // handle menu item click


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // get item id

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            firebaseAuth.signOut();
            checkUserStatus();
        }
        return super.onOptionsItemSelected(item);
    }
}
