package coffee.amo.interactionkey.client

import coffee.amo.interactionkey.api.Interactable
import coffee.amo.interactionkey.api.InteractableBlockEntityHolder
import coffee.amo.interactionkey.api.InteractableEntityHolder
import coffee.amo.interactionkey.math.MathHelper
import coffee.amo.interactionkey.net.PacketHandler
import coffee.amo.interactionkey.net.ServerboundEntityInteractionPacket
import coffee.amo.interactionkey.registry.KeyRegistry
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.*
import com.mojang.math.Matrix4f
import crystalspider.harvestwithease.config.HarvestWithEaseConfig
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3
import net.minecraftforge.client.event.RenderGuiOverlayEvent
import net.minecraftforge.common.ForgeMod
import net.minecraftforge.network.PacketDistributor
import net.minecraftforge.registries.ForgeRegistries
import java.util.*
import kotlin.collections.ArrayList

object ClientUtil {
    fun useItemHack() {
        (Minecraft.getInstance() as IWhyDoesThisExist).startUseItem(1)
    }

    fun getEntities(player: Player?, position: Vec3?, range: Double): List<Interactable> {
        val entities = ArrayList<Interactable>()
        if (player != null) {
            for (entity in player.level.getEntities(player, player.getBoundingBox().inflate(range))) {
                if (entity is Entity) {
                    if (position?.let { entity.position().distanceTo(it) }!! <= range) {
                        val interactable = InteractableEntityHolder(entity, null, null ,0);
                        entities.add(interactable)
                    }
                }
            }
        }
        return entities
    }

    fun getBlockEntities(player: Player?, position: Vec3?, range: Double): List<Interactable> {
        val entities = ArrayList<Interactable>()
        val blockPos = position?.let { BlockPos(it) };
        val blocksInArea = getBlocksInArea(player, blockPos, range);
        for (block in blocksInArea) {
            val blockEntity = player?.level?.getBlockEntity(block);
            if (blockEntity != null) {
                var interactable = InteractableBlockEntityHolder(blockEntity, null, null ,0);
                entities.add(interactable)
            }
        }
        return entities
    }

    private fun getBlocksInArea(player: Player?, blockPos: BlockPos?, range: Double): ArrayList<BlockPos> {
        val blocks = ArrayList<BlockPos>()
        if (player != null) {
            for (x in -range.toInt()..range.toInt()) {
                for (y in -range.toInt()..range.toInt()) {
                    for (z in -range.toInt()..range.toInt()) {
                        val bp = blockPos?.offset(x, y, z)
                        if (bp != null) {
                            blocks.add(bp)
                        }
                    }
                }
            }
        }
        return blocks
    }

    fun fillInteractionStack(vararg interactables: List<Interactable>) {
        InteractionStack.clear();
        for (list in interactables) {
            for(interactable in list) {
                InteractionStack.getStack().add(interactable);
            }
        }
    }

