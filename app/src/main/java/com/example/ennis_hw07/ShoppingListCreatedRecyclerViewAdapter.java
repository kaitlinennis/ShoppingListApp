/*
HW 07
ShoppingListCreatedRecyclerViewAdapter.java
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

public class ShoppingListCreatedRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingListCreatedRecyclerViewAdapter.ShoppingListViewHolder>{
    private FirebaseAuth mAuth;

    ArrayList<ShoppingList> shoppingListsCreated;

    public ShoppingListCreatedRecyclerViewAdapter(ArrayList<ShoppingList> data, Fragment fragment) {

        if(fragment instanceof ShoppingListCreatedRecyclerViewAdapter.IRecyclerAdapterListener) {
            Log.d("demo", "ShoppingListCreatedRecyclerViewAdapter: FRAGMENT: " + fragment.toString());
            recyclerAdapterListener = (ShoppingListCreatedRecyclerViewAdapter.IRecyclerAdapterListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString() + " must implement IRecyclerAdapterListener");
        }

        this.shoppingListsCreated = data;
    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.your_list_row_item, parent, false);
        ShoppingListViewHolder shoppingListViewHolder =  new ShoppingListViewHolder(view);

        return shoppingListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position) {

        ShoppingList shoppingList = shoppingListsCreated.get(position);

        holder.displayListCreateTitle.setText(shoppingList.title);
        holder.displayListCreateCreator.setText("Created by: " + shoppingList.creator);

        mAuth = FirebaseAuth.getInstance();


        /*
        //only shows the trash can icon for shopping lists the current user created
        if(shoppingListsCreated.get(holder.getAdapterPosition()).creator.equals(mAuth.getCurrentUser().getDisplayName())) {
            holder.deleteCreateListImageButton.setVisibility(View.VISIBLE);
        } else {
            holder.deleteCreateListImageButton.setVisibility(View.INVISIBLE);
        }

         */


        //clicking on a recycler view item (a shopping list)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("demo", "onClick: Position of the shopping list you clicked: " + holder.getAdapterPosition());

                for(int i = 0; i < shoppingListsCreated.size(); i++) {
                    if(i == holder.getAdapterPosition()) {
                        Log.d("demo", "Title of id: " + shoppingListsCreated.get(i).title);
                        recyclerAdapterListener.sendClickedShoppingList(shoppingListsCreated.get(i));
                    }
                }
            }
        });




        holder.deleteCreateListImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Log.d("demo", "onClick: Delete doc id: " + db.collection("shoppingLists")
                        .document(shoppingListsCreated.get(holder.getAdapterPosition()).docId));

                String id = shoppingListsCreated.get(holder.getAdapterPosition()).docId;

                recyclerAdapterListener.sendDeletedShoppingList(id);
            }
        });



    }

    @Override
    public int getItemCount() {
        if(this.shoppingListsCreated == null) {
            return 0;
        }
        return this.shoppingListsCreated.size();
    }

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        TextView displayListCreateTitle, displayListCreateCreator;
        ImageButton deleteCreateListImageButton;


        public ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);

            displayListCreateTitle = itemView.findViewById(R.id.displayListCreateTitle);
            displayListCreateCreator = itemView.findViewById(R.id.displayListCreateCreator);
            deleteCreateListImageButton = itemView.findViewById(R.id.deleteCreateListImageButton);

        }
    }


    ShoppingListCreatedRecyclerViewAdapter.IRecyclerAdapterListener recyclerAdapterListener;

    public interface IRecyclerAdapterListener {
        void sendDeletedShoppingList(String deleteShoppingListId);
        void sendClickedShoppingList(ShoppingList shoppingListSelected);
    }

}
