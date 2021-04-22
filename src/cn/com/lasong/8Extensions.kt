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

// 4. 扩展的作用域
// 通常扩展函数定义下顶级包下
// 如果需要用其他包下的扩展, 需要import导入
// 可以理解成静态函数定义, 其他包下需要使用就需要导入静态方法
/**
 * @{link pkg1/8_1Extensions}
 */

// 5. 扩展声明为成员
// 在一个类内部你可以为另一个类声明扩展
// 其中扩展方法定义所在类(ExtensionsMember)的实例称为分发接受者，
// 而扩展方法的目标类型(Extensions)的实例称为扩展接受者
open class ExtensionsMember {
    fun memberNative() {
        println("memberNative")
    }

    fun extension() {
        println("ExtensionsMember extension")
    }
    // 5.1 可以调用分发接受者的方法
    // 字节码显示是作为一个参数为Extensions的方法加入到ExtensionsMember中
    fun Extensions.member() {
        memberNative()
        // 5.2 相同方法名, 扩展的方法优先, 如果要使用当前类的方法, 使用this@
        extension()
        this@ExtensionsMember.extension()
        println("ExtensionsMember member method, message = $name")
    }

    // 5.3 可重写的扩展方法
    // 扩展方法还是根据扩展接受者的静态解析类型, 这里就是Extensions
    // 分发接受者, 就是调用callExtensions方法的实例是虚拟解析
    // 所以在子类重写扩展后, 会调用子类实例(ExtensionsMember2)的扩展方法
    open fun ExtensionsChild.extension() {
        println("ExtensionsChild extension in ExtensionsMember, name = $name")
    }

    open fun Extensions.extension() {
        println("Extensions extension in ExtensionsMember, name = $name")
    }

    fun callMember(extensions: Extensions) {
        extensions.member()
    }
    fun callExtensions(extensions: Extensions) {
        extensions.extension()
    }
}

class ExtensionsMember2 : ExtensionsMember() {
    override fun Extensions.extension() {
        println("Extensions extension in ExtensionsMember2, name = $name")
    }

    override fun ExtensionsChild.extension() {
        println("ExtensionsChild extension in ExtensionsMember2, name = $name")
    }
}

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

    ExtensionsMember().callMember(Extensions("Hello"))


    ExtensionsMember().callExtensions(Extensions("Hello"))
    ExtensionsMember().callExtensions(ExtensionsChild())
    ExtensionsMember2().callExtensions(Extensions("Hello"))
    ExtensionsMember2().callExtensions(ExtensionsChild())
}