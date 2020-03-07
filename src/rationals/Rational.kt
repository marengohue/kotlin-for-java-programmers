package rationals

import java.math.BigInteger

infix fun Int.divBy(d: Int): Rational =
        Rational(this.toBigInteger(), d.toBigInteger())

infix fun Long.divBy(d: Long): Rational =
        Rational(this.toBigInteger(), d.toBigInteger())

infix fun BigInteger.divBy(d: BigInteger): Rational =
        Rational(this, d)

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


class RationalRange(override val start: Rational,
                    override val endInclusive: Rational)
    : ClosedRange<Rational>

class Rational(n: BigInteger, d: BigInteger = 1.toBigInteger()) : Comparable<Rational> {

    private val n: BigInteger;

    private val d: BigInteger;

    init {
        if (d == 0.toBigInteger())
            throw IllegalArgumentException("Can't have denomenator be zero.")

        val gcd = n.gcd(d)
        this.n = (n.abs() / gcd) * (n.signum() * d.signum()).toBigInteger()
        this.d = (d.abs() / gcd).abs()
    }

    operator fun plus(other: Rational): Rational =
            Rational(
                    this.n * other.d + other.n * this.d,
                    this.d * other.d
            )

    operator fun unaryMinus() =
            Rational(-this.n, this.d)

    operator fun minus(other: Rational) =
            this + (-other)

    operator fun times(other: Rational) =
            Rational(
                    this.n * other.n,
                    this.d * other.d
            )

    operator fun div(other: Rational) =
            this * Rational(other.d, other.n)


    override fun equals(other: Any?): Boolean =
            if (other is Rational) {
                this.n == other.n && this.d == other.d
            } else {
                false
            }

    fun signum() = n.signum() * d.signum()

    override operator fun compareTo(other: Rational): Int {
        return if (this == other) {
            0
        } else {
            (this - other).signum()
        }
    }

    operator fun rangeTo(other: Rational): ClosedRange<Rational> =
            RationalRange(this, other)


    override fun hashCode(): Int {
        var result = n.hashCode()
        result = 31 * result + d.hashCode()
        return result
    }

    override fun toString(): String {
        return if (d == 1.toBigInteger())
            n.toString()
        else
            "$n/$d"
    }
}