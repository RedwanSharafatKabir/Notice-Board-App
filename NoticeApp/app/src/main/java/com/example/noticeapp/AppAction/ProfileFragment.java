package com.example.noticeapp.AppAction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.noticeapp.Authentication.LoginActivity;
import com.example.noticeapp.Authentication.ResetPassword;
import com.example.noticeapp.HomePage.HomeFragment;
import com.example.noticeapp.Interfaces.BackListenerFragment;
import com.example.noticeapp.ModelClasses.StoreUserImage;
import com.example.noticeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment implements BackListenerFragment, View.OnClickListener{

    FirebaseAuth mAuth;
    FirebaseUser user;
    StorageReference storageReference;
    DatabaseReference databaseReference, imageDatabaseReference;
    View views, parentLayout;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    Snackbar snackbar;
    ProgressBar progressBar;
    ProgressDialog dialog;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    public static BackListenerFragment backBtnListener;
    CardView changePassword, changePicture, logoutBtn;
    CircleImageView profilePic;
    private Uri uriProfileImage;
    TextView nameText, emailText, phoneText;
    String userEmail, userPhone, userName, imageUrl, image_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        views = inflater.inflate(R.layout.fragment_profile, container, false);

        parentLayout = views.findViewById(android.R.id.content);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userPhone = user.getDisplayName();
        dialog = new ProgressDialog(getActivity());

        changePassword = views.findViewById(R.id.changePasswordId);
        changePassword.setOnClickListener(this);
        changePicture = views.findViewById(R.id.uploadProfilePicId);
        changePicture.setOnClickListener(this);
        logoutBtn = views.findViewById(R.id.logoutID);
        logoutBtn.setOnClickListener(this);

        profilePic = views.findViewById(R.id.profilePicId);
        nameText = views.findViewById(R.id.profileNameId);
        emailText = views.findViewById(R.id.profileEmailId);
        phoneText = views.findViewById(R.id.profilePhoneId);

        progressBar = views.findViewById(R.id.profileProgressbarId);

        cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getActiveNetworkInfo();
        databaseReference = FirebaseDatabase.getInstance().getReference("User Info");
        imageDatabaseReference = FirebaseDatabase.getInstance().getReference("User Image");

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            getUserData();
        } else {
            snackbar = Snackbar.make(parentLayout, "Turn on internet connection", Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.Red));
            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
            snackbar.setDuration(10000).show();
        }

        return views;
    }

    private void getUserData(){
        databaseReference.child(userPhone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = snapshot.child("username").getValue().toString();
                userEmail = snapshot.child("userEmail").getValue().toString();

                nameText.setText(userName);
                emailText.setText(userEmail);
                phoneText.setText(userPhone);

                try{
                    imageDatabaseReference.child(userPhone).child("avatar").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            try {
                                Picasso.get().load(snapshot.getValue().toString()).into(profilePic);
                                progressBar.setVisibility(View.GONE);

                            } catch (Exception e){
                                Log.i("Error", e.getMessage());
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                } catch (Exception e){
                    Log.i("Error", e.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.uploadProfilePicId){
            someActivityResultLauncher.launch("image/*");
        }

        if(view.getId()==R.id.changePasswordId){
            Bundle armgs = new Bundle();
            armgs.putString("email_key", userEmail);

            ResetPassword resetPassword = new ResetPassword();
            resetPassword.setArguments(armgs);
            resetPassword.show(getActivity().getSupportFragmentManager(), "Sample dialog");
        }

        if(view.getId()==R.id.logoutID){
            logoutApp();
        }
    }

    ActivityResultLauncher<String> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        profilePic.setImageURI(result);
                        uriProfileImage = result;
                        uploadImageToFirebase();
                    }
                }
            });

    private void uploadImageToFirebase(){
        dialog.setMessage("Uploading.....");
        dialog.show();

        image_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        storageReference = FirebaseStorage.getInstance()
                .getReference("profile images/" + image_name + ".jpg");

        if(uriProfileImage!=null){
            storageReference.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUrl = uri.toString();
                            saveUserInfo();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Could not upload", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {}
            });
        }
    }

    private void saveUserInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!=null && imageUrl!=null){
            UserProfileChangeRequest profile;
            profile = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(imageUrl)).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {}
            });

            storeImageMethod(imageUrl);
            dialog.dismiss();
            Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
        }
    }

    public void storeImageMethod(String profileImageUrl){
        StoreUserImage storeUserImage = new StoreUserImage(profileImageUrl);
        imageDatabaseReference.child(userPhone).setValue(storeUserImage);
    }

    private void logoutApp(){
        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Logout !");
        alertDialogBuilder.setMessage("Are you sure you want to logout ?");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nullValue = "";
                setNullDataMethod(nullValue);

                mAuth.signOut();

                getActivity().finish();
                Intent it = new Intent(getActivity(), LoginActivity.class);
                startActivity(it);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        alertDialogBuilder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setNullDataMethod(String nullValue){
        try {
            FileOutputStream fileOutputStream = getActivity().openFileOutput("Users_Info.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(nullValue.getBytes());
            fileOutputStream.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        backBtnListener = this;
    }

    @Override
    public void onPause() {
        backBtnListener = null;
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        ((MainActivity)getActivity()).actionBarText.setText("General Notice");
        fragment = new HomeFragment();
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragmentID, fragment, "MY_FRAGMENT");
        fragmentTransaction.commit();
    }
}

