package org.example.adapter

import org.example.contract.IShootingObject
import org.example.model.UObject

class ShootingObjectAdapter(
    private val obj: UObject,
) : IShootingObject
