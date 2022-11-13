package com.ufv.project.db;

import com.ufv.project.model.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Professor_has_subjectDB
{
    private static final String TABLE_PROFESSOR_HAS_SUBJECT = "tb_professor_has_subject";
    private static final String COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID = "TB_teacher_User_id";
    private static final String COLUMN_PROFESSOR_HAS_SUBJECT_SUBJECT_ID = "TB_Discipline_id";

    private static final int COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID_INDEX = 1;
    private static final int COLUMN_PROFESSOR_HAS_SUBJECT_FIELD_ID_INDEX = 2;

    private static final String QUERY_SUBJECTS_BY_PROFESSOR = "SELECT * FROM " + TABLE_PROFESSOR_HAS_SUBJECT + " WHERE " + COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID + " = ?";
    private static final String QUERY_PROFESSOR_HAS_SUBJECTS = "SELECT * FROM " + TABLE_PROFESSOR_HAS_SUBJECT;
    private static final String INSERT_PROFESSOR_HAS_SUBJECT = "INSERT INTO " + TABLE_PROFESSOR_HAS_SUBJECT + " (" + COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID + ", " + COLUMN_PROFESSOR_HAS_SUBJECT_SUBJECT_ID + ") VALUES (?, ?)";
    private static final String UPDATE_PROFESSOR_HAS_SUBJECT = "UPDATE " + TABLE_PROFESSOR_HAS_SUBJECT + " SET " + COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID + " = ?, " + COLUMN_PROFESSOR_HAS_SUBJECT_SUBJECT_ID + " = ? WHERE " + COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID + " = ? AND " + COLUMN_PROFESSOR_HAS_SUBJECT_SUBJECT_ID + " = ?";
    private static final String DELETE_PROFESSOR_HAS_SUBJECT = "DELETE FROM " + TABLE_PROFESSOR_HAS_SUBJECT + " WHERE " + COLUMN_PROFESSOR_HAS_SUBJECT_SUBJECT_ID + " = ? AND " + COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID + " = ?";

    private final Connection conn;

    private final PreparedStatement querySubjectsByProfessor;
    private final PreparedStatement queryProfessor_has_subjects;
    private final PreparedStatement insertProfessor_has_subject;
    private final PreparedStatement deleteProfessor_has_subject;
    private final PreparedStatement updateProfessor_has_subject;

    public Professor_has_subjectDB(Connection conn) throws SQLException
    {
        this.conn = conn;

        querySubjectsByProfessor = conn.prepareStatement(QUERY_SUBJECTS_BY_PROFESSOR);
        queryProfessor_has_subjects = conn.prepareStatement(QUERY_PROFESSOR_HAS_SUBJECTS);
        insertProfessor_has_subject = conn.prepareStatement(INSERT_PROFESSOR_HAS_SUBJECT, PreparedStatement.RETURN_GENERATED_KEYS);
        deleteProfessor_has_subject = conn.prepareStatement(DELETE_PROFESSOR_HAS_SUBJECT);
        updateProfessor_has_subject = conn.prepareStatement(UPDATE_PROFESSOR_HAS_SUBJECT);
    }

    public List<Subject> querySubjectsByProfessor(String professorID) throws SQLException
    {
        SubjectDB subjectDB = new SubjectDB(conn);
        querySubjectsByProfessor.setString(1, professorID);

        ResultSet resultSet = querySubjectsByProfessor.executeQuery();
        List<Subject> subjects = new ArrayList<>();

        while (resultSet.next())
        {
            subjects.add(subjectDB.querySubjectByID(resultSet.getInt(COLUMN_PROFESSOR_HAS_SUBJECT_SUBJECT_ID)));
        }

        return subjects;
    }

    public void insertProfessorHasSubject(String professorID, int subjectID) throws SQLException
    {
        insertProfessor_has_subject.setString(1, professorID);
        insertProfessor_has_subject.setInt(2, subjectID);

        int affectedRows = insertProfessor_has_subject.executeUpdate();

        if (affectedRows != 1)
        {
            throw new SQLException("Couldn't insert professor_has_subject!");
        }
    }

    public void deleteProfessorHasSubject(String professorID, int subjectID) throws SQLException
    {
        deleteProfessor_has_subject.setString(2, professorID);
        deleteProfessor_has_subject.setInt(1, subjectID);


        int affectedRows = deleteProfessor_has_subject.executeUpdate();

        if (affectedRows != 1)
        {
            throw new SQLException("Couldn't delete professor_has_subject!");
        }
    }

    public void updateProfessorHasSubject(String oldProfessorID, int oldSubjectID, String newProfessorID, int newSubjectID) throws SQLException
    {
        updateProfessor_has_subject.setString(1, newProfessorID);
        updateProfessor_has_subject.setInt(2, newSubjectID);
        updateProfessor_has_subject.setString(3, oldProfessorID);
        updateProfessor_has_subject.setInt(4, oldSubjectID);

        int affectedRows = updateProfessor_has_subject.executeUpdate();

        if (affectedRows != 1)
        {
            throw new SQLException("Couldn't update professor_has_subject!");
        }
    }

    public void close() throws SQLException
    {
        if (querySubjectsByProfessor != null)
        {
            querySubjectsByProfessor.close();
        }
        if (queryProfessor_has_subjects != null)
        {
            queryProfessor_has_subjects.close();
        }
        if (insertProfessor_has_subject != null)
        {
            insertProfessor_has_subject.close();
        }
        if (deleteProfessor_has_subject != null)
        {
            deleteProfessor_has_subject.close();
        }
        if (updateProfessor_has_subject != null)
        {
            updateProfessor_has_subject.close();
        }
    }

}
