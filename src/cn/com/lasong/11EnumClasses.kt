package cn.com.lasong
// 1.枚举
// 枚举类最基本的用法是实现一个类型安全的枚举
// 最简单的枚举, 值从0开始。
enum class Color {
    RED,BLACK,BLUE,GREEN,WHITE
}
// 有数值的枚举值
enum class Color2(val rgb:Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF)
}
// 定义成员, 一旦定义就需要加上分号
enum class Color3(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF);

    fun toHex() : String{
        return "0x" + "%06x".format(rgb).toUpperCase()
    }
}

// 2. 使用枚举常量
// val name: String //获取枚举名称
// val ordinal: Int //获取枚举值在所有枚举数组中定义的顺序
// valueOf 解析成枚举, name为枚举的名称, 没有抛出异常
// enumValueOf<T>(name) 达到跟 valueOf相同效果
// enumValues<T>(), 获取所有枚举值
fun main() {
    val black = Color.BLACK
    println("$black value = ${black.ordinal}")
    val green = Color2.GREEN
    println("$green value = ${green.rgb.toString(16)}")
    val blue = Color3.BLUE
    println("$blue value = ${blue.toHex()}")
    val red = Color3.valueOf("RED")
    println("$red value = ${red.toHex()}")
    println("enumValueOf<Color3> value = ${enumValueOf<Color3>("BLUE").toHex()}")
    enumValues<Color3>().forEach {
        println("$it value = ${it.toHex()}")
    }



}