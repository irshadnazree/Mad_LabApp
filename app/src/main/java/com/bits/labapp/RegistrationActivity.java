package com.bits.labapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bits.labapp.databinding.ActivityRegistrationBinding;
import com.bits.labapp.sqlite.StudentDB;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;

    private Vector<Student> students;
    private StudentAdapter adapter;

    private DatePickerDialog datePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.fabAdd.setOnClickListener(this::fnAdd);

        binding.edtBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                int mHour = cldr.get(Calendar.HOUR_OF_DAY);
                int mMinute = cldr.get(Calendar.MINUTE);
                String strDay = "";
                // date picker dialog
                datePicker = new DatePickerDialog(RegistrationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                binding.edtBirthdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });

        StudentDB studentDB = new StudentDB(getApplicationContext());
        ArrayList<Student> studentArrayList = (ArrayList<Student>) studentDB.fnGetAllStudents();
        if (studentArrayList.isEmpty())
            students = new Vector<>();
        else
            students = new Vector<Student>(studentArrayList);

        adapter = new StudentAdapter(getLayoutInflater(), students);

        binding.rcvStud.setAdapter(adapter);
        binding.rcvStud.setLayoutManager(new LinearLayoutManager(this));
    }

    private void fnAdd(View view) {
        String fullname = binding.edtFullName.getText().toString();
        String studNo = binding.edtStudNum.getText().toString();
        String email = binding.edtEmail.getText().toString();
        String birth = binding.edtBirthdate.getText().toString();
        String gender = "";
        String state = binding.spnState.getSelectedItem().toString();

        if (binding.rbMale.isChecked())
            gender = binding.rbMale.getText().toString();
        else if (binding.rbFemale.isChecked())
            gender = binding.rbFemale.getText().toString();

        Student student = new Student(fullname, studNo, email, gender, birth, state);

        students.add(student);
        adapter.notifyItemInserted(students.size());

        StudentDB studentDB = new StudentDB(getApplicationContext());
        int code = (int) studentDB.fnInsertStudent(student);

        if (code == -1)
            Toast.makeText(getApplicationContext(), "Unable to save data. Might be conflict primary key.", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Data saved!" + code, Toast.LENGTH_SHORT).show();


    }
}