package com.delevery.metyassaradeliveryboy.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.delevery.metyassaradeliveryboy.R;
import com.delevery.metyassaradeliveryboy.model.RequestsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomFragment extends Fragment {
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
        InitRecycler();
        GetData();
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
            RequestsModel requestsModel=requestsModels.get(position);

            holder.ResturentName.setText(requestsModel.getRestaurant_name());
            holder.ClientAddress.setText(requestsModel.getAddreasr());
            holder.AcceptOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
