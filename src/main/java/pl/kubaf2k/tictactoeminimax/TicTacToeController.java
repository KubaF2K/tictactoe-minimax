package pl.kubaf2k.tictactoeminimax;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import kotlin.Pair;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TicTacToeController implements Initializable {
    public final char CIRCLE = 'O';
    public final char CROSS = 'X';
    @FXML
    public GridPane mainGrid;

    public StateNode root;
    public StateNode currState;

    private final Button[][] buttons = new Button[3][3];

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setMinWidth(100);
                buttons[i][j].setMinHeight(100);
                int finalI = i;
                int finalJ = j;
                buttons[i][j].setOnAction((e) -> onClick(finalI, finalJ));
                mainGrid.add(buttons[i][j], i, j);
            }
        }
        root = new StateNode(false, getState(), null);
        root.generateStates();
        currState = root;
    }

    private char[][] getState() {
        char[][] state = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().isEmpty())
                    state[i][j] = ' ';
                else
                    state[i][j] = buttons[i][j].getText().charAt(0);
            }
        }
        return state;
    }

    private void onClick(int x, int y) {
        if (setField(x, y, true)) {
            currState = currState.findState(getState());
            if(currState.getWon()) {
                win();
                return;
            }
            if(currState.isFull()) {
                draw();
                return;
            }
            enemyMove();
            if(currState.getWon())
                lose();
        }
    }

    private boolean setField(int x, int y, boolean player) {
        if (Objects.equals(buttons[x][y].getText(), "")) {
            if (player)
                buttons[x][y].setText(String.valueOf(CROSS));
            else
                buttons[x][y].setText(String.valueOf(CIRCLE));
            return true;
        }
        return false;
    }


    private void enemyMove() {
        Pair<StateNode, Pair<Integer, Integer>> newState = currState.findGoodMove();
        currState = newState.component1();
        setField(newState.component2().component1(), newState.component2().component2(), false);
    }

    private void win() {
        Alert winAlert = new Alert(Alert.AlertType.CONFIRMATION);
        winAlert.setContentText("Wygrałeś!");
        winAlert.showAndWait();
        Platform.exit();
    }
    private void lose() {
        Alert winAlert = new Alert(Alert.AlertType.ERROR);
        winAlert.setContentText("Przegrałeś!");
        winAlert.showAndWait();
        Platform.exit();
    }
    private void draw() {
        Alert drawAlert = new Alert(Alert.AlertType.INFORMATION);
        drawAlert.setContentText("Remis!");
        drawAlert.showAndWait();
        Platform.exit();
    }
}