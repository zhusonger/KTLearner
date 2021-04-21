package cn.com.lasong

// Kotlin 可以对一个类的属性和方法进行扩展
// 且不需要继承或使用 Decorator 模式。
// 扩展是一种静态行为，对被扩展的类代码本身不会造成任何影响。

// 1. 扩展函数
// 扩展函数可以在已有类中添加新的方法，不会对原类做修改
// fun receiverType.functionName(params){
//    body
//}
open class Extensions(var name: String)
fun Extensions.extension() {
    println("extension for class Extensions $name")
}

// 2. 扩展函数是静态解析的
// 在调用函数, 只会调用的调用类型(receiverType)的扩展方法, 不根据当前的动态类型
// 比如Child与Parent都扩展了方法A
// 某个方法参数是Parent类型, 那传入的不管是Child的实例还是Parent的实例, 都是执行Parent的方法A

class ExtensionsChild : Extensions(name = "Child") {
    fun memberMethod() {
        println("$name is memberMethod")
    }
}
fun ExtensionsChild.extension() {
    println("extension child for class Extensions $name")
}
fun doExtension(extensions: Extensions) {
    extensions.extension()
}
// 成员方法存在同名的方法, 优先使用成员方法
fun ExtensionsChild.memberMethod() {
    println("$name is extension Method")
}
// 扩展空对象
// 如扩展toString, this表示当前对象, 可以识别出null对象
fun Any?.toString(): String {
    if (this == null) {
        return "is NULL"
    }
    return toString()
}
// 也可以扩展属性
val ExtensionsChild.nameLength: Int
    get() = name.length
// 扩展属性没有 backing field，即没有field关键字，不能用来存储变量
// 扩展不能凭空塞东西进原有的类 看编译后的java字节码 扩展函数其实就是个静态函数,
// 把扩展的实例传进去操作, 扩展属性可以理解成就是定义了get/set方法,
// 封装了下而已，所以它没地方给你分配内存保存到实例里面去的
var ExtensionsChild.age: Int
    get() = 11
    set(value) {
        name = value.toString()
    }
// 3. 伴生对象的扩展
// 伴生对象：类内部的对象声明可以用 companion 关键字标记
// 可以直接通过类名调用伴生对象的方法
// java形式就是内部类 public static final class Companion
class CompanionClass {
    // 对象名称可省略, 默认使用名称 Companion
    companion object /*Factory */{
        fun newInstance() = CompanionClass()
    }
}
fun CompanionClass.Companion.message(string: String) {
    println("CompanionClass.Companion println $string")
}
val CompanionClass.Companion.type: String
    get() = "Companion"

fun main() {
    doExtension(Extensions("Hello")) // receiverType是extensions
    doExtension(ExtensionsChild()) // receiverType是extensions
    ExtensionsChild().extension() // receiverType是ExtensionsChild
    ExtensionsChild().memberMethod() // receiverType是ExtensionsChild

    val nullValue = null
    println(nullValue.toString())

    val child = ExtensionsChild()
    println(child.nameLength)
    println(child.age)
    child.age = 111
    println(child.name)

    val companionClass = CompanionClass.newInstance()

    CompanionClass.message(CompanionClass.type)
}