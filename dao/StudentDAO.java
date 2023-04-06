package com.example.testfirebase.dao;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.example.testfirebase.AddStudentCallBack;
import com.example.testfirebase.model.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StudentDAO {
    private DatabaseReference studentRef;
    private DatabaseReference classRef;
    public StudentDAO () {
        studentRef = FirebaseDatabase.getInstance().getReference("students");
        classRef = FirebaseDatabase.getInstance().getReference("classes");
    }

    /*public ArrayList<Student> readAllStudent(){
        ArrayList<Student> students = new ArrayList<>();
        studentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String name = childSnapshot.child("name").getValue().toString().trim();
                    String age = childSnapshot.child("age").getValue().toString().trim();
                    students.add(new Student(name, age));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return students;
    }*/

    public void addNewStudent(String name, String age, String classId, AddStudentCallBack callBack){
        DatabaseReference classRefName = FirebaseDatabase.getInstance().getReference("classes/" + classId);
        classRefName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Query lastStudentQuery = studentRef.orderByKey().limitToLast(1);
                    lastStudentQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Get last student ID
                            String lastStudentId = "";
                            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                lastStudentId = childSnapshot.getKey(); // Get the key of the last student node
                            }

                            String newStudentId = generateNewStudentId(lastStudentId);
                            Student newStudent = new Student(name, age, classId);

                            // Set new value to database
                            studentRef.child(newStudentId).setValue(newStudent);
                            classRef.child(classId).child("students").child(newStudentId).setValue(true);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    callBack.onSuccess();
                }
                else {
                    callBack.onFailure("Không tồn tại mã lớp này!");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onFailure(error.getMessage());
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private String generateNewStudentId(String lastStudentId){
        String newStudentId;
        if (lastStudentId.isEmpty())
            newStudentId = "SV001";
        else {
            int lastStudentNumber = Integer.parseInt(lastStudentId.substring(2));
            int newStudentNumber = lastStudentNumber + 1;
            newStudentId = "SV" + String.format("%03d", newStudentNumber);
        }
        return newStudentId;
    }

    public void deleteStudent(String studentId){
        // Get a reference to the student with the given ID
        studentRef = FirebaseDatabase.getInstance().getReference("students").child(studentId);

        // Retrieve the student object
        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the class ID of the student
                String classId = dataSnapshot.child("classId").getValue(String.class);

                // Decrement the numberOfStudents field of the class object with the given class ID
                classRef = FirebaseDatabase.getInstance().getReference("classes").child(classId);
                /*classRef.child("numberOfStudents").runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Integer numberOfStudents = mutableData.getValue(Integer.class);
                        if (numberOfStudents == null) {
                            return Transaction.abort();
                        }
                        mutableData.setValue(numberOfStudents - 1);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                        if (databaseError != null || !committed) {
                            Log.e(TAG, "Failed to update numberOfStudents field: " + databaseError);
                        }
                    }
                });*/

                // Remove the student object with the given ID from the students node
                studentRef.removeValue();

                // Remove the student ID from the studentIds field of the class object with the given class ID
                classRef.child("students").child(studentId).removeValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void updateStudent(String id, String name, String age, String oldClassId, String newClassId, AddStudentCallBack callBack){
        DatabaseReference classRefName = FirebaseDatabase.getInstance().getReference("classes/" + newClassId);
        classRefName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    classRef.child(oldClassId).child("students").child(id).removeValue();
                    Student student = new Student(name, age, newClassId);
                    studentRef.child(id).setValue(student);
                    classRef.child(newClassId).child("students").child(id).setValue(true);
                    callBack.onSuccess();
                }
                else {
                    callBack.onFailure("Không tồn tại mã lớp này!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callBack.onFailure(error.getMessage());
            }
        });
    }
}
