package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardBase(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = SquareBoardWithData(width)

class SquareBoardWithData<T>(override val width: Int): SquareBoardBase(width), GameBoard<T> {

    private val valuesMap = mutableMapOf<Cell, T?>()

    override fun get(cell: Cell): T? = valuesMap[cell]

    override fun set(cell: Cell, value: T?) {
        valuesMap[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
        getAllCells().filter { predicate(get(it)) }


    override fun find(predicate: (T?) -> Boolean): Cell? =
        getAllCells().find { predicate(get(it)) }


    override fun any(predicate: (T?) -> Boolean): Boolean =
        getAllCells().any { predicate(get(it)) }

    override fun all(predicate: (T?) -> Boolean): Boolean =
        getAllCells().all { predicate(get(it)) }

}

open class SquareBoardBase(override val width: Int) : SquareBoard {

    private val cellMap = mutableMapOf<Int, Cell>()

    override fun getCellOrNull(i: Int, j: Int): Cell? =
        if (indicesValid(i, j))
            getCell(i, j)
        else
            null

    override fun getCell(i: Int, j: Int): Cell =
        if (indicesValid(i, j)) {
            val cellId = (i - 1) + width * (j - 1)
            if (!cellMap.containsKey(cellId))
                cellMap[cellId] = Cell(i, j)
            cellMap.getValue(cellId)
        }
        else
            throw IllegalArgumentException("Indices out of valid range!")

    private fun indicesValid(i: Int, j: Int): Boolean =
        i in 1..width && j in 1..width

    override fun getAllCells(): Collection<Cell> =
            (1..width).flatMap {
                i -> (1..width).map { j -> getCell(i, j) }
            }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> =
        jRange
                .map { j -> getCell(i, j.coerceIn(1, width)) }
                .distinct()

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> =
        iRange
                .map { i -> getCell(i.coerceIn(1, width), j) }
                .distinct()

    override fun Cell.getNeighbour(direction: Direction): Cell? =
            when (direction) {
                UP -> getCellOrNull(this.i - 1, this.j)
                LEFT -> getCellOrNull(this.i, this.j - 1)
                DOWN -> getCellOrNull(this.i + 1, this.j)
                RIGHT -> getCellOrNull(this.i, this.j + 1)
            }
}