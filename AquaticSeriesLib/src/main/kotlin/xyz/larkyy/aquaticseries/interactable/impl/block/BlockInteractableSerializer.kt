package xyz.larkyy.aquaticseries.interactable.impl.block

import org.bukkit.Location
import xyz.larkyy.aquaticseries.interactable.AbstractInteractable
import xyz.larkyy.aquaticseries.interactable.AbstractInteractableSerializer

class BlockInteractableSerializer : AbstractInteractableSerializer<SpawnedBlockInteractable>() {
    override fun serialize(value: SpawnedBlockInteractable): String {
        return ""
    }

    override fun deserialize(
        value: String,
        location: Location,
        interactable: AbstractInteractable
    ): SpawnedBlockInteractable? {
        if (interactable !is BlockInteractable) return null
        return interactable.spawn(location)
    }
}