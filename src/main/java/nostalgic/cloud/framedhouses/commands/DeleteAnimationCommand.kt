package nostalgic.cloud.framedhouses.commands

import nostalgic.cloud.framedhouses.FramedHouses
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.File

class DeleteAnimationCommand(private val plugin: FramedHouses) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            if (args.isEmpty()) {
                sender.sendMessage("Please provide the animation name to delete.")
                return false
            }

            val animationName = args[0].lowercase()
            val playerUUID = sender.uniqueId.toString()
            val animationFile = File(plugin.dataFolder, "$playerUUID/ani_$animationName.txt")

            if (animationFile.exists()) {
                if (animationFile.delete()) {
                    plugin.frameHandler.removeAnimation(playerUUID, animationName)
                    sender.sendMessage("Animation $animationName deleted successfully.")
                    return true
                } else {
                    sender.sendMessage("Failed to delete animation $animationName.")
                    return false
                }
            } else {
                sender.sendMessage("Animation $animationName does not exist.")
                return false
            }
        }
        return false
    }
}