package SimplexMethod

import Input.Task
import models.LeastElemMethodBasis
import models.SimplexTable
import models.SolvingState

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
		var solvingState = SolvingState.NOT_SOLVED
		var iteration = 0
		while (solvingState == SolvingState.NOT_SOLVED) {
			iteration++
			println("started iteration $iteration")
			val (refRow, refCol) = table.findReferenceElementIndices()
			val refElement = table.getValue(refRow, refCol)
			table.switchVariables(refRow, refCol)

			table.matrix[refRow].forEachIndexed { colIndex, element ->
				if (colIndex == refCol) {
					table.setValue(1 / refElement, refRow, colIndex)
				} else {
					table.setValue(element / refElement, refRow, colIndex)
				}
			}

			table.matrix.forEachIndexed { rowIndex, row ->
				if (rowIndex != refRow) {
					row.forEachIndexed { colIndex, element ->
						if (colIndex == refCol) {
							table.setValue(
								value = (element / refElement).unaryMinus(),
								row = rowIndex,
								col = colIndex
							)
						} else {
							table.setValue(
								value = element - (table.getValue(rowIndex, refCol) * table.getValue(refRow, colIndex)),
								row = rowIndex,
								col = colIndex
							)
						}
					}
				}
			}
			table.apply {
				this.matrix.add(createCostsRow(table))
			}
			solvingState = table.checkSolvingState()
			println("finished iteration $iteration")
			table.print()
		}
		println("final solution")
		println(solvingState.toString())
		table.print()
	}
}