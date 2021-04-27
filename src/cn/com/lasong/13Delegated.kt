package cn.com.lasong

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// 委托 : 把一个任务交给另一个对象来处理, 使用by关键字
// 1. 类委托
// 在类定义时, 把委托对象作为成员变量传给发起委托任务的类, 通过关键字by来指定委托执行的对象
// 内部会实现委托对象的所有接口, 并调用委托对象的实现
interface Worker {
    fun doDaily()
}
class WorkerImpl : Worker {
    override fun doDaily() {
        println("doDaily implemented")
    }
}
class Boss(worker: Worker) : Worker by worker

// 2.属性委托 & 属性委托要求
// val/var <属性名>: <类型> by <表达式>
// 类的属性不在类中直接定义, 而是交给一个代理类, 实现统一管理
class Property {
    // 这里是委托给系统的一个懒加载的类, 内部会判断是否已经初始化,
    // 没有初始化就执行初始换函数, 并更新value
    // 已经初始化就直接返回value
    val value:Int by lazy (initializer = {
        println("lazy") // 首次会执行, 二次不会执行
        1
    })

    // 自定义属性赋值
    var customValue: Int by PropertyDelegate()

}
// 属性委托的要求可设置值时, 需要有setValue方法,
// operator fun setValue(ref: <委托属性所在的类或者它的超类>, property:KProperty<*>,
//                                                      newValue: <属性类型或者它的超类>)
//
// 可读取时, 需要有getValue方法
// operator fun getValue(ref: <委托属性所在的类或者它的超类>, property: KProperty<*>)
//                                                       : <属性类型或者它的超类>
class PropertyDelegate : ReadWriteProperty<Any?, Int>{
    var value: Int = 0
    override operator fun getValue(thisRef: Any?, property: KProperty<*>): Int {
        println("getValue from $thisRef and name is ${property.name}")
        return value
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Int) {
        println("setValue from $thisRef and name is ${property.name} and value is $value")
        this.value = value
    }
}

// 3. 标准委托
// kotlin内置的工厂方法委托
class KotlinDelegate {
    // 3.1 延迟属性 Lazy
    // lazy定义的属性都是不可变的, 因为泛型类型都定义为out
    // 在获取这个值的时候赋值, 值为写入的对象, 实现为构造方法传入val的属性, 所以只能用于val修饰的数据
    val lazyOfValue: Int by lazyOf(1)
    // 同步的方式懒加载, 声明处泛型定义为out, 只能输出, 所以也只能用val修饰
    val lazyValue: Int by lazy {
        println("lazyValue initializer")
        2
    }
    // 可以选择线程模式
    // NONE 不考虑线程安全的模式
    // SYNCHRONIZED 同步锁synchronized的方式初始化对象, 悲观锁
    // PUBLICATION 通过原子性操作CAS的原理初始化对象, 乐观锁
    val lazyThreadModeValue: Int by lazy(LazyThreadSafetyMode.PUBLICATION, initializer = {
        println("lazyThreadModeValue initializer")
        3
    })
    // 同lazy, 只是指定的自己的锁对象
    val lazyLockModeValue: Int by lazy(KotlinDelegate::class, initializer = {
        println("lazyLockModeValue initializer")
        4
//        return@lazy 4 // 等同上面, 直接return只是结束initializer匿名函数, 如果是返回值, 需要到lazy标签位置
    })
}

// 4. 可观察属性 Observable
class ObservableDelegate {
    // 内部实现就是用onChange实现afterChange
    var afterValue: Int by Delegates.observable(1, onChange = {
        property, oldValue, newValue ->
            println("property $property, oldValue $oldValue, newValue $newValue")
    })

    // 内部实现就是用onChange实现beforeChange, 如果不需要修改, 就返回false
    var beforeValue: Int by Delegates.vetoable(1, onChange = {
        property: KProperty<*>, oldValue: Int, newValue: Int ->
            println("property $property, oldValue $oldValue, newValue $newValue")
            // 不一样才更新
            oldValue != newValue
    })

    // 9.1 会进行报错, 因为PropertyChecker检测是否是在Property2中创建的对象
    val checkerValue by PropertyChecker()
}

