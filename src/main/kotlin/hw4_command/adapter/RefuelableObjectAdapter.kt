package org.example.hw4_command.adapter

import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.UObject
import org.example.hw4_command.contract.IRefuelableObject

class RefuelableObjectAdapter(
    private val obj: UObject,
) : IRefuelableObject {

    override fun setFuel(amount: Int) {
        obj.setProperty(Property.FUEL, amount)
    }

    override fun getFuel(): Int {
        return obj.getProperty(Property.FUEL) as Int
    }

    override fun getFuelConsumptionSpeed(): Int {
        return obj.getProperty(Property.FUEL_CONSUMPTION) as Int
    }
}
