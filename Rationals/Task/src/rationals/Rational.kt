package rationals

import java.math.BigInteger


fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    val x = 2000000000L divBy 4000000000L
    println( x == 1 divBy 2)

//    println("912016490186296920119201192141970416029".toBigInteger() divBy
//            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}

private infix fun Number.divBy(number: Number): Rational {
    this.toString()
    return Rational(this.toString().toBigInteger(), number.toString().toBigInteger())
}


data class Rational(val numerator: BigInteger, val denominator: BigInteger) {
    fun inverse() = Rational(denominator, numerator)
    val hcm
    init {
         hcm = getHcm(this)
     }
    private fun reduce(): Rational {
        val hcm = getHcm(this)
        return Rational(numerator / hcm, denominator / hcm)
    }

    override fun toString(): String {
        return if (denominator == 1.toBigInteger()) "$numerator"
        else {
            val reducedRational = reduce()
            "${reducedRational.numerator}/${reducedRational.denominator}"
        }
    }
}


operator fun Rational.plus(other: Rational): Rational {
    return if (denominator == other.denominator) Rational(numerator + other.numerator, denominator)
    else {
        val unifiedRationalPair = unifyDenominator(this, other)
        unifiedRationalPair.first + unifiedRationalPair.second
    }
}

operator fun Rational.minus(other: Rational): Rational {
    return if (denominator == other.denominator) Rational(numerator - other.numerator, denominator)
    else {
        val unifiedRationalPair = unifyDenominator(this, other)
        unifiedRationalPair.first - unifiedRationalPair.second
    }
}

fun unifyDenominator(first: Rational, second: Rational): Pair<Rational, Rational> {
    val firstUnifiedRational = Rational(first.numerator * second.denominator,
            first.denominator * second.denominator)
    val secondUnifiedRational = Rational(second.numerator * first.denominator,
            second.denominator * first.denominator)
    return Pair(firstUnifiedRational, secondUnifiedRational)
}

operator fun Rational.times(other: Rational): Rational {
    return Rational(this.numerator * other.numerator, this.denominator * other.denominator)
}

operator fun Rational.div(other: Rational): Rational {
    return this * other.inverse()
}

operator fun Rational.unaryMinus(): Rational {
    return Rational(-numerator, denominator)
}

fun String.toRational(): Rational {
    val splitedString = this.split("/")
    return Rational(splitedString[0].toBigInteger(), splitedString[1].toBigInteger())
}

fun getHcm(rational: Rational): BigInteger {
    var smaller: BigInteger;
    var larger: BigInteger;
    if (rational.numerator < rational.denominator) {
        smaller = rational.numerator
        larger = rational.denominator
    } else {
        smaller = rational.denominator
        larger = rational.numerator
    }
    while (true) {
        val remainder = larger % smaller;
        if (remainder == 0.toBigInteger())
            return smaller.abs();
        larger = smaller;
        smaller = remainder;
    }
}

operator fun Rational.compareTo(other: Rational): Int {
    val value = numerator.toDouble() / denominator.toDouble()
    val comparedValue = other.numerator.toDouble() / other.denominator.toDouble()
    return when {
        value > comparedValue -> 1
        value < comparedValue -> -1
        else -> 0
    }
}

operator fun Rational.rangeTo(end: Rational): RationalRange = RationalRange(this, end)

operator fun RationalRange.contains(rational: Rational): Boolean{
    return (start.numerator.toDouble() / start.denominator.toDouble()) <= (rational.numerator.toDouble() / rational.denominator.toDouble())
            && (end.numerator.toDouble() / end.denominator.toDouble()) >= (rational.numerator.toDouble() / rational.denominator.toDouble())
}

class RationalRange(val start: Rational, val end: Rational)
