package cn.com.lasong

// 1. 函数定义
// 有返回类型
fun sum(a: Int, b: Int): Int {
    return a + b
}
fun sum2(a: Int, b: Int) = a + b

// 无返回值
fun printSum(a: Int, b: Int) {
    println(a+b)
}
fun printSum2(a: Int, b: Int) = println(a+b)

// 可变长参数
fun vars(vararg v: Int) {
    for (vt in v)
        print(vt)
}

// 2. lambada表达式
// lambda 表达式的语法格式如下：
// (parameters)->expression
// (parameters)->{statements;}
// 以下是lambda表达式的重要特征:
//
// 可选类型声明：不需要声明参数类型，编译器可以统一识别参数值。
// 可选的参数圆括号：一个参数无需定义圆括号，但多个参数需要定义圆括号。
// 可选的大括号：如果主体包含了一个语句，就不需要使用大括号。
// 可选的返回关键字：如果主体只有一个表达式返回值则编译器会自动返回值，大括号需要指定明表达式返回了一个数值。
fun lambda() {
    println("lambda")
    // : lambda类型
    //  () -> 5
    val lambda0: () -> Int = { 5 }
    println(lambda0())
    // 接收一个参数(数字类型),返回其2倍的值
    // x -> 2 * x
    val lambda1: (Int) -> Int = {x -> x * 2}
    println(lambda1(5))
    // 接受2个参数(数字),并返回他们的差值
    // (x, y) -> x – y
    val lambda2: (Int, Int) -> Int = { x, y -> x-y}
    println(lambda2(5, 6))
    val lambda3: (Int, Int) -> Unit = { x, y ->
        println(x+y);
        println(x-y);
    }
    lambda3(5, 6)

    val sumLambda: (Int, Int) -> Int = { x, y -> x + y }
    println(sumLambda(1, 2))
}

// 3. 定义常量与变量
fun vars() {
    // 可变变量定义：var 关键字
    // var <标识符> : <类型> = <初始化值>
    var a: Int = 1
    println(a)
    // 变量可修改
    a += 1
    println(a)
    // 不可变变量定义：val 关键字，只能赋值一次的变量(类似Java中final修饰的变量)
    // val <标识符> : <类型> = <初始化值>
    val b: Int = 2
    println(b)
    // 如果不在声明时初始化则必须提供变量类型
    val c: Int
    c = 1
}

// 4. 字符串模板
fun string() {
    // $ 表示一个变量名或者变量值
    var a = 1
    // $varName 表示变量值
    println("a = $a")
    // ${varName.fun()} 表示变量的方法返回值:
    println("a + 1 = ${a+1}")
}

// 5. NULL检查机制
fun nullCheck() {
    // 类型后面加?表示可为空
    var age: String? = "22"

    // 抛出空指针异常
    // 字段后加!!像Java一样抛出空异常
    val age_int_throw = age!!.toInt()

    // 不做处理返回 null
    // 字段后加?可不做处理返回值为 null
    val age_int_null = age?.toInt()

    // ?配合?:做空判断处理
    val age_int_default = age?.toInt() ?: -1
}

// 当一个引用可能为 null 值时, 对应的类型声明必须明确地标记为可为 null。
//当 str 中的字符串内容不是一个整数时, 返回 null:
fun parseInt(string: String): Int? {
    return string.toIntOrNull()
}

fun nullReturn() {
    println(parseInt("1"))
    println(parseInt("1a"))
    val x = parseInt("1")
    val y = parseInt("2")
    // 直接使用 `x * y` 会导致错误, 因为它们可能为 null.
    if (x != null && y != null) {
        // 在进行过 null 值检查之后, x 和 y 的类型会被自动转换为非 null 变量
        println(x * y)
    }
}

// 6. 类型检测及自动类型转换
// is 运算符检测一个表达式是否某类型的一个实例
// 类似于Java中的instanceof关键字
fun isString(obj: Any): Boolean {
    if (obj is String) {
        // 自动转换
        println("string length is ${obj.length}")
        return true
    }

    if (obj is Int && obj !is Long) {
        println("$obj is Int")
    }
    return false
}

// 7. 区间
// 辅以 in 和 !in 形成
fun range() {
    // 区间表达式由具有操作符形式 .. 的 rangeTo 函数
    // 输出1~4
    println("1~4")
    for (i in 1..4) print(i)

    // 向下使用downTo
    // 输出4~1
    println("\n4~1")
    for (i in 4 downTo 1) print(i)

    // 使用 step 指定步长
    // 输出13
    println()
    for (i in 1..4 step 2) print(i)

    // 输出42
    println()
    for (i in 4 downTo 1 step 2) print(i)

    // 使用 until 函数排除结束元素
    println("\n1~9")
    for (i in 1 until 10) {   // i in [1, 10) 排除了 10
        print(i)
    }

    // !in 不在范围
    println("0 is in 1~9?")
    if (0 !in 1..9)
        println("0 !in 1~9")
}

fun main() {
    println(sum(1, 2))
    println(sum2(1, 2))
    printSum(1, 2)
    printSum2(1, 2)
    vars(1, 2)
    lambda()
    vars()
    string()
    nullCheck()
    nullReturn()
    isString(1)
    isString(2L)
    isString("1")
    range()
}