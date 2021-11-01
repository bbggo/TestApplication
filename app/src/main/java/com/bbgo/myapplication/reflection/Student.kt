package com.bbgo.myapplication.reflection

class Student constructor(name: String) : Person(name) {

    constructor(name: String, age: Int) : this(name) {
        this.name = name
    }

    var level = "二年级"
    private var high = 170


    fun modifyLevel(level: String) {
        this.level = level
    }

    fun showLevel(): String {
        return this.level
    }

    private fun modifyHigh(high: Int) {
        this.high = high
    }

    private fun showHigh(): Int {
        return this.high
    }

    private fun modifyLevelAndHigh(level: String, high: Int) {
        println("modifyLevel before")
        modifyLevel(level)
        println("modifyLevel after")
        println("----------------------")
        println("modifyHigh before")
        modifyHigh(high)
        println("modifyHigh after")
    }
}