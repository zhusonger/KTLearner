package cn.com.lasong

class BasicTypes {
    // 1. 字面常量
// Byte、Short、Int、Long、Float、Double 数值类型, 就是数字
// Char在Java中是数值类型, 但在kotlin中是数据类型, 必须要用''表示
    fun typeDefine() {
        val byte: Byte = 1
        val short: Short = 1
        val int: Int = 1
        val long: Long = 1
        // 跟Java一样通过L来指定为Long
        val long2 = 1L
        // 必须指定f/F来确定为浮点数
        val float: Float = 1.0f
        val float2: Float = 1.0F
        // 不加后缀就是Double类型
        val double: Double = 1.0
        val doubleE: Double = 1.0e3 // 1.0*10^3
        // 下划线使数字常量更易读
        val int2: Int = 1_000_000
        // 16进制0x开头
        val hex: Int = 0xFFFFFF
        // 2进制0b开头
        val bit: Int = 0b000000
    }

    // 2. 比较两个数字
// Kotlin 中没有基础数据类型，只有封装的数字类型
// 每定义的一个变量，其实 Kotlin 帮你封装了一个对象
// === 完全相等(对象地址)
// == 值大小相等
    fun compare(){
        val a: Int = 100
        val b: Int = 100
        println("a == b? ${a == b}")

        // === 出现警告
        // Identity equality for arguments of types Int and Int is deprecated
        // 搜到其中有个示例很疑惑
        // 从127到128为什么就是不是完全相等了？
        val ranged = arrayListOf(127, 127)
        // true
        println("127 = > ranged[0] === ranged[1]? ${ranged[0] === ranged[1]}")
        // true
        println("127 = > ranged[0] == ranged[1]? ${ranged[0] == ranged[1]}")
        val exclusive = arrayListOf(128, 128)
        // false
        println("128 = > exclusive[0] === exclusive[1]? ${exclusive[0] === exclusive[1]}")
        // true
        println("128 = > exclusive[0] == exclusive[1]? ${exclusive[0] == exclusive[1]}")
        // 突然想起来, 数据类型的表示单位是1个字节8位, 范围[-128, 127), 所以是直接同一个地址
        // "每定义的一个变量，其实 Kotlin 帮你封装了一个对象"
        // 128超过了1个字节能表示的范围, 所以被封装成了Int, 值虽然相同, 但是是2个对象
    }

    // 3. 类型转换
// 在Java中可以进行隐式转换为较大的类型
// 但Kotlin 不同的类型有不同的表示方式，较小的类型不能隐式转换为较大的类型。
// 不进行显式转换的情况下我们不能把 Byte 型值赋给一个 Int 变量。
    fun typeCast() {
        val a: Byte = 1
        // compile error
//    val b: Int = a
        val b: Int = a.toInt()

        // 在运算的时候可以隐式转换, 前提是能根据上下文推断出数据类型
        val b2: Int = 1 + a // Int + Byte => Int
    }

    // 4. 位操作符
// 只用于 Int 与 Long
    fun bitAction(){
        // 0000 0000 0000 0000 0000 0000 0111 1111
        val a: Int = 127
        println("$a shl 1 (a << 1)= ${a shl 1}")
        println("$a shr 1 (a >> 1)= ${a shr 1}")
        // 都为1则为1, 否则为0
        //  0111 1111
        //& 0000 1111
        //= 0000 1111
        //= 15
        println("$a and 0xFFFF0F (a & 0xFFFF0F)= ${a and 0x0F}")
        // 有1则为1, 否则为0
        //  0111 1111
        //| 0000 1111
        //= 0111 1111
        //= 127
        println("$a or 0x00000F (a | 0x00000F)= ${a or 0x0F}")
        // 相同则为0, 否则为1
        //  0111 1111
        //^ 0000 1111
        //= 0111 0000
        //= 112
        println("$a xor 0x00000F (a ^ 0x00000F)= ${a xor 0x0F}")
        // 相同则为0, 否则为1
        //  0111 1111
        //~
        //= 1000 0000
        //= -128
        println("$a .inv (~a)= ${a.inv()}")
    }

    // 5. 字符串
    fun chars() {
        val char: Char = '0'
        // kotlin中字符串不能跟数字直接操作
        // compile error
//    if (char == 0) {
//
//    }
        println("\'0\' = ${char.toInt()}")
    }

    // 6. 布尔
// || – 短路逻辑或
// && – 短路逻辑与
// ! - 逻辑非
    fun boolean() {
        val a: Int = 1
        val b: Int = System.currentTimeMillis().toInt()

        if (a > b || b > a) {
            println("a > b || b > a => a($a) b($b)")
        }

        if (a > b && b < 0) {
            println("a > b && b < 0 => a($a) b($b)")
        }

        if (a != b) {
            println("a != b => a($a) b($b)")
        }
    }

    // 7. 数组
// 数组用类 Array 实现 一个 size 属性及 get 和 set 方法
//  [] 重载了 get 和 set 方法
    fun array() {
        // 使用函数arrayOf() 泛型类型, 不适用泛型指定类型的情况下可以混合类型
        println("arrayOf(1, 2, 3)[2] = ${arrayOf(1, 2, 3)[2]}")
        println("arrayOf<String>(1, 2, 3)[2] = ${arrayOf<String>("s1", "s2", "s3")[2]}")
        println("arrayOf(1, \"s2\", 3)[2] = ${arrayOf(1, "s2", 3)[2]}")
        // 指定类型如intArrayOf,longArrayOf，用来表示各个类型的数组，省去了装箱操作，因此效率更高
        println("intArrayOf(1, 2, 3)[2] = ${intArrayOf(1, 2, 3)[2]}")
        // 使用工厂方法Array, init自己初始化对象, 默认是it,it可以忽略
        println("Array(3, init = {i -> i * 2})[2] = ${Array(3, init = {i -> i * 2})[2]}")
        println("Array(3, init = {it * 2})[2] = ${Array(3, init = {it * 2})[2]}")
        // 未初始化默认是Unit类型, 就是Kotlin里的所有类型默认值
        println("Array(3, init = {})[2] = ${Array(3, init = {})[2]}")
        println("Array(3, init = {0})[2] = ${Array(3, init = {0})[2]}")

        // 数组的遍历
        val array = arrayOf(1, "s2", 3)
        for (v in array)
            println(v)

        for (i in array.indices)
            println(array[i])

        for ((i,v) in array.withIndex())
            println("${array[i]} = $v")
    }

    // 8. 字符串
    fun string() {
        val s = "abc"
        for (c in s)
            println(c)
        println(s[1])
        // Kotlin 支持三个引号 """ 扩起来的字符串，支持多行字符串
        val mulLines = """ 
               哈哈哈哈
            你好
        """
        // 原样输出
        println(mulLines)
        // 左边空白对齐切割
        println(mulLines.trimIndent())
        val mulLines2 = """ 
               |哈哈哈哈
            |你好
        """
        // 使用指定符号作为边界切割, 默认是|, 可以自定义
        println(mulLines2.trimMargin())
        val mulLines3 = """ 
               #哈哈哈哈
            #你好
        """
        println(mulLines3.trimMargin(marginPrefix = "#"))
    }

    // 9. 字符串模板
    fun stringTemplate() {
        val s = "Hello"
        println("s is $s and $s.length = ${s.length}")
        val price = 9.9
        print("$$price")
    }
}


fun main() {
    val instance = BasicTypes()
    instance.typeDefine()
    instance.compare()
    instance.typeCast()
    instance.bitAction()
    instance.chars()
    instance.boolean()
    instance.array()
    instance.string()
    instance.stringTemplate()
}