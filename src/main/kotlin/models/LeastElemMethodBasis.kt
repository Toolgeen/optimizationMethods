package models

class LeastElemMethodBasis(
	private val rows: Int,
	private val cols: Int
) : Matrix(rows, cols) {

	val basisArgs by lazy {
		setBasisArgs()
	}

	private fun setBasisArgs() : List<Int> {
		//заполнение векторов текущей угловой точки и коэффициентов при переменных в целевой функции
		val args = mutableListOf<Int>()
		var index = 0
		for (i in 0 until rows) {
			for (k in 0 until cols) {
				if (this.getValue(i, k) != 0.0) args.add(index)
				index++
			}
		}
		return args
	}
}