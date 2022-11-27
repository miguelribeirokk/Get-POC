//package com.ufv.project.controller.java;
//
//import com.ufv.project.db.ConnectDB;
//import com.ufv.project.db.Professor_has_subjectDB;
//import com.ufv.project.model.DataModel;
//import com.ufv.project.model.Subject;
//import com.ufv.project.model.UserTypesEnum;
//
//import java.sql.SQLException;
//
//public class PersonalInfoController {
//
//    private final DataModel dataModel;
//
//    // Student
//    public PersonalInfoController(DataModel dataModel) {
//        this.dataModel = dataModel;
//    }
//
//    @FXML
//    public void initialize() {
//        UserTypesEnum userType = dataModel.getUserType();
//
//        usernameText.setText(dataModel.getUsername());
//        nameText.setText(dataModel.getName());
//        userTypeText.setText(userType.toString());
//
//        if (userType == UserTypesEnum.STUDENT) {
//            professorSubjectsLabel.setManaged(false);
//            professorSubjectListView.setManaged(false);
//            professorSubjectsLabel.setVisible(false);
//            professorSubjectListView.setVisible(false);
//
//            registrationLabel.setManaged(true);
//            registrationText.setManaged(true);
//            registrationLabel.setVisible(true);
//            registrationText.setVisible(true);
//
//            emailLabel.setManaged(true);
//            emailText.setManaged(true);
//            emailLabel.setVisible(true);
//            emailText.setVisible(true);
//
//            emailText.setText(dataModel.getEmail());
//            registrationText.setText(dataModel.getRegistration());
//        } else if (userType == UserTypesEnum.PROFESSOR) {
//            registrationLabel.setManaged(false);
//            registrationText.setManaged(false);
//            registrationLabel.setVisible(false);
//            registrationText.setVisible(false);
//
//            emailLabel.setManaged(true);
//            emailText.setManaged(true);
//            emailLabel.setVisible(true);
//            emailText.setVisible(true);
//
//            professorSubjectsLabel.setManaged(true);
//            professorSubjectListView.setManaged(true);
//            professorSubjectsLabel.setVisible(true);
//            professorSubjectListView.setVisible(true);
//
//            emailText.setText(dataModel.getEmail());
//
//            try (ConnectDB connectDB = new ConnectDB()) {
//                professorSubjectListView
//                        .getItems()
//                        .setAll(new Professor_has_subjectDB(connectDB.getConnection())
//                                .querySubjectsByProfessor(dataModel.getUsername()));
//            } catch (SQLException e) {
//                System.out.println("ERROR: Couldn't get subjects for professor: " + e.getMessage());
//            }
//        } else if (userType == UserTypesEnum.ADMIN) {
//            professorSubjectsLabel.setManaged(false);
//            professorSubjectListView.setManaged(false);
//            professorSubjectsLabel.setVisible(false);
//            professorSubjectListView.setVisible(false);
//
//            registrationLabel.setManaged(false);
//            registrationText.setManaged(false);
//            registrationLabel.setVisible(false);
//            registrationText.setVisible(false);
//
//            emailLabel.setManaged(false);
//            emailText.setManaged(false);
//            emailLabel.setVisible(false);
//            emailText.setVisible(false);
//        }
//    }
//
//}