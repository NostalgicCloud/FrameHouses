package nostalgic.cloud.framedhouses.commands

import nostalgic.cloud.framedhouses.RegionSelector.RegionSelector
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetCorner2Command(private val regionSelector: RegionSelector) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val location = sender.location
            regionSelector.setCorner2(sender.uniqueId.toString(),location)
            sender.sendMessage("Corner 2 set to your current location!")
            return true
        }
        sender.sendMessage("This command can only be run by a player.")
        return false
    }
}