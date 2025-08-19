package org.example.model

interface UObject {

    fun getProperty(key: Property): Any

    fun getProperty(key: String): Any

    fun setProperty(key: Property, value: Any)

    fun setProperty(key: String, value: Any?)
}
