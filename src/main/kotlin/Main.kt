private const val BASE_VALUE = 0.0

fun main() {

    val matrixSize = insertMatrixSize()
    var rows = matrixSize[0]
    var cols = matrixSize[1]
    val disabledRows = BooleanArray(rows) {true}
    val disabledCols = BooleanArray(cols) {true}

    var x = insertMatrix(rows, cols)
    val a = insertAMatrix(rows)
    val b = insertBMatrix(cols)

//    var x = arrayOf(
//        intArrayOf(800, 100, 900, 300),
//        intArrayOf(400, 600, 200, 1200),
//        intArrayOf(700, 500, 800, 900),
//        intArrayOf(400, 900, 0, 500)
//    )

//    val a = arrayOf(110, 190, 90, 70)
//    val b = arrayOf(100, 60, 170, 130)

    val base = Array(rows){DoubleArray(cols){BASE_VALUE} }


    var iterations = 0

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
        if (a[leastCostRow] == BASE_VALUE) {
            disabledRows[leastCostRow] = false
            x = disableRow(x,leastCostRow, maxElement)
            rows--
        } else if (b[leastCostCol] == BASE_VALUE) {
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

fun disableColumn(matrix: Array<DoubleArray>, column: Int, maxElement: Double) : Array<DoubleArray> {
    println("disabled column = $column")
    matrix.map {
        it[column] = maxElement
    }
    return matrix
}

fun disableRow(matrix: Array<DoubleArray>, disabledRow: Int, maxElement: Double) : Array<DoubleArray> {
    println("disabled row = $disabledRow")
    val newMatrix = mutableListOf<DoubleArray>()
    for (row in matrix.indices) {
        if (row != disabledRow) {
            newMatrix.add(row, matrix[row])
        } else {
            newMatrix.add(row, DoubleArray(matrix[row].size){maxElement})
        }
    }
    return newMatrix.toTypedArray()
}

fun findMaxElementFromMatrix(matrix: Array<DoubleArray>) : Double {
    var max = BASE_VALUE
    matrix.map {
        it.map { matrixElement ->
            if (max < matrixElement) {
                max = matrixElement
            }
        }
    }
    return max
}

fun findMinElementFromMatrix(matrix: Array<DoubleArray>) : DoubleArray {
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

fun printMatrix(matrix: Array<DoubleArray>) {
    matrix.map {
        println("[${it.joinToString(", ")}]")
    }
}

fun insertMatrixSize() : IntArray {
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

fun validateInsert(insert : String?) : Boolean {
    return if (insert == null) {
        false
    } else {
        if (insert.toDoubleOrNull() is Double) {
            insert.toDouble() >= 0
        } else false
    }
}

fun insertMatrix(rows: Int, cols: Int) : Array<DoubleArray> {
    val matrix = mutableListOf<DoubleArray>()
    for (i in 0 until rows) {
        println("INSERTING MATRIX")
        println("Insert #${i+1} row:")
        val row = mutableListOf<Double>()
        for(k in 0 until cols) {
            var value = readLine()
            while (!validateInsert(value)) {
                println("value row ${i+1}, column ${k+1} not valid, try again")
                value = readLine()
            }
            row.add(k,value!!.toDouble())
        }
        matrix.add(row.toDoubleArray())
    }
    return matrix.toTypedArray()
}

fun insertAMatrix(rows: Int) : DoubleArray {
    println("INSERTING A MATRIX")
    val matrix = mutableListOf<Double>()
    for(k in 0 until rows) {
        var value = readLine()
        while (!validateInsert(value)) {
            println("value ${k+1} not valid, try again")
            value = readLine()
        }
        matrix.add(k,value!!.toDouble())
    }
    return matrix.toDoubleArray()
}

fun insertBMatrix(cols: Int) : DoubleArray {
    println("INSERTING B MATRIX")
    val matrix = mutableListOf<Double>()
    for(i in 0 until cols) {
        var value = readLine()
        while (!validateInsert(value)) {
            println("value ${i+1} not valid, try again")
            value = readLine()
        }
        matrix.add(i,value!!.toDouble())
    }
    return matrix.toDoubleArray()
}
