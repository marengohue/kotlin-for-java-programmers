package games.gameOfFifteen

import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {

    private val board: GameBoard<Int> = createGameBoard(4)

    override fun initialize() {
        board
                .getAllCells()
                .zip(initializer.initialPermutation)
                .forEach { (cell, value) -> board[cell] = value }
    }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean =
            board
                    .getAllCells()
                    .mapNotNull(board::get)
                    .zipWithNext()
                    .all { (prev, next) -> next > prev }

    override fun processMove(direction: Direction) {
        val emptyCell = board.find { it == null }!! // There's always an empty cell
        val (di, dj) = direction.reversed().toCoordinateDelta()
        board
                .getCellOrNull(emptyCell.i + di, emptyCell.j + dj)
                ?.let { cellToMove ->
                    val valueToMove = board[cellToMove]
                    board[emptyCell] = valueToMove
                    board[cellToMove] = null
                }
    }

    override fun get(i: Int, j: Int): Int? = board[board.getCell(i, j)]
}

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)

