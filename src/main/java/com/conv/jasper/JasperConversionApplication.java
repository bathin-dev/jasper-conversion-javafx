package com.conv.jasper;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;

public class JasperConversionApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Jasper Conversion Application");

        // Create the registration form grid pane
        GridPane gridPane = createRegistrationFormPane();
        // Add UI controls to the registration form grid pane
        addUIControls(gridPane, primaryStage);
        // Create a scene with registration form grid pane as the root node
        Scene scene = new Scene(gridPane, 800, 500);
        // Set the scene in primary stage	
        primaryStage.setScene(scene);
        
        primaryStage.show();
    }


    private GridPane createRegistrationFormPane() {
        // Instantiate a new Grid Pane
        GridPane gridPane = new GridPane();

        // Position the pane at the center of the screen, both vertically and horizontally
        gridPane.setAlignment(Pos.CENTER);

        // Set a padding of 20px on each side
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Set the horizontal gap between columns
        gridPane.setHgap(10);

        // Set the vertical gap between rows
        gridPane.setVgap(10);

        // Add Column Constraints

        // columnOneConstraints will be applied to all the nodes placed in column one.
        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // columnTwoConstraints will be applied to all the nodes placed in column two.
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    private void addUIControls(GridPane gridPane, Stage primaryStage) {
        // Add Header
        Label headerLabel = new Label("Jasper Conversion Form");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Add inut Label
        Label nameLabel = new Label("Input jasper file : ");
        gridPane.add(nameLabel, 0,1);

        // Add input Text Field
        TextField inputJasper = new TextField();
        inputJasper.setText("C:\\Users\\BATHIN-PC\\Downloads\\First\\First.jrxml");
        inputJasper.setPrefHeight(40);
        gridPane.add(inputJasper, 1,1);
        
        
        FileChooser fileChooser = new FileChooser();
        //Add button select file
        Button selectFile = new Button("Select..");
        selectFile.setOnAction(e -> {
        	File selectedFile = fileChooser.showOpenDialog(primaryStage);
        	//Set selected fle
        	inputJasper.setText(selectedFile.getAbsolutePath());
        });
        selectFile.setPrefHeight(40);
        gridPane.add(selectFile, 2, 1);


        // Add Output folder Label
        Label outputFolder = new Label("Output folder : ");
        gridPane.add(outputFolder, 0, 2);

        // Add Output folder Text Field
        TextField outputField = new TextField();
        outputField.setPrefHeight(40);
        outputField.setText("C:\\Users\\BATHIN-PC\\Downloads\\First");
        gridPane.add(outputField, 1, 2);
        

        DirectoryChooser directoryChooser = new DirectoryChooser();
        
        //Add button choose folder
        Button chooseFolder = new Button("Choose..");
        chooseFolder.setPrefHeight(40);
        chooseFolder.setOnAction(e -> {
            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            //Setting chose folder
            outputField.setText(selectedDirectory.getAbsolutePath());
        });
        gridPane.add(chooseFolder, 2, 2);
        
        // Add file name Label
        Label outputFileName = new Label("Output file name : ");
        gridPane.add(outputFileName, 0, 3);

        // Add file name Field
        TextField outputNameField = new TextField();
        outputNameField.setText("File1_out.jasper");
        outputNameField.setPrefHeight(40);
        gridPane.add(outputNameField, 1, 3);
        
        CheckBox checkBox1 = new CheckBox("Overwrite file if exist");
        checkBox1.setSelected(true);
        gridPane.add(checkBox1, 1, 4);
        checkBox1.setAllowIndeterminate(true);

        // Add Submit Button
        Button submitButton = new Button("Convert");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 5, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(inputJasper.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please Select input file .jrxml");
                    return;
                }
                if(outputField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please choose folder to output");
                    return;
                }
                if(outputNameField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Form Error!", "Please enter output file name");
                    return;
                }
                
                String in = inputJasper.getText();        		
        		String out = outputField.getText() + "\\" ;
                if (checkBox1.isSelected()) {
                	out = out + outputNameField.getText();
                } else {
                	out = out + genNewJasperFileName();
                }
        		
        		try {
        			//Execute convert
        			ConversionUtil.convert(in, out);
        			//Show message success
        			showAlert(Alert.AlertType.INFORMATION, gridPane.getScene().getWindow(), "Conversion Successful!", "Please check output file at: " + out);
        		} catch (JRException e) {
        			showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Conversion failed!","Please check log for detail!");
        			e.printStackTrace();
        		}

                
            }

			private String genNewJasperFileName() {
				String currMil = String.valueOf(System.currentTimeMillis());
				return "Output_" + currMil + ".jasper" ;
			}
        });
    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}