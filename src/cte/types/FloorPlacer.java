package cte.types;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;

public class FloorPlacer extends Block {
    public Floor floor = Blocks.metalFloor.asFloor();
    public TextureRegion drill;
    public float consumesTime = 240;

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
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region, drill};
    }

    public class FloorPlacerBuild extends Building {
        public float t;
        @Override
        public void updateTile() {
            super.updateTile();

            Mathf.randomSeed((long) (t + x - y));
            if(Mathf.chance(0.03f)){
                Mathf.randomSeed((long) (t + x - y + 12));
                Fx.smokeCloud.at(x, y, Pal.gray.mulA(Mathf.random(0.8f, 1.5f)));
            }
            if(t >= consumesTime){
                tile.setFloorNet(floor);
                Fx.placeBlock.at(x, y);
                tile.removeNet();
            }
            t++;
        }

        @Override
        public void draw() {
            super.draw();
            Draw.rect(drill, x, y, 10 * Math.max((t - 10) * Math.min(t / 60, 1), 0));
        }
    }
}
