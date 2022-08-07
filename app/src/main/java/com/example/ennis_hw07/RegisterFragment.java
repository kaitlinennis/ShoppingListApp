/*
HW 07
RegisterFragment.java
Kaitlin Ennis
 */
package com.example.ennis_hw07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterFragment extends Fragment {
    private FirebaseAuth mAuth;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //declare variables
    EditText editTextRegisterName, editTextRegisterEmail, editTextRegisterPassword;
    Button submitRegisterButton, cancelRegisterButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);


        //initialize variables
        editTextRegisterName = view.findViewById(R.id.editTextRegisterName);
        editTextRegisterEmail = view.findViewById(R.id.editTextRegisterEmail);
        editTextRegisterPassword = view.findViewById(R.id.editTextRegisterPassword);
        submitRegisterButton = view.findViewById(R.id.submitRegisterButton);
        cancelRegisterButton = view.findViewById(R.id.cancelRegisterButton);


        cancelRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancelRegister();
            }
        });

        submitRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextRegisterName.getText().toString();
                String email = editTextRegisterEmail.getText().toString();
                String password = editTextRegisterPassword.getText().toString();

                if (email.isEmpty() && password.isEmpty() && name.isEmpty()) {
                    Toast.makeText(container.getContext(), R.string.toast_missing_email_password_name, Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(container.getContext(), R.string.toast_missing_email_password, Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty() && name.isEmpty()) {
                    Toast.makeText(container.getContext(), R.string.toast_missing_email_name, Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty() && name.isEmpty()) {
                    Toast.makeText(container.getContext(), R.string.toast_missing_password_name, Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(container.getContext(), R.string.toast_missing_email_password, Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    Toast.makeText(container.getContext(), R.string.toast_missing_email, Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(container.getContext(), R.string.toast_missing_password, Toast.LENGTH_SHORT).show();
                } else if (name.isEmpty()) {
                    Toast.makeText(container.getContext(), R.string.toast_missing_name, Toast.LENGTH_SHORT).show();
                } else {
                    //no missing inputs
                    Log.d("demo", "onClick: No missing inputs!");

                    //Register a new user
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {

                                        //Save the new user's name
                                        UserProfileChangeRequest profileName = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build();
                                        //update the user's profile with their name
                                        mAuth.getCurrentUser().updateProfile(profileName);
                                        Log.d("demo", "onComplete: " + profileName.getDisplayName());


                                        //add user to the "users" collection
                                        //No missing input; store a new user on Firestore
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                                        HashMap<String, Object> user = new HashMap<>();
                                        user.put("name", profileName.getDisplayName());
                                        user.put("userId", mAuth.getCurrentUser().getUid());
                                        user.put("friendsList", new ArrayList<>());

                                        db.collection("users")
                                                .add(user)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        //User was added to the "users" collection
                                                        String id = documentReference.getId();

                                                        Log.d("demo", "onSuccess: new DOC ID: " + id);

                                                        user.put("docId", id);
                                                        documentReference.set(user);

                                                        Log.d("demo", "onSuccess: NEW USER: " + user);
                                                    }
                                                });

                                        //mListener.registeredUserSuccessfully(user.get("docId").toString(), (ArrayList<String>) user.get("friendsList"));
                                        mListener.registeredUserSuccessfully();

                                    } else {
                                        Toast.makeText(container.getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        return view;
    } //end of onCreateView

    RegisterFragment.RegisterFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (RegisterFragment.RegisterFragmentListener) context;
    }

    interface RegisterFragmentListener {
        void cancelRegister();
        //void registeredUserSuccessfully(String currUserId, ArrayList<String> currUserFriendsList);
        void registeredUserSuccessfully();
    }

}