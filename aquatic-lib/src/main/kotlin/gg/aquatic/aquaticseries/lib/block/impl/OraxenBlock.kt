package gg.aquatic.aquaticseries.lib.block.impl

import gg.aquatic.aquaticseries.lib.audience.AquaticAudience
import gg.aquatic.aquaticseries.lib.block.AquaticBlock
import gg.aquatic.aquaticseries.lib.fake.PacketBlock
import io.th0rgal.oraxen.api.OraxenBlocks
import org.bukkit.Location
import org.bukkit.block.data.BlockData

class OraxenBlock(
    val oraxenId: String
): AquaticBlock() {
    override fun place(location: Location) {
        OraxenBlocks.place(oraxenId,location)
    }

    override fun placePacket(location: Location, audienceList: AquaticAudience): PacketBlock {
        return PacketBlock(location, blockData, audienceList) {}
    }

    override val blockData: BlockData
        get() {
            return OraxenBlocks.getOraxenBlockData(oraxenId)!!
        }
}