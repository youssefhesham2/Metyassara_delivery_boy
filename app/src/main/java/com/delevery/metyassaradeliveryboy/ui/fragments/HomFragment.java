package com.delevery.metyassaradeliveryboy.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.delevery.metyassaradeliveryboy.R;
import com.delevery.metyassaradeliveryboy.model.RequestsModel;
import com.delevery.metyassaradeliveryboy.ui.OrderProcessActivity.OrderProcessActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomFragment extends Fragment {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    RequestsModel requestsModel;
    List<RequestsModel> requestsModels=new ArrayList<>();
    View view;
    RecyclerView recyclerView;
    RequestsAdapter requestsAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return view=inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitSharedPrefrance();
        InitRecycler();
        GetData();
    }

    private void InitSharedPrefrance() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
        String inorder=preferences.getString("in order","");
        if(inorder.equals("in order")){
            Intent intent=new Intent(getActivity(), OrderProcessActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private void InitRecycler() {
        recyclerView=view.findViewById(R.id.OrdersRecycler);
    }

    private void GetData() {
        FirebaseDatabase.getInstance().getReference().child("requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestsModels.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    RequestsModel requestsModel=snapshot.getValue(RequestsModel.class);
                    requestsModels.add(requestsModel);
                }
                requestsAdapter=new RequestsAdapter(requestsModels);
                recyclerView.setAdapter(requestsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.RequestsViewHoleder>{
        List<RequestsModel> requestsModels;

        public RequestsAdapter(List<RequestsModel> requestsModels) {
            this.requestsModels = requestsModels;
        }

        @NonNull
        @Override
        public RequestsViewHoleder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.orders_item,parent,false);
            return new RequestsViewHoleder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RequestsViewHoleder holder, int position) {
            final RequestsModel requestsModel=requestsModels.get(position);

            holder.ResturentName.setText(requestsModel.getRestaurant_name());
            holder.ClientAddress.setText(requestsModel.getAddreasr());
            holder.AcceptOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String delevery_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference().child("requests").child(requestsModel.getClient_id()).child("delevery_id").setValue(delevery_id);
                    FirebaseDatabase.getInstance().getReference().child("requests").child(requestsModel.getClient_id()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           RequestsModel requestsModel1= dataSnapshot.getValue(RequestsModel.class);
                            Intent intent=new Intent(getActivity(), OrderProcessActivity.class);
                            editor.putString("clirnt id",requestsModel.getClient_id());
                            editor.commit();
//                            intent.putExtra("request model",requestsModel1);
                            //FirebaseDatabase.getInstance().getReference().child("requests").child(requestsModel.getClient_id()).removeValue();
                            FirebaseDatabase.getInstance().getReference().child("in process").child(requestsModel.getClient_id()).setValue(requestsModel1);
                            FirebaseDatabase.getInstance().getReference().child("in process").child(requestsModel.getClient_id()).child("status").setValue("go to store");
                            startActivity(intent);
                            getActivity().finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

        @Override
        public int getItemCount() {
            return requestsModels.size();
        }

        class RequestsViewHoleder extends RecyclerView.ViewHolder{
            TextView ResturentName,ClientAddress;
            Button AcceptOrder;
            public RequestsViewHoleder(@NonNull View itemView) {
                super(itemView);
                ResturentName=itemView.findViewById(R.id.ResturentName);
                ClientAddress=itemView.findViewById(R.id.ClientAddress);
                AcceptOrder=itemView.findViewById(R.id.AcceptOrder);
            }
        }
    }

}