    // separate from harvest with ease, allow right clicking with blocks but not placing them
    fun drawHud(event: RenderGuiOverlayEvent.Post) {
        val centerX = event.window.guiScaledWidth / 2f
        val centerY = event.window.guiScaledHeight / 2f
        val ps = event.poseStack
        ps.pushPose()
        ps.translate(centerX.toDouble(), centerY.toDouble(), 0.0)
        val ibe = interactableBlockEntity
        val ie = getItem(Minecraft.getInstance().player)
        val e = lookingAtEntity
        val list = InteractionStack.getStack();
        for(i in 0 until list.size) {
            ps.pushPose()
            val interactable = list[i]
            var text: String = ""
            if(interactable is InteractableBlockEntityHolder){
                text = interactable.entity.blockState.block.name.string
            } else if(interactable is InteractableEntityHolder){
                text = if(interactable.entity is ItemEntity){
                    (interactable.entity as ItemEntity).item.displayName.string
                } else {
                    Component.translatable(interactable.entity.type.descriptionId).string
                }
            }
            val draw = "[" + KeyRegistry.INTERACTION_KEY.key.displayName.string.uppercase(Locale.getDefault()) + "] " + text
            if(InteractionStack.selected > i-1){
                ps.translate(0.0,14.0*(i-InteractionStack.selected), 0.0)
            } else {
                ps.translate(0.0,14.0*(i-InteractionStack.selected), 0.0)
            }
            drawHoverTooltip(ps, draw)
            ps.popPose()
        }
//        if (e != null) {
//            if (e is Interactable) {
//                val text: String? = e.interactableName
//                val draw = "[" + KeyRegistry.INTERACTION_KEY.key.displayName.string.uppercase(Locale.getDefault()) + "] " + text
//                drawHoverTooltip(ps, draw)
//            }
//        } else if (ie != null) {
//            val text = ie.item.displayName.string
//            val draw = "[" + KeyRegistry.INTERACTION_KEY.key.displayName.string.uppercase(Locale.getDefault()) + "] " + text
//            drawHoverTooltip(ps, draw)
//        } else if (ibe != null) {
//            val text = ibe.blockState.block.name.string
//            val draw = "[" + KeyRegistry.INTERACTION_KEY.key.displayName.string.uppercase(Locale.getDefault()) + "] " + text
//            drawHoverTooltip(ps, draw)
//        } else if (interactableBlockState != null) {
//            val text = interactableBlockState!!.block.name.string
//            val draw = "[" + KeyRegistry.INTERACTION_KEY.key.displayName.string.uppercase(Locale.getDefault()) + "] " + text
//            drawHoverTooltip(ps, draw)
//        }
        //        else if (ItemUtil.getClosestItem(Minecraft.getInstance().player) != null) {
//            String itemName = ItemUtil.getClosestItem(Minecraft.getInstance().player).getName().getString();
//            String draw = "[" + KeyRegistry.INTERACTION_KEY.getKey().getDisplayName().getString().toUpperCase() + "] " + itemName;
//            drawHoverTooltip(ps, draw);
//        }
        ps.popPose()
    }

    private fun drawHoverTooltip(ps: PoseStack, text: String) {
        ps.pushPose()
        val width = Minecraft.getInstance().font.width(text)
        val height = Minecraft.getInstance().font.lineHeight
        ps.translate((8 + width / 2f).toDouble(), 0.0, 0.0)
        fill(ps, -width / 2 - 2, -height / 2 - 2, width / 2 + 2, height / 2 + 2, -0x56000000)
        ps.translate((-8 - width / 2f).toDouble(), 0.0, 0.0)
        Gui.drawCenteredString(ps, Minecraft.getInstance().font, text, 8 + width / 2, -4, -0x1)
        ps.popPose()
    }

    val interactableBlockEntity: BlockEntity?
        get() {
            val result = if (Minecraft.getInstance().hitResult!!.type == HitResult.Type.BLOCK) Minecraft.getInstance().hitResult as BlockHitResult? else null
            if (result != null) {
                if (Minecraft.getInstance().level!!.getBlockEntity(result.blockPos) != null) {
                    return Minecraft.getInstance().level!!.getBlockEntity(result.blockPos)
                }
            }
            return null
        }
    val lookingAtItem: ItemEntity?
        get() {
            val result = if (Minecraft.getInstance().hitResult!!.type == HitResult.Type.ENTITY) Minecraft.getInstance().hitResult as EntityHitResult? else null
            if (result != null) {
                if (result.entity is ItemEntity) {
                    return result.entity as ItemEntity
                }
            }
            return null
        }
    val lookingAtEntity: Entity?
        get() {
            val result = if (Minecraft.getInstance().hitResult!!.type == HitResult.Type.ENTITY) Minecraft.getInstance().hitResult as EntityHitResult? else null
            return if (result != null) {
                if (result.entity is ItemEntity || result.entity is ExperienceOrb) null else result.entity
            } else null
        }

    fun handleClientInteract(): Boolean {
        val entity = lookingAtEntity
        if (entity is Interactable) {
            PacketHandler.INSTANCE.send<ServerboundEntityInteractionPacket>(PacketDistributor.SERVER.noArg(), ServerboundEntityInteractionPacket(entity.id, Minecraft.getInstance().player!!.uuid))
            return true
        }
        return false
    }

    val interactableBlockState: BlockState?
        get() {
            val result = if (Minecraft.getInstance().hitResult!!.type == HitResult.Type.BLOCK) Minecraft.getInstance().hitResult as BlockHitResult? else null
            if (result != null) {
                val state = Minecraft.getInstance().level!!.getBlockState(result.blockPos)
                if (isCrop(state.block)) return state
            }
            return null
        }

