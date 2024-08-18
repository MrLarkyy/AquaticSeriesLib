package gg.aquatic.aquaticseries.lib.action.player.impl

import gg.aquatic.aquaticseries.lib.action.player.AbstractPlayerAction
import gg.aquatic.aquaticseries.lib.requirement.RequirementArgument
import gg.aquatic.aquaticseries.lib.toAquatic
import gg.aquatic.aquaticseries.lib.util.AquaticObjectArgument
import gg.aquatic.aquaticseries.lib.util.placeholder.Placeholders
import org.bukkit.entity.Player

class BroadcastAction: AbstractPlayerAction() {
    override fun run(player: Player, args: Map<String, Any?>, placeholders: Placeholders) {
        val message = args["message"] as String
        message.toAquatic().broadcast()
    }

    override fun arguments(): List<AquaticObjectArgument> {
        return listOf(
            AquaticObjectArgument("message", "", true)
        )
    }
}