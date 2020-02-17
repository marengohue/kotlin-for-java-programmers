package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    val (remainingSecret, remainingGuess) = evaluateCorrect(secret, guess)
    val resultingSecret = evaluateWrongPosition(remainingSecret, remainingGuess)
    return Evaluation(
            guess.length - remainingGuess.length,
            remainingSecret.length - resultingSecret.length
    )
}

fun evaluateCorrect(secret: String, guess: String): Pair<String, String> {
    return secret.foldIndexed("" to "",
            { index, (remainingSecret, remainingGuess), secretLetter ->
                when (val guessLetter = guess[index]) {
                    secretLetter ->
                        remainingSecret to remainingGuess
                    else ->
                        remainingSecret + secretLetter to remainingGuess + guessLetter
                }
            })
}

fun evaluateWrongPosition(secret: String, guess: String): String {
    return guess.fold(secret, { remainingSecret, guessLetter ->
        remainingSecret.replaceFirst(guessLetter.toString(), "")
    })
}
