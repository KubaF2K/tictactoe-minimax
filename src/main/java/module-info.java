module pl.kubaf2k.tictactoeminimax {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens pl.kubaf2k.tictactoeminimax to javafx.fxml;
    exports pl.kubaf2k.tictactoeminimax;
}