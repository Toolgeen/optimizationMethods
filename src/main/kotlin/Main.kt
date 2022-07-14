private const val VALUE_ZERO = 0.0
private const val VALUE_ONE = 1.0
private var insertRows: Int = 4 //TODO: rewrite to 0
private var insertCols: Int = 4 //TODO: rewrite to 0

enum class CoefficientPCheck {
    SOLVED, NO_SOLVES, CONTINUE
}

private fun main() {

//    val matrixSize = insertMatrixSize()
//    rows = matrixSize[0]
//    cols = matrixSize[1]

//    var x = insertMatrix(rows, cols)
//    val a = insertAMatrix(rows)
//    val b = insertBMatrix(cols)

    val x = arrayOf(
        doubleArrayOf(800.0, 100.0, 900.0, 300.0),
        doubleArrayOf(400.0, 600.0, 200.0, 1200.0),
        doubleArrayOf(700.0, 500.0, 800.0, 900.0),
        doubleArrayOf(400.0, 900.0, 0.0, 500.0)
    )

    val a = arrayOf(110.0, 190.0, 90.0, 70.0).toDoubleArray()
    val b = arrayOf(100.0, 60.0, 170.0, 130.0).toDoubleArray()

    val basis = leastElementMethod(x, a, b)
    if (isSimplexMethodNeeded()) {
        simplexMethod(basis, a, b, x)
    }


}

private fun simplexMethod(basis: Array<DoubleArray>, a: DoubleArray, b: DoubleArray, matrixC: Array<DoubleArray>) {

    val matrixOfRestrictions = createMatrixOfRestrictions(a, b)
    println("Система ограничений:")
    printMatrix(matrixOfRestrictions)

    val simplexTable = mutableListOf<DoubleArray>()
    val currentCornerPoint = mutableListOf<Double>()
    val vectorC = mutableListOf<Double>()
    val basisArguments = mutableListOf<Int>()
    val nonBasisArguments = mutableListOf<Int>()
    val coefficientsP = mutableListOf<Double>()
    var p0 = 0.0
    var index = 0

    //заполнение векторов текущей угловой точки и коэффициентов при переменных в целевой функции
    for (i in 0 until insertCols) {
        for (k in 0 until insertRows) {
            currentCornerPoint.add(basis[i][k])
            vectorC.add(matrixC[i][k])
            p0 += basis[i][k] * matrixC[i][k]
            if (basis[i][k] == 0.0) {
                nonBasisArguments.add(index)
            } else {
                basisArguments.add(index)
            }
            index++
        }
    }

    for (i in vectorC.indices) {
        p0 += vectorC[i] * currentCornerPoint[i]
    }
    for (i in currentCornerPoint.indices) {
        if (currentCornerPoint[i] == 0.0) {
            coefficientsP.add(0.0)
        } else {
            var sum = 0.0
            for (k in matrixOfRestrictions.indices) {
                sum += vectorC[i] * matrixOfRestrictions[k][i]
            }
            coefficientsP.add(vectorC[i] - sum)
        }

    }
    println("Индексы базисных переменных:")
    println(basisArguments.joinToString(", "))
    println("Индексы небазисных переменных:")
    println(nonBasisArguments.joinToString(", "))
    println("Текущая угловая точка (начальный базис):")
    println(currentCornerPoint.joinToString(", "))
    println("Вектор коэффициентов при переменных в целевой функции:")
    println(vectorC.joinToString(", "))
    println("Коэффициент целевой функции p0, выраженной через свободные переменные:")
    println(p0)
    println("Коэффициенты целевой функции, выраженной через свободные переменные:")
    println(coefficientsP.joinToString(", "))

//    when (checkCoefficientsP(coefficientsP,matrixOfRestrictions)) {
//        CoefficientPCheck.NO_SOLVES -> println("нет решений")
//        CoefficientPCheck.SOLVED -> println("решено")
//        CoefficientPCheck.CONTINUE -> println("продолжаем решение")
//    }

//    val iterationOfSimplexTable = 1
//    while (checkCoefficientsP(coefficientsP,matrixOfRestrictions) == CoefficientPCheck.CONTINUE) {
//        println("Соблюдено условие В, продолжаем решение:")
//
//        var firstRow = mutableListOf<String>()
//        firstRow.add("СТ-$iterationOfSimplexTable")
//        for (i in nonBasisArguments) {
//            firstRow.add("X${i}")
//        }
//        firstRow.add("b")
//        for (i in basisArguments) {
//
//        }
//    }



}



private fun checkCoefficientsP(
    coefficientsP: MutableList<Double>,
    matrixOfRestrictions: Array<DoubleArray>
) : CoefficientPCheck {
    // шаг 3, проверка коэффициентов целевой функции
    var conditionA = true
    var conditionB = true
    var conditionC = true
    for (i in coefficientsP) {
        if (i < 0) {
            for (k in matrixOfRestrictions.indices) {
                var isColNegativeOrZero = true
                isColNegativeOrZero = isColNegativeOrZero && (matrixOfRestrictions[k][coefficientsP.indexOf(i)] <= 0)
                conditionB = conditionB && isColNegativeOrZero
            }
        }
        conditionA = conditionA && (i >= 0)
    }
    if (conditionA) {
        return CoefficientPCheck.SOLVED
    }
    if (conditionB) {
        return CoefficientPCheck.NO_SOLVES
    }
    return CoefficientPCheck.CONTINUE
}


private fun createMatrixOfRestrictions(a: DoubleArray, b: DoubleArray): Array<DoubleArray> {
    val matrixOfRestrictions = mutableListOf<DoubleArray>()
    var counter = 1
    for (i in 0 until (a.size + b.size)) {
        if (i < b.size) {
            matrixOfRestrictions.add(
                Array(a.size * b.size) {
                    if ((it - i) % b.size == 0) {
                        VALUE_ONE
                    } else {
                        VALUE_ZERO
                    }
                }.toDoubleArray().plus(b[i])
            )
        } else {
            matrixOfRestrictions.add(
                Array(a.size * b.size) {
                    if (it + 1 <= counter * b.size && it + 1 > (i - b.size) * b.size) {
                        VALUE_ONE
                    } else {
                        VALUE_ZERO
                    }

                }.toDoubleArray().plus(a[i - b.size])
            )
            counter++
        }
    }
    return matrixOfRestrictions.toTypedArray()
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

private fun leastElementMethod(
    matrix: Array<DoubleArray>,
    aMatrix: DoubleArray,
    bMatrix: DoubleArray
): Array<DoubleArray> {

    var rows = insertRows
    var cols = insertCols
    val a = aMatrix.clone()
    val b = bMatrix.clone()
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