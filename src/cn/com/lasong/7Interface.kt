package cn.com.lasong

// 1. 实现接口 & 接口中的属性
// 与java8语法相似, 接口支持实现方法
// 接口中的属性必须抽象, 实现接口必须重写
interface Interface {
    var value: String
    // 没有方法体 抽象方法
    fun run()
    fun runImpl() {
        println("runImpl $value")
    }
}

interface Interface2 {
    fun run()
    fun runImpl() {
        println("Interface2 runImpl")
    }
}

// 2. 函数重写
// 继承多个相同方法的实现需要重写
class InterfaceImpl(override var value: String= "Hello") : Interface, Interface2 {
    override fun run() {
        println("run")
    }

    override fun runImpl() {
        super<Interface>.runImpl()
        super<Interface2>.runImpl()
        println("InterfaceImpl runImpl")
    }
}

// 2. 函数重写

fun main() {
    InterfaceImpl().run()
    InterfaceImpl().runImpl()
}