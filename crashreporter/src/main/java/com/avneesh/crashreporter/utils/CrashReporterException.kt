package com.avneesh.crashreporter.utils


open class CrashReporterException : RuntimeException {

    constructor() : super() {}


    constructor(message: String) : super(message) {}


    constructor(format: String, vararg args: Any) : this(String.format(format, *args)) {}

    constructor(message: String, throwable: Throwable) : super(message, throwable) {}


    constructor(throwable: Throwable) : super(throwable) {}

    override fun toString(): String {
        return message!!
    }

    companion object {
        internal val serialVersionUID: Long = 1
    }
}
