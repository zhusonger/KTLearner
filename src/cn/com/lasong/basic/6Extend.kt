package cn.com.lasong.basic

// 从Any隐式继承
// equals/hashCode/toString 继承Any的方法
class Empty

// 如果一个类要被继承，可以使用 open 关键字进行修饰。
open class Parent
class Child : Parent()

// 1. 构造函数
// 跟java一样, 如果要使用父类的属性, 父类的构造方法要先定义对应的属性
// 子类有主构造方法 =>
// 就在柱构造方法继承父类的构造方法
// 次级构造方法就跟普通的次级构造方法一样使用this来代理主构造函数
// 间接代理到父类的构造方法
open class People constructor(var name: String = "People"){
//    constructor(name: String = "People") {
//    }
}
class Son(var age: Int) : People("Son") {
    constructor(birth: String) : this(21) {
        println("birth is $birth")
    }
}

// 子类没有构造方法 =>
// 次级构造方法如果需要父类的属性, 使用super来代理父类构造方法
open class People2 constructor(var name: String = "People")
class Son2 : People2 {
    constructor(birth: String) : super("Son") {
        print("birth is $birth => ")
    }
}

// 2. 重写
// 使用open修饰可被重写的方法, override修饰符来重写方法
// 如果有相同的方法, 可以通过super<TYPE>.method来调用指定父类的方法
open class Flyer {
    open fun fly() {
        println("Flyer is fly")
    }
}

interface UFO {
    fun fly() {
        println("UFO is fly")
    }
}

class Bird : Flyer(), UFO {
    override fun fly() {
        super<UFO>.fly()
        super<Flyer>.fly()
        println("Bird is fly")
    }
}

// 3. 属性重写
// val可以重写为var, 因为可以扩展出setter方法
// var不可以重写为val
// 可以在构造方法中直接定义重写属性
open class PropertyParentClass {
    open val value = 1
}
class PropertyChildClass : PropertyParentClass() {
    override var value: Int = 2
}
class PropertyChildClass2(override val value: Int = 3) : PropertyParentClass()

fun main() {
    println(Son(11).name)
    println(Son2("2000-01-01").name)

    Bird().fly()

    println(PropertyChildClass().value)
    println(PropertyChildClass2().value)
}
