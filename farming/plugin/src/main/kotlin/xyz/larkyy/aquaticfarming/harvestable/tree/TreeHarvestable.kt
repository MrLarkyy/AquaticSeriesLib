package xyz.larkyy.aquaticfarming.harvestable.tree

import com.google.gson.JsonParser
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType
import xyz.larkyy.aquaticfarming.AbstractAquaticFarming
import xyz.larkyy.aquaticfarming.farmland.AbstractCropFarmland
import xyz.larkyy.aquaticfarming.harvestable.HarvestableSeed
import xyz.larkyy.aquaticfarming.harvestable.Harvestable
import xyz.larkyy.aquaticfarming.harvestable.condition.growth.ConfiguredGrowthCondition
import xyz.larkyy.aquaticfarming.harvestable.condition.place.ConfiguredPlaceCondition
import xyz.larkyy.aquaticfarming.harvestable.stage.TreeStage
import xyz.larkyy.aquaticseries.interactable.AbstractSpawnedInteractable
import xyz.larkyy.aquaticseries.interactable.impl.block.SpawnedBlockInteractable
import xyz.larkyy.aquaticseries.item.CustomItem
import java.util.*
import kotlin.collections.ArrayList

class TreeHarvestable(
    override val id: String,
    seedItem: CustomItem,
    override val farmlands: ArrayList<AbstractCropFarmland>,
    override val sneakAcceleration: Double,
    override val placeConditions: MutableList<ConfiguredPlaceCondition>,
    override val growthConditions: MutableList<ConfiguredGrowthCondition>,
    val stages: ArrayList<TreeStage>,
) : Harvestable() {

    override val seed: HarvestableSeed = HarvestableSeed(seedItem,this)
    override val type: Type = Type.TREE
    override val cropNamespace = NamespacedKey(AbstractAquaticFarming.instance!!.injection.plugin,"AquaticFarming_Tree_$id")

    override fun spawn(location: Location): SpawnedTree {
        val stage = stages[0]
        val interactable = stage.interactable.spawn(location)
        val uuid = UUID.randomUUID()
        val tree =  SpawnedTree(uuid, this, interactable)
        AbstractAquaticFarming.instance!!.harvestableManager.spawnedHarvestables[uuid] = tree
        return tree
    }

    override fun fromInteractable(interactable: AbstractSpawnedInteractable): SpawnedTree? {
        if (interactable !is SpawnedBlockInteractable) return null
        val pdc = interactable.data
        val json = JsonParser().parse(pdc.get(cropNamespace, PersistentDataType.STRING))
        val treeObject = json.asJsonObject
        val uuid = UUID.fromString(treeObject.get("uuid").asString)
        val tree = SpawnedTree(uuid,this,interactable)

        val growthTicks = treeObject.get("growth-tick").asInt
        val stage = treeObject.get("stage").asInt
        tree.growthTick = growthTicks
        tree.stage = stage

        return tree
    }

    override fun canBeSpawned(location: Location): Boolean {
        val s = stages.getOrNull(0) ?: return false
        return s.interactable.canBePlaced(location)
    }

}