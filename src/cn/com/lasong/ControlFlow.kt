package cn.com.lasong

class ControlFlow {
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
    }
}

fun main() {
    val instance = ControlFlow()
    instance.ifExp()
}