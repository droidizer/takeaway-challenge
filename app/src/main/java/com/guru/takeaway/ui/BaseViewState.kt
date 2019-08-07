package com.guru.takeaway.ui


open class BaseViewState<T> {

    @kotlin.jvm.JvmField
    var data: T? = null
    @kotlin.jvm.JvmField
    var error: Throwable? = null
    @kotlin.jvm.JvmField
    var currentState: Int = 0

    enum class State constructor(@kotlin.jvm.JvmField var value: Int) {
        SUCCESS(1), FAILED(-1)
    }
}
