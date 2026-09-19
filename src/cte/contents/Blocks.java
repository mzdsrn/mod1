package cte.contents;

import arc.math.Mathf;
import cte.types.*;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;

import static mindustry.type.ItemStack.with;

public class Blocks {
    public static Block
            block,
            block2,
            block3, block4, block6, block7,
            block5
    ;

    public static void load(){
        block = new PoolBuilder("pool-builder"){{
            requirements(Category.liquid, with(Items.graphite, 50, Items.silicon, 2, Items.copper, 10));
            size = 1;
            liquidCapacity = 10;
        }};
        block2 = new WallBlaster("wall-blaster"){{
            setItem(Items.blastCompound, b -> Mathf.floor((float) b.items.get(Items.blastCompound) / 2));
            itemCapacity = 45;
            max = Mathf.floor((float) itemCapacity / 2);
            size = 1;
            health = 60;
            requirements(Category.effect, ItemStack.with(Items.copper, 80, Items.lead, 50));
        }};
        block3 = new FloorPlacer("floor-placer"){{
            size = 1;
            health = 60;
            requirements(Category.effect, ItemStack.with(Items.copper, 130, Items.lead, 20));
            recordOverlay = true;
        }};
        block4 = new FloorPlacer("core-zone-placer") {{
            size = 1;
            health = 50;
            requirements(Category.effect, with(Items.copper, 80, Items.lead, 60, Items.silicon, 20));
            floor = mindustry.content.Blocks.coreZone.asFloor();
            consumesTime = consumesTime * 2.5f;
        }};
        block5 = new OreBuilder("ore-builder"){{
            requirements(Category.crafting, with(Items.graphite, 80, Items.silicon, 10, Items.copper, 15));
            size = 1;
            itemCapacity = 20;
        }};
        block6 = new RotatableFloorPlacer("rotatable-floor-placer"){{
            size = 1;
            health = 60;
            requirements(Category.effect, ItemStack.with(Items.copper, 130, Items.lead, 20));
        }};
        block7 = new RotatableContinuableFloorPlacer("rotatable-continuable-floor-placer"){{
            size = 1;
            health = 60;
            consumesTime = 60;
            requirements(Category.effect, ItemStack.with(Items.copper, 180, Items.lead, 80));
        }};
    }
}
