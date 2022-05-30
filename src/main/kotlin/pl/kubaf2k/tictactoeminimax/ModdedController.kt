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

class ModdedController: Initializable {
    private var player2Char = 'O'
    private var player1Char = 'X'
    private var moveNumber = 0
    private var moving = false
    private var currMovingColumn = 0
    private var currMovingRow = 0


    @FXML lateinit var mainGrid: GridPane

    private lateinit var root: ModStateNode
    private lateinit var currState: ModStateNode

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
                root = ModStateNode(moveNumber, false, getState())
                root.generateStates()
                currState = root
            }
            else if (button == ButtonType.NO) {
                val temp = player1Char
                player1Char = player2Char
                player2Char = temp
                root = ModStateNode(moveNumber, true, getState())
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
        if (moveNumber < 6) {
            if (setField(x, y)) {
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
        else {
            if (moving){
                if ((x == currMovingColumn) != (y == currMovingRow)) {
                    if (setField(x, y)) {
                        buttons[currMovingColumn][currMovingRow].text = ""
                        currState = currState.findState(getState())
                        if (currState.won) {
                            win()
                            return
                        }
                        if (currState.isFull || moveNumber >= 7) {
                            draw()
                            return
                        }
                        enemyMove()
                        if (currState.won) {
                            lose()
                            return
                        }
                        if (currState.isFull || moveNumber >= 7) {
                            draw()
                            return
                        }
                    }
                    moving = false
                }
            }
            else {
                if (buttons[x][y].text == player1Char.toString()) {
                    currMovingColumn = x
                    currMovingRow = y
                    moving = true
                }
            }
        }
    }

    private fun setField(x: Int, y: Int): Boolean {
        if (buttons[x][y].text == "") {
            buttons[x][y].text = player1Char.toString()
            moveNumber++
            return true
        }
        return false
    }

    private fun enemyMove() {
        val newState = currState.findGoodMove()
        currState = newState.first
        redrawButtons()
        moveNumber++
    }

    private fun redrawButtons() {
        for (i in buttons.indices)
            for (j in buttons[i].indices) {
                buttons[i][j].text = if (currState.state[i][j] == 'X')
                    player1Char.toString()
                else if (currState.state[i][j] == 'O')
                    player2Char.toString()
                else ""
            }
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