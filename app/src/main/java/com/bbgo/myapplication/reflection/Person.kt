package com.bbgo.myapplication.reflection

open class Person(pName: String) {

    constructor(pName: String, pAge: Int) : this(pName) {
        name = pName
        age = pAge
    }

    var name = pName
    private var age = 0

    fun modifyName(name: String) {
        this.name = name
    }

    fun showName(): String {
        return this.name
    }

    private fun modifyAge(age: Int) {
        this.age = age
    }

    private fun showAge(): Int {
        return this.age
    }

}