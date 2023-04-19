/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package clientfx.controller;

import clientfx.models.Course;
import clientfx.models.RegistrationForm;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author doz
 */
public class SampleController implements Initializable {

    @FXML
    private TableView<Course> listeCours;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField matriculeField;
    @FXML
    private Button registerButton;
    @FXML
    private MenuButton menuSession;
    @FXML
    private Button loadCoursesButton;
    @FXML
    private TableColumn<Course, String> codeColumn;
    @FXML
    private TableColumn<Course, String> courseColumn;

    private Socket clientSocket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Course coursChoisi = new Course();
    private Course coursSelectionne = new Course();
    private final MenuItem hiver = new MenuItem("HIVER");
    private final MenuItem automne = new MenuItem("AUTOMNE");
    private final MenuItem ete = new MenuItem("ETE");
    private String choice;
    ObservableList<Course> observableList = FXCollections.observableArrayList();
    @FXML
    private TextField codeField;

    protected String successMessage = String.format("-fx-text-fill: GREEN;");
    protected String errorMessage = String.format("-fx-text-fill: RED;");
    protected String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");
    protected String successStyle = String.format("-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;");
    @FXML
    private Label invalidValue;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        menuSession.getItems().clear();
        menuSession.getItems().add(hiver);
        menuSession.getItems().add(automne);
        menuSession.getItems().add(ete);

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        listeCours.setItems(observableList);
        listeCours.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Course> observable, Course oldValue, Course newValue) -> {
            if (listeCours.getSelectionModel().getSelectedItem() != null) {
                coursSelectionne = listeCours.getSelectionModel().getSelectedItem();
                codeField.setText(coursSelectionne.getCode());
                System.out.println(coursSelectionne.getCode());
            }
        });

        hiver.setOnAction((ActionEvent event) -> {
            System.out.println("hiver");
            menuSession.setText("Hiver");
            choice = "Hiver";
        });

        automne.setOnAction((ActionEvent event) -> {
            System.out.println("automne");
            menuSession.setText("Automne");
            choice = "Automne";
        });

        ete.setOnAction((ActionEvent event) -> {
            System.out.println("ete");
            menuSession.setText("Ete");
            choice = "Ete";
            try {
                getCourses(choice);
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(SampleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    @FXML
    private void onTap(ActionEvent event) throws IOException, ClassNotFoundException {
        final Node source = (Node) event.getSource();
        System.out.println(source.getId());
        switch (source.getId()) {
            case "loadCoursesButton":
                getCourses(choice);
                break;
            case "registerButton": {
                registerUser();
                break;
            }
            default:
                throw new AssertionError();
        }
    }

    /**
     * Récupère la liste des cours
     *
     * @param arg est l'argument pour preciser la session des cours à récupérer
     * @throws IOException en cas d'erreur lors de la communication avec le
     * serveur
     * @throws ClassNotFoundException en cas de classe non trouvée
     */
    public void getCourses(String arg) throws IOException, ClassNotFoundException {
        connect();
        getSpecificCourses(arg);
    }

    /**
     * Connecte le client au serveur
     *
     * @throws UnknownHostException en cas d'hôte non connu
     * @throws IOException en cas d'erreur lors de la communication avec le
     * serveur
     */
    public void connect() throws UnknownHostException, IOException {
        clientSocket = new Socket(InetAddress.getLocalHost().getHostAddress(), 1337);
    }

    /**
     * Enregistre un étudiant
     *
     * @throws IOException en cas d'echec lors de la communication avec le
     * serveur
     */
    public void registerUser() throws IOException {
        if (validateFields()) {
            try {
                connect();
                objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
                for (Course course : listeCours.getItems()) {
                    if (course.getCode().toUpperCase().equals(codeField.getText().toUpperCase())) {
                        coursChoisi = course;
                    }
                }
                RegistrationForm user = new RegistrationForm(
                        surnameField.getText(),
                        nameField.getText(),
                        emailField.getText(),
                        matriculeField.getText(),
                        coursChoisi);

                objectOutputStream.writeObject("INSCRIRE " + " " + " ");
                objectOutputStream.writeObject(new Gson().toJson(user));
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("UdeM");
                alert.setContentText((String) objectInputStream.readObject());

                alert.showAndWait();
                clientSocket.close();
                objectOutputStream.close();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(SampleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Récupère la liste de cours en fonction de l'argument passé
     *
     * @param arg est la session d'un cours
     * @throws IOException en cas d'echec lors de la communication avec le
     * serveur
     * @throws ClassNotFoundException en cas de classe non trouvée
     */
    public void getSpecificCourses(String arg) throws IOException, ClassNotFoundException {
        listeCours.getItems().clear();
        objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        objectOutputStream.writeObject("CHARGER " + arg + " ");
        ArrayList a = new Gson().fromJson(objectInputStream.readObject().toString(), ArrayList.class);
        Course c;
        for (Object course : a) {
            Map<String, String> cour = new Gson().fromJson((String) course, Map.class);
            c = new Course(cour.get("name"), cour.get("code"), cour.get("session"));
            listeCours.getItems().add(c);
        }
        closeAll();
    }

    /**
     * Coupe la communication avec le serveur
     *
     * @throws IOException en cas d'error lors de l'arret de communication
     */
    public void closeAll() throws IOException {
        objectInputStream.close();
        objectOutputStream.close();
        clientSocket.close();
    }

    @FXML
    private void onDrop(ActionEvent event) {
        System.out.println("clientfx.SampleController.onDrop()");
    }

    /**
     * Valide les champs de saisie
     *
     * @return l'état de la validation
     */
    public boolean validateFields() {
        invalidValue.setStyle(errorMessage);
        boolean valid = false;
        if (nameField.getText().isEmpty() || surnameField.getText().isEmpty()
                || emailField.getText().isEmpty() || matriculeField.getText().isEmpty()
                || codeField.getText().isEmpty()) {
            invalidValue.setText("Tous les champs sont requis");
            valid = false;
        } else {
            System.out.println("clientfx.controller.SampleController.validateFields()");
            if (matriculeField.getText().length() == 6) {
                System.out.println("");
                try {
                    Integer.valueOf(matriculeField.getText());
                    if (codeField.getText().length() != 7) {
                        invalidValue.setText("Le code contient 7 caractères");
                    } else {
                        Pattern pattern = Pattern.compile("\\bIFT[0-9]");
                        Matcher matcher = pattern.matcher(codeField.getText());
                        boolean matchFound = matcher.find();
                        if (matchFound) {
                            invalidValue.setText("");
                            valid = true;
                        } else {
                            invalidValue.setText("Code du cours invalide");
                        }
                    }
                } catch (NumberFormatException e) {
                    invalidValue.setText("Le Matricule n'est pas une chaine de caractères !");
                    valid = false;
                }
            } else {
                invalidValue.setText("Le matricule contient 6 chiffres");
                valid = false;
            }
        }

        return valid;
    }

}
