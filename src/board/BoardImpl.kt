package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardBase(width)

fun <T> createGameBoard(width: Int): GameBoard<T> = SquareBoardWithData(width)

/**
 * A mutable-map based implementation of the square board
 */
private class SquareBoardWithData<T>(override val width: Int): SquareBoardBase(width), GameBoard<T> {

    private val valuesMap = mutableMapOf<Cell, T?>()

    /**
     * Get a value stored in a given cell
     */
    override fun get(cell: Cell): T? = valuesMap[cell]

    /**
     * Set a value in the given cell
     */
    override fun set(cell: Cell, value: T?) {
        valuesMap[cell] = value
    }

    /**
     * Get only the cells for which the containing value satisfies the predicate
     */
    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
        getAllCells().filter { predicate(get(it)) }

    /**
     * Searches for a first element that matches the given predicate and returns the containing cell
     */
    override fun find(predicate: (T?) -> Boolean): Cell? =
        getAllCells().find { predicate(get(it)) }

    /**
     * Determines if there is any value which matches a given predicate stored in the board
     */
    override fun any(predicate: (T?) -> Boolean): Boolean =
        getAllCells().any { predicate(get(it)) }

    /**
     * Determines if all of the values stored in the board match a given predicate
     */
    override fun all(predicate: (T?) -> Boolean): Boolean =
        getAllCells().all { predicate(get(it)) }

}

/**
 * Base class for a square board
 */
private open class SquareBoardBase(override val width: Int) : SquareBoard {

    private val cellMap = mutableMapOf<Int, Cell>()

    /**
     * Try get a cell under the given coordinates or null if the coordinates are outside the board
     */
    override fun getCellOrNull(i: Int, j: Int): Cell? =
        if (validateIndices(i, j))
            getCell(i, j)
        else
            null

    /**
     * Try get a cell under the given coordinates
     */
    override fun getCell(i: Int, j: Int): Cell =
        if (validateIndices(i, j)) {
            cellMap.getOrPut((i - 1) + width * (j - 1), { Cell(i, j) })
        }
        else
            throw IllegalArgumentException("Indices out of valid range!")

    /**
     * Validates that the given coordinates are in the board
     */
    private fun validateIndices(i: Int, j: Int): Boolean =
        i in 1..width && j in 1..width

    /**
     * Returns all of the cells in the board
     */
    override fun getAllCells(): Collection<Cell> =
            (1..width).flatMap {
                i -> (1..width).map { j -> getCell(i, j) }
            }

    /**
     * Retrieves the range of cells from (i, jMin) to (i, jMax)
     */
    override fun getRow(i: Int, jRange: IntProgression): List<Cell> =
            jRange
                    .takeWhile { it in 1..width }
                    .map { getCell(i, it) }

    /**
     * Retrieves the range of cells from (iMin, j) to (iMax, j)
     */
    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> =
            iRange
                    .takeWhile { it in 1..width }
                    .map { getCell(it, j) }

    /**
     * Retrieves the neighbouring cell
     */
    override fun Cell.getNeighbour(direction: Direction): Cell? =
            when (direction) {
                UP -> getCellOrNull(this.i - 1, this.j)
                LEFT -> getCellOrNull(this.i, this.j - 1)
                DOWN -> getCellOrNull(this.i + 1, this.j)
                RIGHT -> getCellOrNull(this.i, this.j + 1)
            }
}