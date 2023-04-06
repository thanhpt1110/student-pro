package com.example.testfirebase.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.testfirebase.activity.AddClass;
import com.example.testfirebase.R;
import com.example.testfirebase.adapter.ClassAdapter;
import com.example.testfirebase.model.Class;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassFragment extends Fragment {
    Button btnAddClass;
    private ArrayList<Class> classes;
    private ClassAdapter classAdapter;
    private ListView classListview;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ClassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassFragment newInstance(String param1, String param2) {
        ClassFragment fragment = new ClassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_class, container, false);
        // Inflate the layout for this fragment
        btnAddClass = v.findViewById(R.id.btnAddClass);
        classListview = v.findViewById(R.id.classListview);
        classes = new ArrayList<>();
        classAdapter = new ClassAdapter(getActivity(), R.layout.custom_class_view, classes);
        getClassData();
        classListview.setAdapter(classAdapter);

        btnAddClass.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), AddClass.class);
            startActivity(i);
        });
        return v;
    }

    private void getClassData(){
        DatabaseReference classRef = FirebaseDatabase.getInstance().getReference("classes");
        classRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classes.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String classId = childSnapshot.getKey();
                    Class c = childSnapshot.getValue(Class.class);
                    c.setId(classId);
                    int numberOfStudent = (int) childSnapshot.child("students").getChildrenCount();
                    c.setNumberOfStudent(numberOfStudent);
                    classes.add(c);
                }
                classAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}