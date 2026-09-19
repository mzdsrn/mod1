package cte.types;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.util.Time;
import cte.JavaMod;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.meta.Stat;

public class FloorPlacer extends Block {
    public Floor floor = Blocks.metalFloor.asFloor();
    public TextureRegion drill;
    public TextureRegion sdrill;
    public TextureRegion node;
    // How long does it need to replace the floor.
    public float consumesTime = 240;
    // Whether keep the overlay.
    public boolean recordOverlay;

    public FloorPlacer(String name) {
        super(name);
        update = true;
        hasShadow = false;
        placeableLiquid = true;
        canOverdrive = false;
    }

    @Override
    public void load() {
        super.load();
        drill = Core.atlas.find(name + "-drill");
        sdrill = Core.atlas.find(name + "-sdrill");
        node = Core.atlas.find(name + "-drill-node");
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region, drill, node};
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(JavaMod.consumesTime, consumesTime);
        stats.add(Stat.output, t ->{
            t.row();
            t.image(floor.uiIcon).size(32);
        });
    }

    public class FloorPlacerBuild extends Building {
        public float t;
        public Block ore;
        @Override
        public void updateTile() {
            super.updateTile();
            if(t >= consumesTime){
                if(recordOverlay) ore = tile.overlay();
                tile.setFloorNet(floor);
                Fx.placeBlock.at(x, y);
                tile.removeNet();
                if(recordOverlay) tile.setOverlayNet(ore);
            }
            t += Time.delta;
        }

        @Override
        public void draw() {
            super.draw();
            Draw.rect(sdrill, x, y, -2 * Math.max((t - 10) * Math.min(t / 60, 1), 0));
            Draw.rect(drill, x, y, 10 * Math.max((t - 10) * Math.min(t / 60, 1), 0));
            Draw.rect(node, x, y, 0);
        }
    }
}
