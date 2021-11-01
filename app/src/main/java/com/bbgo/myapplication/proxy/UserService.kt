package com.bbgo.myapplication.proxy

interface UserService {
    fun select()
    fun update(name: String)
    fun show(): String
}