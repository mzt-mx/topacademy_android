package com.example.topacademy_android.feature_calculator.data

class CalculatorParser {
    fun eval(expr: String): Double {
        return object {
            var pos = -1
            var ch = 0
            fun nextChar() { ch = if (++pos < expr.length) expr[pos].code else -1 }
            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) { nextChar(); return true }
                return false
            }
            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expr.length) {
                    throw RuntimeException()
                }
                return x
            }
            fun parseExpression(): Double {
                var x = parseTerm()
                while(true) {
                    when {
                        eat('+'.code) -> x += parseTerm()
                        eat('-'.code) -> x -= parseTerm()
                        else -> return x
                    }
                }
            }
            fun parseTerm(): Double {
                var x = parseFactor()
                while(true) {
                    when {
                        eat('*'.code) -> x *= parseFactor()
                        eat('/'.code) -> x /= parseFactor()
                        else -> return x
                    }
                }
            }
            fun parseFactor(): Double {
                if (eat('+'.code)) return parseFactor()
                if (eat('-'.code)) return -parseFactor()
                val startPos = pos
                var x: Double
                when {
                    ch in '0'.code..'9'.code || ch == '.'.code -> {
                        while (ch in '0'.code..'9'.code || ch == '.'.code) nextChar()
                        x = expr.substring(startPos, pos).toDouble()
                    }
                    ch == '('.code -> {
                        nextChar()
                        x = parseExpression()
                        if (!eat(')'.code)) throw RuntimeException()
                    }
                    else -> {
                        throw RuntimeException()
                    }
                }
                return x
            }
        }.parse()
    }
}
