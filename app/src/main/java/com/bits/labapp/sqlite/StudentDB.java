package com.bits.labapp.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.bits.labapp.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentDB extends SQLiteOpenHelper {
    public static final String dbName = "dbStudent";
    public static final int version = 1;
    public static final String tblStudent = "studentTable";
    public static final String colFullName = "stud_name";
    public static final String colStudNo = "stud_no";
    public static final String colGender = "stud_gender";
    public static final String colState = "stud_state";
    public static final String colEmail = "stud_email";
    public static final String colBirth = "stud_birth";

    public static final int intVrsn =1;

    public static final String strCrtTblDbStudent = "CREATE TABLE " + tblStudent +" ("+ colStudNo + " INTEGER PRIMARY KEY, " + colFullName +" TEXT, " + colGender + " TEXT," + colState+ " TEXT, " + colEmail + " TEXT, " + colBirth + " DATE)";

    public static final String strDropTblDbStudent = "DROP TABLE IF EXISTS " + tblStudent;

    public StudentDB(@Nullable Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(strCrtTblDbStudent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(strDropTblDbStudent);
        onCreate(sqLiteDatabase);
    }

    public float fnInsertStudent(Student insStudent)
    {
        float retResult = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(colFullName, insStudent.getStrFullname());
        values.put(colEmail, insStudent.getStrEmail());
        values.put(colGender, insStudent.getStrGender());
        values.put(colBirth, insStudent.getStrBirthdate());
        values.put(colState, insStudent.getStrState());

        retResult = sqLiteDatabase.insert(tblStudent, null, values);
        return retResult;
    }

    @SuppressLint("Range")
    public List<Student> fnGetAllStudents()
    {
        List<Student> studLists = new ArrayList<>();
        String strSelectAll = "Select * from " + tblStudent;
        Cursor cursor = this.getReadableDatabase().rawQuery(strSelectAll, null);
        if(cursor.moveToFirst())
        {
            do {
                String strStudName = cursor.getString(cursor.getColumnIndex((colFullName)));
                String strStudNo = cursor.getString(cursor.getColumnIndex((colStudNo)));
                String strEmail = cursor.getString(cursor.getColumnIndex((colEmail)));
                String strGender = cursor.getString(cursor.getColumnIndex((colGender)));
                String strBirth = cursor.getString(cursor.getColumnIndex((colBirth)));
                String strState = cursor.getString(cursor.getColumnIndex((colState)));

                Student student = new Student(strStudName,strStudNo,strEmail,strState,strBirth,strGender);
                studLists.add(student);
            }while(cursor.moveToNext());
        }
        return studLists;
    }
}
