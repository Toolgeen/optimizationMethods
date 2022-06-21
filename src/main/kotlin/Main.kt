fun main() {

    val rows = 4
    val cols = 4
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

    var count = 0

    val answer = findMinElementFromMatrix(x)

    println("${answer[0]}, ${answer[1]}, ${answer[2]}")

}

fun findMinElementFromMatrix(matrix: Array<IntArray>) : Array<Int> {
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
    return arrayOf(leastCost, leastCostRow, leastCostCol)
}