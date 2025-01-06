package nostalgic.cloud.framedhouses.commands

import nostalgic.cloud.framedhouses.FramedHouses
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class EnableAnimationCommand(private val plugin: FramedHouses, private val shouldPlay:Boolean ) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            if (args.isEmpty()) {
                sender.sendMessage("Please provide the animation name to enable.")
                return false
            }

            val animationName = args[0].lowercase()
            val playerUUID = sender.uniqueId.toString()
            val playerFrames = plugin.frameHandler.getPlayerFrames(playerUUID)

            if (playerFrames != null) {
                val animation = playerFrames.find { it.frameName == animationName }

                if (animation != null) {
                    animation.shouldPlay = shouldPlay
                    plugin.frameHandler.saveFrames(playerUUID)
                    if (shouldPlay) {
                        sender.sendMessage("Animation $animationName enabled successfully.")
                    } else {
                        sender.sendMessage("Animation $animationName disabled successfully.")
                    }

                } else {
                    sender.sendMessage("Animation $animationName not found.")
                    return false
                }
            } else {
                sender.sendMessage("No frames found for player.")
                return false
            }
        } else {
            sender.sendMessage("This command can only be run by a player.")
            return false
        }
        return false
    }
}