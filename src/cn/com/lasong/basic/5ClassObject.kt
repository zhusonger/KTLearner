package cn.com.lasong.basic



// 1. 类定义
// 类可以包含：构造函数和初始化代码块、函数、属性、内部类、对象声明。
// 类名为ClassObject
class ClassObject /*private constructor*/(var test: String) {
                    // 2.1 可以有一个 主构造器，以及一个或多个次构造器,
                    // 主构造器是类头部的一部分，位于类名称之后
                    // 没有任何可见度修饰符(private)修饰可以省略constructor
                    // 有注解或可见性修饰符，这个 constructor 关键字是必需的
    // 类构成内容
    // 内部类
    class Empty // 空类

    // 初始化处理
    init {
        test = test.toLowerCase()
    }
    // 2.1 次构造方法, 如果需要委托使用
    // 每个次构造函数都要
    // 直接/间接 通过另一个次构造函数代理主构造函数。否则编译会失败
    // Kotlin: Primary constructor call expected
    // 通过this来代理
    constructor(string: String, int: Int) : this(string) {
        println("constructor int $int")
    }

    // 成员函数
    fun method() {
        println("method")
    }

    // 2.2 类属性
    // 默认实现get/set方法
    // 可以修改get/set方法的具体实现, field代表属性值
    var changeValue: String = "Change"
        get() = field.toLowerCase()
        set(value) {
            field = value + "_append"
        }

    // val不允许设置setter函数，因为它是只读的
    val finalValue: String = "Final"
        get() = field + "_get"

    // 重新设置get/set可见性, 默认都是public
    var privateValue: Int = 2
        private set

    // 非空属性必须在定义的时候初始化
    // 使用 lateinit 关键字描述属性来延迟初始化, 必须指定类型, 这里无法推断类型
    lateinit var lateString: String
}


// 3. 主构造方法
// 类名后加上constructor为主构造方法, 没有修饰符与注解可以不加constructor
class MainConstructor constructor() {
}
class MainConstructor2 {
}
// 主构造方法可以直接定义属性, 并在init代码块中初始化
// 等同 class MainConstructorParameter(var value: String, val int: Int)
class MainConstructorParameter constructor(var value: String) {
    init {
        value += "_init"
    }
    fun test() {
        println("finalInt test $value")
    }
}

// 4. 次构造函数
// 直接/间接 通过另一个次构造函数代理主构造函数。否则编译会失败
// 构造方法参数可以设置默认值, 这样可以优雅的简化以前java是在重载方法的功能
class SubConstructor constructor(val value: String = "1") {
    constructor(value: String = "11", int: Int) : this(value){
        println("value is $value, int is $int")
    }

    constructor(value: String, int: Int, v: Int) : this(value, int){
        println("value is $value, int is $int, v is $v")
    }
}

// 5. 抽象类
// 下一节介绍:
// 默认情况下，Kotlin 类是最终（final）的：它们不能被继承。 要使一个类可继承，请用 open 关键字标记它。
// 无需对抽象类或抽象成员标注open注解。
abstract class AbstractClass {
    fun method() {
    }

    abstract fun abstractMethod();
}

// 6. 嵌套类
class Outer {

    val outValue = 2

    class Nester {
        val value = 1
        // 无法获取outValue
    }
}

// 7. 内部类
// 内部类使用 inner 关键字来表示。
// 内部类会带有一个对外部类的对象的引用
// 所以内部类可以访问外部类成员属性和成员函数。
class Outer2 {

    val outValue = 2

    inner class Inner {
        val value = outValue
        // 效果相同, 更清晰
//        val value = this@Outer2.outValue
    }
}

// 8. 匿名内部类
class AnonymousClass {


    interface AnonymousInterface {
        fun test()
    }

    fun doAnonymous(run: AnonymousInterface) {
        run.test()
    }
}

// 9. 类的修饰符
// classModifier: 类属性修饰符，标示类本身特性。
// abstract 抽象类
// final 类不可继承，默认属性
// open 类可继承
// enum 枚举类
// annotation 注解类
abstract class AbstractClass2
/*final*/ class FinalClass
open class OpenClass
enum class Enum
annotation class AnnotationClass

// accessModifier: 访问权限修饰符
// private    // 仅在同一个文件中可见 / 只在这个类内部（包含其所有成员）可见
// protected  // 同一个文件中或子类可见 /  和 private一样 + 在子类中可见
// public     // 所有调用的地方都可见, 默认修饰符
// internal   // 同一个模块中可见
// 模块
// 可见性修饰符 internal 意味着该成员只在相同模块内可见。更具体地说， 一个模块是编译在一起的一套 Kotlin 文件：
//一个 IntelliJ IDEA 模块；
//一个 Maven 项目；
//一个 Gradle 源集（例外是 test 源集可以访问 main 的 internal 声明）；
//一次 <kotlinc> Ant 任务执行所编译的一套文件。




fun main() {
    // 2.3 实例化对象
    val instance = ClassObject("Test")
    println("test value ${instance.test}")
    println("change value ${instance.changeValue}")
    instance.changeValue = "Change2"
    println("change value ${instance.changeValue}")
    println("final value ${instance.finalValue}")

    val instance2 = ClassObject("Test2", 111)
    println("test value ${instance2.test}")
//    instance2.privateValue = 2 // compile error

    println(SubConstructor(int = 1).value)
    println(Outer.Nester().value)
    println(Outer2().Inner().value)

    AnonymousClass().doAnonymous(object : AnonymousClass.AnonymousInterface {
        override fun test() {
            println("doAnonymous")
        }
    })

}