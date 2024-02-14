package SimplexMethod

import Constants.VALUE_ONE
import Constants.VALUE_ZERO
import Input.Task
import models.LeastElemMethodBasis
import models.Matrix
import models.SimplifiedSimplexTable

object SimplexMethod {

	operator fun invoke(basis: LeastElemMethodBasis, task: Task) {

		val initialSimplexTable = createInitialSimplexTable(task)

		val output by lazy {
			StringBuilder().apply {
				appendLine("Стандартная форма ЗЛП для решения симплекс-методом:")
				appendLine(initialSimplexTable)
				appendLine("Индексы базисных переменных:")
				appendLine(basis.basisArgs.joinToString(", "))
				appendLine("Вектор коэффициентов при переменных в целевой функции:")
				appendLine(task.targetFunCoefficients)
				appendLine("------------------------------------------")
			}
		}

		println(output)

		println("----------")

		val modifiedSimplexTable = mapToLeastElementMethodSolve(
			initialSimplexTable = initialSimplexTable,
			basisArgs = basis.basisArgs,
			vectorB = task.rightPartOfRestrictionsSystem,
		).print()

		println("----------")

		val cleanSimplexTable = formatSimplexTable(initialSimplexTable, basis.basisArgs).print()

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
			add(task.simplexTablePRow.map { it.unaryMinus() }.toMutableList())
		}
		)
	}

	private fun mapToLeastElementMethodSolve(
		initialSimplexTable: Matrix,
		basisArgs: List<Int>,
		vectorB: List<Double>,
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

	private fun formatSimplexTable(simplexTable: Matrix, basisArgs: List<Int>): SimplifiedSimplexTable {
		return SimplifiedSimplexTable(simplexTable.matrix.filter { !it.none { it != 0.0 } }.map {
			it.filterIndexed { index, d -> index !in basisArgs || index == simplexTable.colSize - 1 }.toMutableList()
		}.toMutableList(), basisArgs, simplexTable.colIndices.filter { !basisArgs.contains(it) }.dropLast(1))
	}
}