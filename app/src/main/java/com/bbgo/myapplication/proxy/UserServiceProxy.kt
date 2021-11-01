package com.bbgo.myapplication.proxy

import java.lang.String
import java.util.*

class UserServiceProxy(private val userService: UserService) : UserService {

    override fun select() {
        before()
        userService.select()
        after()
    }

    override fun update(name: kotlin.String) {
        before()
        userService.update(name)
        after()
    }

    override fun show(): kotlin.String {
        return userService.show()
    }

    private fun before() {
        println(String.format("log start time [%s] ", Date()))
    }

    private fun after() {
        println(String.format("log after time [%s] ", Date()))
    }
}