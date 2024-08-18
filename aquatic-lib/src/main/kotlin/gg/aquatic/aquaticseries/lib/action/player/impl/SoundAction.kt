package gg.aquatic.aquaticseries.lib.action.player.impl

import gg.aquatic.aquaticseries.lib.action.player.AbstractPlayerAction
import gg.aquatic.aquaticseries.lib.requirement.RequirementArgument
import gg.aquatic.aquaticseries.lib.util.AquaticObjectArgument
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders
import org.bukkit.entity.Player

class SoundAction: AbstractPlayerAction() {
    override fun run(player: Player, args: Map<String, Any?>, placeholders: Placeholders) {
        val sound = args["sound"] as String
        val volume = args["volume"] as Float
        val pitch = args["pitch"] as Float

        player.playSound(player.location, sound, volume, pitch)
    }

    override fun arguments(): List<AquaticObjectArgument> {
        return listOf(
            AquaticObjectArgument("sound", "minecraft:ambient.basalt_deltas.additions", true),
            AquaticObjectArgument("volume", 1.0f, false),
            AquaticObjectArgument("pitch", 1.0f, false)
        )
    }
}