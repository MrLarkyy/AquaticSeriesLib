package gg.aquatic.aquaticseries.lib.betterinventory2.serialize

import gg.aquatic.aquaticseries.lib.action.ConfiguredAction
import gg.aquatic.aquaticseries.lib.action.player.PlayerActionSerializer
import gg.aquatic.aquaticseries.lib.betterinventory2.SlotSelection
import gg.aquatic.aquaticseries.lib.getSectionList
import gg.aquatic.aquaticseries.lib.item.CustomItem
import gg.aquatic.aquaticseries.lib.requirement.player.PlayerRequirementSerializer
import gg.aquatic.aquaticseries.lib.toAquatic
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import java.util.TreeMap
import java.util.function.Function

object InventorySerializer {

    fun loadInventory(section: ConfigurationSection): InventorySettings? {
        val title = section.getString("title") ?: return null
        val size = section.getInt("size")
        val inventoryType = section.getString("inventory-type")?.let {
            try {
                InventoryType.valueOf(it.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }
        }
        val buttons = section.getSectionList("buttons").mapNotNull { loadButton(it, it.name) }
        val onOpen = PlayerActionSerializer.fromSections(section.getSectionList("on-open"))
        val onClose = PlayerActionSerializer.fromSections(section.getSectionList("on-close"))
        return InventorySettings(
            title.toAquatic(),
            size,
            inventoryType,
            buttons,
            onOpen,
            onClose
        )
    }

    fun loadButton(section: ConfigurationSection, id: String): ButtonSettings? {
        val priority = section.getInt("priority", 0)
        val failItemSection = section.getConfigurationSection("fail-item")

        val failItem = if (failItemSection != null) {
            loadButton(failItemSection, "fail-item")
        } else null

        val conditions = HashMap<Function<Player, Boolean>, ButtonSettings?>()
        for (conditionSection in section.getSectionList("view-conditions")) {
            val condition = PlayerRequirementSerializer.fromSection(conditionSection) ?: continue
            val conditionFailItemSection = conditionSection.getConfigurationSection("fail-item")
            val conditionFailItem: ButtonSettings? = if (conditionFailItemSection != null) {
                loadButton(conditionFailItemSection, "fail-item")
            } else null
            conditions += Function<Player, Boolean> { t: Player ->
                condition.check(t)
            } to conditionFailItem
        }

        val clickSettings = loadClickSettings(section.getSectionList("click-actions"))

        val updateEvery = section.getInt("update-every", 10)
        val framesSection = section.getConfigurationSection("frames")
        if (framesSection != null) {
            val frames = TreeMap<Int, ButtonSettings>()
            for (key in framesSection.getKeys(false)) {
                val frameSection = framesSection.getConfigurationSection(key) ?: continue
                val time = key.toInt()
                val btn = loadButton(frameSection, "frame") ?: continue
                frames[time] = btn
            }
            if (frames.isEmpty()) return null
            return AnimatedButtonSettings(
                id, priority, conditions, failItem, clickSettings, updateEvery,
                frames
            )
        } else {
            val item = CustomItem.loadFromYaml(section) ?: return null
            val slots = section.getIntegerList("slots")
            if (slots.isEmpty()) {
                slots += section.getInt("slot")
            }
            return StaticButtonSettings(
                id, priority, conditions, failItem, clickSettings, updateEvery,
                item,
                SlotSelection.of(slots)
            )
        }
    }

    private fun loadClickSettings(sections: List<ConfigurationSection>): ClickSettings {
        val map = HashMap<ClickSettings.MenuClickActionType, MutableList<ConfiguredAction<Player>>>()
        for (section in sections) {
            val actions = PlayerActionSerializer.fromSections(section.getSectionList("actions"))
            for (menuClickActionType in section.getStringList("on")
                .mapNotNull { ClickSettings.MenuClickActionType.valueOf(it.uppercase()) }) {
                val list = map.getOrPut(menuClickActionType) { ArrayList() }
                list.addAll(actions)
            }
        }
        return ClickSettings(map)

    }
}