package com.bbgo.myapplication.reflection

import android.annotation.SuppressLint

/**
 * 有Declared的，获取不到父类的属性和方法
 * 没有的，能获取到父类的public属性和public方法
 */
@SuppressLint("PrivateApi", "DiscouragedPrivateApi")
fun main(args: Array<String>) {
    val clz = Class.forName("com.bbgo.myapplication.reflection.Student")

    // 获取所有构造方法
    clz.constructors.forEach {
        println("constructors = $it")
    }

    // 编译不通过，NoSuchMethodException 因为没有默认的构造函数
//    val ctr = clz.getConstructor() // 获取默认构造函数
//    val declCtr = clz.getDeclaredConstructor()
//    println("clz.getConstructor() = $ctr")
//    println("clz.getDeclaredConstructor() = $declCtr")
//    println("ctr.newInstance() = ${ctr.newInstance()}")
//    println("declCtr.newInstance() = ${declCtr.newInstance()}")
//    println("clz.newInstance() = ${clz.newInstance()}")

    println("String.javaClass = ${"".javaClass}")
    println("String::class.java = ${String::class.java}")

    val ctr = clz.getConstructor(String::class.java)
    val declCtr = clz.getDeclaredConstructor(String::class.java)
    val student1 = ctr.newInstance("张三")
    val student2 = declCtr.newInstance("李四")
    println("clz.getConstructor() = $ctr")
    println("clz.getDeclaredConstructor() = $declCtr")
    println("ctr.newInstance() = $student1")
    println("declCtr.newInstance() = $student2")


    /********************************************/

    clz.fields.forEach {
        println("fields = $it")
    }

    // 获取类中所有被public修饰的所有变量,因此编译不通过
//    val levelField = clz.getField("level")
//    val highField = clz.getField("high")
//    println("levelField = $levelField")
//    println("highField = $highField")

    // 编译不通过 无法获取继承下来的变量
//    val declNameField = clz.getDeclaredField("name")

    val declLevelField = clz.getDeclaredField("level")
    val declHighField = clz.getDeclaredField("high")
    println("declLevelField = $declLevelField")
    println("declHighField = $declHighField")

    // get 是获取student1这个对象里面的某个属性的值
    declLevelField.isAccessible = true
    declHighField.isAccessible = true
    println("declLevelField.get = ${declLevelField.get(student1)}")
    println("declHighField.get = ${declHighField.get(student1)}")

    declLevelField.set(student1, "大学")
    declHighField.set(student1, 180)
    println("set declLevelField.get = ${declLevelField.get(student1)}")
    println("set declHighField.get = ${declHighField.get(student1)}")




    /********************************************/

    // 获取到所有的public方法，包括父类
    clz.methods.forEach {
        println("methods = $it")
    }

    val highMethodSet = clz.getDeclaredMethod("modifyHigh", Int::class.java)
    highMethodSet.isAccessible = true
    highMethodSet.invoke(student1, 190)

    val highMethodShow = clz.getDeclaredMethod("showHigh")
    highMethodShow.isAccessible = true
    val high = highMethodShow.invoke(student1)
    println("high method.invoke = $high")

    val levelHighMethod = clz.getDeclaredMethod(
        "modifyLevelAndHigh", String::class.java, Int::class.java)
    levelHighMethod.isAccessible = true
    levelHighMethod.invoke(student1, "研究生", 182)



    /********************************************/





}