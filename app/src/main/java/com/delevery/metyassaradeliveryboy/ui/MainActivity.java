package com.delevery.metyassaradeliveryboy.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.delevery.metyassaradeliveryboy.R;
import com.delevery.metyassaradeliveryboy.ui.fragments.HomFragment;
import com.delevery.metyassaradeliveryboy.ui.fragments.MyOrdersFragment;
import com.delevery.metyassaradeliveryboy.ui.fragments.MyProfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onpressnavigation();
    }

    private void onpressnavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigatuon);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                     @Override
                     public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                         switch (menuItem.getItemId()) {
                             case R.id.home:
                                 ReplaceFragment(new HomFragment());
                                 break;

                             case R.id.my_order:
                                 ReplaceFragment(new MyOrdersFragment());
                                 break;

                             case R.id.profile:
                                 ReplaceFragment(new MyProfilFragment());
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
}
