/*
HW 07
UserInviteRecyclerViewAdapter.java
Kaitlin Ennis
 */
package com.example.ennis_hw07;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserInviteRecyclerViewAdapter extends RecyclerView.Adapter<UserInviteRecyclerViewAdapter.UserInviteViewHolder>{

    private FirebaseAuth mAuth;

    ArrayList<User> usersList;
    public UserInviteRecyclerViewAdapter(ArrayList<User> data, Fragment fragment) {

        if(fragment instanceof UserInviteRecyclerViewAdapter.IRecyclerAdapterListener) {
            Log.d("demo", "UserInviteRecyclerViewAdapter: FRAGMENT: " + fragment.toString());
            recyclerAdapterListener = (UserInviteRecyclerViewAdapter.IRecyclerAdapterListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString() + " must implement IRecyclerAdapterListener");
        }

        this.usersList = data;
    }

    @NonNull
    @Override
    public UserInviteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invite_list_row_item, parent, false);
        UserInviteViewHolder forumViewHolder =  new UserInviteViewHolder(view);

        return forumViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserInviteViewHolder holder, int position) {

        User user = usersList.get(position);

        holder.inviteMenuDisplayUserName.setText(user.name);

        mAuth = FirebaseAuth.getInstance();


        //owner cannot invite themselves to their own list!!
        if(user.name.equals(mAuth.getCurrentUser().getDisplayName())) {
            holder.buttonSendInvite.setVisibility(View.INVISIBLE);
            holder.buttonRemoveInvite.setVisibility(View.INVISIBLE);
        }


        holder.buttonSendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if that user's user id isn't already in the list usersInvited arraylist, add it.
                //send user name and/or userid and/or docid thru interface to add to the usersInvited list

                //also, once the owner clicks "send invite" that button should turn invisible (since they can only remove now)
                //maybe also make "remove invite" invisible until the owner adds the user to the usersInvited list

                //if the owner tries to invite themselves to their own list
                if(user.name.equals(mAuth.getCurrentUser().getDisplayName())) {
                    Toast.makeText(view.getContext(), R.string.toast_owner_invite, Toast.LENGTH_SHORT).show();
                } else {
                    recyclerAdapterListener.sendInviteUserClicked(user);
                }
            }
        });

        holder.buttonRemoveInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if that user's user id is already in the list usersInvited arraylist, remove it
                //also, once the owner clicks "remove invite", the "send invite" button should become visible again

                //if the owner tries to un-invite themselves from their own list
                if(user.name.equals(mAuth.getCurrentUser().getDisplayName())) {
                    Toast.makeText(view.getContext(), R.string.toast_owner_remove_invite, Toast.LENGTH_SHORT).show();
                } else {
                    recyclerAdapterListener.removeInviteUserClicked(user);
                }
            }
        });


/*
        //clicking on a recycler view item (a forum)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("demo", "onClick: Position of the forum you clicked: " + holder.getAdapterPosition());

                for(int i = 0; i < forumEntries.size(); i++) {
                    if(i == holder.getAdapterPosition()) {
                        Log.d("demo", "Title of id: " + forumEntries.get(i).title);
                        recyclerAdapterListener.sendClickedFragment(forumEntries.get(i));
                    }
                }
            }
        });

        holder.deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();


                Log.d("demo", "onClick: Delete doc id: " + db.collection("forums")
                        .document(forumEntries.get(holder.getAdapterPosition()).docId));

                String id = forumEntries.get(holder.getAdapterPosition()).docId;

                recyclerAdapterListener.sendDeletedForum(id);


            }
        });


        int unlikeId = R.drawable.like_not_favorite;
        int likeId = R.drawable.like_favorite;


        //if current user id is in the map/list of users that like this forum, show the filled heart
        if(forumEntry.usersLike.contains(mAuth.getCurrentUser().getUid())) {
            holder.likeUnlikeImageButton.setImageResource(likeId);
            //holder.likeUnlikeImageButton.setTag("R.drawable.like_favorite");
        } else {
            holder.likeUnlikeImageButton.setImageResource(unlikeId);
            //holder.likeUnlikeImageButton.setTag("R.drawable.like_not_favorite");
        }


        holder.likeUnlikeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                if(forumEntry.usersLike.contains(mAuth.getCurrentUser().getUid())) {
                    //unlike
                    db.collection("forums").document(forumEntries.get(holder.getAdapterPosition()).docId);

                    String id = forumEntries.get(holder.getAdapterPosition()).docId;

                    forumEntry.usersLike.remove(mAuth.getCurrentUser().getUid());
                    //update on firebase (remove the curr user id from the list on Firestore for this forum)
                    db.collection("forums").document(id).update("usersLike", forumEntry.usersLike);

                    //forumEntry.usersLike.remove(mAuth.getCurrentUser().getUid());
                    holder.likeUnlikeImageButton.setImageResource(unlikeId);
                    holder.numLikesPerForum.setText(String.valueOf(forumEntry.usersLike.size()));

                } else {
                    //like

                    db.collection("forums").document(forumEntries.get(holder.getAdapterPosition()).docId);

                    String id = forumEntries.get(holder.getAdapterPosition()).docId;

                    //update on firebase (add the curr user id from the list on Firestore for this forum)

                    forumEntry.usersLike.add(mAuth.getCurrentUser().getUid());
                    db.collection("forums").document(id).update("usersLike", forumEntry.usersLike);

                    holder.likeUnlikeImageButton.setImageResource(likeId);
                    holder.numLikesPerForum.setText(String.valueOf(forumEntry.usersLike.size()));

                }

            }
        });

         */


    }

    @Override
    public int getItemCount() {
        if(this.usersList == null) {
            return 0;
        }
        return this.usersList.size();
    }

    public static class UserInviteViewHolder extends RecyclerView.ViewHolder {
        TextView inviteMenuDisplayUserName;
        Button buttonSendInvite, buttonRemoveInvite;


        public UserInviteViewHolder(@NonNull View itemView) {
            super(itemView);

            inviteMenuDisplayUserName = itemView.findViewById(R.id.inviteMenuDisplayUserName);
            buttonSendInvite = itemView.findViewById(R.id.buttonSendInvite);
            buttonRemoveInvite = itemView.findViewById(R.id.buttonRemoveInvite);

        }
    }


    UserInviteRecyclerViewAdapter.IRecyclerAdapterListener recyclerAdapterListener;

    public interface IRecyclerAdapterListener {
        void sendInviteUserClicked(User user);
        void removeInviteUserClicked(User user);
    }

}
