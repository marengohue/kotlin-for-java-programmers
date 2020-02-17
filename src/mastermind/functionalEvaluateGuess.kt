package mastermind

import kotlin.math.min

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    val rightPositions = secret.zip(guess).count { (sl, gl) -> sl == gl }
    val commonLetters = ('A'..'F').sumBy { ch ->
        min(secret.count { sl -> sl == ch }, guess.count { gl -> gl == ch })
    }
    return Evaluation(rightPositions, commonLetters - rightPositions)
}
