package Input

import Constants.VALUE_ONE

fun isSimplexMethodNeeded(): Boolean {
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

fun validateInsert(insert: String?): Boolean {
	return if (insert == null) {
		false
	} else {
		if (insert.trim().toDoubleOrNull() is Double) {
			insert.trim().toDouble() >= 0
		} else false
	}
}