/*
HW 07
MainActivity.java
Kaitlin Ennis
 */package com.example.ennis_hw07;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, RegisterFragment.RegisterFragmentListener, HomeFragment.HomeFragmentListener, CreateShoppingListFragment.CreateShoppingListFragmentListener, ShoppingListDetailFragment.ShoppingListDetailFragmentListener, CreateItemFragment.CreateItemFragmentListener, InviteMenuFragment.InviteMenuFragmentListener {

    final String TAG = "demo";
    final String FRAGMENT_TAG = "fragment";

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            //Need to login
            setTitle("Login");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerView, new LoginFragment(), FRAGMENT_TAG)
                    .commit();
        } else {
            //Logged in already; proceed to the Home page
            Log.d("demo", "onCreate: You don't need to log in :) " + mAuth.getCurrentUser().getDisplayName());
            setTitle("Home");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerView, new HomeFragment(), FRAGMENT_TAG)
                    .commit();
        }

    }

    @Override
    public void goToCreateNewAccount() {
        setTitle("Create New Account");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new RegisterFragment(), FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoHomeFragment() {
        setTitle("Home");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new HomeFragment(), FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void cancelRegister() {
        //go back to the Login fragment
        setTitle("Login");
        getSupportFragmentManager().popBackStack();
    }


    @Override
    public void logoutCurrentUser() {
        //go back to the Login fragment
        setTitle("Login");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new LoginFragment(), FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void goToNewShoppingList() {
        setTitle("New Shopping List");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new CreateShoppingListFragment(), FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void sendListClicked(ShoppingList selectedList) {
        setTitle("Shopping List Details");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, ShoppingListDetailFragment.newInstance(selectedList), FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void backToHome() {
        //go back to the Home fragment
        setTitle("Home ");
        getSupportFragmentManager().popBackStack();
    }

    //CreateItemFragment.newInstance(shoppingListChosen)
    @Override
    public void goToAddItem(ShoppingList shoppingListChosen) {
        setTitle("Add Item");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, CreateItemFragment.newInstance(shoppingListChosen.docId), FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void backToListDetail() {
        //go back to the Shopping List Details fragment
        setTitle("Shopping List Details");
        getSupportFragmentManager().popBackStack();
    }


    @Override
    public void backToListDetails() {
        //go back to the Shopping List Details fragment
        setTitle("Shopping List Details");
        getSupportFragmentManager().popBackStack();
    }



    @Override
    public void goToInviteMenu(ShoppingList shoppingListChosen) {
        setTitle("Invite Menu");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, InviteMenuFragment.newInstance(shoppingListChosen), FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }


    //String currUserId, ArrayList<String> currUserFriendsList
    @Override
    public void goToFriendsList() {
        //FriendsListFragment.newInstance(currUserId, currUserFriendsList)

        setTitle("Friends List");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new FriendsListFragment(), FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }


    //String currUserId, ArrayList<String> currUserFriendsList
    @Override
    public void registeredUserSuccessfully() {
        //HomeFragment.newInstance(currUserId, currUserFriendsList),

        setTitle("Home");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new HomeFragment(), FRAGMENT_TAG)
                .commit();

    }

    //resets the titles of each fragment when hitting the back button.
    //reverts to the previous fragment title
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG) instanceof HomeFragment) {
            setTitle("Home");
        } else if(getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG) instanceof ShoppingListDetailFragment) {
            setTitle("Shopping List Details");
        }
    }
}