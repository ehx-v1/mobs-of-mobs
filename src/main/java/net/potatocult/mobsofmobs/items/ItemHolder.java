package net.potatocult.mobsofmobs.items;

import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;
import net.potatocult.mobsofmobs.core.MobsOfMobs;

import static net.potatocult.mobsofmobs.util.InjectionUtil.Null;

@ObjectHolder(MobsOfMobs.MODID)
public class ItemHolder {
    @ObjectHolder("mithril_ingot")
    public static final Item MITHRIL_INGOT = Null();
    @ObjectHolder("mithril_sword")
    public static final Item MITHRIL_SWORD = Null();
    @ObjectHolder("mithril_pickaxe")
    public static final Item MITHRIL_PICKAXE = Null();
    @ObjectHolder("mithril_shovel")
    public static final Item MITHRIL_SHOVEL = Null();
    @ObjectHolder("mithril_axe")
    public static final Item MITHRIL_AXE = Null();
    @ObjectHolder("penguin_feather")
    public static final Item PENGUIN_FEATHER = Null();
    @ObjectHolder("gold_golem_spawn_egg")
    public static final Item GOLD_GOLEM_SPAWN_EGG = Null();
    @ObjectHolder("penguin_spawn_egg")
    public static final Item PENGUIN_SPAWN_EGG = Null();
    @ObjectHolder("pyromancer_spawn_egg")
    public static final Item PYROMANCER_SPAWN_EGG = Null();
    @ObjectHolder("wight_spawn_egg")
    public static final Item WIGHT_SPAWN_EGG = Null();
    // @ObjectHolder("mithril_hoe")
    // public static final Item MITHRIL_HOE = Null();
    public static Item MITHRIL_SHEARS;
}
