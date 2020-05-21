package ru.skillbranch.gameofthrones.utils

import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.Callable
import java.util.concurrent.Executors

fun isOnline(): Boolean {
    val timeoutMs = 1500
    val socket = Socket()
    val socketAddress = InetSocketAddress("8.8.8.8", 53)

    val executor = Executors.newSingleThreadExecutor()
    val callable =  Callable<Boolean> {
        try {
            socket.connect(socketAddress, timeoutMs)
            socket.close()
            true
        } catch (e: IOException) {
            false
        }
    }
    val future = executor.submit(callable)
    executor.shutdown()
    return future.get()
}