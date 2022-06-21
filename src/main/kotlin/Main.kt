fun main() {

    var rows = 4
    var cols = 4
    val disabledRows = emptyArray<Int>()
    val disabledCols = emptyArray<Int>()
    val x = arrayOf(
        intArrayOf(800, 100, 900, 300),
        intArrayOf(400, 600, 200, 1200),
        intArrayOf(700, 500, 800, 900),
        intArrayOf(400, 900, 23, 500)
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
        val leastCostRow = findLeastElement[ROW_INDEX]
        val leastCostCol = findLeastElement[COLUMN_INDEX]
        var leastCost = findLeastElement[LEAST_ELEMENT_INDEX]

        base[leastCostRow!!][leastCostCol!!] = if (a[leastCostRow] > b[leastCostCol]) {
            b[leastCostCol]
        } else {
            a[leastCostRow]
        }
        a[leastCostRow] -= base[leastCostRow][leastCostCol]
        b[leastCostCol] -= base[leastCostRow][leastCostCol]
        if (a[leastCostRow] == 0) {
            disabledRows.plus(leastCostRow)
            disableRow(x,leastCostRow, maxElement)
            rows--
        } else if (b[leastCostCol] == 0) {
            disabledCols.plus(leastCostCol)
            disableColumn(x,leastCostCol, maxElement)
            cols--
        }
        iterations++
    }

    when {
        rows == 1 -> {

        }
        cols == 1 -> {

        }

    }

}

fun disableColumn(matrix: Array<IntArray>, column: Int, maxElement: Int) : Array<IntArray> {
    matrix.map {
        it[column] = maxElement
    }
    return matrix
}

fun disableRow(matrix: Array<IntArray>, disabledRow: Int, maxElement: Int) : Array<IntArray> {
    val newMatrix = emptyArray<IntArray>()
    for (row in matrix.indices) {
        if (row != disabledRow) {
            newMatrix[row] = matrix[row]
        } else {
            newMatrix[row] = intArrayOf(maxElement, maxElement, maxElement, maxElement)
        }
    }
    return newMatrix
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

fun findMinElementFromMatrix(matrix: Array<IntArray>) : Map<String, Int> {
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
    return mapOf(
        Pair(ROW_INDEX,leastCostRow),
        Pair(COLUMN_INDEX,leastCostCol),
        Pair(LEAST_ELEMENT_INDEX,leastCost)
    )
}

    private const val ROW_INDEX = "row_index"
    private const val COLUMN_INDEX = "column_index"
    private const val LEAST_ELEMENT_INDEX = "least_element"

