package com.collegeadmin;

import com.collegeadmin.model.Student;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class StudentFormDialog extends Dialog<Student> {
    public StudentFormDialog() {
        setTitle("Add Student");
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField name = new TextField();
        name.setPromptText("Full name");
        TextField roll = new TextField();
        roll.setPromptText("Roll number");

        VBox v = new VBox(8);
        v.getChildren().addAll(new Label("Name:"), name, new Label("Roll:"), roll);
        getDialogPane().setContent(v);

        setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                Student s = new Student();
                s.setName(name.getText());
                s.setRoll(roll.getText());
                return s;
            }
            return null;
        });
    }
}
