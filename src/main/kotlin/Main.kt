import java.util.*
import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    var input = Input()
    var sudoku_input = input.getInput()
    var readyCells: Queue<Pair<Int, Int>> = LinkedList()
    var sudoku: Array<Array<Field?>> = readInput(sudoku_input)

    printSudoku(sudoku)
    //print("rows: ")
    for(row in sudoku.indices) {
        val lut = findInRow(row, sudoku)
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
    //print("\ncols: ")
    for(col in sudoku.indices) {
        val lut = findInColumn(col, sudoku)
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
    println(readyCells)
    while(readyCells.isNotEmpty()) {
        var prc = readyCells.poll()
        sudoku[prc.first][prc.second]?.value = sudoku[prc.first][prc.second]?.lut?.first()!!
        removeFromLut(prc.first, prc.second)
    }
    printSudoku(sudoku)
}

fun removeFromLut(row: Int, colum: Int) {

}

private fun printSudoku(sudoku: Array<Array<Field?>>) {
    sudoku.forEachIndexed { r, row ->
        row.forEachIndexed { c, field ->
            if (field?.value == 0)
                print("- ")
            else
                print("${field?.value} ")
        }
        println()
    }
}

private fun readInput(sudoku_input: Array<IntArray>): Array<Array<Field?>> {
    var sudoku: Array<Array<Field?>> = Array(9) { Array<Field?>(9) { null } };
    sudoku_input.forEachIndexed { r, row ->
        row.forEachIndexed { c, value ->
            sudoku[r][c] = Field(value, (r / 3) * 3 + c / 3 + 1)
        }
    }
    for (r in 1 until 9 step 3) {
        for (c in 1 until 9 step 3) {
            val lut = findInSquares(r, c, sudoku)
            for (i in -1 until 2) {
                for (j in -1 until 2) {
                    if(sudoku[r + i][c + j]?.value == 0)
                        sudoku[r + i][c + j]?.lut = ArrayList(lut)
                }
            }
        }
    }
    return sudoku
}

fun findInColumn(c: Int, sudoku: Array<Array<Field?>>): MutableList<Int> {
    val lut = mutableListOf<Int>()
    for(i in 0 until 9) {
        if(sudoku[i][c]?.value != 0)
            sudoku[i][c]?.value?.let { lut.add(it) }
    }
    return lut
}

fun findInRow(r: Int, sudoku: Array<Array<Field?>>): MutableList<Int> {
    val lut = mutableListOf<Int>()
    for(i in 0 until 9) {
        if(sudoku[r][i]?.value != 0)
            sudoku[r][i]?.value?.let { lut.add(it) }
    }
    return lut
}

fun findInSquares(row: Int, col: Int, sudoku: Array<Array<Field?>>): MutableList<Int> {
    var lut : MutableList<Int> = mutableListOf()
    // Jump to the Middle of a block, look in all 9 directions, then drop all "0" aka not set fields.
    for(r in row - 1 until row + 2) {
        for(c in col - 1 until col + 2) {
            sudoku[r][c]?.let {
                lut.add(it.value)
            }
        }
    }
    var allNumbers: MutableList<Int> = mutableListOf(1,2,3,4,5,6,7,8,9)
    allNumbers.removeAll(lut)
    return allNumbers
}


class Field (var value: Int, var square: Int){
    var lut: MutableList<Int> = mutableListOf()
}
