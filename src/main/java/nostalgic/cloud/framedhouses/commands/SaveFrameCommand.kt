package nostalgic.cloud.framedhouses.commands

import nostalgic.cloud.framedhouses.FramedHouses
import nostalgic.cloud.framedhouses.RegionSelector.RegionSelector
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SaveFrameCommand(private val plugin: FramedHouses, private val regionSelector: RegionSelector) : CommandExecutor {

    data class BlockData(val location: Location, val material: Material)

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
    if (sender is Player) {
        if (args.size < 1) {
            sender.sendMessage("Please provide an animation name.")
            return false
        }

        val animationName = args[0].lowercase()

        val blocks = mutableListOf<BlockData>()
        val edgePoints = regionSelector.getEdgePoints(sender.uniqueId.toString()) ?: run {
            sender.sendMessage("Could not save the frame: region points are not properly defined.")
            return false
        }
        val (minPoint, maxPoint) = edgePoints
        val chunk = minPoint.chunk
        val minY = minPoint.blockY
        val maxY = maxPoint.blockY

        for (x in 0..15) {
            for (z in 0..15) {
                for (y in minY..maxY) {
                    val block = chunk.getBlock(x, y, z)
                    blocks.add(BlockData(block.location, block.type))
                }
            }
        }

        val playerUUID = sender.uniqueId.toString()
        plugin.frameHandler.addFrame(playerUUID, animationName, blocks)
        plugin.frameHandler.saveFrames(playerUUID)

        sender.sendMessage("Frame saved successfully with animation name: $animationName.")
        return true
    }
    sender.sendMessage("This command can only be run by a player.")
    return false
}
}