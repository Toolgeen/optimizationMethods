package SimplexMethod

import Constants.VALUE_ONE
import Constants.VALUE_ZERO
import Input.Task
import printMatrix

object SimplexMethod {

	operator fun invoke(basis: List<List<Double>>, task: Task) {

		val matrixOfRestrictions = createMatrixOfRestrictions(task.a, task.b)
		println("Система ограничений:")
		matrixOfRestrictions.printMatrix()

		val simplexTable = mutableListOf<DoubleArray>()
		val currentCornerPoint = mutableListOf<Double>()
		val vectorC = mutableListOf<Double>()
		val basisArguments = mutableListOf<Int>()
		val nonBasisArguments = mutableListOf<Int>()
		val coefficientsP = mutableListOf<Double>()
		var p0 = 0.0
		var index = 0

		//заполнение векторов текущей угловой точки и коэффициентов при переменных в целевой функции
		for (i in 0 until task.cols) {
			for (k in 0 until task.rows) {
				currentCornerPoint.add(basis[i][k])
				vectorC.add(task.matrix[i][k])
				p0 += basis[i][k] * task.matrix[i][k]
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

	private fun createMatrixOfRestrictions(a: List<Double>, b: List<Double>): List<List<Double>> {
		val matrixOfRestrictions = mutableListOf<List<Double>>()
		var counter = 1
		for (i in 0 until (a.size + b.size)) {
			if (i < b.size) {
				matrixOfRestrictions.add(
					List(a.size * b.size) {
						if ((it - i) % b.size == 0) {
							VALUE_ONE
						} else {
							VALUE_ZERO
						}
					}.plus(b[i])
				)
			} else {
				matrixOfRestrictions.add(
					List(a.size * b.size) {
						if (it + 1 <= counter * b.size && it + 1 > (i - b.size) * b.size) {
							VALUE_ONE
						} else {
							VALUE_ZERO
						}

					}.plus(a[i - b.size])
				)
				counter++
			}
		}
		return matrixOfRestrictions
	}
}