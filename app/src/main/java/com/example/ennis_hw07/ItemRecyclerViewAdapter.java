/*
HW 07
ItemRecyclerViewAdapter.java
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

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder>{

    private FirebaseAuth mAuth;

    ArrayList<Item> listItems;


    public ItemRecyclerViewAdapter(ArrayList<Item> data, Fragment fragment) {

        if(fragment instanceof ItemRecyclerViewAdapter.IRecyclerAdapterListener) {
            Log.d("demo", "ItemRecyclerViewAdapter: FRAGMENT: " + fragment.toString());
            recyclerAdapterListener = (ItemRecyclerViewAdapter.IRecyclerAdapterListener) fragment;
        } else {
            throw new RuntimeException(fragment.toString() + " must implement IRecyclerAdapterListener");
        }

        this.listItems = data;
    }

    @NonNull
    @Override
    public ItemRecyclerViewAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_entry, parent, false);
        ItemRecyclerViewAdapter.ItemViewHolder itemViewHolder =  new ItemRecyclerViewAdapter.ItemViewHolder(view);

        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRecyclerViewAdapter.ItemViewHolder holder, int position) {

        Item listItem = listItems.get(position);

        holder.displayItemName.setText(listItem.name);
        holder.displayItemCreator.setText("Added by: " + listItem.creator);
        holder.displayItemCost.setText("Estimated cost: $" + String.valueOf(listItem.estimatedCost));
        holder.displayItemStatus.setText(listItem.status);

        double total = 0.0;
        for (Item item: listItems) {
            total += Double.parseDouble(item.estimatedCost);
        }
        recyclerAdapterListener.sendItemTotal(total);

        mAuth = FirebaseAuth.getInstance();


        holder.imageButtonItemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                String id = listItems.get(holder.getAdapterPosition()).docId;

                recyclerAdapterListener.sendDeletedItem(id);
            }
        });



        if(listItem.status.equals("Acquired")) {
            holder.imageButtonItemChecked.setVisibility(View.INVISIBLE);
        }

        holder.imageButtonItemChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change status of item (acquired vs not acquired)
                //get the document id, send it to the ShoppingListDetailFragment, then change
                //the document status

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                String id = listItems.get(holder.getAdapterPosition()).docId;

                recyclerAdapterListener.sendStatusItem(id);
                holder.imageButtonItemChecked.setVisibility(View.INVISIBLE);

                /*
                if(listItem.status.equals("Not yet acquired")) {
                    listItem.status = "Acquired";
                    holder.displayItemStatus.setText(listItem.status);
                } else {
                    listItem.status = "Not yet acquired";
                    holder.displayItemStatus.setText(listItem.status);
                }
                 */
            }
        });

    }

    @Override
    public int getItemCount() {
        if(this.listItems == null) {
            return 0;
        }
        return this.listItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView displayItemName, displayItemCreator, displayItemCost, displayItemStatus;
        ImageButton imageButtonItemChecked, imageButtonItemDelete;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            displayItemName = itemView.findViewById(R.id.displayItemName);
            displayItemCreator = itemView.findViewById(R.id.displayItemCreator);
            displayItemCost = itemView.findViewById(R.id.displayItemCost);
            displayItemStatus = itemView.findViewById(R.id.displayItemStatus);
            imageButtonItemChecked = itemView.findViewById(R.id.imageButtonItemChecked);
            imageButtonItemDelete = itemView.findViewById(R.id.imageButtonItemDelete);
        }
    }


    ItemRecyclerViewAdapter.IRecyclerAdapterListener recyclerAdapterListener;

    public interface IRecyclerAdapterListener {
        void sendDeletedItem(String deleteItemId);
        void sendItemTotal(double itemTotal);
        void sendStatusItem(String statusItemId);
    }



}
