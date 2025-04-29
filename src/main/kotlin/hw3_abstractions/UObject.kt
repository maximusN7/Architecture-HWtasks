package org.example.hw3_abstractions

interface UObject {

    fun getProperty(key: Property): Any

    fun setProperty(key: Property, value: Any)
}
