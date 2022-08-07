/*
HW 07
HomeFragment.java
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment implements ShoppingListCreatedRecyclerViewAdapter.IRecyclerAdapterListener, ShoppingListsInvitedRecyclerViewAdapter.IRecyclerAdapterListener{


/*
    private static final String ARG_PARAM_USER_ID = "ARG_PARAM_USER_ID";
    private static final String ARG_PARAM_USER_FRIENDS_LIST = "ARG_PARAM_USER_FRIENDS_LIST";
    private String currUserId;
    public ArrayList<String> currUserFriendsList;

 */


    private FirebaseAuth mAuth;

    public HomeFragment() {
        // Required empty public constructor
    }

/*
    public static FriendsListFragment newInstance(String currUserId, ArrayList<String> currUserFriendsList) {
        FriendsListFragment fragment = new FriendsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_USER_ID, currUserId);
        args.putStringArrayList(ARG_PARAM_USER_FRIENDS_LIST, currUserFriendsList);
        fragment.setArguments(args);
        return fragment;
    }

 */



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            currUserId =  getArguments().getString(ARG_PARAM_USER_ID);
            currUserFriendsList = getArguments().getStringArrayList(ARG_PARAM_USER_FRIENDS_LIST);
        }

         */
    }



    //declare variables
    Button logoutButton, newShoppingListButton, friendsListButton;
    RecyclerView listsInvitedToRecyclerView, yourShoppingListsRecyclerView;
    LinearLayoutManager layoutManagerCreated;
    ShoppingListCreatedRecyclerViewAdapter adapterCreated;
    LinearLayoutManager layoutManagerInvited;
    ShoppingListsInvitedRecyclerViewAdapter adapterInvited;
    ArrayList<ShoppingList> shoppingListsCreated, shoppingListsInvited;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //initialize variables
        logoutButton = view.findViewById(R.id.logoutButton);
        newShoppingListButton = view.findViewById(R.id.newShoppingListButton);
        friendsListButton = view.findViewById(R.id.friendsListButton);
        listsInvitedToRecyclerView = view.findViewById(R.id.listsInvitedToRecyclerView);
        yourShoppingListsRecyclerView = view.findViewById(R.id.yourShoppingListsRecyclerView);


        //Shopping Lists You Created"

        shoppingListsCreated = new ArrayList<>();
        //shoppingListsCreated.clear();

        //read shoppingLists collection from Firestore
        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("shoppingLists")

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        shoppingListsCreated.clear();
                        for (QueryDocumentSnapshot document: value) {

                            //Only shows documents that the current user created
                            if(document.get("creator").equals(mAuth.getCurrentUser().getDisplayName())) {

                                ShoppingList shoppingList = document.toObject(ShoppingList.class);
                                shoppingListsCreated.add(shoppingList);
                            }
                        }
                        adapterCreated.notifyDataSetChanged();
                    }
                });

        layoutManagerCreated = new LinearLayoutManager(container.getContext());
        yourShoppingListsRecyclerView.setLayoutManager(layoutManagerCreated);

        adapterCreated = new ShoppingListCreatedRecyclerViewAdapter(shoppingListsCreated, HomeFragment.this);
        yourShoppingListsRecyclerView.setAdapter(adapterCreated);

        //add divider lines in between each list item
        yourShoppingListsRecyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL));




        //"Shopping Lists You've Been Invited To"

        shoppingListsInvited = new ArrayList<>();

        //read data from Firestore
        mAuth = FirebaseAuth.getInstance();


        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("shoppingLists")

                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        shoppingListsInvited.clear();
                        for (QueryDocumentSnapshot document: value) {
                            ShoppingList shoppingList = document.toObject(ShoppingList.class);

                            //if the current user was invited to a shopping list,
                            //add that shopping list to the shoppingListsInvited list to be displayed
                            if(shoppingList.usersInvited.contains(mAuth.getCurrentUser().getUid())) {
                                shoppingListsInvited.add(shoppingList);
                            }
                        }
                        adapterInvited.notifyDataSetChanged();
                    }
                });

        //adapter.notifyDataSetChanged();

        layoutManagerInvited = new LinearLayoutManager(container.getContext());
        listsInvitedToRecyclerView.setLayoutManager(layoutManagerInvited);

        adapterInvited = new ShoppingListsInvitedRecyclerViewAdapter(shoppingListsInvited, HomeFragment.this);
        listsInvitedToRecyclerView.setAdapter(adapterInvited);

        //add divider lines in between each list item
        listsInvitedToRecyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL));


        newShoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.goToNewShoppingList();
            }
        });

        friendsListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mListener.goToFriendsList(currUserId, currUserFriendsList);
                mListener.goToFriendsList();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sign out the current user
                FirebaseAuth.getInstance().signOut();
                mListener.logoutCurrentUser();
            }
        });


        return view;
    } //end of onCreateView


    @Override
    public void sendDeletedShoppingList(String deleteShoppingListId) {
        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //delete all list items from the items subcollection first
        db.collection("shoppingLists").document(deleteShoppingListId).collection("items")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        //shoppingListsCreated.clear();

                        //delete all documents from the items subcollection first
                        for (QueryDocumentSnapshot document: value) {

                            db.collection("shoppingLists").document(deleteShoppingListId).collection("items")
                                    .document(document.getId()).delete();

                        }
                        adapterCreated.notifyDataSetChanged();
                    }
                });

        //then delete the shoppingList document
        db.collection("shoppingLists").document(deleteShoppingListId).delete();
        adapterCreated.notifyDataSetChanged();
    }

    @Override
    public void sendClickedShoppingList(ShoppingList shoppingListSelected) {
        mListener.sendListClicked(shoppingListSelected);
    }


    @Override
    public void sendLeaveShoppingList(ShoppingList leaveList) {

        //remove the current user's id from the invites list (for the selected shopping list)
        leaveList.usersInvited.remove(mAuth.getCurrentUser().getUid());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //update usersInvited list
        db.collection("shoppingLists").document(leaveList.docId)
                .update("usersInvited", leaveList.usersInvited);


        adapterInvited.notifyDataSetChanged();

        Toast.makeText(getContext(), R.string.toast_success_leave_list, Toast.LENGTH_SHORT).show();

    }


    HomeFragment.HomeFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (HomeFragment.HomeFragmentListener) context;
    }

    interface HomeFragmentListener {
        void logoutCurrentUser();
        void goToNewShoppingList();
        void sendListClicked(ShoppingList selectedList);
        void goToFriendsList();

    }

}