    private fun isCrop(block: Block): Boolean {
        return block is CropBlock || block === Blocks.NETHER_WART || block === Blocks.COCOA || HarvestWithEaseConfig.getCrops().contains(ForgeRegistries.BLOCKS.getKey(block).toString())
    }

    fun getEntityItem(player: Player?, position: Vec3?, look: Vec3): HitResult? {
        val include = look.subtract(position)
        val list: List<*> = player!!.level.getEntities(player, player.boundingBox.expandTowards(include))
        for (i in list.indices) {
            val entity = list[i] as Entity
            if (entity is ItemEntity) {
                val aabb = entity.getBoundingBox().inflate(0.2)
                val vec = aabb.clip(position, look)
                if (vec.isPresent) {
                    return EntityHitResult(entity, vec.get())
                }
                if (aabb.contains(position)) {
                    return EntityHitResult(entity, position)
                }
            }
        }
        return null
    }

    fun getItem(player: Player?): ItemEntity? {
        var distance = player!!.getAttributeValue(ForgeMod.REACH_DISTANCE.get())
        val partialTicks = Minecraft.getInstance().deltaFrameTime
        val position = player.getEyePosition(partialTicks)
        val look = player.getViewVector(partialTicks)
        if (Minecraft.getInstance().hitResult != null && Minecraft.getInstance().hitResult!!.type != HitResult.Type.MISS) {
            distance = Minecraft.getInstance().hitResult!!.location.distanceTo(position)
        }
        val result = getEntityItem(player, position, position.add(look.x * distance, look.y * distance, look.z * distance))
        return if (result != null && result.type == HitResult.Type.ENTITY) {
            (result as EntityHitResult).entity as ItemEntity
        } else null
    }

    fun fill(pPoseStack: PoseStack, pMinX: Int, pMinY: Int, pMaxX: Int, pMaxY: Int, pColor: Int) {
        innerFill(pPoseStack.last().pose(), pMinX, pMinY, pMaxX, pMaxY, pColor)
    }

    private fun innerFill(pMatrix: Matrix4f, pMinX: Int, pMinY: Int, pMaxX: Int, pMaxY: Int, pColor: Int) {
        var pMinX = pMinX
        var pMinY = pMinY
        var pMaxX = pMaxX
        var pMaxY = pMaxY
        if (pMinX < pMaxX) {
            val i = pMinX
            pMinX = pMaxX
            pMaxX = i
        }
        if (pMinY < pMaxY) {
            val j = pMinY
            pMinY = pMaxY
            pMaxY = j
        }
        val f3 = (pColor shr 24 and 255).toFloat() / 255.0f
        val f = (pColor shr 16 and 255).toFloat() / 255.0f
        val f1 = (pColor shr 8 and 255).toFloat() / 255.0f
        val f2 = (pColor and 255).toFloat() / 255.0f
        val bufferbuilder = Tesselator.getInstance().builder
        RenderSystem.enableBlend()
        RenderSystem.disableTexture()
        RenderSystem.defaultBlendFunc()
        RenderSystem.setShader { GameRenderer.getPositionColorShader() }
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR)
        bufferbuilder.vertex(pMatrix, pMinX.toFloat(), pMaxY.toFloat(), 0.0f).color(f, f1, f2, 0.25f).endVertex()
        bufferbuilder.vertex(pMatrix, pMaxX.toFloat(), pMaxY.toFloat(), 0.0f).color(f, f1, f2, 0.25f).endVertex()
        bufferbuilder.vertex(pMatrix, pMaxX.toFloat(), pMinY.toFloat(), 0.0f).color(f, f1, f2, 0.25f).endVertex()
        bufferbuilder.vertex(pMatrix, pMinX.toFloat(), pMinY.toFloat(), 0.0f).color(f, f1, f2, 0.25f).endVertex()
        BufferUploader.drawWithShader(bufferbuilder.end())
        RenderSystem.enableTexture()
        RenderSystem.disableBlend()
    }
}

private fun ItemEntity.distanceTo(position: Vec3?): Float {
    val x = position!!.x - this.x
    val y = position.y - this.y
    val z = position.z - this.z
    return MathHelper.sqrt(x * x + y * y + z * z)
}
