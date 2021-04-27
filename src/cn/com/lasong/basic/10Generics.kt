package cn.com.lasong.basic

// 1. 泛型
// 跟java一样, kotlin也支持泛型
// 在编译后会进行类型擦除
class Generics<T>(t: T) {
    init {
        when(t) {
            is Int -> {
                println("Generics Int is $t")
            }

            is String -> {
                println("Generics String is $t")
            }

            else -> {
                println("Generics Other is $t")
            }
        }
    }
}

// 2. 泛型约束
// Java上界约束 extends
// Kotlin上界约束 :, 默认的上界是Any?(可空的任意对象) 多个上界用where
// 如 where T : CharSequence, T : Comparable<T>
fun <T> filterGreater(list: List<T>, threshold: T): List<String>
    where T : CharSequence, T : Comparable<T> {
    return list.filter {
        // 传入lambada函数, 作为循环里的判断条件
        it > threshold/* 等价 it.compareTo(threshold) > 0*/
    }.map {
        // 传入lambada函数, 作为循环里的变换条件
        it.toString()
    }
}

// 3. 型变
// 在一门程序设计语言的类型系统中，一个类型规则或者类型构造器是：
//
// 协变（covariant），如果它保持了子类型序关系≦。该序关系是：子类型≦基类型。
// 逆变（contravariant），如果它逆转了子类型序关系。
// 不变（invariant），如果上述两种均不适用。
// Example:
// 协变：一个Cat[]也是一个Animal[]
// 逆变：一个Animal[]也是一个Cat[]
// 以上二者均不是（不变）？
//
// 只读数据类型（源）是协变的；只写数据类型（汇/sink）是逆变的。可读可写类型应是“不变”的。

// Java 中的泛型是不型变的, 为了灵活性使用通配符类型参数 ?
// Joshua Bloch 称那些你只能从中 "读取"的对象为"生产者"，并称那些你只能"写入"的对象为"消费者"
// 简写为PECS, Producer-Extends Consumer-Super
// https://fantasy1022.medium.com/kotlin-%E4%B8%AD%E7%9A%84-in-and-out-2ece7c86c8d6
// Kotlin 中没有通配符类型
// 它有两个其他的东西：
// 声明处型变（declaration-site variance）
// 与类型投影（type projections）

// 3.1 声明处型变
// 在Java中
// List<String> string = new ArrayList<>();
// List<Object> object = string; // 这里是编译通不过的,
//      string的类型是String, 不能转换类型到Object
// 引入in/out可以解决这一问题, 如果都不加, 就是跟Java一样是不型变的
//
// 在类型参数声明处提供, 称为声明处型变
// in 消费者 out 生产者
// 父类泛型对象可以赋值给子类泛型对象，用 in；-> 协变
// 子类泛型对象可以赋值给父类泛型对象，用 out。 -> 逆变
// 攒钱记忆法： 爸爸给我钱 就是入账 in, 我给爸爸钱 就是花钱 out
// 那在Java中不能赋值的情况, 这里只要定义为List<out T>就可以实现转换类型

// in
class Girl : People("Girl")
interface Mother <out M> {
//    var y: P // compile error
    fun giveBirthTo(): M
}
class AnyMother : Mother<People> {
    override fun giveBirthTo(): People {
        println("giveBirthTo AnyPeople")
        return People("AnyPeople")
    }
}
class SonMother : Mother<Son> {
    override fun giveBirthTo(): Son {
        println("giveBirthTo Son")
        return Son(11)
    }
}
class GirlMother : Mother<Girl> {
    override fun giveBirthTo(): Girl {
        println("giveBirthTo Girl")
        return Girl()
    }
}

// out
interface Father<in F> {
    fun givePocket(p : F);
}
class AnyFather : Father<People> {
    override fun givePocket(p: People) {
        println("givePocket Any : ${p.name}")
    }
}
class SonFather : Father<Son> {
    override fun givePocket(p: Son) {
        println("givePocket Son : ${p.name}")
    }
}
class GirlFather : Father<Girl> {
    override fun givePocket(p: Girl) {
        println("givePocket Girl : ${p.name}")
    }
}

// 3.2 使用处型变: 类型投影
// 声明处型变可以方便的解决子类/父类转换问题
// 但在实际使用中经常会碰到既作为传入参数, 又作为返回参数
// 不能是协变/逆变
// 这时候就可以使用处型变来解决
//
interface UseGenericsInterface<T> {
    fun get(index: Int) : T
    fun set(t: T)
}
class UseGenericsIntImpl : UseGenericsInterface<Int> {
    override fun get(index: Int): Int {
        println("Int get $index")
        return index
    }
    override fun set(t: Int) {
        println("Int set $t")
    }
}
class UseGenericsStringImpl : UseGenericsInterface<String> {
    override fun get(index: Int): String {
        return index.toString()
    }
    override fun set(t: String) {
        println("String set $t")
    }
}

