package hw6_adapter_and_bridge.resolvers

import org.example.hw3_abstractions.Property
import org.example.hw3_abstractions.UObject
import org.example.hw6_adapter_and_bridge.ioc.generating.contract.IResolver

class DirectionResolver : IResolver {

    override fun resolve(uObject: UObject, params: List<Any?>?): Any {
        uObject.setProperty(Property.ANGLE.methodName, params?.get(1))

        return Unit
    }
}
