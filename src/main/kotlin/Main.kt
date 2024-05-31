fun main(args: Array<String>) {
    val input = Input()
    val sudoku = input.getInput()

    for (row in sudoku) {
        println(row.joinToString(" "))
    }
    var squares = findInSquares(0,3, sudoku);
    //lut erstellen
    var lut = arrayOf(ArrayList<Int>())
    for(r in sudoku.indices){
        for(c in sudoku[r].indices){
            var test = sudoku[r][c]
        }
    }
}

fun findInSquares(row: Int, col: Int, sudoku: Array<IntArray>): Array<IntArray> {
    return sudoku
}
