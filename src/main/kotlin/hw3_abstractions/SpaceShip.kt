package org.example.hw3_abstractions

class SpaceShip : UObject {

    private val TAG = SpaceShip::class.simpleName

    private val property: HashMap<Property, Any> = hashMapOf()

    override fun getProperty(key: Property): Any {
        return property[key] ?: throw NoSuchElementException("in $TAG no property $key found")
    }

    override fun setProperty(key: Property, value: Any) {
        property[key] = value
    }
}
