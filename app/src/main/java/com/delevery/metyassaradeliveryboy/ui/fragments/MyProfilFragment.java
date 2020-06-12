package com.delevery.metyassaradeliveryboy.ui.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.delevery.metyassaradeliveryboy.R;
import com.delevery.metyassaradeliveryboy.model.DeliveryBoyModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfilFragment extends Fragment
{
    private View myProfileFragment;

    // Dialogs
    private ProgressDialog enterDialog;
    private ProgressDialog saveDialog;
    private ProgressDialog passDialog;

    // FireBase
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    // require data
    private String myEmail;
    private String uid;
    private DeliveryBoyModel myModel;
    private String image_urL;

    // all editTexts
    private EditText usernameField, currentPasswordField, newPasswordField, confirmNewPasswordField, newPhoneNumber;

    // all TextViews
    private TextView openChangePassword, openChangePhoneNumber;

    private Switch onOff;

    //linear which has password fields
    private LinearLayout editPasswordLin;

    //(open/close) state
    private int editPhoneOpen = 0;
    private int editPasswordOpen = 0;

    private Button saveChanges;
    private Button changePassword;
    private CircleImageView profilePhoto;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        myProfileFragment=inflater.inflate ( R.layout.fragment_my_profil,null );
        initHints ();
        return myProfileFragment;
    }

    private void initHints()
    {
        editPasswordLin = myProfileFragment.findViewById(R.id.change_password);
        newPhoneNumber = myProfileFragment.findViewById(R.id.new_phone_number);

        newPhoneNumber.setVisibility(View.GONE);
        editPasswordLin.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        initFirebase();
        initDialog();
        uid = auth.getUid ();
        myEmail = auth.getCurrentUser().getEmail();
        getMyModel(uid);
    }

    //to get your data from database
    private void getMyModel(final String uid) {
        enterDialog.show();
        databaseReference.child("delivery boy").child(uid).addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myModel = dataSnapshot.getValue(DeliveryBoyModel.class);
                initViews();
                enterDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                enterDialog.dismiss();
            }
        });
    }

    //definition all dialogs
    private void initDialog() {
        //dialog appeared when enter to fragment
        enterDialog = new ProgressDialog(getContext());
        enterDialog.setMessage("please wait...");
        enterDialog.setCancelable(false);

        //dialog appeared when click "save" button
        saveDialog = new ProgressDialog(getContext());
        saveDialog.setTitle("save changes");
        saveDialog.setMessage("please wait...");
        saveDialog.setCancelable(false);

        //dialog appeared when click "change" button
        passDialog = new ProgressDialog(getContext());
        passDialog.setTitle("change password");
        passDialog.setMessage("please wait...");
        passDialog.setCancelable(false);
    }

    //definition firebase
    private void initFirebase() {
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
    }

    //definition other views in fragment
    private void initViews() {
        profilePhoto = myProfileFragment.findViewById(R.id.change_profile_image);
        Picasso.get().load(myModel.getImag_url ()).into(profilePhoto);

        usernameField = myProfileFragment.findViewById(R.id.edit_username_field);
        usernameField.setText(myModel.getName ());

        TextView emailField = myProfileFragment.findViewById(R.id.email_field);
        emailField.setText(myModel.getMail ());

        openChangePassword = myProfileFragment.findViewById(R.id.open_change_password);
        openChangePhoneNumber = myProfileFragment.findViewById(R.id.open_change_phone_number);

        saveChanges = myProfileFragment.findViewById(R.id.save_changes);

        currentPasswordField = myProfileFragment.findViewById(R.id.current_password);
        newPasswordField = myProfileFragment.findViewById(R.id.new_password);
        confirmNewPasswordField = myProfileFragment.findViewById(R.id.confirm_new_password);

        changePassword = myProfileFragment.findViewById(R.id.save_password);

        onOff = myProfileFragment.findViewById ( R.id.on_off );
        String status = myModel.getStatus ();
        if(status.equals ( "on" ))
        {
            onOff.setChecked ( true );
        }
        else
            {
                onOff.setChecked ( false );
            }

        TextView myEarn = myProfileFragment.findViewById ( R.id.my_earn );
        myEarn.setText ( String.valueOf ( myModel.getMyEarn () ) );

        RatingBar myRate = myProfileFragment.findViewById ( R.id.rating );
        myRate.setRating ( myModel.getRate () );

        setChangePassword(0);
        setChangePhoneNumber(0);
        Clicks();
    }

    private void Clicks() {
        //click on imageView to change photo
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });

        //click on this Button to save your changes without password
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeData();
            }
        });

        //click on this button to change password only
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword();
            }
        });

        //click on "change password" to (open / close) password layout
        openChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if password layout close
                if (editPasswordOpen == 0)  //if close
                {
                    //to open it
                    setChangePassword(1);
                } else  //if open
                {
                    //to close it
                    setChangePassword(0);
                }
            }
        });

        //click on "change phone number" to (open / close) change layout
        openChangePhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if phone number field close
                if (editPhoneOpen == 0)  //if close
                {
                    //to open it
                    setChangePhoneNumber(1);
                } else  //if open
                {
                    //to close it
                    setChangePhoneNumber(0);
                }
            }
        });
    }

    //when click on "change" button to check empty field
    private void checkPassword() {
        final String currentPassword = currentPasswordField.getText().toString();
        final String newPassword = newPasswordField.getText().toString();
        String confirmNewPassword = confirmNewPasswordField.getText().toString();

        if (currentPassword.isEmpty()) {
            Toast.makeText(getContext(), "please enter your current password", Toast.LENGTH_SHORT).show();
            currentPasswordField.requestFocus();
            return;
        }

        if (newPassword.isEmpty()) {
            Toast.makeText(getContext(), "please enter new password", Toast.LENGTH_SHORT).show();
            newPasswordField.requestFocus();
            return;
        }

        if (!confirmNewPassword.equals(newPassword)) {
            Toast.makeText(getContext(), "password doesn't match", Toast.LENGTH_SHORT).show();
            confirmNewPasswordField.requestFocus();
            return;
        }

        passDialog.show();
        //if it finishes form validation can do changes
        changePassword(currentPassword, newPassword);

    }

    //when it finishes from password validation do this
    private void changePassword(final String currentPassword, final String newPassword) {
        //do signIn again to test if the "currentPassword" is correct
        auth.signInWithEmailAndPassword(myEmail, currentPassword).addOnCompleteListener(new OnCompleteListener<AuthResult> () {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //if "currentPassword" correct
                if (task.isSuccessful()) {
                    //do change password in firebase
                    auth.getCurrentUser().updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //if change finished
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "password changed successfully", Toast.LENGTH_SHORT).show();
                                setChangePassword(0);
                                passDialog.dismiss();
                                //unless changed finished
                            } else {
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                passDialog.dismiss();
                            }
                        }
                    });

                    //if "currentPassword" doesn't correct
                } else {
                    Toast.makeText(getContext(), "your password doesn't correct", Toast.LENGTH_SHORT).show();
                    passDialog.dismiss();
                }
            }
        });
    }

    //when click on imageView u will go to gallery to choose photo
    private void selectPhoto() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //when u finish go to get image URI
        startActivityForResult(galleryIntent, 505);
    }

    //here you can get image URI
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 505 && resultCode == getActivity().RESULT_OK && data != null) {
            //you get image URI
            Uri image_uri = data.getData();
            Picasso.get().load(image_uri).into(profilePhoto);

            //here you upload image to storage
            uploadPhoto(image_uri);
        }
    }

    //here you upload image to storage
    private void uploadPhoto(Uri image_uri) {
        enterDialog.show();
        storageReference = firebaseStorage.getReference().child("delivery_boy_image/" + image_uri.getLastPathSegment());
        UploadTask uploadTask = storageReference.putFile(image_uri);

        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>> () {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                //here you get image URL to can save it in database
                image_urL = task.getResult().toString();
                enterDialog.dismiss();
            }
        });

    }

    //when click on "save" button you can upload data on your dataBase
    private void changeData() {
        String userName = usernameField.getText().toString();
        String nPhone = newPhoneNumber.getText().toString();

        //check data
        if (image_urL == null) {
            image_urL = myModel.getImag_url ();
        }

        if (userName.isEmpty()) {
            Toast.makeText(getContext(), "should enter user name", Toast.LENGTH_SHORT).show();
            usernameField.setText(myModel.getName ());
            usernameField.requestFocus();
            return;
        }

        //if it finishes form validation can save changes
        updateData(userName, nPhone, image_urL);
    }

    //when it finishes from data validation do this
    private void updateData(final String userName, final String nPhone, final String image_urL) {
        saveDialog.show();

        uploadData("imag_url", image_urL);
        uploadData("name", userName);
        changeStatus ( onOff.isChecked () );
        databaseReference.child("delivery boy").child(uid).child("phone").setValue(nPhone).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Picasso.get().load(image_urL).into(profilePhoto);
                saveDialog.dismiss();

                //to reload data
                getMyModel(uid);
            }
        });


    }

    //to change visible password layout state
    private void setChangePassword(int state) {
        if (state == 0) {
            editPasswordLin.setVisibility(View.GONE);
            editPasswordOpen = state;
            currentPasswordField.setText("");
            newPasswordField.setText("");
            confirmNewPasswordField.setText("");
        } else {
            editPasswordLin.setVisibility(View.VISIBLE);
            editPasswordOpen = state;
        }
    }

    //to change visible phone number field state
    private void setChangePhoneNumber(int state) {
        if (state == 0) {
            newPhoneNumber.setVisibility(View.GONE);
            editPhoneOpen = state;
            newPhoneNumber.setText(myModel.getPhone());
        } else {
            newPhoneNumber.setVisibility(View.VISIBLE);
            editPhoneOpen = state;
        }
    }

    //to set delivery status on data
    private void changeStatus(boolean status) {
        if(status)
        {
            uploadData ( "status","on" );
        }
        else
        {
            uploadData ( "status","off" );
        }
    }

    //to change your data
    private void uploadData(String path, String value) {
        databaseReference.child("delivery boy").child(uid).child(path).setValue(value);
    }
}
