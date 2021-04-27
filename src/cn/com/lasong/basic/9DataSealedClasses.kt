package cn.com.lasong.basic

// 1. 数据类
// 使用关键字 data
// 编译器自动根据主构造函数提取属性方法及默认的几个方法
// equals() / hashCode()
// toString()
// componentN 根据属性顺序component1表示第一个属性
// copy() 函数
//
// 数据类不能继承其他类 可以实现接口
data class Data(val value: Int, val name: String)

class DataSealedClasses {
    fun  data() {
        val data = Data(1, "Hello")
        println(data)
        val copy = data.copy(value = 2)
        println(copy)
        // 解构声明
        val (v, n) = data
        println("v = $v, n = $n")
        // 标准数据类
        // 但是一般是建议命名数据类是更好的设计选择
        // 这样代码可读性更强而且提供了有意义的名字和属性
        // 二元数据
        val pair = Pair<Int, String> (1, "string")
        val (v1, n1) = pair
        println("v1 = $v1, n1 = $n1")
        // 三元数据
        val triple = Triple<Int, Int, String> (1, 2, "s3")
        val (v2_0, v2_1, s3) = triple
        println("v2_0 = $v2_0, v2_1 = $v2_1, s3 = $s3")
    }

    fun sealed(sealed : SealedClass) : Any {
        return when(sealed) {
            is SubSealedClass -> sealed.value.toString()
            is Sub2SealedClass -> sealed.value
            OthersSealedClass -> "Others"
        }
    }
}

// 2.密封类
// 密封类用来表示受限的类继承结构：当一个值为有限几种的类型、而不能有任何其他类型时。
// 在某种意义上，他们是枚举类的扩展：枚举类型的值集合也是受限的，但每个枚举常量只存在一个实例，
// 而密封类的一个子类可以有可包含状态的多个实例。
//
// 理解就是跟枚举差不多, 但是枚举没办法保存多个字段储存数据
//
sealed class SealedClass
data class SubSealedClass(val value: Int) : SealedClass()
data class Sub2SealedClass(val value: String) : SealedClass()
// object 修饰静态类
object OthersSealedClass : SealedClass()

fun main() {

    val instance = DataSealedClasses()
    instance.data()

    println(instance.sealed(SubSealedClass(111)))
    println(instance.sealed(Sub2SealedClass("HHHH")))
    println(instance.sealed(OthersSealedClass))

}