package com.ufv.project.db;


import java.sql.*;
import java.util.Calendar;

import static com.ufv.project.db.tb_area.*;
import static com.ufv.project.db.tb_discipline.*;
import static com.ufv.project.db.tb_PDF.*;
import static com.ufv.project.db.tb_keyword.*;
import static com.ufv.project.db.tb_user.*;
import static com.ufv.project.db.tb_teacher.*;
import static com.ufv.project.db.tb_poc.*;


public class ConnectDB {

    public static final String DB_NAME = "get_poc";
    public static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/" + DB_NAME;
    public static final String USER = "root";
    public static final String PASSWORD = "123456";







    /*
     * TB_Teacher table columns names
     */

    public static String TABLE_TEACHER = "TB_Teacher";
    public static String COLUMN_TEACHER_EMAIL = "Email";
    public static String COLUMN_TEACHER_ID = "TB_User_ID";






    /*
     * TB_POC table columns names
     */
    public static String TABLE_POC = "TB_POC";
    public static String COLUMN_POC_ID = "ID";
    public static String COLUMN_POC_TITLE = "Title";
    public static String COLUMN_POC_DEFENSE_DATE = "Defense_Date";
    public static String COLUMN_POC_SUMMARY = "Summary";
    public static String COLUMN_POC_AREA_ID = "TB_Area_ID";
    public static String COLUMN_POC_PDF_ID = "TB_PDF_ID";
    public static String COLUMN_POC_TEACHER_REGISTRANT_ID = "Teacher_Registrant";
    public static String COLUMN_POC_TEACHER_ADVISOR_ID = "Teacher_Advisor";

    /*
     * TB_POC_has_Keyword table columns names
     */
    public static String TABLE_POC_HAS_KEYWORD = "TB_POC_has_Keyword";
    public static String COLUMN_POC_HAS_KEYWORD_POC_ID = "TB_POC_ID";
    public static String COLUMN_POC_HAS_KEYWORD_KEYWORD_ID = "TB_Keyword_ID";

    /*
     * TB_Student table columns names
     */
    public static String TABLE_STUDENT = "TB_Student";
    public static String COLUMN_STUDENT_EMAIL = "Email";
    public static String COLUMN_STUDENT_REGISTRATION = "Registration";
    public static String COLUMN_STUDENT_POC_ID = "POC";
    public static String COLUMN_STUDENT_USER_ID = "TB_User_ID";

    /*
     * TB_Teacher_co-advises-Poc table columns names
     */

    public static String TABLE_TEACHER_CO_ADVISES_POC = "TB_Teacher_co-advises_Poc";
    public static String COLUMN_TABLE_TEACHER_CO_ADVISES_POC_POC_ID = "TB_POC_ID";
    public static String COLUMN_TABLE_TEACHER_CO_ADVISES_POC_TEACHER_ID = "tb_teacher_TB_User_ID";

    /*
     * TB_Teacher_has_Discipline table columns names
     */
    public static String TABLE_TEACHER_HAS_DISCIPLINE = "TB_Teacher_has_Discipline";
    public static String COLUMN_TEACHER_HAS_DISCIPLINE_DISCIPLINE_ID = "TB_Discipline_ID";
    public static String COLUMN_TEACHER_HAS_DISCIPLINE_TEACHER_ID = "TB_Teacher_User_ID";

    /*
     * TB_User_manages_User table columns names
     */
    public static String TABLE_USER_MANAGES_USER = "TB_User_manages_User";
    public static String COLUMN_USER_MANAGES_USER_ADMINISTRATOR_ID = "TB_User_Administrator_ID";
    public static String COLUMN_USER_MANAGES_USER_ADMINISTERED_ID = "TB_User_Administered_ID";

    /*
     * Methods
     */


    public static Connection connect() {
        /*
         * Connect to the database
         */

        try {
            return DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return null;
        }
    }

    public static void close(Connection conn) {
        /*
         *  Close the database connection
         */
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }




    public static void main(String[] args) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Couldn't find driver: " + e.getMessage());

        }


        Connection conn = connect();
        if (conn == null) {
            System.out.println("Connection failed");
            return;
        }


        addPoc(1, "POC 1", setDate(1999, 03, 04), "Summary 1", 1, 1, "nacif", "fabricio");

















        close(conn);


    }

}
