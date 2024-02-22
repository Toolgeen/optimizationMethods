package SimplexMethod

import Input.Task
import models.LeastElemMethodBasis

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

		val cleanSimplexTable = initialSimplexTable.run {
			mapToLeastElementMethodSolve(
				basisArgs = basis.basisArgs,
				vectorB = task.rightPartOfRestrictionsSystem,
			)
			println("----------")
			print()
			formatSimplexTable(basis.basisArgs).also {
				println("----------")
				it.print()
			}
		}


	}




}