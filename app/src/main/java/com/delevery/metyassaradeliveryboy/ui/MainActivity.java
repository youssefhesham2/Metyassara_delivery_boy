package com.delevery.metyassaradeliveryboy.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.delevery.metyassaradeliveryboy.R;
import com.delevery.metyassaradeliveryboy.service.MyFirebaseMessagingService;
import com.delevery.metyassaradeliveryboy.service.Token;
import com.delevery.metyassaradeliveryboy.ui.fragments.HomFragment;
import com.delevery.metyassaradeliveryboy.ui.fragments.MyOrdersFragment;
import com.delevery.metyassaradeliveryboy.ui.fragments.MyProfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    private TextView Cash;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UpdateToken();
        onpressnavigation();
    }

    private void onpressnavigation() {
        IntialSharedPreferences();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigatuon);
        ReplaceFragment(new HomFragment());
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                     @Override
                     public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                         switch (menuItem.getItemId()) {
                             case R.id.home:
                                 ReplaceFragment(new HomFragment());
                                 menuItem.setChecked(true);
                                 break;

                             case R.id.my_order:
                                 ReplaceFragment(new MyOrdersFragment());
                                 menuItem.setChecked(true);
                                 break;

                             case R.id.profile:
                                 ReplaceFragment(new MyProfilFragment());
                                 menuItem.setChecked(true);
                                 break;
                         }
                         return false;
                     }
                 }
                );
    }

    public void ReplaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fram, fragment);
        fragmentTransaction.commit();
    }
    private void IntialSharedPreferences() {
        Cash=findViewById(R.id.cash);
        String myid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("delivery boy").child(myid).child("myEarn")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Double cash=dataSnapshot.getValue(Double.class);
                        Cash.setText(cash+"");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void UpdateToken() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Token token = new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("delivery boy").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("token").setValue(refreshToken);
    }
}
