package com.ufv.project.db;

import com.ufv.project.model.Field;
import com.ufv.project.model.PDF;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class PDFDB {
    /*
     * TB_PDF table columns names
     */

    public static final String TABLE_PDF = "TB_PDF";
    public static final String COLUMN_PDF_ID = "ID";
    public static final String COLUMN_PDF_PATH = "Path";
    public static final String COLUMN_PDF_CREATION_DATE = "Creation_Date";

    public static final int COLUMN_PDF_ID_INDEX = 1;
    public static final int COLUMN_PDF_CREATION_DATE_INDEX = 2;
    public static final int COLUMN_PDF_PATH_INDEX = 3;

    public static final String QUERY_PDF = "SELECT * FROM " + TABLE_PDF + " WHERE " + COLUMN_PDF_ID + " = ?";

    public static final String QUERY_PDFS = "SELECT * FROM " + TABLE_PDF;
    public static final String INSERT_PDF = "INSERT INTO " + TABLE_PDF + " (" + COLUMN_PDF_ID + ", " + COLUMN_PDF_CREATION_DATE + ", " + COLUMN_PDF_PATH + ") VALUES (?, ?, ?)";

    public static final String DELETE_PDF = "DELETE FROM " + TABLE_PDF + " WHERE " + COLUMN_PDF_ID + " = ?";
    public static final String UPDATE_PDF = "UPDATE " + TABLE_PDF + " SET " + COLUMN_PDF_PATH + " = ?, " + COLUMN_PDF_CREATION_DATE + " = ? WHERE " + COLUMN_PDF_ID + " = ?";
    private PreparedStatement queryPDF;
    private PreparedStatement queryPDFs;
    private PreparedStatement insertPDF;
    private PreparedStatement deletePDF;
    private PreparedStatement updatePDF;


    private Connection conn;

    public PDFDB(Connection conn) {
        this.conn = conn;

        try {
            queryPDF = conn.prepareStatement(QUERY_PDF);
            queryPDFs = conn.prepareStatement(QUERY_PDFS);
            insertPDF = conn.prepareStatement(INSERT_PDF, Statement.RETURN_GENERATED_KEYS);
            deletePDF = conn.prepareStatement(DELETE_PDF);
            updatePDF = conn.prepareStatement(UPDATE_PDF);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public PDF queryPDFByID(int id) throws SQLException {
        queryPDF.setInt(1, id);
        try (ResultSet results = queryPDF.executeQuery()) {
            if (results.next()) {
                return new PDF(results.getInt(COLUMN_PDF_ID_INDEX), results.getString(COLUMN_PDF_PATH_INDEX), results.getDate(COLUMN_PDF_CREATION_DATE_INDEX).toLocalDate());
            } else {
                return null;
            }
        }
    }

    public List<PDF> queryPDFs() throws SQLException {
        try (ResultSet results = queryPDFs.executeQuery()) {
            List<PDF> pdfs = new ArrayList<>();
            while (results.next()) {
                pdfs.add(new PDF(results.getInt(COLUMN_PDF_ID_INDEX), results.getString(COLUMN_PDF_PATH_INDEX), results.getDate(COLUMN_PDF_CREATION_DATE_INDEX).toLocalDate()));
            }
            return pdfs;
        }
    }

    public int insertPDF(PDF pdfToInsert) throws SQLException {
        insertPDF.setInt(COLUMN_PDF_ID_INDEX, pdfToInsert.getId());
        insertPDF.setDate(COLUMN_PDF_CREATION_DATE_INDEX, Date.valueOf(pdfToInsert.getLastModificationDate()));
        insertPDF.setString(COLUMN_PDF_PATH_INDEX, pdfToInsert.getPath());
        System.out.println(insertPDF.toString());

        int affectedRows = insertPDF.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException("Couldn't insert pdf");
        }

        try (ResultSet generatedKeys = insertPDF.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for pdf");
            }
        }

    }

    public PDF deletePDF(PDF pdfToDelete) throws SQLException {
        deletePDF.setInt(COLUMN_PDF_ID_INDEX, pdfToDelete.getId());
        int affectedRows = deletePDF.executeUpdate();
        if (affectedRows == 1) {
            return pdfToDelete;
        } else {
            return null;
        }
    }

    public PDF updatePDF(PDF pdfToUpdate) throws SQLException {
        PDF oldPDF = queryPDFByID(pdfToUpdate.getId());
        if (oldPDF == null) {
            return null;
        }

        if (pdfToUpdate.getPath() != null) {
            updatePDF.setString(1, pdfToUpdate.getPath());
        } else {
            updatePDF.setString(1, oldPDF.getPath());
        }

        if (pdfToUpdate.getLastModificationDate() != null) {
            updatePDF.setDate(2, Date.valueOf(pdfToUpdate.getLastModificationDate()));
        } else {
            updatePDF.setDate(2, Date.valueOf(oldPDF.getLastModificationDate()));
        }
        System.out.println(updatePDF.toString());

        updatePDF.setInt(3, pdfToUpdate.getId());
        int affectedRows = updatePDF.executeUpdate();
        if (affectedRows == 1) {
            return pdfToUpdate;
        } else {
            return null;
        }
    }
    void close() throws SQLException {
        if (queryPDF != null) {
            queryPDF.close();
        }
        if (queryPDFs != null) {
            queryPDFs.close();
        }
        if (insertPDF != null) {
            insertPDF.close();
        }
        if (deletePDF != null) {
            deletePDF.close();
        }
        if (updatePDF != null) {
            updatePDF.close();
        }
    }


}
