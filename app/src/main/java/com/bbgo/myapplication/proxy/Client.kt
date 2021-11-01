package com.bbgo.myapplication.proxy

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*

fun main(args: Array<String>) {

    /**
     * 静态代理
     */
    val userService = UserServiceImpl()
//    val userProxy = UserServiceProxy(userService)
//    userProxy.select()
//    userProxy.update()

    /**
     * 动态代理
     */
    val classLoader = userService.javaClass.classLoader
    val interfaces = userService.javaClass.interfaces
    val proxy = Proxy.newProxyInstance(classLoader, interfaces, LogHandler(userService)) as UserService
//    proxy.select()
    proxy.update("test")


}

class LogHandler(private val target: Any) : InvocationHandler {

    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
        before()
        val result = if (args == null) {
            method?.invoke(target)
        } else {
            method?.invoke(target, args[0])
        }
        after()
        return result
    }

    private fun before() {
        println(java.lang.String.format("log start time [%s] ", Date()))
    }

    private fun after() {
        println(java.lang.String.format("log after time [%s] ", Date()))
    }

}