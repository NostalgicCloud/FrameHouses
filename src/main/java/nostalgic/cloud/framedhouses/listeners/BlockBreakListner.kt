package nostalgic.cloud.framedhouses.listeners

import nostalgic.cloud.framedhouses.Handlers.FrameHandler
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class BlockBreakListener(private val frameHandler: FrameHandler) : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        val playerUUID = event.player.uniqueId.toString()
        val allPlayerFrames = frameHandler.getAllFrames()

        for (playerFrames in allPlayerFrames) {
            for (frameData in playerFrames.value) {
                if (frameData.chunkLocation != block.chunk) {
                    continue
                }

                for (frame in frameData.frames) {
                    if (frame.any { it.location.block == block }) {
                        if (playerFrames.key == playerUUID && !frameData.shouldPlay) {
                            block.type = Material.AIR
                        }
                        event.isCancelled = true
                        return
                    }
                }
            }
        }
    }
}