package com.mokshjn.firebasesample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PHONE = 100;
    DatabaseReference database;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance().getReference().getRoot();

        final Drawable tick = getDrawable(R.drawable.ic_tick);

        final Drawable cross = getDrawable(R.drawable.ic_cross);

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_CODE_PHONE);
        }
        final EditText pin = (EditText) findViewById(R.id.pin);
        final ImageView status = (ImageView) findViewById(R.id.image_status);
        pin.requestFocus();
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if(s.toString().length() == 4) {
                    pin.setEnabled(false);
                    database.child("6505551212").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue().toString().equals(s.toString())) {
                                Toast.makeText(MainActivity.this, "Transaction Done!", Toast.LENGTH_SHORT).show();
                                status.setImageDrawable(tick);
                            } else {
                                Toast.makeText(MainActivity.this, "Transaction Failed!", Toast.LENGTH_SHORT).show();
                                pin.setEnabled(true);
                                pin.requestFocus();
                                showInput();
                                status.setImageDrawable(cross);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }
        });
    }

    @Override
    public void onEnterAnimationComplete() {
        showInput();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_PHONE:
                if(!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    finish();
                }
        }
    }

    private void showInput() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Method showSoftInputUnchecked = null;
        try {
            showSoftInputUnchecked =InputMethodManager.class.getMethod(
                    "showSoftInputUnchecked", int.class, ResultReceiver.class);
            showSoftInputUnchecked.setAccessible(true);
            showSoftInputUnchecked.invoke(im, 0, null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
