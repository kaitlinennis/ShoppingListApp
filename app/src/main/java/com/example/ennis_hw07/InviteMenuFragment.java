/*
HW 07
InviteMenuFragment.java
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class InviteMenuFragment extends Fragment implements UserInviteRecyclerViewAdapter.IRecyclerAdapterListener {

    private static final String ARG_PARAM_LIST_CHOSEN = "ARG_PARAM_LIST_CHOSEN";

    private FirebaseAuth mAuth;

    private ShoppingList listChosen;


    public InviteMenuFragment() {
        // Required empty public constructor
    }

    public static InviteMenuFragment newInstance(ShoppingList listChosen) {
        InviteMenuFragment fragment = new InviteMenuFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_LIST_CHOSEN, listChosen);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listChosen = (ShoppingList) getArguments().getSerializable(ARG_PARAM_LIST_CHOSEN);
        }
    }


    //declare variables
    Button buttonBackToList;
    RecyclerView inviteMenuRecyclerView;
    LinearLayoutManager layoutManager;
    UserInviteRecyclerViewAdapter adapter;
    ArrayList<User> usersList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invite_menu, container, false);

        //initialize variables
        buttonBackToList = view.findViewById(R.id.buttonBackToList);
        inviteMenuRecyclerView = view.findViewById(R.id.inviteMenuRecyclerView);

        usersList = new ArrayList<>();

        //read data from the "users" collection
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        usersList.clear();
                        for (QueryDocumentSnapshot document: value) {

                            User user = document.toObject(User.class);
                            usersList.add(user);

                        }
                        adapter.notifyDataSetChanged();
                    }
                });


        layoutManager = new LinearLayoutManager(container.getContext());
        inviteMenuRecyclerView.setLayoutManager(layoutManager);

        adapter = new UserInviteRecyclerViewAdapter(usersList, InviteMenuFragment.this);
        inviteMenuRecyclerView.setAdapter(adapter);

        //add divider lines in between each list item
        inviteMenuRecyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL));


        buttonBackToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.backToListDetails();
            }
        });

        return view;
    } //end of onCreateView


    InviteMenuFragment.InviteMenuFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (InviteMenuFragment.InviteMenuFragmentListener) context;
    }

    @Override
    public void sendInviteUserClicked(User user) {
        //IDEA: once the owner clicks "send invite" that button should turn invisible (since they can only remove now)
        //maybe also make "remove invite" invisible until the owner adds the user to the usersInvited list

        //if the owner attempts to invite a user that had already been invited
        if(listChosen.usersInvited.contains(user.userId)) {
            Toast.makeText(getContext(), R.string.toast_attempt_invite, Toast.LENGTH_LONG).show();
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            //add the user id to the invites list (firebase)
            listChosen.usersInvited.add(user.userId);

            //update usersInvited list
            db.collection("shoppingLists").document(listChosen.docId)
                    .update("usersInvited", listChosen.usersInvited);

            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), R.string.toast_success_invite_user, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void removeInviteUserClicked(User user) {
        //IDEA: once the owner clicks "remove invite", the "send invite" button should become visible again

        //if the owner attempts to un-invite a user that was never invited
        if(listChosen.usersInvited.contains(user.userId) == false) {
            Toast.makeText(getContext(), R.string.toast_attempt_uninvite, Toast.LENGTH_LONG).show();
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            //remove the user id from the invites list (firebase)
            listChosen.usersInvited.remove(user.userId);

            //update usersInvited list
            db.collection("shoppingLists").document(listChosen.docId)
                    .update("usersInvited", listChosen.usersInvited);


            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), R.string.toast_success_remove_user, Toast.LENGTH_SHORT).show();
        }
    }

    interface InviteMenuFragmentListener {
        void backToListDetails();
    }
}