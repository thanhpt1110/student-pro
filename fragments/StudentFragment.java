package com.example.testfirebase.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.testfirebase.activity.AddStudent;
import com.example.testfirebase.R;
import com.example.testfirebase.activity.UpdateStudent;
import com.example.testfirebase.adapter.CustomAdapter;
import com.example.testfirebase.dao.StudentDAO;
import com.example.testfirebase.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentFragment extends Fragment {
    private Button btnAddStudent;
    private ListView listView;
    private CustomAdapter customAdapter;
    private ArrayList<Student> students;
    private StudentDAO studentDAO;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StudentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentFragment newInstance(String param1, String param2) {
        StudentFragment fragment = new StudentFragment();
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
        View v = inflater.inflate(R.layout.fragment_student, container, false);
        btnAddStudent = v.findViewById(R.id.btnAddStudent);
        listView = v.findViewById(R.id.studentListview);

        students = new ArrayList<>();
        customAdapter = new CustomAdapter(getActivity(), R.layout.custom_student_view, students);
        getStudentData();
        listView.setAdapter(customAdapter);

        btnAddStudent.setOnClickListener(view -> {
            Intent i = new Intent(getActivity(), AddStudent.class);
            startActivity(i);
        });

        // Click to update
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Student student = (Student) adapterView.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putString("studentId", student.getId());
                bundle.putString("studentName", student.getName());
                bundle.putString("studentAge",  student.getAge());
                bundle.putString("classId", student.getClassId());

                Intent intent = new Intent(getActivity(), UpdateStudent.class);
                intent.putExtra("studentData", bundle);
                startActivity(intent);
            }
        });

        // Long click to delete
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Get the student object from the adapter
                Student student = (Student) adapterView.getItemAtPosition(position);

                // Get the student ID from the tag of the selected row
                String studentId = student.getId();

                // Show a dialog to confirm deletion
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Xóa thông tin sinh viên")
                        .setMessage("Bạn có chắc chắn muốn xóa " + student.getName() + " ?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete the student from Firebase
                                studentDAO = new StudentDAO();
                                studentDAO.deleteStudent(studentId);
                                students.remove(position);
                                customAdapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
                // Return true to indicate that the event has been consumed
                return true;
            }
        });
        return v;
    }

    private void getStudentData(){
        DatabaseReference studentRef = FirebaseDatabase.getInstance().getReference("students");
        studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                students.clear();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String studentId = childSnapshot.getKey();
                    Student student = childSnapshot.getValue(Student.class);
                    student.setId(studentId);
                    students.add(student);
                }
                customAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}