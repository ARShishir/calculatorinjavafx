package idontknow;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Idontknow extends Application {

    private TextField display = new TextField();
    private double num1 = 0;
    private String operator = "";
    private boolean startNewNumber = false;
    private boolean justCalculated = false;

    @Override
    public void start(Stage primaryStage) {
        display.setEditable(false);
        display.setStyle("-fx-font-size: 18px;");
        display.setPrefHeight(50);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "C", "=", "+"
        };

        int row = 1;
        int col = 0;

        // Create buttons and add them to the grid
        for (String text : buttons) {
            Button btn = new Button(text);
            btn.setPrefSize(50, 50);
            btn.setOnAction(e -> handleInput(text));
            grid.add(btn, col, row);
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }

        // Add display at the top
        grid.add(display, 0, 0, 4, 1);

        // Name and ID label
        Label nameLabel = new Label("Name: Abdur Rahaman Shishir");
        Label idLabel = new Label("ID: 223071092");
        
        nameLabel.setStyle("-fx-font-size: 14px;");
        nameLabel.setAlignment(Pos.CENTER);
        idLabel.setStyle("-fx-font-size: 14px;");
        idLabel.setAlignment(Pos.CENTER);

        // Add the label to the grid below the calculator
        grid.add(nameLabel, 0, row, 4, 1);
        grid.add(idLabel, 0, row, 4, 5);
        // Set column and row constraints for responsiveness
        for (int i = 0; i < 4; i++) {
            grid.getColumnConstraints().add(createColumnConstraints());
        }
        for (int i = 0; i < 5; i++) {
            grid.getRowConstraints().add(createRowConstraints());
        }

        Scene scene = new Scene(grid, 240, 350);
        primaryStage.setTitle("Simple Calculator");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(240);  // Set a default minimum width
        primaryStage.setMinHeight(350); // Set a default minimum height        
        primaryStage.setMaxWidth(260);  // Set a default Maximum width
        primaryStage.setMaxHeight(370); // Set a default Maximum height
        primaryStage.show();

    }

    // Create flexible row constraints to make the layout responsive
    private javafx.scene.layout.RowConstraints createRowConstraints() {
        javafx.scene.layout.RowConstraints rowConstraints = new javafx.scene.layout.RowConstraints();
        rowConstraints.setVgrow(javafx.scene.layout.Priority.ALWAYS);
        return rowConstraints;
    }

    // Create flexible column constraints to make the layout responsive
    private javafx.scene.layout.ColumnConstraints createColumnConstraints() {
        javafx.scene.layout.ColumnConstraints columnConstraints = new javafx.scene.layout.ColumnConstraints();
        columnConstraints.setHgrow(javafx.scene.layout.Priority.ALWAYS);
        return columnConstraints;
    }

    private void handleInput(String input) {
        switch (input) {
            case "C":
                display.clear();
                num1 = 0;
                operator = "";
                startNewNumber = false;
                justCalculated = false;
                break;

            case "=":
                if (!operator.isEmpty()) {
                    String[] parts = display.getText().split(" \\" + operator + " ");
                    if (parts.length == 2) {
                        try {
                            double num2 = Double.parseDouble(parts[1]);
                            if (operator.equals("/") && num2 == 0) {
                                display.setText("Cannot divide by zero");
                            } else {
                                double result = switch (operator) {
                                    case "+" -> num1 + num2;
                                    case "-" -> num1 - num2;
                                    case "*" -> num1 * num2;
                                    case "/" -> num1 / num2;
                                    default -> 0;
                                };

                                String resultText = (result % 1 == 0) ?
                                        String.valueOf((int) result) :
                                        String.valueOf(result);

                                display.setText(resultText);
                                num1 = result; // Set result as the new first operand
                                justCalculated = true;
                            }
                            operator = "";
                            startNewNumber = true;
                        } catch (NumberFormatException e) {
                            display.setText("Error");
                        }
                    }
                }
                break;

            case "+": case "-": case "*": case "/":
                try {
                    if (justCalculated) {
                        operator = input;
                        display.setText(formatNumber(num1) + " " + operator + " ");
                        justCalculated = false;
                        startNewNumber = false;
                    } else if (!display.getText().isEmpty() && operator.isEmpty()) {
                        num1 = Double.parseDouble(display.getText());
                        operator = input;
                        display.setText(formatNumber(num1) + " " + operator + " ");
                    }
                } catch (NumberFormatException e) {
                    display.setText("Error");
                }
                break;

            default: // numbers
                if (startNewNumber || display.getText().equals("Cannot divide by zero") || display.getText().equals("Error")) {
                    display.clear();
                    startNewNumber = false;
                }
                display.appendText(input);
        }
    }

    private String formatNumber(double value) {
        return (value % 1 == 0) ? String.valueOf((int) value) : String.valueOf(value);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
