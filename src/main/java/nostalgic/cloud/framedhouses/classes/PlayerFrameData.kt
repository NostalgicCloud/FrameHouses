package nostalgic.cloud.framedhouses.classes

import nostalgic.cloud.framedhouses.commands.SaveFrameCommand
import org.bukkit.Chunk

class PlayerFrameData(
    val frameName: String,
    var shouldPlay: Boolean,
    val chunkLocation: Chunk,
    var TPF: Int,
    val frames: MutableList<List<SaveFrameCommand.BlockData>> = mutableListOf()
) {
    private var currentFrameIndex = 0
    private var ticksSinceLastFrame = 0
    fun addFrame(frame: List<SaveFrameCommand.BlockData>) {
        frames.add(frame)
    }
    fun getCurrentFrame(): List<SaveFrameCommand.BlockData>? {
        if (frames.isEmpty()) return null
        val returnerframe = frames[currentFrameIndex]
        currentFrameIndex++
        if (currentFrameIndex >= frames.size) currentFrameIndex = 0
        return returnerframe
    }
    fun ShouldPlay(): Boolean {
        //if should not play early return logic, dont do other logic
        if (!shouldPlay){
            return false
        }

        ticksSinceLastFrame++
        if (ticksSinceLastFrame > TPF){
            ticksSinceLastFrame = 0
            return true
        }

        return false
    }
}