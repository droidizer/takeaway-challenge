package com.guru.takeaway

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


    @Test
    fun addition_isCorrect() {
        val a = 0
        val b =0
        sumOfTwoNumbers(a, b)
        System.out.println("Hello World!")

    }

    @Test
    fun sub_isCorrect() {
        System.out.println("Hello World!")
    }

    private fun sumOfTwoNumbers(a: Int, b: Int): Int {
        return a + b
    }
}
