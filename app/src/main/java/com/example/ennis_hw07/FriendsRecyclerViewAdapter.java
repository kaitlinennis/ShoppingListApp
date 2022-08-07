/*
HW 07
FriendsRecyclerViewAdapter.java
Kaitlin Ennis
 */
package com.example.ennis_hw07;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FriendsRecyclerViewAdapter extends RecyclerView.Adapter<FriendsRecyclerViewAdapter.FriendsViewHolder>{
    private FirebaseAuth mAuth;

    ArrayList<User> friendsList;
    public FriendsRecyclerViewAdapter(ArrayList<User> data, Fragment fragment) {

        if(fragment instanceof FriendsRecyclerViewAdapter.IRecyclerAdapterListener) {
            Log.d("demo", "UserRecyclerViewAdapter: FRAGMENT: " + fragment.toString());
            recyclerAdapterListener = (FriendsRecyclerViewAdapter.IRecyclerAdapterListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString() + " must implement IRecyclerAdapterListener");
        }

        this.friendsList = data;
    }

    @NonNull
    @Override
    public FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_row_item, parent, false);
        FriendsViewHolder friendsViewHolder =  new FriendsViewHolder(view);

        return friendsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsViewHolder holder, int position) {

        User friend = friendsList.get(position);

        holder.displayFriendName.setText(friend.name);

        //mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public int getItemCount() {
        if(this.friendsList == null) {
            return 0;
        }
        return this.friendsList.size();
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder {
        TextView displayFriendName;

        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);

            displayFriendName = itemView.findViewById(R.id.displayFriendName);

        }
    }

    FriendsRecyclerViewAdapter.IRecyclerAdapterListener recyclerAdapterListener;

    public interface IRecyclerAdapterListener {
    }

}
