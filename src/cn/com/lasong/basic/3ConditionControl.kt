package cn.com.lasong.basic

private class ControlFlow {
    // 1. IF 表达式
    fun ifExp() {
        // 普通的if else
        val a = 1
        val b = 2
        var max:Int
        if (a < b) {
            max = b
        } else {
            max = a
        }

        // 同？：用法的表达式
        max = if (a > b) a else b

        // 直接赋值, 并且可以处理
        max = if (a > b) {
            println("a is $a")
            a
        } else {
            println("b is $b")
            b
        }

        if (a in 0..8) {
            println("$a in 0..8")
        }
    }


    // 2. When 表达式
    // when 既可以被当做表达式使用也可以被当做语句使用
    // 表达式是可以被求值的代码，而语句是一段可执行代码。
    fun whenExp() {
        val x = 1
        // 忽略其他不符合要求的分支
        // 类似switch,else 类似于switch的default
        when(x) {
            1-> println("x == 1")
            2-> println("x == 2")
            else -> {
                println("x != 1 && x != 2")
            }
        }
        // 多个值同一个分支处理, 用,分割
         when(x) {
             1, 2 -> println("x == 1 or x == 2")
             else -> println("x != 1 && x != 2")
         }
        // 可以用其他运算符(in/!in/is/!is)检测, 只会执行第一个满足要求的
        // 即使后面有其他满足要求的条件
        when(x) {
            in 0..8 -> println("$x in 0..8")
            !in 8..10 -> println("$x !in 8..10")
            is Int -> println("$x is Int")
            !is Int -> println("$x !is Int")
        }

        // when 也可以用来取代 if-else if链。
        // 如果不提供参数，所有的分支条件都是简单的布尔表达式，
        // 而当一个分支的条件为真时则执行该分支
        when {
            x == 1 -> println("x == 1")
            x == 2 -> println("x == 2")
            else -> println("x != 1 && x != 2")
        }

        // 自 Kotlin 1.3 起，可以使用以下语法将 when 的主语捕获到变量中
        when(val t = System.currentTimeMillis()) {
            in 0..Long.MAX_VALUE -> println("time is $t, in 0..${Long.MAX_VALUE}")
            else -> println("Get Error")
        }
    }
}

fun main() {
    val instance = ControlFlow()
    instance.ifExp()
    instance.whenExp()
}