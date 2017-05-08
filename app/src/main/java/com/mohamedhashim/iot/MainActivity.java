package com.mohamedhashim.iot;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.skydoves.colorpickerview.ColorPickerView;

public class MainActivity extends AppCompatActivity implements ValueEventListener {

    //    private EditText HeadingInput;
//    private Button submit;
    public DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    ImageView searchFlag;
    TextView toolbartxt;

    TextView HeadingText;
    private ColorPickerView colorPickerView;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference mHeadingRefernce = mRootReference.child("color");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        drawerToggle = setupDrawerToggle();
        View headerLayout = nvDrawer.inflateHeaderView(R.layout.nav_header);
        mDrawer.addDrawerListener(drawerToggle);
        setSupportActionBar(toolbar);
        setupDrawerContent(nvDrawer);
        searchFlag = (ImageView) findViewById(R.id.back_btn);
        toolbartxt = (TextView) findViewById(R.id.toolbartxt);
        toolbartxt.setText("IOT");
        searchFlag.setImageResource(R.drawable.menue);
        searchFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(GravityCompat.START);

            }
        });


//        HeadingInput =(EditText) findViewById(R.id.input);
//        submit= (Button) findViewById(R.id.submit);
        HeadingText = (TextView) findViewById(R.id.heading);
        colorPickerView = (ColorPickerView) findViewById(R.id.colorPickerView);

        colorPickerView.setColorListener(new ColorPickerView.ColorListener() {
            @Override
            public void onColorSelected(int color) {
                String HexColor = colorPickerView.getColorHtml();
                Log.d("colors", HexColor);
                mHeadingRefernce.setValue("#" + HexColor);
            }
        });

    }

//    public void submitHeading(View view) {
//        String heading = HeadingInput.getText().toString();
//        mHeadingRefernce.setValue(heading);
//        HeadingInput.setText("");
//    }


    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getValue(String.class) != null) {
            String key = dataSnapshot.getKey();

            if (key.equals("color")) {
                String heading = dataSnapshot.getValue(String.class);
                HeadingText.setText(heading);
                HeadingText.setTextColor(Color.parseColor(heading));
            }
//            else if (key.equals("fontcolor")) {
//
//                String color = dataSnapshot.getValue(String.class);
//
//                if (color.equals("red")) {
//                    HeadingText.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
//                }else if(color.equals("blue"))
//                    HeadingText.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
//            }
        }

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isConnected())
            mHeadingRefernce.addValueEventListener(this);
        else
            Toast.makeText(getApplicationContext(), "Check the internet connection", Toast.LENGTH_LONG).show();
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }


    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                Intent i2 = new Intent(this, MainActivity.class);
                startActivity(i2);
                break;
            case R.id.nav_second_fragment:
                Intent i = new Intent(this, AboutUsActivity.class);
                startActivity(i);
                break;
            case R.id.nav_third_fragment:
                Intent logout = new Intent(MainActivity.this, MainActivity.class);
                logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logout);
                finish();
                moveTaskToBack(true);
                break;
        }
//        menuItem.setChecked(true);
        mDrawer.closeDrawers();
    }
}
