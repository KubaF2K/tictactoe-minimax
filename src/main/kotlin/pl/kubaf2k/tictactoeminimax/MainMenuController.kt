package pl.kubaf2k.tictactoeminimax

import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class MainMenuController {
    fun launchTTT() {
        val stage = Stage()
        val fxmlLoader = FXMLLoader(javaClass.getResource("tictactoe-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 400.0, 400.0)
        stage.title = "Kółko i krzyżyk!"
        stage.scene = scene
        stage.show()
    }

    fun launchMod() {
        val stage = Stage()
        val fxmlLoader = FXMLLoader(javaClass.getResource("modded-view.fxml"))
        val scene = Scene(fxmlLoader.load(), 400.0, 400.0)
        stage.title = "Przerobione kółko i krzyżyk!"
        stage.scene = scene
        stage.show()
    }

    fun exit() {
        Platform.exit()
    }
}