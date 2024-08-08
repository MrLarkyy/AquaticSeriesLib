package gg.aquatic.aquaticseries.paper.adapt

import gg.aquatic.aquaticseries.lib.adapt.AquaticString
import gg.aquatic.aquaticseries.paper.PaperAdapter
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class PaperString(
    override val string: String
): AquaticString() {
    override fun send(player: Player) {
        val component = miniMessage.deserialize(string)
        player.sendMessage(component)
    }

    override fun broadcast() {
        val component = miniMessage.deserialize(string)
        Bukkit.broadcast(component)
    }

    override fun send(vararg players: Player) {
        val component = miniMessage.deserialize(string)
        players.forEach { player -> player.sendMessage(component) }
    }

    private val miniMessage: MiniMessage
        get() {
            return PaperAdapter.minimessage
        }
}