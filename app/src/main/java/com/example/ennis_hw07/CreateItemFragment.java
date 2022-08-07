/*
HW 07
CreateItemFragment.java
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

import java.util.HashMap;

public class CreateItemFragment extends Fragment {

    private FirebaseAuth mAuth;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_SHOPPING_LIST_CHOSEN_ID = "ARG_PARAM_SHOPPING_LIST_CHOSEN_ID";

    private String shoppingListChosenId;

    public CreateItemFragment() {
        // Required empty public constructor
    }

    public static CreateItemFragment newInstance(String shoppingListChosenId) {
        CreateItemFragment fragment = new CreateItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_SHOPPING_LIST_CHOSEN_ID, shoppingListChosenId);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            shoppingListChosenId = getArguments().getString(ARG_PARAM_SHOPPING_LIST_CHOSEN_ID);
        }
    }


    //declare variables
    EditText editTextItemName, editTextItemCost;
    Button buttonSubmitItem, buttonCancelItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_item, container, false);

        //initialize variables
        editTextItemName = view.findViewById(R.id.editTextItemName);
        editTextItemCost = view.findViewById(R.id.editTextItemCost);
        buttonSubmitItem = view.findViewById(R.id.buttonSubmitItem);
        buttonCancelItem = view.findViewById(R.id.buttonCancelItem);

        mAuth = FirebaseAuth.getInstance();

        buttonSubmitItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String itemName = editTextItemName.getText().toString();
                String itemCost = editTextItemCost.getText().toString();

                //Input validation; checking for missing input
                if (itemName.isEmpty() && itemCost.isEmpty()) {
                    Toast.makeText(container.getContext(), R.string.toast_missing_item_name_cost, Toast.LENGTH_SHORT).show();
                } else if (itemCost.isEmpty()) {
                    Toast.makeText(container.getContext(), R.string.toast_missing_item_cost, Toast.LENGTH_SHORT).show();
                } else if (itemName.isEmpty()) {
                    Toast.makeText(container.getContext(), R.string.toast_missing_item_name, Toast.LENGTH_SHORT).show();
                } else {
                        //No missing input; store a new item on Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        HashMap<String, Object> listItem = new HashMap<>();
                        listItem.put("name", itemName);
                        listItem.put("creator", mAuth.getCurrentUser().getDisplayName());
                        listItem.put("status", "Not yet acquired");
                        listItem.put("estimatedCost", itemCost);


                        db.collection("shoppingLists").document(shoppingListChosenId).collection("items")
                                .add(listItem)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        //Item was added; go back to the Shopping List Details fragment
                                        String id = documentReference.getId();

                                        Log.d("demo", "onSuccess: new DOC ID: " + id);

                                        listItem.put("docId", id);

                                        documentReference.set(listItem);

                                        Log.d("demo", "onSuccess: NEW ITEM: " + listItem);
                                        mListener.backToListDetail();
                                    }
                                });
                    }
                }
            });

        buttonCancelItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.backToListDetail();
            }
        });


        return view;
    } //end of onCreateView


    CreateItemFragment.CreateItemFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateItemFragment.CreateItemFragmentListener) context;
    }


    interface CreateItemFragmentListener {
        void backToListDetail();
    }


}