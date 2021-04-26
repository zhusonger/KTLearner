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



class ObjectDeclarations {

    var v1 = 1
    var v2 = "s1"

    private fun doPrivateObject() = object : ObjectDeclarationInterface{
        val x: String = "PRIVATE"
        override var value: Int = 0

        override fun input(int: Int) {
            // 1.4 可以访问实现它的对象的成员, 内部类持有外部的引用
            v1 = int
        }

        override fun output(): Int {
            println(v2)
            return v1
        }
    }

    fun doPublicObject() = object : ObjectDeclarationInterface {
        val x: String = "x"
        override var value: Int = 0

        override fun input(int: Int) {
            v1 = int
        }

        override fun output(): Int {
            println(v2)
            return v1
        }
    }

    fun doPrivate() {
        // 返回类型 <no name provided>, 是因为这是一个匿名对象,
        // 具体类型是cn/com/lasong/ObjectDeclarations$doPrivateObject$1
        // 不同于Java, Kotlin可以推断出具体的类型, 所以可以取到匿名类内部的成员变量/函数
        val x = doPrivateObject().x
        println("doObject x = $x")
    }
}


// 2. 对象声明
// 使用 object 关键字来声明一个对象
// 我们可以方便的通过对象声明来获得一个单例。
// 如下定义的类, 调用方法是直接对象名.方法名
object ViewHelper {
    fun register() {
    }

    fun unregister() {
    }
}

// 2.3 对象声明也可以继承
object ObjectDeclarations2 : ObjectDeclarationInterface {

    override var value: Int = 0

    override fun input(int: Int) {
        value = int
    }

    override fun output(): Int {
        return value
    }
}

class ObjectDeclarations3 {
    val x = "ObjectDeclarations3"
    object ObjectDeclarationsInner {
        fun readX(): String {
            // 2.4 不同于对象表达式, 是在类内部创建持有外部引用的实例匿名对象
            // 对象声明是直接声明一个类, 与外部对象没有任何关联, 所以取不到任何数据
            // 调用的方式对象表达式是需要实例化外部对象才能调用内部定义的匿名对象
            // 对象声明直接使用外部的类名就可以访问内部定义的对象声明
//            return x // compile error, 找不到x
            return "X"
        }
    }
}

// 3. 伴生对象
// 如果觉得放在内部的对象声明使用起来略冗长,就可以用 companion 关键字标记, 把它与外部对象关联起来
class ObjectDeclarations4 {
    val x = "ObjectDeclarations4"
    // 3.1 伴生对象的类型可有可无, 没有默认就是 Companion
    companion object /*ObjectDeclarationsInner*/ {
        fun readX(): String {
            // 3.2 同2.4, 只是简化的调用的层级
            return "x" // compile error, 找不到x
        }
    }

    // 3.3 一个类里只能有一个伴生对象
//    companion object {
//
//    }
}

// 4. 对象表达式和对象声明之间的语义差异
// 对象表达式和对象声明之间有一个重要的语义差别：
//
// 对象表达式是在使用他们的地方立即执行（及初始化）的；
// 对象声明是在第一次被访问到时延迟初始化的；
// 伴生对象的初始化是在相应的类被加载（解析）时，与 Java 静态初始化器的语义相匹配。

fun main() {

    val initInt = 100
    // 1.1 对象表达式实现接口/抽象类方法
    //  可以重写成员属性和方法
    action(object : ObjectDeclarationInterface, Comparable<Int> {
        override var value: Int = initInt

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

    // 1.3 匿名对象可以用作只在本地和私有作用域中声明的类型。
    // 这意思简单理解就是你如果是自己使用的, 我可以推断出具体类型在内部使用, 可以随便使用匿名对象的成员对象/函数
    // 如果是公有的, 那其他地方也需要用到, 不能直接推断出他具体使用时的类型定义,
    // 因为其他地方可能就不是要这个类型了, 所以我只能返回匿名对象的超类, 就是继承的类/接口
    val instance = ObjectDeclarations()
    instance.doPrivate()
    instance.doPublicObject() // 这里 doPublicObject 返回类型是ObjectDeclarationInterface

    // 2.1 直接调用对象的方法
    ViewHelper.register()
    ViewHelper.unregister()

    // 2.2 他们是完全相同(包括内存地址)的实例对象
    val v1 = ViewHelper
    val v2 = ViewHelper
    println("v1 ${if (v1 === v2) "===" else "!=="} v2 ")

    // 2.4 内部对象声明无法访问外部对象的成员
    ObjectDeclarations3.ObjectDeclarationsInner.readX()
    // 3. 伴生对象
    ObjectDeclarations4.readX()
}