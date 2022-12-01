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
    /* Table Professor_has_subject constants. */
    private static final String TABLE_PROFESSOR_HAS_SUBJECT = "tb_professor_has_subject";
    private static final String COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID = "TB_teacher_User_id";
    private static final String COLUMN_PROFESSOR_HAS_SUBJECT_SUBJECT_ID = "TB_Discipline_id";

    /* Table Professor_has_subject constants indexes. */

    private static final int COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID_INDEX = 1;
    private static final int COLUMN_PROFESSOR_HAS_SUBJECT_FIELD_ID_INDEX = 2;

    /* Table Professor_has_subject queries. */

    private static final String QUERY_SUBJECTS_BY_PROFESSOR = "SELECT * FROM " + TABLE_PROFESSOR_HAS_SUBJECT + " WHERE " + COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID + " = ?";
    private static final String INSERT_PROFESSOR_HAS_SUBJECT = "INSERT INTO " + TABLE_PROFESSOR_HAS_SUBJECT + " (" + COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID + ", " + COLUMN_PROFESSOR_HAS_SUBJECT_SUBJECT_ID + ") VALUES (?, ?)";
    private static final String UPDATE_PROFESSOR_HAS_SUBJECT = "UPDATE " + TABLE_PROFESSOR_HAS_SUBJECT + " SET " + COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID + " = ?, " + COLUMN_PROFESSOR_HAS_SUBJECT_SUBJECT_ID + " = ? WHERE " + COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID + " = ? AND " + COLUMN_PROFESSOR_HAS_SUBJECT_SUBJECT_ID + " = ?";
    private static final String DELETE_PROFESSOR_HAS_SUBJECT = "DELETE FROM " + TABLE_PROFESSOR_HAS_SUBJECT + " WHERE " + COLUMN_PROFESSOR_HAS_SUBJECT_SUBJECT_ID + " = ? AND " + COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID + " = ?";

    /* Connection to the database. */
    private final Connection conn;

    public Professor_has_subjectDB(Connection conn)
    {
        this.conn = conn;
    }

    /* Query a subject by its ID.
     * @param    professorID     ID of the professor to query subjects from him.
     * @return   subjects with the given professor ID.
     */

    public List<Subject> querySubjectsByProfessor(String professorID) throws SQLException
    {
        try (PreparedStatement querySubjectsByProfessor = conn.prepareStatement(QUERY_SUBJECTS_BY_PROFESSOR))
        {
            querySubjectsByProfessor.setString(COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID_INDEX, professorID);

            try (ResultSet resultSet = querySubjectsByProfessor.executeQuery())
            {
                List<Subject> subjects = new ArrayList<>();
                SubjectDB subjectDB = new SubjectDB(conn);

                while (resultSet.next())
                {
                    subjects.add(subjectDB.querySubjectByID(resultSet.getInt(COLUMN_PROFESSOR_HAS_SUBJECT_SUBJECT_ID)));
                }

                return subjects;
            }
        }
    }

    /* Insert a new professor has subject.
     * @param    professorID     ID of the professor to insert in relation.
     * @param    subjectID       ID of the subject to insert in relation.
     */

    public void insertProfessorHasSubject(String professorID, int subjectID) throws SQLException
    {
        try (PreparedStatement insertProfessor_has_subject = conn.prepareStatement(INSERT_PROFESSOR_HAS_SUBJECT, PreparedStatement.RETURN_GENERATED_KEYS))
        {
            insertProfessor_has_subject.setString(COLUMN_PROFESSOR_HAS_SUBJECT_PROFESSOR_ID_INDEX, professorID);
            insertProfessor_has_subject.setInt(COLUMN_PROFESSOR_HAS_SUBJECT_FIELD_ID_INDEX, subjectID);

            if (insertProfessor_has_subject.executeUpdate() != 1)
            {
                throw new SQLException("ERROR: Couldn't insert professor_has_subject with professor ID: '" + professorID + "' and subject ID: '" + subjectID + "'.");
            }
        }
    }

    /* Delete a professor has subject.
     * @param    professorID     ID of the professor to delete in relation.
     * @param    subjectID       ID of the subject to delete in relation.
     */

    public void deleteProfessorHasSubject(String professorID, int subjectID) throws SQLException
    {
        try (PreparedStatement deleteProfessor_has_subject = conn.prepareStatement(DELETE_PROFESSOR_HAS_SUBJECT))
        {
            deleteProfessor_has_subject.setString(2, professorID);
            deleteProfessor_has_subject.setInt(1, subjectID);

            if (deleteProfessor_has_subject.executeUpdate() != 1)
            {
                throw new SQLException("ERROR: Couldn't delete professor_has_subject with professor ID: '" + professorID + "' and subject ID: '" + subjectID + "'.");
            }
        }
    }

    /* Update a professor has subject.
     *
     * @param    oldProfessorID  ID of the professor to update in relation.
     * @param    oldSubjectID    ID of the subject to update in relation.
     * @param    newProfessorID  ID of the professor to update in relation.
     * @param    newSubjectID    ID of the subject to update in relation.
     */


    public void updateProfessorHasSubject(String oldProfessorID, int oldSubjectID, String newProfessorID, int newSubjectID) throws SQLException
    {
        try (PreparedStatement updateProfessor_has_subject = conn.prepareStatement(UPDATE_PROFESSOR_HAS_SUBJECT))
        {
            updateProfessor_has_subject.setString(1, newProfessorID);
            updateProfessor_has_subject.setInt(2, newSubjectID);
            updateProfessor_has_subject.setString(3, oldProfessorID);
            updateProfessor_has_subject.setInt(4, oldSubjectID);

            if (updateProfessor_has_subject.executeUpdate() != 1)
            {
                throw new SQLException("ERROR: Couldn't update professor_has_subject with professor ID: '" + oldProfessorID + "' and subject ID: '" + oldSubjectID + "'.");
            }
        }
    }

}
