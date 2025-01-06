package nostalgic.cloud.framedhouses.listeners

import nostalgic.cloud.framedhouses.Handlers.FrameHandler
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockIgniteEvent

class FireTickListener(private val frameHandler: FrameHandler) : Listener {
    @EventHandler
    fun onBlockIgnite(event: BlockIgniteEvent) {
        val block = event.block
        val allPlayerFrames = frameHandler.getAllFrames()

        for (playerFrames in allPlayerFrames) {
            for (frameData in playerFrames.value) {
                if (frameData.chunkLocation != block.chunk) {
                    continue
                }
                for (frame in frameData.frames) {
                    if (frame.any { it.location.block == block }) {
                        event.isCancelled = true
                        return
                    }
                }
            }
        }
    }
}