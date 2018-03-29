package com.mokshjn.firebasesample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by moksh on 20/5/17.
 */

public class CallStateListener extends BroadcastReceiver {
    DatabaseReference database;

    @Override
    public void onReceive(Context context, Intent intent) {
        database = FirebaseDatabase.getInstance().getReference();
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            checkDatabase(context, intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
        }
    }

    private void checkDatabase(final Context context, final String incomingNumber) {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(incomingNumber)){
                    Toast.makeText(context, incomingNumber + ": Found on database", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Not found on database!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        database.addListenerForSingleValueEvent(listener);
    }
}
