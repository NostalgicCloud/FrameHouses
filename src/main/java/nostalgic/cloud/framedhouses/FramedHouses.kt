package nostalgic.cloud.framedhouses

import nostalgic.cloud.framedhouses.Handlers.*
import nostalgic.cloud.framedhouses.commands.*
import nostalgic.cloud.framedhouses.listeners.*
import nostalgic.cloud.framedhouses.RegionSelector.RegionSelector
import org.bukkit.plugin.java.JavaPlugin

public class FramedHouses : JavaPlugin() {
    private lateinit var animationHandler: AnimationHandler
    public lateinit var frameHandler: FrameHandler
    private lateinit var regionSelector: RegionSelector

    override fun onEnable() {
        logger.info("Framed Houses has been enabled")
        logger.info("onEnable called")
        InitHandlers()

        logger.info("Registering Commands")
        registerCommands()

        registerEvents()
    }
    private fun InitHandlers() {
        regionSelector = RegionSelector()
        frameHandler = FrameHandler(this)
        frameHandler.loadFrames()
        animationHandler = AnimationHandler(this, frameHandler)
        animationHandler.startAnimation()
    }

    private fun registerCommands() {
        getCommand("sf")?.setExecutor(SaveFrameCommand(this, regionSelector))
        getCommand("tog")?.setExecutor(ToggleAnimationCommand(animationHandler))
        getCommand("set1")?.setExecutor(SetCorner1Command(regionSelector))
        getCommand("set2")?.setExecutor(SetCorner2Command(regionSelector))
        getCommand("list")?.setExecutor(ListAllAnimationCommand(frameHandler))
        getCommand("edit")?.setExecutor(EditAnimationCommand(frameHandler))
        getCommand("delete")?.setExecutor(DeleteAnimationCommand(this))
        getCommand("enable")?.setExecutor(EnableAnimationCommand(this, true))
        getCommand("disable")?.setExecutor(EnableAnimationCommand(this, false))
    }

    private fun registerEvents() {
        server.pluginManager.registerEvents(BlockBreakListener(frameHandler), this)
        server.pluginManager.registerEvents(LeafDecayListener(frameHandler), this)
        server.pluginManager.registerEvents(FireTickListener(frameHandler), this)
        server.pluginManager.registerEvents(ExplosionListener(frameHandler), this)
    }

    override fun onDisable() {
        frameHandler.saveAllFrames()
        logger.info("Framed Houses has been disabled")
    }
}