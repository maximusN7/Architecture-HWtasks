package org.example.hw3_abstractions

class SpaceShip : UObject {

    private val TAG = SpaceShip::class.simpleName

    private val property: HashMap<String, Any?> = hashMapOf()

    override fun getProperty(key: Property): Any {
        println("AAAAA getProperty $key name = ${key.methodName}")
        return property[key.methodName] ?: throw NoSuchElementException("in $TAG no property ${key.methodName} found")
    }

    override fun getProperty(key: String): Any {
        println("AAAAA getProperty $key name = ${key}")
        return property[key] ?: throw NoSuchElementException("in $TAG no property $key found")
    }

    override fun setProperty(key: Property, value: Any) {
        println("AAAAA setProperty $key name = ${key.methodName} set $value")
        property[key.methodName] = value
    }

    override fun setProperty(key: String, value: Any?) {
        println("AAAAA setProperty $key name = ${key}  set $value")
        property[key] = value
    }
}