// 5. 把属性储存在映射中
fun mapToProperty(map: Map<String, Any?>): Pair<String, Int> {
    val stringV : String by map
    val intV : Int by map
    return Pair<String, Int>(stringV, intV)
}
fun mutableMapToProperty(map: MutableMap<String, Any?>): Pair<String, Int> {
    val stringV : String by map
    val intV : Int by map
    return Pair<String, Int>(stringV, intV)
}
// 6. Not Null
// 无法在初始化阶段就确定属性值
// 在赋值前就被访问的话则会抛出异常
var notNullString: String by Delegates.notNull<String>()

// 7. 局部委托属性
// 局部变量声明为委托属性
fun partDelegate(ready: Boolean, currentSystemMs: ()->Long) {
    val getCurrentSystemMs by lazy (currentSystemMs) // lazy 只支持传入无参lambada
    println("1. start partDelegate")
    // ready 为true, getCurrentSystemMs这里调用会进行懒加载的调用
    // ready 为false, getCurrentSystemMs不会调用
    if (ready && getCurrentSystemMs > 0) {
        println("3. system ms : $getCurrentSystemMs")
        println("4. system ms : $getCurrentSystemMs")
    }
}

// 8. 翻译规则
// 委托的实际是编译器辅助生成一个变量 <属性名>$delegate,
// 然后把被代理对象的get/set的改为调用生成的委托对象
class Property2 {

    // 自定义属性赋值
//    var customValue: Int by PropertyDelegate()
    // 翻译之后的实现等同于上面的by关键字
    private val `customValue$delegate` = PropertyDelegate()
    var customValue: Int
        get() = `customValue$delegate`.getValue(this, this::customValue)
        set(value) = `customValue$delegate`.setValue(this, this::customValue, value)

    val checkerValue by PropertyChecker()
}

// 9. 提供委托provideDelegate
// 如果by关键字提供的对象有成员方法 operator fun provideDelegate(ref: Any?, property: KProperty<*>)
// 就会使用provideDelegate来创建属性委托实例
// 作用就是在定义时就检测属性, 比如我这个就要求只能在Property2中定义
// 这样可以属性定义按照某些规则定义
// 编译器转换之后, 跟属性委托唯一不同的是委托实例的生成不同
// 原来是 private val `customValue$delegate` = PropertyDelegate()
// provideDelegate变成如下
// private val `customValue$delegate` = PropertyChecker().provideDelegate(this, this::customValue)
// 其他的并没有改变, 可以看到这样就是在加载类时就会执行provideDelegate方法来进行检查委托属性的定义
class PropertyChecker {
    operator fun provideDelegate(ref: Any?, property: KProperty<*>): ReadWriteProperty<Any?, Int> {
        if (ref !is Property2) {
            throw IllegalAccessException("Not Property2")
        }

        return PropertyDelegate()
    }
}

fun main() {
    Boss(WorkerImpl()).doDaily()
    val p = Property()
    p.value
    p.value

    println(p.customValue)
    p.customValue = 111
    println(p.customValue)

    val kd = KotlinDelegate()
    println(kd.lazyOfValue)
    println(kd.lazyValue)
    println(kd.lazyThreadModeValue)
    println(kd.lazyLockModeValue)
    // 二次获取不会输出xxx initializer
    println(kd.lazyOfValue)
    println(kd.lazyValue)
    println(kd.lazyThreadModeValue)
    println(kd.lazyLockModeValue)

    // 观察者属性
    val od = ObservableDelegate()
    println("get Init Value: ${od.afterValue}")
    od.afterValue = 1
    println("get After Value: ${od.afterValue}")

    println(od.beforeValue)
    od.beforeValue = 2
    println(od.beforeValue)

    println(od.beforeValue)
    od.beforeValue = 2
    println(od.beforeValue)

    val pair: Pair<String, Int> = mapToProperty(mapOf("stringV" to "string", "intV" to 1))
    println(pair.first +","+ pair.second)

    val pair2: Pair<String, Int> = mutableMapToProperty(mutableMapOf("stringV" to "string", "intV" to 2))
    println(pair2.first +","+ pair2.second)

    // 未赋值前调用会报错
    notNullString = "Hello"
    println(notNullString.length)

    partDelegate(true, currentSystemMs = {
        val ts = System.currentTimeMillis()
        println("2. currentSystemMs get, ts is $ts")
        return@partDelegate ts
    })

    partDelegate(false, currentSystemMs = {
        val ts = System.currentTimeMillis()
        println("2. currentSystemMs get, ts is $ts")
        return@partDelegate ts
    })
}