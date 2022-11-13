package com.ufv.project.db;

import com.ufv.project.model.Professor;
import com.ufv.project.model.Student;
import com.ufv.project.model.User;
import com.ufv.project.model.UserTypesEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDB
{
    private static final String TABLE_STUDENT = "TB_Student";
    private static final String COLUMN_STUDENT_EMAIL = "Email";
    private static final String COLUMN_STUDENT_REGISTRATION = "Registration";
    private static final String COLUMN_STUDENT_POC = "POC";
    private static final String COLUMN_USER_STUDENT_ID = "TB_User_ID";

    private static final int COLUMN_STUDENT_EMAIL_INDEX = 1;
    private static final int COLUMN_STUDENT_REGISTRATION_INDEX = 2;
    private static final int COLUMN_STUDENT_POC_INDEX = 3;
    private static final int COLUMN_USER_STUDENT_ID_INDEX = 4;

    private static final String QUERY_STUDENT = "SELECT * FROM " + TABLE_STUDENT + " WHERE " + COLUMN_USER_STUDENT_ID + " = ?";
    private static final String QUERY_STUDENTS = "SELECT * FROM " + TABLE_STUDENT;
    private static final String UPDATE_STUDENT = "UPDATE " + TABLE_STUDENT + " SET " + COLUMN_STUDENT_REGISTRATION + " = ?, " + COLUMN_STUDENT_EMAIL + " = ?, " + COLUMN_STUDENT_POC + " = ? WHERE " + COLUMN_USER_STUDENT_ID + " = ?";
    private static final String INSERT_STUDENT = "INSERT INTO " + TABLE_STUDENT + " (" + COLUMN_STUDENT_EMAIL + ", " + COLUMN_STUDENT_REGISTRATION + ", " + COLUMN_STUDENT_POC + ", " + COLUMN_USER_STUDENT_ID + ") VALUES (?, ?, ?, ?)";
    private static final String DELETE_STUDENT = "DELETE FROM " + TABLE_STUDENT + " WHERE " + COLUMN_USER_STUDENT_ID + " = ?";

    private PreparedStatement queryStudent;
    private PreparedStatement queryStudents;
    private PreparedStatement insertStudent;
    private PreparedStatement deleteStudent;
    private PreparedStatement updateStudent;

    private Connection conn;

    public StudentDB(Connection conn)
    {
        this.conn = conn;

        try
        {
            queryStudent = conn.prepareStatement(QUERY_STUDENT);
            queryStudents = conn.prepareStatement(QUERY_STUDENTS);
            insertStudent = conn.prepareStatement(INSERT_STUDENT, PreparedStatement.RETURN_GENERATED_KEYS);
            deleteStudent = conn.prepareStatement(DELETE_STUDENT);
            updateStudent = conn.prepareStatement(UPDATE_STUDENT);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    protected Student queryStudent(String username, String name, String password) throws SQLException
    {
        queryStudent.setString(1, username);

        try (ResultSet resultSet = queryStudent.executeQuery())
        {
            if (resultSet.next())
            {
                return new Student(username,
                        name,
                        password,
                        resultSet.getString(COLUMN_STUDENT_REGISTRATION_INDEX),
                        resultSet.getInt(COLUMN_STUDENT_POC_INDEX),
                        resultSet.getString(COLUMN_STUDENT_EMAIL_INDEX)
                );
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    protected String insertStudent(Student student) throws SQLException
    {
        insertStudent.setString(COLUMN_STUDENT_EMAIL_INDEX, student.getEmail());
        insertStudent.setString(COLUMN_STUDENT_REGISTRATION_INDEX, student.getRegistration());
        insertStudent.setInt(COLUMN_STUDENT_POC_INDEX, student.getPoc_id());
        insertStudent.setString(COLUMN_USER_STUDENT_ID_INDEX, student.getUsername());

        int affectedRows = insertStudent.executeUpdate();

        if (affectedRows != 1)
        {
            throw new SQLException("Couldn't insert student!");
        }
        return student.getUsername();
    }


    protected Student deleteStudent(String username, String name, String password) throws SQLException
    {
        Student foundStudent = queryStudent(username, name, password);
        deleteStudent.setString(1, username);

        int affectedRows = deleteStudent.executeUpdate();

        if (affectedRows != 1)
        {
            throw new SQLException("Couldn't delete student!");
        }

        return foundStudent;
    }

    protected Student updateStudent(Student newStudent) throws SQLException
    {
        Student oldStudent = queryStudent(newStudent.getUsername(), newStudent.getName(), newStudent.getPassword());

        if (oldStudent == null)
        {
            System.out.println("Student not found");
            return null;
        }

        if (newStudent.getEmail() != null)
        {
            oldStudent.setEmail(newStudent.getEmail());
        }

        if (newStudent.getRegistration() != null)
        {
            oldStudent.setRegistration(newStudent.getRegistration());
        }
        if (newStudent.getPoc_id() != 0)
        {
            oldStudent.setPoc_id(newStudent.getPoc_id());
        }

        if (newStudent.getUsername() != null)
        {
            oldStudent.setUsername(newStudent.getUsername());
        }

        updateStudent.setString(1, oldStudent.getRegistration());
        updateStudent.setString(2, oldStudent.getEmail());
        updateStudent.setInt(3, oldStudent.getPoc_id());
        updateStudent.setString(4, oldStudent.getUsername());

        int affectedRows = updateStudent.executeUpdate();

        if (affectedRows != 1)
        {
            throw new SQLException("Couldn't update student!");
        }

        return oldStudent;
    }

    public List<Student> getAllStudents() throws SQLException
    {
        return new UserDB(conn).queryUsers().stream()
                .filter(user -> user.getUserType() == UserTypesEnum.STUDENT)
                .map(user -> (Student) user)
                .toList();
    }

    protected void close()
    {
        try
        {
            if (queryStudent != null)
            {
                queryStudent.close();
            }
            if (queryStudents != null)
            {
                queryStudents.close();
            }
            if (insertStudent != null)
            {
                insertStudent.close();
            }
            if (deleteStudent != null)
            {
                deleteStudent.close();
            }
            if (updateStudent != null)
            {
                updateStudent.close();
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
