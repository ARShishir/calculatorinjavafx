package idontknow;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DECIMAL;
import static javafx.scene.input.KeyCode.PERIOD;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class Idontknow extends Application {

    private final TextField display = new TextField();
    private double num1 = 0;
    private String operator = "";
    private boolean startNewNumber = false;
    private boolean justCalculated = false;

    @Override
    public void start(Stage primaryStage) {

        display.setEditable(false);
        display.setStyle("""
            -fx-font-size: 20px;
            -fx-font-weight: bold;
            -fx-background-color: #f4f4f4;
            -fx-border-color: #cccccc;
            -fx-border-radius: 5px;
            -fx-padding: 5px;
        """);
        display.setPrefHeight(60);
        display.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(display, Priority.ALWAYS);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        grid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        String[] buttons = {
             "%", "CE", "C", "/",
             "7", "8", "9", "*",
             "4", "5", "6", "-",
             "1", "2", "3", "+",
             "±", "0", ".", "="
         };

        int row = 1;
        int col = 0;

        for (String text : buttons) {
            Button btn = new Button(text);
            btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            btn.setStyle("""
                -fx-font-size: 16px;
                -fx-font-weight: bold;
                -fx-background-color: #ffffff;
                -fx-border-radius: 8px;
                -fx-background-radius: 8px;
                -fx-border-color: #bbbbbb;
                -fx-cursor: hand;
            """);

            btn.setOnMouseEntered(e -> btn.setStyle(btn.getStyle().replace("#ffffff", "#e6e6e6")));
            btn.setOnMouseExited(e -> btn.setStyle(btn.getStyle().replace("#e6e6e6", "#ffffff")));

            btn.setOnAction(e -> handleInput(text));

            GridPane.setHgrow(btn, Priority.ALWAYS);
            GridPane.setVgrow(btn, Priority.ALWAYS);
            grid.add(btn, col, row);
            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }

        grid.add(display, 0, 0, 4, 1);

        for (int i = 0; i < 4; i++) {
            grid.getColumnConstraints().add(createColumnConstraints());
        }
        for (int i = 0; i < 6; i++) {  // 1 for display, 5 for button rows
            grid.getRowConstraints().add(createRowConstraints());
        }

        Scene scene = new Scene(grid, 280, 440);

        
        primaryStage.setTitle("Calculator By Abdur Rahaman Shishir | 223071092");
        primaryStage.setMinWidth(280);
        primaryStage.setMinHeight(440);
        primaryStage.setScene(scene);
        primaryStage.show();


        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            String text = event.getText();

            if (code.isDigitKey() || code.isKeypadKey()) {
                handleInput(text);
            } else {
                switch (code) {
                    case PLUS, ADD -> handleInput("+");
                    case MINUS, SUBTRACT -> handleInput("-");
                    case SLASH, DIVIDE -> handleInput("/");
                    case ASTERISK, MULTIPLY -> handleInput("*");
                    case ENTER, EQUALS -> handleInput("=");
                    case BACK_SPACE -> handleInput("CE");
                    case DELETE -> handleInput("C");
                    case PERIOD, DECIMAL -> handleInput(".");
                    case DIGIT5 -> {
                        if (event.isShiftDown()) handleInput("%");
                    }
                    case F1 -> handleInput("±");
                }
            }
        });
    }

    private RowConstraints createRowConstraints() {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100.0 / 6); // 6 rows: 1 for display, 5 for buttons
        rowConstraints.setVgrow(Priority.ALWAYS);
        return rowConstraints;
    }

    private ColumnConstraints createColumnConstraints() {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(25); // 4 columns
        columnConstraints.setHgrow(Priority.ALWAYS);
        return columnConstraints;
    }

    private void handleInput(String input) {
        switch (input) {
            case "C" -> {
                display.clear();
                num1 = 0;
                operator = "";
                startNewNumber = false;
                justCalculated = false;
            }
            case "CE" -> {
                String currentText = display.getText();
                if (!currentText.isEmpty()) {
                    display.setText(currentText.substring(0, currentText.length() - 1));
                }
            }
            case "%" -> {
                try {
                    String text = display.getText();
                    if (operator.isEmpty()) {
                        double value = Double.parseDouble(text);
                        value = value / 100;
                        display.setText(formatNumber(value));
                    } else {
                        String[] parts = text.split(" \\" + operator + " ");
                        if (parts.length == 2) {
                            double value = Double.parseDouble(parts[1]);
                            value = value / 100;
                            display.setText(parts[0] + " " + operator + " " + formatNumber(value));
                        }
                    }
                } catch (NumberFormatException e) {
                    display.setText("Error");
                }
            }
            case "±" -> {
                String text = display.getText();
                if (text.equals("Cannot divide by zero") || text.equals("Error") || text.isEmpty()) {
                    return;
                }

                try {
                    if (operator.isEmpty()) {
                        double value = Double.parseDouble(text);
                        value *= -1;
                        display.setText(formatNumber(value));
                    } else {
                        String[] parts = text.split(" \\" + operator + " ");
                        if (parts.length == 2) {
                            double value = Double.parseDouble(parts[1]);
                            value *= -1;
                            display.setText(parts[0] + " " + operator + " " + formatNumber(value));
                        }
                    }
                } catch (NumberFormatException e) {
                    display.setText("Error");
                }
            }
            case "=" -> {
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
                                String resultText = (result % 1 == 0) ? String.valueOf((int) result) : String.valueOf(result);
                                display.setText(resultText);
                                num1 = result;
                                justCalculated = true;
                            }
                            operator = "";
                            startNewNumber = true;
                        } catch (NumberFormatException e) {
                            display.setText("Error");
                        }
                    }
                }
            }
            case "+", "-", "*", "/" -> {
                try {
                    String text = display.getText();

                    if (justCalculated) {
                        operator = input;
                        display.setText(formatNumber(num1) + " " + operator + " ");
                        justCalculated = false;
                        startNewNumber = false;
                    } else if (!text.isEmpty()) {
                        if (!operator.isEmpty() && text.matches(".* [\\+\\-\\*/] $")) {
                            display.setText(text.substring(0, text.length() - 3) + " " + input + " ");
                            operator = input;
                        } else if (operator.isEmpty()) {
                            num1 = Double.parseDouble(text);
                            operator = input;
                            display.setText(formatNumber(num1) + " " + operator + " ");
                        }
                    }
                } catch (NumberFormatException e) {
                    display.setText("Error");
                }
            }
            default -> {
                if (startNewNumber || display.getText().equals("Cannot divide by zero") || display.getText().equals("Error")) {
                    display.clear();
                    startNewNumber = false;
                }

                if (input.equals(".")) {
                    String text = display.getText();
                    if (operator.isEmpty()) {
                        if (text.contains(".")) return;
                    } else {
                        String[] parts = text.split(" \\" + operator + " ");
                        if (parts.length == 2 && parts[1].contains(".")) return;
                    }
                }

                display.appendText(input);
            }
        }
    }

    private String formatNumber(double value) {
        return (value % 1 == 0) ? String.valueOf((int) value) : String.valueOf(value);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
