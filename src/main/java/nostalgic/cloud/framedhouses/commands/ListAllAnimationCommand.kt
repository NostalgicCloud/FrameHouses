package nostalgic.cloud.framedhouses.commands

import nostalgic.cloud.framedhouses.Handlers.FrameHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ListAllAnimationCommand(private val frameHandler: FrameHandler) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        return if (sender is Player) {
            val playerUUID = sender.uniqueId.toString()
            val playerFrames = frameHandler.getPlayerFrames(playerUUID)

            if (!playerFrames.isNullOrEmpty()) {
                val animationNames = playerFrames.map { it.frameName }
                sender.sendMessage("You have the following animations: ${animationNames.joinToString(", ")}")
            } else {
                sender.sendMessage("You do not have any animations.")
            }
            true
        } else {
            sender.sendMessage("This command can only be run by a player.")
            false
        }
    }
}