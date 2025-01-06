package nostalgic.cloud.framedhouses.commands

import nostalgic.cloud.framedhouses.Handlers.AnimationHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ToggleAnimationCommand(private val animationHandler: AnimationHandler) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            if (args.isEmpty()) {
                sender.sendMessage("Please provide a command: start or stop.")
                return false
            }
            when (args[0].lowercase()) {
                "start" -> {
                    animationHandler.startAnimation()
                    sender.sendMessage("Animations started.")
                }
                "stop" -> {
                    animationHandler.stopAnimation()
                    sender.sendMessage("Animations stopped.")
                }
                else -> {
                    sender.sendMessage("Invalid command. Use start or stop.")
                    return false
                }
            }
            return true
        }
        sender.sendMessage("This command can only be run by a player.")
        return false
    }
}