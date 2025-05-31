package org.example.hw3_abstractions

class SpaceShip : UObject {

    private val TAG = SpaceShip::class.simpleName

    private val property: HashMap<String, Any?> = hashMapOf()

    override fun getProperty(key: Property): Any {
        return property[key.methodName] ?: throw NoSuchElementException("in $TAG no property ${key.methodName} found")
    }

    override fun getProperty(key: String): Any {
        return property[key] ?: throw NoSuchElementException("in $TAG no property $key found")
    }

    override fun setProperty(key: Property, value: Any) {
        property[key.methodName] = value
    }

    override fun setProperty(key: String, value: Any?) {
        property[key] = value
    }
}
