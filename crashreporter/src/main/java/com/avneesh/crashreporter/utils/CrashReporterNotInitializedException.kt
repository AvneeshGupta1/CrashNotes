package com.avneesh.crashreporter.utils

class CrashReporterNotInitializedException : CrashReporterException {

    constructor() : super() {}

    constructor(message: String) : super(message) {}

    constructor(message: String, throwable: Throwable) : super(message, throwable) {}

    constructor(throwable: Throwable) : super(throwable) {}

    companion object {
        internal val serialVersionUID: Long = 1
    }
}