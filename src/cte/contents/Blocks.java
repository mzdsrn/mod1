package cte.contents;

import cte.types.FloorPlacer;
import cte.types.PoolMaker;
import cte.types.WallBlaster;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;

import static mindustry.type.ItemStack.with;

public class Blocks {
    public static Block block, block2, block3;
    public static void load(){
        block = new PoolMaker("pool-maker"){{
            requirements(Category.liquid, with(Items.graphite, 50, Items.silicon, 2, Items.copper, 10));
            size = 1;
            liquidCapacity = 10;
        }};
        block2 = new WallBlaster("wall-blaster"){{
            setItem(Items.blastCompound, b ->b.items.get(Items.blastCompound));
            itemCapacity = 45;
            max = itemCapacity;
            size = 1;
            health = 60;
            requirements(Category.effect, ItemStack.with(Items.copper, 80, Items.lead, 50));
        }};
        block3 = new FloorPlacer("floor-placer"){{
            size = 1;
            health = 10;
            requirements(Category.effect, ItemStack.with(Items.copper, 130, Items.lead, 20));
        }};
    }
}
