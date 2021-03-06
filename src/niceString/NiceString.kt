package nicestring

val badSubstrings = arrayOf("bu", "ba", "be")
val vowels = arrayOf('a', 'e', 'i', 'o', 'u')
const val niceStringThreshold = 2

fun noBadSubstrings(input: String): Boolean {
    return badSubstrings.none { input.contains(it) }
}

fun countVowels(input: String): Int {
    return input.count { vowels.contains(it) }
}

fun hasDoubleLetter(input: String): Boolean {
    return input.zipWithNext().any { it.first == it.second }
}

fun String.isNice(): Boolean {
    val thisLowercase = this.toLowerCase()
    return sequenceOf(
            { -> noBadSubstrings(thisLowercase) },
            { -> countVowels(thisLowercase) > 2 },
            { -> hasDoubleLetter(thisLowercase) }
    ).filter { it() } // Lazily run the conditions to stop when we've reached the necessary threshold of N = 2
    .take(niceStringThreshold)
    .count() == niceStringThreshold
}