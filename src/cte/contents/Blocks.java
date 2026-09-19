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
            poolBuilder,
            wallBlaster,
            floorPlacer, coreZonePlacer, rotatableFloorPlacer, rotatableContinuableFloorPlacer,
            oreBuilder
    ;

    public static void load(){
        poolBuilder = new PoolBuilder("pool-builder"){{
            requirements(Category.liquid, with(Items.graphite, 50, Items.silicon, 2, Items.copper, 10));
            size = 1;
            liquidCapacity = 10;
        }};
        wallBlaster = new WallBlaster("wall-blaster"){{
            setItem(Items.blastCompound, b -> Mathf.floor((float) b.items.get(Items.blastCompound) / 2));
            itemCapacity = 45;
            max = Mathf.floor((float) itemCapacity / 2);
            size = 1;
            health = 60;
            requirements(Category.effect, ItemStack.with(Items.copper, 80, Items.lead, 50));
        }};
        floorPlacer = new FloorPlacer("floor-placer"){{
            size = 1;
            health = 60;
            requirements(Category.effect, ItemStack.with(Items.copper, 130, Items.lead, 20));
            recordOverlay = true;
        }};
        coreZonePlacer = new FloorPlacer("core-zone-placer") {{
            size = 1;
            health = 50;
            requirements(Category.effect, with(Items.copper, 80, Items.lead, 60, Items.silicon, 20));
            floor = mindustry.content.Blocks.coreZone.asFloor();
            consumesTime = consumesTime * 2.5f;
        }};
        oreBuilder = new OreBuilder("ore-builder"){{
            requirements(Category.crafting, with(Items.graphite, 80, Items.silicon, 10, Items.copper, 15));
            size = 1;
            itemCapacity = 20;
        }};
        rotatableFloorPlacer = new RotatableFloorPlacer("rotatable-floor-placer"){{
            size = 1;
            health = 60;
            requirements(Category.effect, ItemStack.with(Items.copper, 130, Items.lead, 20));
        }};
        rotatableContinuableFloorPlacer = new RotatableContinuableFloorPlacer("rotatable-continuable-floor-placer"){{
            size = 1;
            health = 60;
            consumesTime = 60;
            requirements(Category.effect, ItemStack.with(Items.copper, 180, Items.lead, 80));
        }};
    }
}
