package potatocult.mobsofmobs.client.entity.render;


import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.ZombieModel;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import potatocult.mobsofmobs.client.entity.model.PyromancerModel;
import potatocult.mobsofmobs.core.MobsOfMobs;
import potatocult.mobsofmobs.entities.boss.PyromancerEntity;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractPyromancerRenderer<T extends PyromancerEntity, M extends PyromancerModel<T>> extends BipedRenderer<T, M> {
    private static final ResourceLocation field_217771_a = new ResourceLocation(MobsOfMobs.MODID,
            "textures/entity/pyromancer.png");

    protected AbstractPyromancerRenderer(EntityRendererManager p_i50974_1_, M p_i50974_2_, M p_i50974_3_, M p_i50974_4_) {
        super(p_i50974_1_, p_i50974_2_, 0.5F);
        this.addLayer(new BipedArmorLayer<>(this, p_i50974_3_, p_i50974_4_));
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(PyromancerEntity entity) {
        return field_217771_a;
    }

    protected void applyRotations(T entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }
}