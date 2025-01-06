package nostalgic.cloud.framedhouses.Handlers

import nostalgic.cloud.framedhouses.classes.PlayerFrameData
import nostalgic.cloud.framedhouses.commands.SaveFrameCommand
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.plugin.Plugin
import java.io.*

class FrameHandler(private val plugin: Plugin) {
    val playersAnimations = mutableMapOf<String, MutableList<PlayerFrameData>>()
    init {
        if (!plugin.dataFolder.exists()) {
            plugin.dataFolder.mkdirs()
        }
    }

   fun saveFrames(playerUUID: String): Boolean {
    val playerDir = File(plugin.dataFolder, playerUUID)
    if (!playerDir.exists()) {
        playerDir.mkdirs()
    }
    val playerFrames = playersAnimations[playerUUID] ?: return false
    try {
        for (playerFrameData in playerFrames) {
            val file = File(playerDir, "ani_${playerFrameData.frameName}.txt")
            FileWriter(file).use { writer ->
                writer.write("${playerFrameData.frameName},${playerFrameData.shouldPlay},${playerFrameData.TPF}\n")
                for (frame in playerFrameData.frames) {
                    for (blockData in frame) {
                        writer.write("${blockData.location.world.name},${blockData.location.blockX},${blockData.location.blockY},${blockData.location.blockZ},${blockData.material}\n")
                    }
                    writer.write("FRAME_END\n")
                }
            }
        }
    } catch (e: IOException) {
        plugin.logger.severe("Failed to save frames for player $playerUUID: ${e.message}")
        return false
    }
    return true
}

    fun saveAllFrames() {
        for (playerUUID in playersAnimations.keys) {
            val playerDir = File(plugin.dataFolder, playerUUID)
            if (!playerDir.exists()) {
                playerDir.mkdirs()
            }
            saveFrames(playerUUID)
        }
    }

   fun loadFrames() {
    val playerDirs = plugin.dataFolder.listFiles { file -> file.isDirectory } ?: return
    for (playerDir in playerDirs) {
        val playerUUID = playerDir.name
        val frameFiles = playerDir.listFiles { file -> file.extension == "txt" } ?: continue
        val playerFrames = mutableListOf<PlayerFrameData>()
        for (frameFile in frameFiles) {
            val frames = mutableListOf<List<SaveFrameCommand.BlockData>>()
            var frameName = ""
            var shouldPlay = false
            var TPF = 4 // Default value
            BufferedReader(FileReader(frameFile)).use { reader ->
                var frame = mutableListOf<SaveFrameCommand.BlockData>()
                reader.forEachLine { line ->
                    if (line == "FRAME_END") {
                        frames.add(frame)
                        frame = mutableListOf()
                    } else if (frameName.isEmpty()) {
                        val parts = line.split(",")
                        frameName = parts[0]
                        shouldPlay = parts[1].toBoolean()
                        if (parts.size > 2) {
                            TPF = parts[2].toInt()
                        }
                    } else {
                        val parts = line.split(",")
                        val world = Bukkit.getWorld(parts[0])
                        val x = parts[1].toInt()
                        val y = parts[2].toInt()
                        val z = parts[3].toInt()
                        val material = Material.valueOf(parts[4])
                        val location = Location(world, x.toDouble(), y.toDouble(), z.toDouble())
                        frame.add(SaveFrameCommand.BlockData(location, material))
                    }
                }
            }
            val chunkLocation = frames.firstOrNull()?.firstOrNull()?.location?.chunk ?: continue
            playerFrames.add(PlayerFrameData(frameName, shouldPlay, chunkLocation, TPF, frames))
            println("Loaded frames for player $playerUUID")
        }
        playersAnimations[playerUUID] = playerFrames
    }
}

    fun addFrame(playerUUID: String, frameName: String, frame: List<SaveFrameCommand.BlockData>) {
        val playerFrames = playersAnimations.getOrPut(playerUUID) { mutableListOf() }
        val chunkLocation = frame.firstOrNull()?.location?.chunk ?: return
        val playerFrameData = playerFrames.find { it.chunkLocation == chunkLocation }
            ?: PlayerFrameData(frameName, false, chunkLocation, 4).also { playerFrames.add(it) }
        playerFrameData.addFrame(frame)
    }

    fun removeAnimation(playerUUID: String, frameName: String): Boolean {
    val playerFrames = playersAnimations[playerUUID] ?: return false
    val animation = playerFrames.find { it.frameName == frameName } ?: return false
    playerFrames.remove(animation)
    playersAnimations[playerUUID] = playerFrames
    return true
}

    fun getPlayerFrames(playerUUID: String): List<PlayerFrameData>? {
        return playersAnimations[playerUUID]
    }
    fun getAllFrames(): Map<String, List<PlayerFrameData>> {
        return playersAnimations
    }
}