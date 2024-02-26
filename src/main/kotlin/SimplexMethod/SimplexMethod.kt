package SimplexMethod

import Input.Task
import models.LeastElemMethodBasis
import models.SimplexTable

object SimplexMethod {

	operator fun invoke(basis: LeastElemMethodBasis, task: Task) {

		val restrictionsSystem = createRestrictionsSystem(task)

		val output by lazy {
			StringBuilder().apply {
				appendLine("Стандартная форма ЗЛП для решения симплекс-методом:")
				appendLine(restrictionsSystem)
				appendLine("Индексы базисных переменных:")
				appendLine(basis.basisArgs.joinToString(", "))
				appendLine("Вектор коэффициентов при переменных в целевой функции:")
				appendLine(task.targetFunCoefficients)
				appendLine("------------------------------------------")
			}
		}

		println(output)

		val cleanSimplexTable = restrictionsSystem.run {
			mapToLeastElementMethodSolve(basisArgs = basis.basisArgs)
			println("----------")
			print()
			formatSimplexTable(
				basisArgs = basis.basisArgs,
				targetFunCoefficients = task.targetFunCoefficients
			)
		}
		cleanSimplexTable.print()

		findSolve(cleanSimplexTable)
	}

	fun findSolve(table: SimplexTable) {
		var isSolved = false
		var iteration = 0
		while (!isSolved) {
			iteration++
			println("started iteration $iteration")
			val (refRow, refCol) = table.findReferenceElementIndices()
			val refElement = table.getValue(refRow, refCol)
			table.switchVariables(refRow, refCol)

			table.matrix[refRow].forEachIndexed { colIndex, element ->
				table.setValue(element / refElement, refRow, colIndex)
			}

			table.matrix.forEachIndexed { rowIndex, row ->
				if (rowIndex != refRow) {
					row.forEachIndexed { colIndex, element ->
						table.setValue(
							value = table.getValue(rowIndex, colIndex) - (table.getValue(rowIndex, refCol) * table.getValue(refRow, colIndex)),
							row = rowIndex,
							col = colIndex
						)
					}
				}
			}
			table.apply {
				this.matrix.dropLast(1)
				this.matrix.add(createCostsRow(table))
			}

			isSolved = table.isSolved()
			println("finished iteration $iteration")
		}
		println("final solution")
		table.print()
	}
}