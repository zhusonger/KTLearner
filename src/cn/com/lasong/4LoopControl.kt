package cn.com.lasong

private class LoopControl {

    // 1. For 循环
    fun forLoop() {
        val intArray = intArrayOf(1, 2, 3, 4)
        // for 循环可以对任何提供迭代器（iterator）的对象进行遍历
        for (item in intArray) {
            println("item is $item")
        }
        // 注意这种 "在区间上遍历" 会编译成优化的实现而不会创建额外对象。
        // 或者你可以用库函数 withIndex
        // 查看字节码后发现,实现是先获取到数组长度
        // var5 = intArray.length;
        // 再进行循环遍历, 如下
        // for(int length = intArray.length, i = 0; i < length; i++){}
        // 使用索引遍历
        for (index in intArray.indices) {
            println("value at $index is ${intArray[index]}")
        }
        for ((index, value) in intArray.withIndex()) {
            println("value at $index is $value")
        }
    }

    // 2. while 与 do...while 循环
    fun whileLoop() {
        var i = 0
        while(i++ < 3) {
            println("loop $i")
        }

        var x = 0
        do {
            println("do loop $x")
        } while (x++ < 3)
    }

    // 3. 返回和跳转
    fun jump() {
        for (i in 0..10) {
            if (i == 3) {
                println("break at 3")
                break
            }

            if (i == 0) {
                println("continue at 0")
                continue
            }
            println("default at $i")
        }

        for(i in 0..5) {
            if (i == 5) {
                println("return at 5")
                return
            }
            println("default at $i")
        }

        println("END")
    }

    // 4. Break 和 Continue 标签
    // 类似于C语言的goto, java的label, 直接跳转到指定位置
    // java的定位是标识符跟 : 符号, 如 a:
    // 任何表达式都可以用标签（label）来标记
    // 标签的格式为标识符后跟 @ 符号，如： a@ b@ c@
    fun label() {
        loop1@ for(i in 0..3) {
            loop2@ for (j in 3..5) {
                if (i == 1 && j == 4) {
                    println("break loop1 i == $i && j == $j")
                    // 普通break/continue, 跳转当前循环, 重新进入loop1循环
                    // 等同于break@loop2
//                    break
//                    break@loop2
                    // 跳转loop1循环
                    break@loop1
                }

                if (i == 0 && j == 4) {
                    println("continue loop1 i == $i && j == $j")
                    continue@loop1
                }
                println("default i == $i && j == $j")
            }
        }

        // 标签处返回
        val stringArray = arrayOf<String>("s1", "s2", "s3")
        stringArray.forEach {
            if (it == "s2") {
                println("Get $it")
                // return直接返回整个函数(label), 不会执行label函数后续的代，码,
                // 这是因为forEach定义时添加inline修饰符,
                // 简单理解就是forEach传入的lambda表达式内部的代码, 被抽出来放到了forEach循环内执行了
//                return
                // return@标签, 局部返回forEach, 本次内联函数后续代码不执行, 效果相当于continue
                return@forEach
            }
            println("string is $it")
        }

        // 或者用一个匿名函数代替lambda, return直接从匿名函数返回
        stringArray.forEach(fun(value:String){
                if (value == "s2") {
                    println("Get $value")
                    // return不会执行本次匿名函数的后续代码, 相当于continue
                    return
                }
                println("string is $value")
        })

        // 给block代码设置标签
        val v = run loop@{
            stringArray.forEach(fun(value: String) {
                if (value == "s2") {
                    println("Get $value")
                    // return@标签, 返回到block代码, 后续代码不再执行, 相当于break
                    // 可以添加返回值
                    return@loop 1
                }
                println("string is $value")
            })
        }
        println("Label End : $v" )

    }
}

fun main() {
    val instance = LoopControl()
    instance.forLoop()
    instance.whileLoop()
    instance.jump()
    instance.label()
}