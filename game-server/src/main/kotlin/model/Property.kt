package org.example.model

enum class Property(val methodName: String) {
    LOCATION("Location"),
    VELOCITY("Velocity"),
    ANGLE("Angle"),
    ANGULAR_VELOCITY("AngularVelocity"),
    FUEL_CONSUMPTION("FuelConsumption"),
    FUEL("Fuel"),
    COLLISION_STATE("IsCollided"),
}
