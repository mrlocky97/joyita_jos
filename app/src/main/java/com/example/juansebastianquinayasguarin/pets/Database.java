package com.example.juansebastianquinayasguarin.pets;

import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by "El equipo de desarrollo de la aplicacion" on 24/5/18.
 */

public class Database {
    GotUser gotUser;
    GotPost gotPost;

    private Usuario user;
    private Post post;


    public Usuario getLoggedUser(Activity activity){

        final Activity act = activity;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userRef = firebaseUser.getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("usuarios");
        mDatabase.child(userRef).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(Usuario.class);
                gotUser = (GotUser) act;
                gotUser.loggedUser(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return user;
    }

    public Post getLoggedPost(Activity activity, String idPost) {

        final Activity act = activity;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String postRef = idPost;
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("post");
        mDatabase.child(postRef).addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                post = dataSnapshot.getValue(Post.class);
                gotPost = (GotPost) act;
                gotPost.getPostdate(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return post;
    }
}
