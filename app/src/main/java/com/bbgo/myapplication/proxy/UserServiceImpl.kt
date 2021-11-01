package com.bbgo.myapplication.proxy

class UserServiceImpl : UserService {
    override fun select() {
        println("this is select")
    }

    override fun update(name: String) {
        println("this is update")
        show()
    }

    override fun show(): String {
        return "this is show"
    }
}