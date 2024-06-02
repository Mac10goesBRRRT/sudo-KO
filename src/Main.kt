import java.util.*
import kotlin.collections.ArrayList

fun main() {
    val input = Input()
    val sudoku = Sudoku(input.getInput())
    println(sudoku.toString())
    sudoku.solve()
    println(sudoku.toString())
}


class Sudoku (input: Array<IntArray>){
    private val readyCells: MutableSet<Pair<Int, Int>> = LinkedHashSet()
    private val sudoku: Array<Array<Field?>> = readInput(input)

    fun solve(){
        setSquares()
        //print("rows: ")
        for(row in sudoku.indices) {
            val lut = findInRowColumn(row,false)
            //print("Row lut: $lut\n")
            for(column in sudoku.indices) {
                if(sudoku[row][column]?.lut?.size!! > 0) {
                    //print("removing: $lut from: ${sudoku[row][column]?.lut}, ")
                    sudoku[row][column]?.lut?.removeAll(lut)
                    //print("result: ${sudoku[row][column]?.lut}\n")
                } else {
                    //print("value set to: [${sudoku[row][column]?.value}]\n")
                }
            }
        }
        //print("\n cols: ")
        for(col in sudoku.indices) {
            val lut = findInRowColumn(col,true)
            //print("Col lut: $lut\n")
            for(row in sudoku.indices) {
                if(sudoku[row][col]?.lut?.size!! > 0) {
                    //print("removing: $lut from: ${sudoku[row][col]?.lut}, ")
                    sudoku[row][col]?.lut?.removeAll(lut)
                    //print("result: ${sudoku[row][col]?.lut}\n")
                } else {
                    //print("value set to: [${sudoku[row][col]?.value}]\n")
                }
                if(sudoku[row][col]?.lut?.size!! == 1) {
                    readyCells.add(Pair(row, col))
                }
            }
        }
        while(readyCells.isNotEmpty()) {
            //println(readyCells)
            val prc = readyCells.first()
            readyCells.remove(prc)
            val toSet = sudoku[prc.first][prc.second]?.lut?.first()!!
            sudoku[prc.first][prc.second]?.value = toSet
            removeFromLut(prc.first, prc.second, toSet)
            //println(toString())
        }
    }

    private fun setSquares() {
        for (r in 1 until 9 step 3) {
            for (c in 1 until 9 step 3) {
                val lut = findInSquares(r, c)
                for (i in -1 .. 1) {
                    for (j in -1 .. 1) {
                        if (sudoku[r + i][c + j]?.value == 0)
                            sudoku[r + i][c + j]?.lut = ArrayList(lut)
                    }
                }
            }
        }
    }

    private fun removeFromLut(row: Int, colum: Int, toRemove: Int) {
        val squareMidRow = (row/3)*3+1
        val squareMidCol = (colum/3)*3+1
        //println("Square Middle for $row,$colum is $squareMidRow,$squareMidCol, value is $toRemove")
        for(i in -1 .. 1){
            for (j in -1 .. 1){
                sudoku[squareMidRow+i][squareMidCol+j]?.lut?.remove(toRemove)
                if(sudoku[squareMidRow+i][squareMidCol+j]?.lut?.size!! == 1) {
                    readyCells.add(Pair(squareMidRow+i,squareMidCol+j))
                }
            }
        }
        for(i in 0 until 9){
            sudoku[row][i]?.lut?.remove(toRemove)
            if(sudoku[row][i]?.lut?.size!! == 1) {
                readyCells.add(Pair(row,i))
            }
            sudoku[i][colum]?.lut?.remove(toRemove)
            if(sudoku[i][colum]?.lut?.size!! == 1) {
                readyCells.add(Pair(i,colum))
            }
        }
    }

    /** @param coordinate coordinate of row/column
     *  @param isColumn direction that is checked, true for row 0-9, false for column 0-9
     *  @return A list of possibilities that need to be removed
     */
    private fun findInRowColumn(coordinate: Int, isColumn: Boolean ): MutableList<Int> {
        val lut = mutableListOf<Int>()
        for(i in 0 until 9){
            val value = if(isColumn) sudoku[i][coordinate]?.value else sudoku[coordinate][i]?.value
            value?.takeIf { it != 0 }?.let { lut.add(it) }
        }
        return lut
    }

    private fun findInSquares(row: Int, col: Int): MutableList<Int> {
        val lut : MutableList<Int> = mutableListOf()
        // Jump to the Middle of a block, look in all 9 directions, then drop all "0" aka non set fields.
        for(r in row - 1 until row + 2) {
            for(c in col - 1 until col + 2) {
                sudoku[r][c]?.let { lut.add(it.value) }
            }
        }
        return (1..9).toMutableList().apply { removeAll(lut) }
    }

    override fun toString(): String {
        val str = StringBuilder()
        sudoku.forEachIndexed { _, row ->
            row.forEachIndexed { _, field ->
                if (field?.value == 0)
                    str.append("- ")
                else
                    str.append("${field?.value} ")
            }
            str.append("\n")
        }
        return str.toString()
    }

    private fun readInput(sudokuInput: Array<IntArray>): Array<Array<Field?>> {
        val sudoku: Array<Array<Field?>> = Array(9) { Array(9) { null } }
        sudokuInput.forEachIndexed { r, row ->
            row.forEachIndexed { c, value ->
                sudoku[r][c] = Field(value)
            }
        }
        return sudoku
    }
}


class Field(var value: Int){
    var lut: MutableList<Int> = mutableListOf()
}
