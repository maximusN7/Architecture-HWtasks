package org.example.hw12_chain_of_responsibility_pattern

data class RegionSystem(
    val regionSize: Int,
    val pointOfMeasureShift: Int,
    val regionsAndObjects: MutableMap<Region, MutableList<IGameFieldObject>>,
) {

    fun putObjectInRegion(obj: IGameFieldObject, newRegion: Region) {
        var oldRegion: Region? = null
        regionsAndObjects.forEach {
            if (it.value.contains(obj)) {
                oldRegion = it.key
                return@forEach
            }
        }

        if (oldRegion == newRegion) return

        val objectsInRegion = regionsAndObjects[newRegion]
        val objectsInOldRegion = regionsAndObjects[oldRegion]

        objectsInOldRegion?.remove(obj)

        if (objectsInRegion == null) {
            regionsAndObjects[newRegion] = mutableListOf(obj)
        } else {
            regionsAndObjects[newRegion]?.add(obj)
        }
    }
}
