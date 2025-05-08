package org.example.hw4_command.contract

interface IRefuelableObject {

    fun setFuel(amount: Int)

    fun getFuel(): Int

    fun getFuelConsumptionSpeed(): Int
}
