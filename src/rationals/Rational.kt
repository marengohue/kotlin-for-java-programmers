package rationals

import java.math.BigInteger

/**
 * Produces a rational number out of an integer ratio
 */
infix fun Int.divBy(d: Int): Rational =
        Rational(this.toBigInteger(), d.toBigInteger())

/**
 * Produces a rational number out of a long int ratio
 */
infix fun Long.divBy(d: Long): Rational =
        Rational(this.toBigInteger(), d.toBigInteger())

/**
 * Produces a rational number out of a bigint ratio
 */
infix fun BigInteger.divBy(d: BigInteger): Rational =
        Rational(this, d)

/**
 * Converts a rational string value to a rational number
 */
fun String.toRational(): Rational {
    val parts = this.split('/')
    return when (parts.count()) {
        1 -> Rational(parts[0].toBigInteger())
        2 -> Rational(
                parts[0].toBigInteger(),
                parts[1].toBigInteger()
        )
        else -> throw IllegalArgumentException("Given string $this doesn't contain a valid Rational")
    }
}

/**
 * A data type which represents a Rational nunber range
 */
class RationalRange(override val start: Rational,
                    override val endInclusive: Rational)
    : ClosedRange<Rational>

/**
 * A rational data type designed to represent rational numbers
 * A rational is a number which can be represented as N/D
 * where N is integer and D is a non-zero integer
 */
class Rational(n: BigInteger, d: BigInteger = 1.toBigInteger()) : Comparable<Rational> {

    /**
     * Nominator of the rational
     */
    private val n: BigInteger;

    /**
     * Denomentor of the rational
     */
    private val d: BigInteger;

    /**
     * Initializes the instance of a Rational type
     */
    init {
        if (d == 0.toBigInteger())
            throw IllegalArgumentException("Can't have denomenator be zero.")

        val gcd = n.gcd(d)
        this.n = (n.abs() / gcd) * (n.signum() * d.signum()).toBigInteger()
        this.d = (d.abs() / gcd).abs()
    }

    /**
     * Adds the two rationals together
     */
    operator fun plus(other: Rational): Rational =
            Rational(
                    this.n * other.d + other.n * this.d,
                    this.d * other.d
            )

    /**
     * Negates the rational
     */
    operator fun unaryMinus() =
            Rational(-this.n, this.d)

    /**
     * Subtracts one rational from another
     */
    operator fun minus(other: Rational) =
            this + (-other)

    /**
     * Multiplies the two rationals
     */
    operator fun times(other: Rational) =
            Rational(
                    this.n * other.n,
                    this.d * other.d
            )

    /**
     * Divides the two rationals
     */
    operator fun div(other: Rational) =
            this * Rational(other.d, other.n)

    /**
     * Compares the two rationals
     * Currently only rational equality is supported
     */
    override fun equals(other: Any?): Boolean =
            if (other is Rational) {
                this.n == other.n && this.d == other.d
            } else {
                false
            }

    /**
     * Returns the sign of the rational
     */
    fun signum() = n.signum()

    /**
     * Implements the Comparable<Rational> interface
     */
    override operator fun compareTo(other: Rational): Int {
        return if (this == other) {
            0
        } else {
            (this - other).signum()
        }
    }

    /**
     * Builds a RationalRange object instance
     */
    operator fun rangeTo(other: Rational): ClosedRange<Rational> =
            RationalRange(this, other)

    /**
     * Calculates the hash code of the rational
     * Contains generated default implementation
     */
    override fun hashCode(): Int {
        var result = n.hashCode()
        result = 31 * result + d.hashCode()
        return result
    }

    /**
     * Converts the rational to a normalized string representation
     * The following is true:
     * r1.toString().toRational() == r1
     */
    override fun toString(): String {
        return if (d == 1.toBigInteger())
            n.toString()
        else
            "$n/$d"
    }
}
