package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)


class GameBoardImpl<T>(width: Int) : SquareBoardImpl(width), GameBoard<T> {
    private val gameBoard: MutableMap<Cell, T?> = getAllCells().associate { cell -> cell to null }.toMutableMap()


    override fun get(cell: Cell): T? = gameBoard[cell]

    override fun set(cell: Cell, value: T?) {
        gameBoard[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return gameBoard.filter { (_, value) -> predicate(value) }.map { (cell, _) -> cell }
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return gameBoard.toList().find { (_, value) -> predicate(value) }?.first
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return gameBoard.toList().any { (_, value) -> predicate(value) }
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return gameBoard.toList().all { (_, value) -> predicate(value) }
    }


}

open class SquareBoardImpl(final override val width: Int) : SquareBoard {
    private val board: List<List<Cell>> = List(width) { row -> List(width) { column -> Cell(row + 1, column + 1) } }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return if (board.size > i.index() && board[i.index()].size > j.index()) board[i.index()][j.index()] else null
    }


    override fun getCell(i: Int, j: Int): Cell = board[i.index()][j.index()]

    override fun getAllCells(): Collection<Cell> {
        val cellsCollection = ArrayList<Cell>()
        for (row in board) for (cell in row) cellsCollection.add(cell)
        return cellsCollection
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val boardRow = board[i.index()]
        val listSize = if (jRange.count() > boardRow.size) boardRow.size else jRange.count()
        return List(listSize) { index -> boardRow[jRange.elementAt(index).index()] }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val listSize = iRange.intersect(1..board.size).size
        return List(listSize) { index -> board[iRange.elementAt(index).index()][j.index()] }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            UP -> if (i.index() <= 0) null else board[i.index() - 1][j.index()]
            DOWN -> if (i.index() + 1 >= board.size) null else board[i.index() + 1][j.index()]
            LEFT -> board[i.index()].getOrNull(j.index() - 1)
            RIGHT -> board[i.index()].getOrNull(j.index() + 1)
        }
    }

    private fun Int.index(): Int = this - 1

}