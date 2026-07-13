package com.collegeadmin;

import com.collegeadmin.config.DataLoader;
import com.collegeadmin.dao.DBHelper;
import com.collegeadmin.dao.ExamDAO;
import com.collegeadmin.dao.RoomDAO;
import com.collegeadmin.dao.StudentDAO;
import com.collegeadmin.model.Exam;
import com.collegeadmin.model.Room;
import com.collegeadmin.model.Student;
import com.collegeadmin.service.SeatingService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ExamSeatingApp extends Application {

    private Stage primaryStage;

    private final StudentDAO studentDAO = new StudentDAO();
    private final ExamDAO examDAO = new ExamDAO();
    private final RoomDAO roomDAO = new RoomDAO();
    private final SeatingService seatingService = new SeatingService();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        DBHelper.initDatabase();
        DataLoader.loadDemoData();
    }

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("College Administration - Smart Exam Seating");

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        // Top: title bar
        Label title = new Label("Smart Exam Seating System");
        title.getStyleClass().add("title");
        HBox top = new HBox(title);
        top.setPadding(new Insets(12));
        top.setAlignment(Pos.CENTER_LEFT);
        root.setTop(top);

        // Left: navigation
        VBox nav = new VBox(8);
        nav.setPadding(new Insets(12));
        nav.getStyleClass().add("nav");
        Button btnDashboard = new Button("Dashboard");
        Button btnStudents = new Button("Students");
        Button btnExams = new Button("Exams");
        Button btnRooms = new Button("Rooms");
        Button btnSeating = new Button("Seating Plan");
        Button btnExit = new Button("Exit");
        btnDashboard.setMaxWidth(Double.MAX_VALUE);
        btnStudents.setMaxWidth(Double.MAX_VALUE);
        btnExams.setMaxWidth(Double.MAX_VALUE);
        btnRooms.setMaxWidth(Double.MAX_VALUE);
        btnSeating.setMaxWidth(Double.MAX_VALUE);
        btnExit.setMaxWidth(Double.MAX_VALUE);
        nav.getChildren().addAll(btnDashboard, btnStudents, btnExams, btnRooms, btnSeating, btnExit);
        root.setLeft(nav);

        // Center: content area
        BorderPane content = new BorderPane();
        content.setPadding(new Insets(12));
        root.setCenter(content);

        // Footer
        Label footer = new Label("© College Admin - Smart Exam Seating");
        HBox foot = new HBox(footer);
        foot.setPadding(new Insets(8));
        foot.setAlignment(Pos.CENTER);
        root.setBottom(foot);

        // Handlers
        btnDashboard.setOnAction(e -> content.setCenter(createDashboard()));
        btnStudents.setOnAction(e -> content.setCenter(createStudentView()));
        btnExams.setOnAction(e -> content.setCenter(createExamView()));
        btnRooms.setOnAction(e -> content.setCenter(createRoomView()));
        btnSeating.setOnAction(e -> content.setCenter(createSeatingView()));
        btnExit.setOnAction(e -> primaryStage.close());

        content.setCenter(createDashboard());

        Scene scene = new Scene(root, 1000, 640);
        scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createDashboard() {
        VBox v = new VBox(10);
        v.setPadding(new Insets(8));
        v.getStyleClass().add("card");

        Label h = new Label("Dashboard");
        h.getStyleClass().add("heading");

        List<Student> students = studentDAO.findAll();
        List<Exam> exams = examDAO.findAll();
        List<Room> rooms = roomDAO.findAll();

        Label s = new Label("Students: " + students.size());
        Label e = new Label("Exams: " + exams.size());
        Label r = new Label("Rooms: " + rooms.size());
        v.getChildren().addAll(h, s, e, r);
        return v;
    }

    private VBox createStudentView() {
        VBox v = new VBox(8);
        v.getStyleClass().add("card");
        Label h = new Label("Students");
        h.getStyleClass().add("heading");

        TableView<Student> table = new TableView<>();
        TableColumn<Student, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(param -> param.getValue().idProperty().asObject());
        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(param -> param.getValue().nameProperty());
        TableColumn<Student, String> rollCol = new TableColumn<>("Roll");
        rollCol.setCellValueFactory(param -> param.getValue().rollProperty());
        table.getColumns().addAll(idCol, nameCol, rollCol);

        ObservableList<Student> list = FXCollections.observableArrayList(studentDAO.findAll());
        table.setItems(list);

        Button add = new Button("Add");
        Button del = new Button("Delete");
        HBox controls = new HBox(8, add, del);

        add.setOnAction(e -> {
            StudentFormDialog dlg = new StudentFormDialog();
            var res = dlg.showAndWait();
            res.ifPresent(student -> {
                studentDAO.save(student);
                table.getItems().setAll(studentDAO.findAll());
            });
        });

        del.setOnAction(ev -> {
            Student sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                studentDAO.delete(sel.getId());
                table.getItems().setAll(studentDAO.findAll());
            }
        });

        VBox.setVgrow(table, Priority.ALWAYS);
        v.getChildren().addAll(h, table, controls);
        return v;
    }

    private VBox createExamView() {
        VBox v = new VBox(8);
        v.getStyleClass().add("card");
        Label h = new Label("Exams");
        h.getStyleClass().add("heading");

        TableView<Exam> table = new TableView<>();
        TableColumn<Exam, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(param -> param.getValue().idProperty().asObject());
        TableColumn<Exam, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(param -> param.getValue().nameProperty());
        table.getColumns().addAll(idCol, nameCol);

        ObservableList<Exam> list = FXCollections.observableArrayList(examDAO.findAll());
        table.setItems(list);

        Button add = new Button("Add");
        Button del = new Button("Delete");
        HBox controls = new HBox(8, add, del);

        add.setOnAction(e -> {
            TextInputDialog d = new TextInputDialog();
            d.setTitle("Add Exam");
            d.setHeaderText("Create new exam");
            d.setContentText("Exam name:");
            d.showAndWait().ifPresent(name -> {
                Exam ex = new Exam();
                ex.setName(name);
                examDAO.save(ex);
                table.getItems().setAll(examDAO.findAll());
            });
        });

        del.setOnAction(ev -> {
            Exam sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                examDAO.delete(sel.getId());
                table.getItems().setAll(examDAO.findAll());
            }
        });

        VBox.setVgrow(table, Priority.ALWAYS);
        v.getChildren().addAll(h, table, controls);
        return v;
    }

    private VBox createRoomView() {
        VBox v = new VBox(8);
        v.getStyleClass().add("card");
        Label h = new Label("Rooms");
        h.getStyleClass().add("heading");

        TableView<Room> table = new TableView<>();
        TableColumn<Room, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(param -> param.getValue().idProperty().asObject());
        TableColumn<Room, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(param -> param.getValue().nameProperty());
        TableColumn<Room, Integer> capCol = new TableColumn<>("Capacity");
        capCol.setCellValueFactory(param -> param.getValue().capacityProperty().asObject());
        table.getColumns().addAll(idCol, nameCol, capCol);

        ObservableList<Room> list = FXCollections.observableArrayList(roomDAO.findAll());
        table.setItems(list);

        Button add = new Button("Add");
        Button del = new Button("Delete");
        HBox controls = new HBox(8, add, del);

        add.setOnAction(e -> {
            Dialog<Room> dlg = new Dialog<>();
            dlg.setTitle("Add Room");
            Label n = new Label("Name:");
            TextField nf = new TextField();
            Label c = new Label("Capacity:");
            TextField cf = new TextField();
            VBox box = new VBox(8, n, nf, c, cf);
            dlg.getDialogPane().setContent(box);
            dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            dlg.setResultConverter(btn -> {
                if (btn == ButtonType.OK) {
                    Room r = new Room();
                    r.setName(nf.getText());
                    try { r.setCapacity(Integer.parseInt(cf.getText())); } catch (NumberFormatException ex) { r.setCapacity(0); }
                    return r;
                }
                return null;
            });
            dlg.showAndWait().ifPresent(room -> {
                roomDAO.save(room);
                table.getItems().setAll(roomDAO.findAll());
            });
        });

        del.setOnAction(ev -> {
            Room sel = table.getSelectionModel().getSelectedItem();
            if (sel != null) {
                roomDAO.delete(sel.getId());
                table.getItems().setAll(roomDAO.findAll());
            }
        });

        VBox.setVgrow(table, Priority.ALWAYS);
        v.getChildren().addAll(h, table, controls);
        return v;
    }

    private VBox createSeatingView() {
        VBox v = new VBox(8);
        v.getStyleClass().add("card");
        Label h = new Label("Seating Plan");
        h.getStyleClass().add("heading");

        ComboBox<Exam> examCombo = new ComboBox<>(FXCollections.observableArrayList(examDAO.findAll()));
        examCombo.setPromptText("Select exam");
        Button gen = new Button("Generate Seating");

        TextArea output = new TextArea();
        output.setEditable(false);
        output.setWrapText(true);

        gen.setOnAction(e -> {
            Exam selected = examCombo.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Alert a = new Alert(Alert.AlertType.WARNING, "Select an exam first");
                a.showAndWait();
                return;
            }
            Map<Room, List<Student>> plan = seatingService.generatePlan(selected.getId());
            StringBuilder sb = new StringBuilder();
            plan.forEach((room, students) -> {
                sb.append("Room: ").append(room.getName()).append(" (cap=").append(room.getCapacity()).append(")\n");
                int col = 1;
                for (Student s : students) {
                    sb.append(String.format("%3d. %s (%s)\n", col++, s.getName(), s.getRoll()));
                }
                sb.append("\n");
            });
            output.setText(sb.toString());
        });

        VBox.setVgrow(output, Priority.ALWAYS);
        v.getChildren().addAll(h, examCombo, gen, output);
        return v;
    }

}