// 转换指定from只能作为输出, 根据方法内的需要, 保证哪些泛型的数据只能取/存, 避免出现把不属于的类型加到目标数组中
// 使用时声明为out等价于java的 <? extends Object>, 限制为生产者, 只能取, 写方法被解析为Nothing, 不能执行成功
// 声明为in等价于java的 <? super Any?>, 限制为消费者, 只能写, 读方法被解析为返回Any?, 不能协变, 只能手动as强转指定类型
fun transform(from: UseGenericsInterface<out Any>, to: UseGenericsInterface<in String>) {
    val value = from.get(1)
//    from.set(2) // compile error 参数为Nothing, 没对象可以传入
    to.set(value.toString())
//    val s:String = to.get(1) // compile error 返回为Any?, 不是String类型
    // 返回Any?, 可能是所有类型, 不能确定具体类型
    val s:Int? = to.get(1) as? Int
    println("in s : $s") // null

    val s1:String? = to.get(1) as? String
    println("in s1 : $s1") //  1
}

// 4. 星投影
// 在不清具体类型的时候, 想要安全的使用泛型的参数, 在不指定具体类型时, 使用*代替
// 说白了跟之前的定义核心是一样的, 就是out定义的范围最大, in的范围最小
// 作为out生产者, 只能输出, 那可能输出任何类型, 就是<out Any?>
// 作为in消费者, 只能输入, 那不指定类型的情况下, 不能输入任何类型, 就是 <in Nothing>

interface Function<in I, out O> {
    fun action(input: I) : O
}
fun doFunction1(function: Function<*, *>) {
    // I : Nothing, O : Any?
//    function.action()
//    println("doFunction1 = ${function.action(1)}") // compile error, 无法输入任何内容, I为Nothing
}

fun doFunction2(function: Function<String, *>) {
    // I : String, O : Any?
    println("doFunction2 = ${function.action("s1")}")
}

fun doFunction3(function: Function<String, Number>) {
    // I : String, O : Number
    println("doFunction3 = ${function.action("1")}")
}

// 5. 泛型函数
// 不仅类可以有类型参数。
// 函数也可以有。类型参数要放在函数名称之前
fun <T> genericMethod(t: T) : List<T> {
    println(t.basicString())
    if (t is Int) {
        println("is Int")
    }
    return arrayListOf(t)
}
fun <T> T.basicString(): String {
    return toString() + "_basicString"
}

// 6. 类型擦除
// Java中, 在泛型类被类型擦除的时候，之前泛型类中的类型参数部分如果没有指定上限，
// 如 <T> 则会被转译成普通的Object 类型，
// 如果指定了上限如 <T extends String> 则类型参数就被替换成类型上限

// 泛型声明仅在编译期, 运行时会进行类型擦除, 不保留类型参数的实参
// 如 List<Int>/List<String>都变成List<*>, 跟Java的List<Object>类似
fun main() {
    // 等价
    Generics<Int>(1)
    Generics(2)
    // 字符串类型
    Generics<String>("s1")

    // out
    val am: Mother<People> = AnyMother()
    am.giveBirthTo()
    // 如果没有out声明处型变, 就需要使用as强转, 其实就是简化了Java中强转的作用
    val sm: Mother<People> = SonMother() /*as Mother<People>*/
    sm.giveBirthTo()
    val gm: Mother<People> = GirlMother() /*as Mother<People>*/
    gm.giveBirthTo()

    // in
    val af: Father<Girl> = AnyFather()
    af.givePocket(Girl())
    val sf: Father<Son> = SonFather()
    sf.givePocket(Son(11))
    val gf: Father<Girl> = GirlFather()
    gf.givePocket(Girl())

    // 投影型变
    transform(
            UseGenericsIntImpl(),
            UseGenericsStringImpl()
    )

    // 星投影
    doFunction1(function = object : Function<Int, Int> {
        override fun action(input: Int): Int {
            return input + 1
        }
    })
    doFunction2(function = object : Function<String, Char> {
        override fun action(input: String): Char {
            return input[0]
        }
    })
    doFunction3(function = object : Function<String, Number> {
        override fun action(input: String): Number {
            return input.toInt()
        }
    })

    // 泛型函数
    // 可以自动推断
    // 编译后都是List
    val intList:List<Int> = genericMethod<Int>(10)
    val stringList:List<String> = genericMethod("1111")
}