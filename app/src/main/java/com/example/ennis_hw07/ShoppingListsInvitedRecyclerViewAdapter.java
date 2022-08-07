/*
HW 07
ShoppingListsInvitedRecyclerViewAdapter.java
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ShoppingListsInvitedRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingListsInvitedRecyclerViewAdapter.ShoppingListViewHolder>{
    private FirebaseAuth mAuth;

    ArrayList<ShoppingList> shoppingListsInvited;

    public ShoppingListsInvitedRecyclerViewAdapter(ArrayList<ShoppingList> data, Fragment fragment) {

        if(fragment instanceof ShoppingListsInvitedRecyclerViewAdapter.IRecyclerAdapterListener) {
            Log.d("demo", "ShoppingListsInvitedRecyclerViewAdapter: FRAGMENT: " + fragment.toString());
            recyclerAdapterListener = (ShoppingListsInvitedRecyclerViewAdapter.IRecyclerAdapterListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString() + " must implement IRecyclerAdapterListener");
        }

        this.shoppingListsInvited = data;
    }

    @NonNull
    @Override
    public ShoppingListsInvitedRecyclerViewAdapter.ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invited_list_row_item, parent, false);
        ShoppingListsInvitedRecyclerViewAdapter.ShoppingListViewHolder shoppingListViewHolder =  new ShoppingListsInvitedRecyclerViewAdapter.ShoppingListViewHolder(view);

        return shoppingListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListsInvitedRecyclerViewAdapter.ShoppingListViewHolder holder, int position) {

        ShoppingList shoppingList = shoppingListsInvited.get(position);

        holder.displayListInviteTitle.setText(shoppingList.title);
        holder.displayListInviteCreator.setText("Invited by: " + shoppingList.creator);

        mAuth = FirebaseAuth.getInstance();

        holder.leaveListInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove the current user's user id from the selected shopping list invite list

                //send the selected shopping list that the current user wants to leave
                recyclerAdapterListener.sendLeaveShoppingList(shoppingList);
            }
        });

        //clicking on a recycler view item (a shopping list shared with you)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("demo", "onClick: Position of the shopping list you clicked: " + holder.getAdapterPosition());

                for(int i = 0; i < shoppingListsInvited.size(); i++) {
                    if(i == holder.getAdapterPosition()) {
                        Log.d("demo", "Title of id: " + shoppingListsInvited.get(i).title);
                        recyclerAdapterListener.sendClickedShoppingList(shoppingListsInvited.get(i));
                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if(this.shoppingListsInvited == null) {
            return 0;
        }
        return this.shoppingListsInvited.size();
    }

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder {

        TextView displayListInviteTitle, displayListInviteCreator;
        Button leaveListInviteButton;


        public ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);

            displayListInviteTitle = itemView.findViewById(R.id.displayListInviteTitle);
            displayListInviteCreator = itemView.findViewById(R.id.displayListInviteCreator);
            leaveListInviteButton = itemView.findViewById(R.id.leaveListInviteButton);

        }
    }


    ShoppingListsInvitedRecyclerViewAdapter.IRecyclerAdapterListener recyclerAdapterListener;

    public interface IRecyclerAdapterListener {
        void sendLeaveShoppingList(ShoppingList leaveList);
        void sendClickedShoppingList(ShoppingList selectedList);
    }

}
