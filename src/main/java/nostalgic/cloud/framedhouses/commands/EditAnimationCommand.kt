package nostalgic.cloud.framedhouses.commands

import nostalgic.cloud.framedhouses.Handlers.FrameHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class EditAnimationCommand(private val frameHandler: FrameHandler) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            if (args.size < 2) {
                sender.sendMessage("Usage: /editanimation <animationName> <TPF>")
                return false
            }

            val animationName = args[0].lowercase()
           val tpf: Int = args[1].toIntOrNull() ?: run {
    sender.sendMessage("Please enter a valid number for TPF.")
    return false
}

            val playerUUID = sender.uniqueId.toString()
            val playerFrames = frameHandler.getPlayerFrames(playerUUID) ?: run {
                sender.sendMessage("No animations found for your player.")
                return false
            }

            val animation = playerFrames.find { it.frameName == animationName } ?: run {
                sender.sendMessage("Animation with name $animationName not found.")
                return false
            }

            animation.TPF = tpf
            frameHandler.saveFrames(playerUUID)
            sender.sendMessage("TPF for animation $animationName set to $tpf.")
            return true
        }

        sender.sendMessage("This command can only be run by a player.")
        return false
    }
}