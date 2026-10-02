package task04

import kotlin.random.Random
import kotlin.math.sqrt

class QuadraticEquation(
    a: Double,
    var b: Double,
    var c: Double
) {

    init {
        require(a != 0.0) {
            "Коефіцієнт 'a' не може бути нулем!"
        }
    }

    var a: Double = a
        set(value) {
            require(value != 0.0) {
                "Коефіцієнт 'a' не може бути нулем!"
            }
            field = value
        }

    constructor(a: Int, b: Int, c: Int) : this(
        a.toDouble(),
        b.toDouble(),
        c.toDouble()
    )

    constructor(b: Double, c: Double) : this(1.0, b, c)

    val discriminant: Double
        get() = b * b - 4 * a * c

    fun solve(): List<Double> {

        if (discriminant > 0) {
            val x1 = (-b + sqrt(discriminant)) / (2 * a)
            val x2 = (-b - sqrt(discriminant)) / (2 * a)
            return listOf(x1, x2)
        }

        if (discriminant == 0.0) {
            val x = -b / (2 * a)
            return listOf(x)
        }

        return emptyList()
    }

    override fun toString(): String {
        return "${a}x^2 + (${b})x + ${c} = 0"
    }
}

fun main() {

    val equation1 = QuadraticEquation(2.0, -4.0, 2.0)

    println(equation1)
    println("Дискримінант: ${equation1.discriminant}")
    println("Корені: ${equation1.solve()}")

    val equation2 = QuadraticEquation(1, 5, -6)

    println(equation2)
    println("Дискримінант: ${equation2.discriminant}")
    println("Корені: ${equation2.solve()}")

    val equation3 = QuadraticEquation(5.0, -6.0)

    println(equation3)
    println("Дискримінант: ${equation3.discriminant}")
    println("Корені: ${equation3.solve()}")

    equation1.a = 1.0
    equation1.b = 5.0
    equation1.c = 6.0

    println("Після зміни коефіцієнтів:")
    println(equation1)
    println("Новий дискримінант: ${equation1.discriminant}")
    println("Нові корені: ${equation1.solve()}")

    val equations = List(100) {

        var a = Random.nextInt(-50, 51)
        while (a == 0) {
            a = Random.nextInt(-50, 51)
        }
        val b = Random.nextInt(-50, 51)
        val c = Random.nextInt(-50, 51)
        QuadraticEquation(a, b, c)
    }

    val equationsWithTwoRoots = equations.filter {
        it.discriminant > 0
    }

    equationsWithTwoRoots.forEach {
        println("$it")
        println("Корені: ${it.solve()}")
        println()
    }
    println("Кількість рівнянь з двома коренями: ${equationsWithTwoRoots.size}")
}