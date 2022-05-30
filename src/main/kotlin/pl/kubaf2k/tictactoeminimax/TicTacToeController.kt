package pl.kubaf2k.tictactoeminimax

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.layout.GridPane
import javafx.stage.Stage
import java.net.URL
import java.util.*

class TicTacToeController: Initializable {
    private var player2Char = 'O'
    private var player1Char = 'X'

    @FXML lateinit var mainGrid: GridPane

    private lateinit var root: StateNode
    private lateinit var currState: StateNode

    private val buttons = Array(3) {Array(3) {Button()} }

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
        val choiceAlert = Alert(Alert.AlertType.INFORMATION, "Czy chcesz grać jako pierwszy?",
            ButtonType.YES, ButtonType.NO)
        choiceAlert.title = "Wybór symbolu"
        val alertResult = choiceAlert.showAndWait()
        alertResult.ifPresent { button: ButtonType ->
            if (button == ButtonType.YES) {
                root = StateNode(false, getState())
                root.generateStates()
                currState = root
            }
            else if (button == ButtonType.NO) {
                val temp = player1Char
                player1Char = player2Char
                player2Char = temp
                root = StateNode(true, getState())
                root.generateStates()
                currState = root
                enemyMove()
            }
        }
    }

    private fun getState(): Array<CharArray> {
        val state = Array(3) {CharArray(3)}
        for (i in state.indices) {
            for (j in state[i].indices) {
                if (buttons[i][j].text.isEmpty())
                    state[i][j] = ' '
                else {
                    if (buttons[i][j].text == player1Char.toString())
                        state[i][j] = 'X'
                    else
                        state[i][j] = 'O'
                }
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
            if (currState.isFull) {
                draw()
                return
            }
            if (currState.won)
                lose()

        }
    }

    private fun setField(x: Int, y: Int, player: Boolean): Boolean {
        if (buttons[x][y].text == "") {
            if (player)
                buttons[x][y].text = player1Char.toString()
            else
                buttons[x][y].text = player2Char.toString()
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