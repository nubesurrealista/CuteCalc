package com.sosauce.cutecalc

import com.notkamui.keval.KevalInvalidExpressionException
import com.notkamui.keval.keval

data class CalcState(val field: String)
sealed interface CalcAction {
    object GetResult : CalcAction
    object ResetField : CalcAction
    object RemoveLast : CalcAction
    data class AddToField(val value: String) : CalcAction
}

class GetFormulaResultUseCase {
    operator fun invoke(formula: String): String {
        // Handle back-to-back operators by replacing them with a single operator
        val cleanedFormula = handleBackToBackOperators(formula)
        return try {
            cleanedFormula.keval().toBigDecimal().stripTrailingZeros().toPlainString()
        } catch (e: KevalInvalidExpressionException) {
            // Handle invalid expression, e.g., by returning an error message
            "Error"
        }
    }

    private fun handleBackToBackOperators(formula: String): String {
        // Define the set of allowed operators
        val allowedOperators = setOf('+', '-', '*', '/')

        // Replace back-to-back occurrences with a single operator
        val cleanedFormula = StringBuilder()
        var lastChar: Char? = null

        for (char in formula) {
            if (lastChar != null && allowedOperators.contains(lastChar) && allowedOperators.contains(char)) {
                // If both current and last characters are operators, skip the current one
                continue
            }
            cleanedFormula.append(char)
            lastChar = char
        }

        return cleanedFormula.toString()
    }
}
