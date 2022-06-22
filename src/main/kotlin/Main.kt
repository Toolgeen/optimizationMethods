fun main() {

    var rows = 4
    var cols = 4
    val disabledRows = booleanArrayOf(true, true, true, true)
    val disabledCols = booleanArrayOf(true, true, true, true)
    var x = arrayOf(
        intArrayOf(800, 100, 900, 300),
        intArrayOf(400, 600, 200, 1200),
        intArrayOf(700, 500, 800, 900),
        intArrayOf(400, 900, 0, 500)
    )
    val base = arrayOf(
        intArrayOf(0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0)
    )
    val a = arrayOf(110, 190, 90, 70)
    val b = arrayOf(100, 60, 170, 130)
    var iterations = 0

    val maxElement = findMaxElementFromMatrix(x)

    while ((rows != 1) || (cols != 1)) {
        val findLeastElement = findMinElementFromMatrix(x)
        val leastCost = findLeastElement[0]
        val leastCostRow = findLeastElement[1]
        val leastCostCol = findLeastElement[2]
        println("least element = $leastCost, indices $leastCostRow:$leastCostCol")

        base[leastCostRow][leastCostCol] = if (a[leastCostRow] > b[leastCostCol]) {
            b[leastCostCol]
        } else {
            a[leastCostRow]
        }
        printMatrix(base)

        a[leastCostRow] -= base[leastCostRow][leastCostCol]
        b[leastCostCol] -= base[leastCostRow][leastCostCol]
        if (a[leastCostRow] == 0) {
            disabledRows[leastCostRow] = false
            x = disableRow(x,leastCostRow, maxElement)
            rows--
        } else if (b[leastCostCol] == 0) {
            disabledCols[leastCostCol] = false
            x = disableColumn(x,leastCostCol, maxElement)
            cols--
        }
        iterations++
    }

    when {
        rows == 1 -> {
            for (i in base.indices) {
                if (disabledRows[i]) {
                    for (k in base.indices) {
                        if (disabledCols[k]) {
                            base[i][k] = b[k]
                        }
                    }
                }
            }
        }
        cols == 1 -> {
            for (i in base.indices) {
                if (disabledCols[i]) {
                    for (k in base.indices) {
                        if (disabledRows[k]) {
                            base[k][i] = a[k]
                        }
                    }
                }
            }
        }
    }

    printMatrix(base)
    println(iterations)
    println("SOLVED")
}

fun disableColumn(matrix: Array<IntArray>, column: Int, maxElement: Int) : Array<IntArray> {
    println("disabled column = $column")
    matrix.map {
        it[column] = maxElement
    }
    return matrix
}

fun disableRow(matrix: Array<IntArray>, disabledRow: Int, maxElement: Int) : Array<IntArray> {
    println("disabled row = $disabledRow")
    val newMatrix = mutableListOf<IntArray>()
    for (row in matrix.indices) {
        if (row != disabledRow) {
            newMatrix.add(row, matrix[row])
        } else {
            newMatrix.add(row, IntArray(matrix[row].size){maxElement})
        }
    }
    return newMatrix.toTypedArray()
}

fun findMaxElementFromMatrix(matrix: Array<IntArray>) : Int {
    var max = 0
    matrix.map {
        it.map { matrixElement ->
            if (max < matrixElement) {
                max = matrixElement
            }
        }
    }
    return max
}

fun findMinElementFromMatrix(matrix: Array<IntArray>) : IntArray {
    var leastCostRow = 0
    var leastCostCol = 0
    var leastCost = matrix[leastCostRow][leastCostCol]
    for (row in matrix.indices) {
        for (col in matrix[row].indices) {
            if (leastCost > matrix[row][col]) {
                leastCostRow = row
                leastCostCol = col
                leastCost = matrix[row][col]
            }
        }
    }
    return intArrayOf(leastCost, leastCostRow, leastCostCol)
}

fun printMatrix(matrix: Array<IntArray>) {
    matrix.map {
        println("[${it.joinToString(", ")}]")
    }
}


