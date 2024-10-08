package gg.aquatic.aquaticseries.paper.adapt

import gg.aquatic.aquaticseries.lib.adapt.AquaticString
import gg.aquatic.aquaticseries.lib.adapt.IItemStackAdapter
import gg.aquatic.aquaticseries.paper.PaperAdapter
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.md_5.bungee.api.ChatColor
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

object ItemStackAdapter: IItemStackAdapter {
    override fun displayName(string: AquaticString, itemStack: ItemStack) {
        val im = itemStack.itemMeta
        im?.displayName(convert(string))
        itemStack.itemMeta = im
    }

    override fun displayName(string: AquaticString, itemMeta: ItemMeta) {
        itemMeta.displayName(convert(string))
    }

    override fun lore(vararg strings: AquaticString, itemStack: ItemStack) {
        val im = itemStack.itemMeta
        im?.lore(strings.map { string -> convert(string) })
        itemStack.itemMeta = im
    }

    override fun lore(strings: Collection<AquaticString>, itemStack: ItemStack) {
        lore(*strings.toTypedArray(), itemStack = itemStack)
    }

    override fun lore(vararg strings: AquaticString, itemMeta: ItemMeta) {
        itemMeta.lore(strings.map { string -> convert(string) })
    }

    override fun lore(strings: Collection<AquaticString>, itemMeta: ItemMeta) {
        val newLore = strings.map { string ->
            convert(string)
        }
        itemMeta.lore(newLore)
    }

    override fun getAquaticLore(itemStack: ItemStack): List<AquaticString> {
        return itemStack.itemMeta.lore()?.map { PaperString(MiniMessage.miniMessage().serialize(it)) } ?: return listOf()
    }

    override fun getAquaticDisplayName(itemStack: ItemStack): AquaticString? {
        return itemStack.itemMeta.displayName()?.let { PaperString(MiniMessage.miniMessage().serialize(it)) }
    }

    override fun getAquaticDisplayName(itemMeta: ItemMeta): AquaticString? {
        return itemMeta.displayName()?.let { PaperString(MiniMessage.miniMessage().serialize(it)) }
    }

    override fun getAquaticLore(itemMeta: ItemMeta): List<AquaticString> {
        return itemMeta.lore()?.map { PaperString(MiniMessage.miniMessage().serialize(it)) } ?: return listOf()
    }

    private fun convert(aquaticString: AquaticString): Component {
        return PaperAdapter.minimessage.deserialize(ChatColor.stripColor(aquaticString.string
            .replace("&a", "<green>")
            .replace("&c", "<red>")
            .replace("&b", "<aqua>")
            .replace("&e", "<yellow>")
            .replace("&6", "<gold>")
            .replace("&d", "<light_purple>")
            .replace("&f", "<white>")
            .replace("&3", "<dark_aqua>")
            .replace("&9", "<blue>")
            .replace("&f", "<white>")
            .replace("&7", "<gray>")
            .replace("&8", "<dark_gray>")
            .replace("&4", "<dark_red>")
            .replace("&1", "<dark_blue>")
            .replace("&4", "<dark_red>")
            .replace("&8", "<dark_gray>")
            .replace("&2", "<dark_green>")
            .replace("&5", "<dark_purple>"))).decoration(TextDecoration.ITALIC, false)
    }
}