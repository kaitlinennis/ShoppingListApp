/*
HW 07
CreateShoppingListFragment.java
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CreateShoppingListFragment extends Fragment {

    private FirebaseAuth mAuth;

    public CreateShoppingListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //declare variables
    EditText editTextListTitle;
    Button buttonSubmitNewList, buttonCancelNewList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_shopping_list, container, false);


        editTextListTitle = view.findViewById(R.id.editTextListTitle);
        buttonSubmitNewList = view.findViewById(R.id.buttonSubmitNewList);
        buttonCancelNewList = view.findViewById(R.id.buttonCancelNewList);

        mAuth = FirebaseAuth.getInstance();

        buttonSubmitNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String listTitle = editTextListTitle.getText().toString();

                //Input validation; checking for missing input
                if(listTitle.isEmpty()) {
                    Toast.makeText(container.getContext(), R.string.toast_missing_list_title, Toast.LENGTH_SHORT).show();
                } else {
                    //No missing input; store a new shopping list on Firestore

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    HashMap<String, Object> shoppingList = new HashMap<>();

                    shoppingList.put("title", listTitle);
                    shoppingList.put("creator", mAuth.getCurrentUser().getDisplayName());
                    shoppingList.put("ownerId", mAuth.getCurrentUser().getUid());
                    shoppingList.put("usersInvited", new ArrayList<>());

                    //shoppingList.put("usersLike", new ArrayList<>());

                    db.collection("shoppingLists")
                            .add(shoppingList)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    //Shopping List was added; go back to the Home fragment
                                    String id = documentReference.getId();

                                    Log.d("demo", "onSuccess: new DOC ID: " + id);

                                    shoppingList.put("docId", id);

                                    documentReference.set(shoppingList);

                                    Log.d("demo", "onSuccess: NEW SHOPPING LIST: " + shoppingList);
                                    mListener.backToHome();
                                }
                            });
                }
            }
        });



        buttonCancelNewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.backToHome();
            }
        });


        return view;
    } //end of onCreateView


    CreateShoppingListFragment.CreateShoppingListFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateShoppingListFragment.CreateShoppingListFragmentListener) context;
    }


    interface CreateShoppingListFragmentListener {
        void backToHome();
    }



}