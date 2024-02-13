package SimplexMethod

import CoefficientPCheck
import Constants.VALUE_ONE
import Constants.VALUE_ZERO
import Input.Task
import models.Matrix
import java.lang.Exception

object SimplexMethod {

	operator fun invoke(basis: Matrix, task: Task) {

		val initialSimplexTable = createInitialSimplexTable(task)
		println("Стандартная форма ЗЛП для решения симплекс-методом:")
		initialSimplexTable.print()

		val currentCornerPoint = mutableListOf<Double>()
		val vectorB = task.rightPartOfRestrictionsSystem
		val vectorC = task.targetFunCoefficients
		val basisArgs = mutableListOf<Int>()
		val nonBasisArgs = mutableListOf<Int>()
		val coefficientsP = mutableListOf<Double>()
		var p0 = 0.0
		var index = 0

		//заполнение векторов текущей угловой точки и коэффициентов при переменных в целевой функции
		for (i in 0 until task.cols) {
			for (k in 0 until task.rows) {
				currentCornerPoint.add(basis.getValue(i, k))
				p0 += basis.getValue(i, k) * task.matrix[i][k]
				if (basis.getValue(i, k) == 0.0) {
					nonBasisArgs.add(index)
				} else {
					basisArgs.add(index)
				}
				index++
			}
		}

//		for (i in vectorC.indices) {
//			p0 += vectorC[i] * currentCornerPoint[i]
//		}

//		for (i in currentCornerPoint.indices) {
//			if (currentCornerPoint[i] == 0.0) {
//				coefficientsP.add(0.0)
//			} else {
//				var sum = 0.0
//				for (k in initialSimplexTable.indices) {
//					sum += vectorC[i] * initialSimplexTable.getValue(k, i)
//				}
//				coefficientsP.add(vectorC[i] - sum)
//			}
//		}

		println("------------------------------------------")

		val modifiedSimplexTable = mapToLeastElementMethodSolve(
			initialSimplexTable = initialSimplexTable,
			basis = basis,
			basisArgs = basisArgs,
			vectorB = vectorB,
			vectorC = vectorC
		).print()

		println("Индексы базисных переменных:")
		println(basisArgs.joinToString(", "))
		println("Индексы небазисных переменных:")
		println(nonBasisArgs.joinToString(", "))
//		println("Текущая угловая точка (начальный базис):")
//		println(currentCornerPoint.joinToString(", "))
//		println("Вектор коэффициентов при переменных в целевой функции:")
//		println(task.targetFunCoefficients)
//		println("Коэффициент целевой функции p0, выраженной через свободные переменные:")
//		println(p0)
//		println("Коэффициенты целевой функции, выраженной через свободные переменные:")
//		println(coefficientsP.joinToString(", "))

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

	private fun createInitialSimplexTable(task: Task): Matrix {
		var counter = 1
		return Matrix(mutableListOf<MutableList<Double>>().apply {
			for (i in 0 until (task.a.size + task.b.size)) {
				if (i < task.b.size) {
					add(
						List(task.a.size * task.b.size) {
							if ((it - i) % task.b.size == 0) {
								VALUE_ONE
							} else {
								VALUE_ZERO
							}
						}.plus(task.b[i]).toMutableList()
					)

				} else {
					add(
						List(task.a.size * task.b.size) {
							if (it + 1 <= counter * task.b.size && it + 1 > (i - task.b.size) * task.b.size) {
								VALUE_ONE
							} else {
								VALUE_ZERO
							}

						}.plus(task.a[i - task.b.size]).toMutableList()
					)
					counter++
				}
			}
			add(task.targetFunCoefficients.map { it.unaryMinus() }.toMutableList())
		}
		)
	}

	fun mapToLeastElementMethodSolve(
		initialSimplexTable: Matrix,
		basis: Matrix,
		basisArgs: List<Int>,
		vectorB: List<Double>,
		vectorC: List<Double>,
		) : Matrix {

		initialSimplexTable.matrix.forEachIndexed { rowIndex, row ->
			row.forEachIndexed { colIndex, element ->

				if (colIndex in basisArgs) {

					var minimumRightElement = Double.MAX_VALUE
					var minIndex = 0

					vectorB.forEachIndexed { indexB, b ->
						if (initialSimplexTable.getValue(indexB, colIndex) > 0.0 && b < minimumRightElement) {
							minimumRightElement = b
							minIndex = indexB
						}
					}

					var currentCoefficient = initialSimplexTable.getValue(minIndex, colIndex)

					for (k in 0 until initialSimplexTable.colSize) {
						if (currentCoefficient != 0.0) {
							initialSimplexTable.setValue(initialSimplexTable.getValue(minIndex, k) * currentCoefficient, minIndex, k)
						}
					}

					for (k in 0 until initialSimplexTable.rowSize) {
						if (initialSimplexTable.getValue(k, colIndex) != 0.0 && k != minIndex) {

							currentCoefficient = initialSimplexTable.getValue(k, colIndex)

							for (j in 0 until initialSimplexTable.colSize) {
								initialSimplexTable.setValue(
									value = initialSimplexTable.getValue(k, j) - (currentCoefficient * initialSimplexTable.getValue(minIndex, j)),
									row = k,
									col = j
								)
							}
						}
					}
				}
			}
		}
		return initialSimplexTable
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
}