package games.gameOfFifteen

fun Int.isEven() = this % 2 == 0

/*
 * This function should return the parity of the permutation.
 * true - the permutation is even
 * false - the permutation is odd
 * https://en.wikipedia.org/wiki/Parity_of_a_permutation

 * If the game of fifteen is started with the wrong parity, you can't get the correct result
 *   (numbers sorted in the right order, empty cell at last).
 * Thus the initial permutation should be correct.
 */
fun isEven(permutation: List<Int>): Boolean =
    permutation.indices
            .flatMap { i ->
                (i until permutation.size).map { j -> i to j }
            }
            .count { (i, j) -> permutation[i] > permutation[j] }
            .isEven()