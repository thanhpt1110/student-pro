package com.example.testfirebase.dao;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.example.testfirebase.model.Class;
import com.example.testfirebase.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ClassDAO {
    DatabaseReference classRef;
    public ClassDAO() {
        classRef = FirebaseDatabase.getInstance().getReference("classes");
    }

    public void addNewClass(String id, String name){
        classRef.child(id).child("name").setValue(name);
    }
}
