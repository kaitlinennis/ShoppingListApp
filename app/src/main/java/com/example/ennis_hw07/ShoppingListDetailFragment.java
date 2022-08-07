/*
HW 07
ShoppingListDetailFragment.java
Kaitlin Ennis
 */
package com.example.ennis_hw07;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShoppingListDetailFragment extends Fragment implements ItemRecyclerViewAdapter.IRecyclerAdapterListener {

    private FirebaseAuth mAuth;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_SHOPPING_LIST_CHOSEN = "ARG_PARAM_SHOPPING_LIST_CHOSEN";

    private ShoppingList shoppingListChosen;

    public ShoppingListDetailFragment() {
        // Required empty public constructor
    }


    public static ShoppingListDetailFragment newInstance(ShoppingList shoppingListChosen) {
        ShoppingListDetailFragment fragment = new ShoppingListDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_SHOPPING_LIST_CHOSEN, shoppingListChosen);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            shoppingListChosen = (ShoppingList) getArguments().getSerializable(ARG_PARAM_SHOPPING_LIST_CHOSEN);
        }
    }




    //declare variables
    Button inviteMenuButton, addItemButton;
    TextView displaySelectedListTitle, displaySelectedListCreator, displaySelectedListTotalCost;
    RecyclerView itemsRecyclerView;
    LinearLayoutManager layoutManager;
    ItemRecyclerViewAdapter adapter;
    ArrayList<Item> listItems;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_list_detail, container, false);

        //initialize variables
        inviteMenuButton = view.findViewById(R.id.inviteMenuButton);
        addItemButton = view.findViewById(R.id.addItemButton);
        displaySelectedListTitle = view.findViewById(R.id.displaySelectedListTitle);
        displaySelectedListCreator = view.findViewById(R.id.displaySelectedListCreator);
        displaySelectedListTotalCost = view.findViewById(R.id.displaySelectedListTotalCost);
        itemsRecyclerView = view.findViewById(R.id.itemsRecyclerView);

        mAuth = FirebaseAuth.getInstance();


        //only the owner of the list should be able to access the Invite Menu button
        if(mAuth.getCurrentUser().getDisplayName().equals(shoppingListChosen.creator)) {
            inviteMenuButton.setVisibility(View.VISIBLE);
        } else {
            inviteMenuButton.setVisibility(View.INVISIBLE);
        }


        listItems = new ArrayList<>();

        //read items subcollection from Firestore
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("shoppingLists").document(shoppingListChosen.docId).collection("items")

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        listItems.clear();
                        for (QueryDocumentSnapshot document: value) {

                                Item listItem = document.toObject(Item.class);
                                listItems.add(listItem);

                        }
                        adapter.notifyDataSetChanged();
                    }
                });



        layoutManager = new LinearLayoutManager(container.getContext());
        itemsRecyclerView.setLayoutManager(layoutManager);

        adapter = new ItemRecyclerViewAdapter(listItems, ShoppingListDetailFragment.this);
        itemsRecyclerView.setAdapter(adapter);

        //add divider lines in between each list item
        itemsRecyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL));


        displaySelectedListTitle.setText(shoppingListChosen.title);
        displaySelectedListCreator.setText("Created by: " + shoppingListChosen.creator);


        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToAddItem(shoppingListChosen);
            }
        });


        inviteMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //only the owner of the list should be able to access the invite menu
                if(mAuth.getCurrentUser().getDisplayName().equals(shoppingListChosen.creator)) {
                    mListener.goToInviteMenu(shoppingListChosen);
                }
            }
        });


        return view;
    } //end of onCreateView


    @Override
    public void sendDeletedItem(String deleteItemId) {
        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //delete the comment document from the comments subcollection
        db.collection("shoppingLists").document(shoppingListChosen.docId).collection("items")
                .document(deleteItemId).delete();

        adapter.notifyDataSetChanged();

    }

    @Override
    public void sendItemTotal(double itemTotal) {
        displaySelectedListTotalCost.setText("Total cost: $" + String.format("%.2f", itemTotal));
    }

    @Override
    public void sendStatusItem(String statusItemId) {
        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        /*
        if(db.collection("shoppingLists").document(shoppingListChosen.docId).collection("items")
                .document(statusItemId).)
         */

        //update the item document status (from the items subcollection) to acquired
        db.collection("shoppingLists").document(shoppingListChosen.docId).collection("items")
                .document(statusItemId)
                .update("status", "Acquired");

        adapter.notifyDataSetChanged();
    }


    ShoppingListDetailFragment.ShoppingListDetailFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (ShoppingListDetailFragment.ShoppingListDetailFragmentListener) context;
    }

    interface ShoppingListDetailFragmentListener {
        void goToAddItem(ShoppingList shoppingListChosen);
        void goToInviteMenu(ShoppingList shoppingListChosen);
    }

}