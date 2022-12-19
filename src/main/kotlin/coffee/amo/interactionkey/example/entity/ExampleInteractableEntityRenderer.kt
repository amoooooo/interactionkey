package coffee.amo.interactionkey.example.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Vector3f
import net.minecraft.client.model.Model
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.ModelPart.Cube
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import java.util.function.Function

class ExampleInteractableEntityRenderer(pContext: EntityRendererProvider.Context) : EntityRenderer<ExampleInteractableEntity>(pContext) {
    override fun getTextureLocation(pEntity: ExampleInteractableEntity): ResourceLocation {
        return ResourceLocation("minecraft:textures/block/stone.png")
    }

    private val model = ExampleEntityModel()
    override fun render(pEntity: ExampleInteractableEntity, pEntityYaw: Float, pPartialTick: Float, pPoseStack: PoseStack, pBuffer: MultiBufferSource, pPackedLight: Int) {
        pPoseStack.pushPose()
        pPoseStack.scale(0.5f, 0.5f, 0.5f)
        model.renderToBuffer(pPoseStack, pBuffer.getBuffer(lightning(getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 0.5f)
        pPoseStack.translate(0.05, 0.05, 0.05)
        pPoseStack.scale(0.9f, 0.9f, 0.9f)
        model.renderToBuffer(pPoseStack, pBuffer.getBuffer(lightning(getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 0.5f)
        pPoseStack.popPose()
    }

    class ExampleEntityModel : Model(Function { pLocation: ResourceLocation -> lightning(pLocation) }) {
        private val core: ModelPart

        init {
            val cube = listOf(
                    Cube(0, 0, 4f, 4f, 4f, 8f, 8f, 8f, 0f, 0f, 0f, false, 1f, 1f)
            )
            core = ModelPart(cube, mapOf())
        }

        override fun renderToBuffer(pPoseStack: PoseStack, pBuffer: VertexConsumer, pPackedLight: Int, pPackedOverlay: Int, pRed: Float, pGreen: Float, pBlue: Float, pAlpha: Float) {
            pPoseStack.pushPose()
            pPoseStack.mulPose(Vector3f.ZP.rotationDegrees(45f))
            pPoseStack.mulPose(Vector3f.XP.rotationDegrees(45f))
            core.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, pRed, pGreen, pBlue, pAlpha)
            pPoseStack.popPose()
        }
    }

    companion object {
        fun lightning(pLocation: ResourceLocation): RenderType {
            return RenderType.lightning()
        }
    }
}