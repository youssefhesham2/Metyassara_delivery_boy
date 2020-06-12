package com.delevery.metyassaradeliveryboy.ui.OrderProcessActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.delevery.metyassaradeliveryboy.R;
import com.delevery.metyassaradeliveryboy.model.RequestsModel;
import com.delevery.metyassaradeliveryboy.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shuhart.stepview.StepView;
import com.williamww.silkysignature.views.SignaturePad;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class OrderProcessActivity extends AppCompatActivity {
    private int EndOrderHour, EndOrderMiute;
    private String endtime,client_id,client_phone;
    private StepView mStepView;
    private CircularProgressBar circularProgressBar;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String StartOderTime, my_id, status="";
    private TextView textminuteleft, statusview,describeOrder,min;
    private Button next,call_client;
    private ImageView imageView;
    private  Calendar calendar;
    private RequestsModel requestsModel,requestsModel2;
    private EditText OrderCost,DeliveryCoast;
    private Double Cash,deliverycost;
    SignaturePad signaturePad;
    private boolean coast=false,finish=false;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_process);
        IntialSharedPreferences();
        IntialViews();
        ItialStepView();
        IntialCircularBrogressBar();
        timer();
    }
    private void IntialSharedPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        client_id=preferences.getString("clirnt id","");

        editor.putString("in order", "in order");
        editor.commit();

        status=preferences.getString("status","");
        FirebaseDatabase.getInstance().getReference().child("in process").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(client_id)){
                    editor.putString("in order", "no");
                    editor.putString("status", "");
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("in process").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild(client_id)){
                    editor.putString("in order", "no");
                    editor.putString("status", "");
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                else {
                     requestsModel=dataSnapshot.child(client_id).getValue(RequestsModel.class);
                    client_phone=dataSnapshot.child(client_id).child("phone").getValue(String.class);
                    EndOrderTime();

                    SetStep(status);
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(finish==true){
                               /* FirebaseDatabase.getInstance().getReference().child("in process").child(client_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                         requestsModel2=dataSnapshot.getValue(RequestsModel.class);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                FirebaseDatabase.getInstance().getReference().child("orders").child(client_id).setValue(requestsModel2);
                                FirebaseDatabase.getInstance().getReference().child("in process").child(client_id).removeValue();*/
                                editor.putString("status", "");
                                editor.putString("in order", "no");
                                editor.commit();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finish();
                                return;
                            }

                            if(coast==true){
                                String OrderCoastString=OrderCost.getText().toString();
                                String DeliveOrderString=DeliveryCoast.getText().toString();
                                if(OrderCoastString.isEmpty()){
                                    Toast.makeText(OrderProcessActivity.this, "please enter Order coast", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if(DeliveOrderString.isEmpty()){
                                    Toast.makeText(OrderProcessActivity.this, "please enter Delivery coast", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Double ordercost=Double.parseDouble(OrderCoastString);
                                 deliverycost=Double.parseDouble(DeliveOrderString);
                                 Cash=ordercost+deliverycost;
                                FirebaseDatabase.getInstance().getReference().child("in process").child(client_id).child("cash").setValue(Cash);
                                coast=false;
                            }
                            describeOrder.setVisibility(View.GONE);
                            OrderCost.setVisibility(View.GONE);
                            DeliveryCoast.setVisibility(View.GONE);
                            Toast.makeText(OrderProcessActivity.this, status+"", Toast.LENGTH_SHORT).show();
                            switch (status){
                                case "Go to Store":
                                    mStepView.go(2, true);
                                    editor.putString("status", "The cost");
                                    editor.commit();
                                    status="The cost";
                                    break;

                                case "The cost":
                                    mStepView.go(3, true);
                                    editor.putString("status", "In way");
                                    editor.commit();
                                    status="In way";
                                    break;
                                case "In way":
                                    mStepView.go(4, true);
                                    editor.putString("status", "Cash");
                                    editor.commit();
                                    status="Cash";
                                    break;

                                    default:
                                        mStepView.go(1, true);
                                        editor.putString("status", "Go to Store");
                                        editor.commit();
                                        status="Go to Store";
                            }
                            SetStep(status);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void IntialViews() {
        textminuteleft = findViewById(R.id.minute_left);
        min = findViewById(R.id.min);
        statusview =findViewById(R.id.status);
        describeOrder =findViewById(R.id.describe_order);
        OrderCost =findViewById(R.id.order_cost);
        DeliveryCoast =findViewById(R.id.delivery_cost);
        signaturePad =findViewById(R.id.signature_pad);

        next =findViewById(R.id.next_to_rateing);
        call_client=findViewById(R.id.contactwithclient);
        call_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + client_phone));
                startActivity(intent2);
            }
        });
    }
    private void EndOrderTime() {
        FirebaseDatabase.getInstance().getReference().child("in process").child(client_id).child("date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               endtime=dataSnapshot.getValue(String.class);

                Date date = null;
                 calendar = Calendar.getInstance(); //A calendar set to the current time
                try {
                    date=new SimpleDateFormat("dd-mm-yyyy HH:mm").parse(endtime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendar.setTime(date);
                calendar.add(Calendar.MINUTE, 40); //Add one hour
                EndOrderHour=calendar.get(Calendar.HOUR);
                EndOrderMiute=calendar.get(Calendar.MINUTE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void ItialStepView() {
        mStepView = findViewById(R.id.step_vieww);
        mStepView.getState()
                .steps(new ArrayList<String>() {{
                    add("Go to Store");
                    add("In Store");
                    add("The cost");
                    add("IN WAY");
                    add("CASH");
                }})
                .commit();
        mStepView.go(0, true);
    }
    private void IntialCircularBrogressBar() {
        circularProgressBar =findViewById(R.id.progress_bar);
    }
    private void timer() {
        final int timeinminutes = 40 * 60;
        CountDownTimer timer = new CountDownTimer(timeinminutes * 1000, 1000) {
            int minuteleft;
            int check = preferences.getInt("check", 0);
            public void onTick(long millisUntilFinished) {
               /* if (check == 11) {
                    textminuteleft.setText("0");
                    circularProgressBar.setProgress(0);
                    return;
                }*/
                Calendar currenttime = Calendar.getInstance();
                if (currenttime.get(Calendar.HOUR) < EndOrderHour) {
                    int leftminute = 60 - (currenttime.get(Calendar.MINUTE));
                    minuteleft = leftminute + EndOrderMiute;
                } else {
                    minuteleft = EndOrderMiute - currenttime.get(Calendar.MINUTE);
                }
                // if time left ended and save in shared pref to check are was end or no
                if(currenttime.get(Calendar.HOUR)>EndOrderHour||(currenttime.get(Calendar.HOUR)==EndOrderHour&&EndOrderMiute<currenttime.get(Calendar.MINUTE))){
                    textminuteleft.setText("0");
                    circularProgressBar.setProgress(0);
                    return;
                }

             /*   if (minuteleft <= 0) {
                    editor.putInt("check", 11);
                    textminuteleft.setText("0");
                    editor.commit();
                    return;
                }*/
                textminuteleft.setText(minuteleft + "");
                circularProgressBar.setProgress(minuteleft);
            }

            public void onFinish() {
            }
        }.start();
    }
    private void SetStep(String s) {
        if (s.isEmpty()) {
            return;
        }
        switch (s) {
            case "Go to Store":
                mStepView.go(1, true);
                statusview.setText("Now buy the order");
                describeOrder.setVisibility(View.VISIBLE);
                describeOrder.setText(requestsModel.getDescribe_order());
                FirebaseDatabase.getInstance().getReference().child("in process").child(client_id).child("status").setValue("In Store");
                break;
            case "The cost":
                mStepView.go(2, true);
                statusview.setText("How much?");
                DeliveryCoast.setVisibility(View.VISIBLE);
                OrderCost.setVisibility(View.VISIBLE);
                coast=true;
                break;
            case "In way":
                mStepView.go(3, true);
                statusview.setText("Now go to the customer");
                describeOrder.setVisibility(View.VISIBLE);
                describeOrder.setText(requestsModel.getAddreasr()+"\n Building:"+requestsModel.getBuilding()+"\n Floor:"+requestsModel.getFloor());
                FirebaseDatabase.getInstance().getReference().child("in process").child(client_id).child("status").setValue("in way");
                break;
            case "Cash":
                mStepView.go(4, true);
                statusview.setText("Take the cash");
                FirebaseDatabase.getInstance().getReference().child("in process").child(client_id).child("status").setValue("cash");
                describeOrder.setVisibility(View.VISIBLE);
                if(requestsModel.getCash()==0){
                    describeOrder.setText(Cash+" L.E");
                    CalculateEarnMany();
                }
                else {
                    describeOrder.setText(requestsModel.getCash()+" L.E");
                }
                finish=true;
                next.setText("finish");
                call_client.setVisibility(View.GONE);
                signaturePad.setVisibility(View.VISIBLE);
                break;
        }
    }
    private void CalculateEarnMany(){
        final String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("delivery boy").child(id).child("myEarn").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double olderan=dataSnapshot.getValue(Double.class);
                Double newearn=olderan+deliverycost;
                FirebaseDatabase.getInstance().getReference().child("delivery boy").child(id).child("myEarn").setValue(newearn);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
