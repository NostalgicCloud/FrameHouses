package nostalgic.cloud.framedhouses.RegionSelector

import org.bukkit.Bukkit
import org.bukkit.Location

class RegionSelector {
    private val corner1: MutableMap<String, Location> = mutableMapOf()
    private val corner2: MutableMap<String, Location> = mutableMapOf()

    fun setCorner1(playerUUID: String, location: Location) {
        corner1[playerUUID] = location
    }

    fun setCorner2(playerUUID: String, location: Location) {
        corner2[playerUUID] = location
    }

    fun isWithinRegion(playerLocation: Location, animationLocation: Location): Boolean {
        val chunkX = animationLocation.chunk.x
        val chunkZ = animationLocation.chunk.z
        val chunkXLoc = playerLocation.chunk.x
        val chunkZLoc = playerLocation.chunk.z

        val renderDistance = Bukkit.getServer().viewDistance

        return chunkXLoc in (chunkX - renderDistance)..(chunkX + renderDistance) &&
                chunkZLoc in (chunkZ - renderDistance)..(chunkZ + renderDistance)
    }

    fun getEdgePoints(playerUUID: String): Pair<Location, Location>? {
        val loc1 = corner1[playerUUID]
        val loc2 = corner2[playerUUID]

        if (loc1 == null || loc2 == null) {
            return null
        }

        if (loc1.chunk != loc2.chunk) {
            return null
        }

        val minY = minOf(loc1.blockY, loc2.blockY)
        val maxY = maxOf(loc1.blockY, loc2.blockY)

        val minPoint = Location(loc1.world, loc1.blockX.toDouble(), minY.toDouble(), loc1.blockZ.toDouble())
        val maxPoint = Location(loc1.world, loc2.blockX.toDouble(), maxY.toDouble() - 1, loc2.blockZ.toDouble())

        return Pair(minPoint, maxPoint)
    }
}