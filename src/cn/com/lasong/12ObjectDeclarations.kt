package cn.com.lasong

// 对象表达式和对象声明
// 用对象表达式和对象声明来实现创建一个对某个类做了轻微改动的类的对象，且不需要去声明一个新的子类。

// 1. 对象表达式
// 其实就是匿名类的
interface ObjectDeclarationInterface {
    var value: Int

    fun input(int: Int)
    fun output(): Int
}

fun action(action: ObjectDeclarationInterface) {
    action.input(11)
    val res = action.output()
    println("res = $res")
}

fun main() {
    // 1.1 对象表达式实现接口/抽象类方法
    //  可以重写成员属性和方法
    action(object : ObjectDeclarationInterface, Comparable<Int> {
        override var value: Int = 1

        override fun input(int: Int) {
            value = int
        }

        override fun output(): Int {
            return value * value
        }

        override fun compareTo(other: Int): Int {
            return other - value
        }
    })

    // 1.2 只要一个对象, 不需要超类
    val onlyObj = object {
        val x = 1
        val y = 1
    }
    println(onlyObj.x.toString() + ","+ onlyObj.y.toString())
}