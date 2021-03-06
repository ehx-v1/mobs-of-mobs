package net.potatocult.mobsofmobs.entities.boss;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerBossInfo;
import net.potatocult.mobsofmobs.entities.ai.goal.PyromancerAttackGoal;
import net.potatocult.mobsofmobs.items.ItemHolder;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

public class PyromancerEntity extends MonsterEntity {
    private static final Predicate<LivingEntity> NOT_UNDEAD = (p_213797_0_) -> {
        return p_213797_0_.getCreatureAttribute() != CreatureAttribute.UNDEAD && p_213797_0_.attackable();
    };
    private static final EntityPredicate field_213798_bB = (new EntityPredicate()).setDistance(20.0D).setCustomPredicate(NOT_UNDEAD);
    private static final Predicate<Difficulty> HARD_DIFFICULTY_PREDICATE = (p_213697_0_) -> {
        return p_213697_0_ == Difficulty.HARD;
    };
    private final ServerBossInfo bossInfo = (ServerBossInfo) (new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
    private final BreakDoorGoal breakDoor = new BreakDoorGoal(this, HARD_DIFFICULTY_PREDICATE);
    private boolean isBreakDoorsTaskSet;

    public PyromancerEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
        this.setHealth(this.getMaxHealth());
        this.experienceValue = 30;
    }

    public static boolean spawnPredicate(EntityType<? extends MonsterEntity> type, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        return world.getBlockState(pos.down()).getBlock() == Blocks.NETHERRACK;
    }

    public static boolean spawning(EntityType<PyromancerEntity> type, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(4, new PyromancerEntity.AttackTurtleEggGoal(this, 1.0D, 3));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.applyEntityAI();
    }

    protected void applyEntityAI() {
        this.goalSelector.addGoal(2, new PyromancerAttackGoal(this, 1.25D, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, MobEntity.class, 10, true, false, NOT_UNDEAD));
    }

    protected void updateAITasks() {
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.36F);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(160.0D);
    }

    protected boolean shouldDrown() {
        return true;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_ZOMBIE_STEP;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setEquipmentBasedOnDifficulty(difficultyIn);
        this.setEnchantmentBasedOnDifficulty(difficultyIn);
        return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        if (this.rand.nextInt(30) == 0) {
            ItemStack itemstack = new ItemStack(ItemHolder.MITHRIL_AXE, 1);
            if (this.rand.nextInt(2) == 0) {
                Map<Enchantment, Integer> map = Maps.newHashMap();
                map.put(Enchantments.EFFICIENCY, 5);
                EnchantmentHelper.setEnchantments(map, itemstack);
            }
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack);
            this.inventoryHandsDropChances[EquipmentSlotType.MAINHAND.getSlotIndex()] = 1.0f;
        } else if (this.rand.nextInt(30) == 0) {
            ItemStack itemstack = new ItemStack(ItemHolder.MITHRIL_SHOVEL, 1);
            if (this.rand.nextInt(2) == 0) {
                Map<Enchantment, Integer> map = Maps.newHashMap();
                map.put(Enchantments.EFFICIENCY, 5);
                EnchantmentHelper.setEnchantments(map, itemstack);
            }
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack);
            this.inventoryHandsDropChances[EquipmentSlotType.MAINHAND.getSlotIndex()] = 1.0f;
        } else if (this.rand.nextInt(50) == 0) {
            ItemStack itemstack = new ItemStack(ItemHolder.MITHRIL_PICKAXE, 1);
            if (this.rand.nextInt(2) == 0) {
                Map<Enchantment, Integer> map = Maps.newHashMap();
                map.put(Enchantments.EFFICIENCY, 5);
                map.put(Enchantments.UNBREAKING, 3);
                EnchantmentHelper.setEnchantments(map, itemstack);
            }
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack);
            this.inventoryHandsDropChances[EquipmentSlotType.MAINHAND.getSlotIndex()] = 1.0f;
        } else {
            ItemStack itemstack = new ItemStack(ItemHolder.MITHRIL_SWORD, 1);
            if (this.rand.nextInt(2) == 0) {
                Map<Enchantment, Integer> map = Maps.newHashMap();
                map.put(Enchantments.SHARPNESS, 5);
                EnchantmentHelper.setEnchantments(map, itemstack);
            }
            this.setItemStackToSlot(EquipmentSlotType.MAINHAND, itemstack);
            this.inventoryHandsDropChances[EquipmentSlotType.MAINHAND.getSlotIndex()] = 0.015f;
        }
        if (this.rand.nextInt(100) == 0) {
            this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(ItemHolder.MITHRIL_INGOT, 1));
            this.inventoryHandsDropChances[EquipmentSlotType.OFFHAND.getSlotIndex()] = 1.0f;
        }
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEAD;
    }

    public boolean isNonBoss() {
        return false;
    }

    public boolean isPreventingPlayerRest(PlayerEntity playerIn) {
        return true;
    }

    public void checkDespawn() {
        if (this.world.getDifficulty() == Difficulty.PEACEFUL && this.isDespawnPeaceful()) {
            this.remove();
        } else {
            this.idleTime = 0;
        }
    }

    class AttackTurtleEggGoal extends BreakBlockGoal {
        AttackTurtleEggGoal(CreatureEntity creatureIn, double speed, int yMax) {
            super(Blocks.TURTLE_EGG, creatureIn, speed, yMax);
        }

        public void playBreakingSound(IWorld worldIn, BlockPos pos) {
            worldIn.playSound(null, pos, SoundEvents.ENTITY_ZOMBIE_DESTROY_EGG, SoundCategory.HOSTILE, 0.5F, 0.9F + PyromancerEntity.this.rand.nextFloat() * 0.2F);
        }

        public void playBrokenSound(World worldIn, BlockPos pos) {
            worldIn.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_BREAK, SoundCategory.BLOCKS, 0.7F, 0.9F + worldIn.rand.nextFloat() * 0.2F);
        }

        public double getTargetDistanceSq() {
            return 1.14D;
        }
    }
}
