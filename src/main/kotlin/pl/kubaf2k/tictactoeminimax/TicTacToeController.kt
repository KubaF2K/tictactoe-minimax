package pl.kubaf2k.tictactoeminimax

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import java.net.URL
import java.util.*

class TicTacToeController: Initializable {
    val CIRCLE = 'O'
    val CROSS = 'X'

    @FXML lateinit var mainGrid: GridPane

    lateinit var root: StateNode
    lateinit var currState: StateNode

    val buttons = Array(3) {Array(3) {Button()} }

    @FXML
    override fun initialize(url: URL?, resourceBundle: ResourceBundle?) {
        for (i in buttons.indices) {
            for (j in buttons[i].indices) {
                buttons[i][j].minWidth = 100.0
                buttons[i][j].minHeight = 100.0
                buttons[i][j].setOnAction { onClick(i, j) }
                mainGrid.add(buttons[i][j], i, j)
            }
        }
        root = StateNode(false, getState())
        root.generateStates()
        currState = root
    }

    private fun getState(): Array<CharArray> {
        val state = Array(3) {CharArray(3)}
        for (i in state.indices) {
            for (j in state[i].indices) {
                if (buttons[i][j].text.isEmpty())
                    state[i][j] = ' '
                else
                    state[i][j] = buttons[i][j].text[0]
            }
        }
        return state
    }

    private fun onClick(x: Int, y: Int) {
        if (setField(x, y, true)) {
            currState = currState.findState(getState())
            if (currState.won) {
                win()
                return
            }
            if (currState.isFull) {
                draw()
                return
            }
            enemyMove()
            if (currState.won)
                lose()
        }
    }

    private fun setField(x: Int, y: Int, player: Boolean): Boolean {
        if (buttons[x][y].text == "") {
            if (player)
                buttons[x][y].text = CROSS.toString()
            else
                buttons[x][y].text = CIRCLE.toString()
            return true
        }
        return false
    }

    private fun enemyMove() {
        val newState = currState.findGoodMove()
        currState = newState.first
        setField(newState.second.first, newState.second.second, false)
    }

    private fun win() {
        val winAlert = Alert(Alert.AlertType.CONFIRMATION)
        winAlert.contentText = "Wygrałeś!"
        winAlert.showAndWait()
        closeWindow()
    }

    private fun lose() {
        val loseAlert = Alert(Alert.AlertType.ERROR)
        loseAlert.contentText = "Przegrałeś!"
        loseAlert.showAndWait()
        closeWindow()
    }

    private fun draw() {
        val drawAlert = Alert(Alert.AlertType.INFORMATION)
        drawAlert.contentText = "Remis!"
        drawAlert.showAndWait()
        closeWindow()
    }

    private fun closeWindow() {
        (mainGrid.scene.window as Stage).close()
    }
}