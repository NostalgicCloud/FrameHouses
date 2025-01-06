package nostalgic.cloud.framedhouses.Handlers

import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class AnimationHandler(private val plugin: Plugin, private val frameHandler: FrameHandler) {

    private var task: BukkitTask? = null
    fun isAnimationEnabled(): Boolean {
        // Implement your logic to check if the animation is enabled
        return true
    }
    fun startAnimation() {
        if (task == null) {
            task = object : BukkitRunnable() {
                override fun run() {
                    if (!isAnimationEnabled()) {
                        stopAnimation()
                        return
                    }
                    for (playerAni in frameHandler.playersAnimations) {
                        for (frame in playerAni.value) {
                            if (frame.ShouldPlay()) {
                                val currentFrame = frame.getCurrentFrame()
                                if (currentFrame != null) {
                                    for (blockData in currentFrame) {
                                        val block = blockData.location.block
                                        block.type = blockData.material
                                    }
                                }
                            }
                        }
                    }


                }
            }.runTaskTimer(plugin, 0L, 5L)
        } else {
            println("Animation task is already running")
        }
    }
    fun stopAnimation() {
        task?.cancel()
        task = null
    }
}