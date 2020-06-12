package com.delevery.metyassaradeliveryboy.ui.LoginActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.delevery.metyassaradeliveryboy.R;
import com.delevery.metyassaradeliveryboy.model.DeliveryBoyModel;
import com.delevery.metyassaradeliveryboy.service.Token;
import com.delevery.metyassaradeliveryboy.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {
    private TextView signUp;
    private EditText email, password;
    private ProgressDialog dialog;
    private TextView title;
    private Button login_Button;

    //2- override On Create view to inflate LoginFragment View


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        email =findViewById(R.id.email_field_login);
        password = findViewById(R.id.password_field_login);
        title = findViewById(R.id.titlelogin);
        login_Button =findViewById(R.id.login);


        //intial dialog
        dialog = new ProgressDialog(this);
        dialog.setMessage("plese wait...");
        dialog.setTitle("Login");
        dialog.setCancelable(false);

        //login button
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
        String email_text = email.getText().toString();
        String password_text = password.getText().toString();

        //check if empty or no
        if (email_text.isEmpty()) {
            Toast.makeText(this, "please enter email", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }
        if((!(email_text.substring(email_text.length()-14)).equals("metyassara.com"))){
            Toast.makeText(this, "please correct mail", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }
        if (password_text.isEmpty()) {
            Toast.makeText(this, "please enter password", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return;
        }
        //her i make sure is not empty then i will make login auth
        dialog.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email_text, password_text).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String delevery_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    SearchUserInDataOrNo(delevery_id);
                } else {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void SearchUserInDataOrNo(final String id){
        FirebaseDatabase.getInstance().getReference().child("delivery boy").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(id)){
                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    dialog.dismiss();
                    title.setText("please enter your name and phone");
                    email.setText("");
                    email.setHint("name");
                    password.setText("");
                    password.setHint("phone");
                    password.setInputType(3);
                    login_Button.setText("finish");

                    login_Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.show();
                            uploadedate();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadedate() {

        String name=email.getText().toString();
        String phone=password.getText().toString();
        if(name.isEmpty()){
            Toast.makeText(this, "please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        if(phone.isEmpty()){
            Toast.makeText(this, "please enter your phone", Toast.LENGTH_SHORT).show();
            return;
        }
        String delevery_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        DeliveryBoyModel deliveryBoyModel=new DeliveryBoyModel(delevery_id,name,phone,"","of","no",refreshToken,0,0,0,50,10);
        FirebaseDatabase.getInstance().getReference().child("delivery boy").child(delevery_id).setValue(deliveryBoyModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    dialog.dismiss();
                }
            }
        });
    }

}

