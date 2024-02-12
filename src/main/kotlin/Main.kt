
import Input.Task
import Input.isSimplexMethodNeeded
import LeastElementMethod.LeastElementMethod
import SimplexMethod.SimplexMethod

enum class CoefficientPCheck {
    SOLVED, NO_SOLVES, CONTINUE
}

private fun main() {

    val task = Task.TestData

    val basis = LeastElementMethod(task)
    if (isSimplexMethodNeeded()) {
        SimplexMethod(basis, task)
    }
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

