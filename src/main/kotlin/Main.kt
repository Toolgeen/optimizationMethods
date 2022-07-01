private const val VALUE_ZERO = 0.0
private const val VALUE_ONE = 1.0
private var rows: Int = 0
private var cols: Int = 0

private fun main() {

    val matrixSize = insertMatrixSize()
    rows = matrixSize[0]
    cols = matrixSize[1]

    var x = insertMatrix(rows, cols)
    val a = insertAMatrix(rows)
    val b = insertBMatrix(cols)

    val basis = leastElementMethod(x, b, a)
    if (isSimplexMethodNeeded()) {
        simplexMethod()
    }

//        тестовый вариант
//     var x = arrayOf(
//        intArrayOf(800, 100, 900, 300),
//        intArrayOf(400, 600, 200, 1200),
//        intArrayOf(700, 500, 800, 900),
//        intArrayOf(400, 900, 0, 500)
//    )

//    val a = arrayOf(110, 190, 90, 70)
//    val b = arrayOf(100, 60, 170, 130)

}

private fun simplexMethod() {

}

private fun isSimplexMethodNeeded(): Boolean {
    println("Нужно ли продолжить решение симплекс-методом?")
    println("Введите ${VALUE_ONE.toInt()}, если да, и любое другое число, если нет.")
    var result = readLine()
    while (!(validateInsert(result))) {
        println("Неизвестный вариант, пожалуйста, введите ${VALUE_ONE.toInt()}, если да, и любое другое число, если нет.")
        result = readLine()
    }
    return when (result?.toInt()) {
        VALUE_ONE.toInt() -> true
        else -> false
    }
}

private fun leastElementMethod(matrix: Array<DoubleArray>, b: DoubleArray, a: DoubleArray): Array<DoubleArray> {

    var x = matrix
    val disabledRows = BooleanArray(rows) { true }
    val disabledCols = BooleanArray(cols) { true }
    val base = Array(rows) { DoubleArray(cols) { VALUE_ZERO } }


    var iterations = VALUE_ZERO.toInt()

    val maxElement = findMaxElementFromMatrix(x)

    while ((rows != 1) || (cols != 1)) {
        val findLeastElement = findMinElementFromMatrix(x)
        val leastCost = findLeastElement[0]
        val leastCostRow = findLeastElement[1].toInt()
        val leastCostCol = findLeastElement[2].toInt()
        println("least element = $leastCost, indices $leastCostRow:$leastCostCol")

        base[leastCostRow][leastCostCol] = if (a[leastCostRow] > b[leastCostCol]) {
            b[leastCostCol]
        } else {
            a[leastCostRow]
        }
        printMatrix(base)

        a[leastCostRow] -= base[leastCostRow][leastCostCol]
        b[leastCostCol] -= base[leastCostRow][leastCostCol]
        if (a[leastCostRow] == VALUE_ZERO) {
            disabledRows[leastCostRow] = false
            x = disableRow(x, leastCostRow, maxElement)
            rows--
        } else if (b[leastCostCol] == VALUE_ZERO) {
            disabledCols[leastCostCol] = false
            x = disableColumn(x, leastCostCol, maxElement)
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
    println("Решено за $iterations итераций.")
    println("ПОИСК БАЗИСА МЕТОДОМ НАИМЕНЬШЕЙ СТОИМОСТИ ЗАКОНЧЕН")
    return base
}

private fun disableColumn(matrix: Array<DoubleArray>, column: Int, maxElement: Double): Array<DoubleArray> {
    println("disabled column = $column")
    matrix.map {
        it[column] = maxElement
    }
    return matrix
}

private fun disableRow(matrix: Array<DoubleArray>, disabledRow: Int, maxElement: Double): Array<DoubleArray> {
    println("disabled row = $disabledRow")
    val newMatrix = mutableListOf<DoubleArray>()
    for (row in matrix.indices) {
        if (row != disabledRow) {
            newMatrix.add(row, matrix[row])
        } else {
            newMatrix.add(row, DoubleArray(matrix[row].size) { maxElement })
        }
    }
    return newMatrix.toTypedArray()
}

private fun findMaxElementFromMatrix(matrix: Array<DoubleArray>): Double {
    var max = VALUE_ZERO
    matrix.map {
        it.map { matrixElement ->
            if (max < matrixElement) {
                max = matrixElement
            }
        }
    }
    return max
}

private fun findMinElementFromMatrix(matrix: Array<DoubleArray>): DoubleArray {
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
    return doubleArrayOf(leastCost, leastCostRow.toDouble(), leastCostCol.toDouble())
}

private fun printMatrix(matrix: Array<DoubleArray>) {
    matrix.map {
        println("[${it.joinToString(", ")}]")
    }
}

private fun insertMatrixSize(): IntArray {
    println("Insert rows count:")
    var insertRows = readLine()
    println("Insert cols count:")
    var insertCols = readLine()
    while (!(validateInsert(insertRows) && validateInsert(insertCols))) {
        println("Data not valid, insert rows count:")
        insertRows = readLine()
        println("Insert cols count:")
        insertCols = readLine()
    }
    return intArrayOf(insertRows!!.toInt(), insertCols!!.toInt())
}

private fun validateInsert(insert: String?): Boolean {
    return if (insert == null) {
        false
    } else {
        if (insert.trim().toDoubleOrNull() is Double) {
            insert.trim().toDouble() >= 0
        } else false
    }
}

private fun insertMatrix(rows: Int, cols: Int): Array<DoubleArray> {
    val matrix = mutableListOf<DoubleArray>()
    for (i in 0 until rows) {
        println("INSERTING MATRIX")
        println("Insert #${i + 1} row:")
        val row = mutableListOf<Double>()
        for (k in 0 until cols) {
            var value = readLine()
            while (!validateInsert(value)) {
                println("value row ${i + 1}, column ${k + 1} not valid, try again")
                value = readLine()
            }
            row.add(k, value!!.toDouble())
        }
        matrix.add(row.toDoubleArray())
    }
    return matrix.toTypedArray()
}

private fun insertAMatrix(rows: Int): DoubleArray {
    println("INSERTING A MATRIX")
    val matrix = mutableListOf<Double>()
    for (k in 0 until rows) {
        var value = readLine()
        while (!validateInsert(value)) {
            println("value ${k + 1} not valid, try again")
            value = readLine()
        }
        matrix.add(k, value!!.toDouble())
    }
    return matrix.toDoubleArray()
}

private fun insertBMatrix(cols: Int): DoubleArray {
    println("INSERTING B MATRIX")
    val matrix = mutableListOf<Double>()
    for (i in 0 until cols) {
        var value = readLine()
        while (!validateInsert(value)) {
            println("value ${i + 1} not valid, try again")
            value = readLine()
        }
        matrix.add(i, value!!.toDouble())
    }
    return matrix.toDoubleArray()
}