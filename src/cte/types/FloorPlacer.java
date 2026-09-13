package cte.types;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.Tile;
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
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        return true;
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region, drill};
    }

    public class FloorPlacerBuild extends Building {
        public float timer;
        @Override
        public void updateTile() {
            super.updateTile();

            Mathf.randomSeed((long) (timer + x - y));
            if(Mathf.chance(0.01f)){
                Fx.smoke.at(x, y);
            }
            if(timer >= consumesTime){
                tile.setFloorNet(floor);
                tile.removeNet();
            }
            timer++;
        }

        @Override
        public void draw() {
            super.draw();
            Draw.rect(drill, x, y, Math.max((timer - 10) * Math.min(60 / timer, 1), 0));
        }
    }
}
