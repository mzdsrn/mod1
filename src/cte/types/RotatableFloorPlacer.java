package cte.types;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

public class RotatableFloorPlacer extends Block {
    public Floor floor = Blocks.metalFloor.asFloor();
    public TextureRegion arrow;
    // How long does it need to replace the floor.
    public float consumesTime = 30;

    public RotatableFloorPlacer(String name) {
        super(name);
        update = true;
        canOverdrive = false;
        rotate = true;
        rotateDraw = false;
    }

    @Override
    public void load() {
        super.load();
        arrow = Core.atlas.find(name + "-arrow");
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region, arrow};
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);
        float l = Vars.tilesize;
        float r = rotation * 90;
        float px = x * Vars.tilesize + Mathf.cosDeg(r) * l;
        float py = y * Vars.tilesize + Mathf.sinDeg(r) * l;
        Drawf.dashSquare(Pal.placing, px, py, size * Vars.tilesize);
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        float l = Vars.tilesize;
        float r = rotation * 90;
        float px = tile.drawx() + Mathf.cosDeg(r) * l;
        float py = tile.drawy() + Mathf.sinDeg(r) * l;
        Tile t = Vars.world.tileWorld(px, py);
        return t.floor().isLiquid || t.floor().isDeep() || t.floor().solid;
    }

    public class RotatableFloorPlacerBuild extends Building {
        public float t;
        public boolean finish;
        @Override
        public void updateTile() {
            super.updateTile();
            if(finish) return;
            float l = Vars.tilesize;
            float r = rotation * 90;
            float px = x + Mathf.cosDeg(r) * l;
            float py = y + Mathf.sinDeg(r) * l;
            if(t >= consumesTime){
                Vars.world.tileWorld(px, py).setFloorNet(floor);
                Fx.placeBlock.at(px, py);
                placed(px, py);
                finish = true;
            }
            t += Time.delta;
        }

        @Override
        public void draw() {
            super.draw();
            if(finish) return;
            Draw.rect(arrow, x, y, drawrot());
            float l = Vars.tilesize * (t / consumesTime);
            float r = rotation * 90;
            float px = x + Mathf.cosDeg(r) * l;
            float py = y + Mathf.sinDeg(r) * l;
            Drawf.dashSquare(Pal.placing, px, py, size * Vars.tilesize);
        }

        @Override
        public float drawrot() {
            return rotation * 90;
        }

        public void placed(float px, float py){}
    }
}
