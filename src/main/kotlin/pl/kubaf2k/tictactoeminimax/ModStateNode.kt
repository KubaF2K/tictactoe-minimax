package pl.kubaf2k.tictactoeminimax

class ModStateNode(var depth: Int, var player: Boolean, var state: Array<CharArray>) {

    var children = ArrayList<ModStateNode>()
    var payout = 0

    val won: Boolean
        get() {
            for (i in 0 until 3) {
                var won = true
                for (j in 0 until 3) {
                    if ((player && state[i][j] != 'X') || (!player && state[i][j] != 'O')) {
                        won = false
                        break
                    }
                }
                if (won) return true
            }
            for (j in 0 until 3) {
                var won = true
                for (i in 0 until 3) {
                    if ((player && state[i][j] != 'X') || (!player && state[i][j] != 'O')) {
                        won = false
                        break
                    }
                }
                if (won) return true
            }
            var won = true
            for (i in 0 until 3) {
                if ((player && state[i][i] != 'X') || (!player && state[i][i] != 'O')) {
                    won = false
                    break
                }
            }
            if (won) return true
            won = true
            for (i in 0 until 3) {
                if ((player && state[2 - i][i] != 'X') || (!player && state[2 - i][i] != 'O')) {
                    won = false
                    break
                }
            }
            if (won) return true
            return false
        }

    val isFull: Boolean
        get() {
            for (row in state)
                for (spot in row)
                    if (spot == ' ')
                        return false
            return true
        }

    fun addChild(child: ModStateNode) {
        children.add(child)
    }

    fun findState(state: Array<CharArray>): ModStateNode {
        for (child in children) {
            var same = true
            for (i in 0 until 3)
                for (j in 0 until 3) {
                    if (child.state[i][j] != state[i][j]) {
                        same = false
                        break
                    }
                    if (!same)
                        break
                }
            if (same) return child
        }
        throw NullPointerException()
    }

    fun findPayout() {
        payout = if (children.isEmpty()) 0 else if (player) findMaxPayout().payout else findMinPayout().payout
    }

    fun findMinPayout(): ModStateNode {
        var minChild = children[0]
        var minPayout = 2
        for (child in children) {
            if (child.payout < minPayout) {
                minPayout = child.payout
                minChild = child
            }
        }
        return minChild
    }

    fun findMaxPayout(): ModStateNode {
        var maxChild = children[0]
        var maxPayout = -2
        for (child in children) {
            if (child.payout > maxPayout) {
                maxPayout = child.payout
                maxChild = child
            }
        }
        return maxChild
    }

    fun findGoodMove(): Pair<ModStateNode, Pair<Int, Int>> {
        val goodState = findMaxPayout()
        var x = 0
        var y = 0
        for (i in 0 until 3) {
            var broken = false
            for (j in 0 until 3) {
                if (state[i][j] != goodState.state[i][j]) {
                    x = i
                    y = j
                    broken = true
                    break
                }
            }
            if (broken)
                break
        }
        return Pair(goodState, Pair(x, y))
    }


    fun generateStates() {
        if (depth < 6) {
            for (i in 0 until 3) {
                for (j in 0 until 3) {
                    if (state[i][j] != ' ')
                        continue
                    val newState = Array(3) { CharArray(3) }
                    for (i2 in 0 until 3)
                        for (j2 in 0 until 3)
                            newState[i2][j2] = state[i2][j2]
                    newState[i][j] = if (player) 'O' else 'X'
                    val newStateNode = ModStateNode(depth + 1, !player, newState)
                    addChild(newStateNode)
                    if (!newStateNode.won && !newStateNode.isFull)
                        newStateNode.generateStates()
                    else {
                        if (newStateNode.won && newStateNode.player)
                            newStateNode.payout = -1
                        else if (newStateNode.won && !newStateNode.player)
                            newStateNode.payout = 1
                        else
                            newStateNode.payout = 0
                    }
                }
            }
            findPayout()
        } else {
            for (i in 0 until 3) {
                for (j in 0 until 3) {
                    if (state[i][j] == ' ' || state[i][j] == if (player) 'X' else 'O')
                        continue
                    for (i2 in 0 until 3) {
                        if (i2 == i) continue
                        if (state[i2][j] == ' ') {
                            val newState = Array(3) { CharArray(3) }
                            for (i3 in 0 until 3)
                                for (j3 in 0 until 3)
                                    newState[i3][j3] = if (i == i3 && j == j3) ' ' else state[i3][j3]
                            newState[i2][j] = if (player) 'O' else 'X'
                            val newStateNode = ModStateNode(depth + 1, !player, newState)
                            addChild(newStateNode)
                            if (!newStateNode.won && !newStateNode.isFull && depth < 7)
                                newStateNode.generateStates()
                            else {
                                if (newStateNode.won && newStateNode.player)
                                    newStateNode.payout = -1
                                else if (newStateNode.won && !newStateNode.player)
                                    newStateNode.payout = 1
                                else
                                    newStateNode.payout = 0
                            }
                        }
                    }
                    for (j2 in 0 until 3) {
                        if (j2 == j) continue
                        if (state[i][j2] == ' ') {
                            val newState = Array(3) { CharArray(3) }
                            for (i3 in 0 until 3)
                                for (j3 in 0 until 3)
                                    newState[i3][j3] = if (i == i3 && j == j3) ' ' else state[i3][j3]
                            newState[i][j2] = if (player) 'O' else 'X'
                            val newStateNode = ModStateNode(depth + 1, !player, newState)
                            addChild(newStateNode)
                            if (!newStateNode.won && !newStateNode.isFull && depth < 7)
                                newStateNode.generateStates()
                            else {
                                if (newStateNode.won && newStateNode.player)
                                    newStateNode.payout = -1
                                else if (newStateNode.won && !newStateNode.player)
                                    newStateNode.payout = 1
                                else
                                    newStateNode.payout = 0
                            }
                        }
                    }
                }
                findPayout()
            }
        }
    }
}