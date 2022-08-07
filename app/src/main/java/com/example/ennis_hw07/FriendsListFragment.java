/*
HW 07
FriendsListFragment.java
Kaitlin Ennis
 */
package com.example.ennis_hw07;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FriendsListFragment extends Fragment implements FriendsRecyclerViewAdapter.IRecyclerAdapterListener {
    /*
    private static final String ARG_PARAM_USER_ID = "ARG_PARAM_USER_ID";
    private static final String ARG_PARAM_USER_FRIENDS = "ARG_PARAM_USER_ID";

    private String currUserId;
    private ArrayList<String> currUserFriendsList;
     */

    private FirebaseAuth mAuth;

    public FriendsListFragment() {
        // Required empty public constructor
    }

/*
    public static FriendsListFragment newInstance(String currUserId, ArrayList<String> currUserFriendsList) {
        FriendsListFragment fragment = new FriendsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_USER_ID, currUserId);
        args.putStringArrayList(ARG_PARAM_USER_ID, currUserFriendsList);
        fragment.setArguments(args);
        return fragment;
    }
 */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            currUserId = getArguments().getString(ARG_PARAM_USER_ID);
            currUserFriendsList = getArguments().getStringArrayList(ARG_PARAM_USER_FRIENDS);
        }
        */
    }


    //declare variables
    TextView textViewFriendsListLabel;
    RecyclerView friendsRecyclerView;
    LinearLayoutManager layoutManager;
    FriendsRecyclerViewAdapter adapter;
    ArrayList<User> friendsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);

        //initialize variables
        textViewFriendsListLabel = view.findViewById(R.id.textViewFriendsListLabel);
        friendsRecyclerView = view.findViewById(R.id.friendsRecyclerView);

        friendsList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //db.collection("users").document(mAuth.getCurrentUser().getUid());

        //find the User document for the current user (from the Users collection)
        db.collection("users").whereEqualTo("userId", mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        friendsList.clear();
                        for (QueryDocumentSnapshot document : value) {
                            User currUser = document.toObject(User.class);

                            /*
                            db.collection("shoppingLists").document(listChosen.docId)
                            .update("usersInvited", listChosen.usersInvited);
                             */

                            db.collection("users")
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            for (QueryDocumentSnapshot document : value) {
                                                User user = document.toObject(User.class);

                                                if(currUser.friendsList.contains(user.userId)) {
                                                    friendsList.add(user);
                                                }
                                            }
                                        }
                                    });
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

        layoutManager = new LinearLayoutManager(container.getContext());
        friendsRecyclerView.setLayoutManager(layoutManager);

        adapter = new FriendsRecyclerViewAdapter(friendsList, FriendsListFragment.this);
        friendsRecyclerView.setAdapter(adapter);

        //add divider lines in between each list item
        friendsRecyclerView.addItemDecoration(new DividerItemDecoration(container.getContext(), DividerItemDecoration.VERTICAL));

        return view;
    }
}