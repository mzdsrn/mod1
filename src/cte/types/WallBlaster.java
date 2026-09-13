package cte.types;

import arc.Core;
import arc.func.Func;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Log;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.Tile;

public class WallBlaster extends Block {
    public Func<Building, Integer> range;
    public int max;
    public Item accept;
    public TextureRegion bottom;
    public TextureRegion a;

    public WallBlaster(String name) {
        super(name);
        hasItems = true;
        rotate = true;
        rotateDraw = false;
        drawArrow = false;
        sync = true;
        solid = true;
        canOverdrive = false;
    }

    @Override
    public void load() {
        super.load();
        bottom = Core.atlas.find(name + "-bottom");
        a = Core.atlas.find(name + "-a");
    }

    public void setItem(Item i, Func<Building, Integer> f){
        range = f;
        accept = i;
    }
    public void init(){
        super.init();
        itemFilter[accept.id] = true;
    }

    public void explode(float x, float y, Building build){
        Damage.dynamicExplosion(x, y, 0.5f, 0, 0, 15, true, Fx.flakExplosion);
        Damage.damage(x, y, 8, 5 * build.items.get(accept));
        Log.log(Log.LogLevel.none, "exploded!");
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);
        float tx = x * Vars.tilesize;
        float ty = y * Vars.tilesize;
        float length = max * Vars.tilesize;
        float r = rotation * 90;
        float px = tx + Mathf.cosDeg(r) * length;
        float py = ty + Mathf.sinDeg(r) * length;
        Drawf.dashLine(Pal.placing, tx, ty, px, py);
    }

    public class WallBlasterBuild extends Building{
        @Override
        public void draw() {
            Draw.rect(bottom, x, y, 0);
            drawShadow(tile);
            Draw.rect(region, x, y, 0);
            Draw.rect(a, x, y, this.drawrot() - 90);
            Draw.z(Layer.max);
            Draw.alpha(0.5f);
            float tx = x;
            float ty = y;
            float length = items.get(accept) * Vars.tilesize;
            float r = drawrot();
            float px = tx + Mathf.cosDeg(r) * length;
            float py = ty + Mathf.sinDeg(r) * length;
            Drawf.dashLine(Pal.placing, tx, ty, px, py);
        }

        @Override
        public float drawrot() {
            return rotation * 90;
        }

        @Override
        public void drawSelect(){
            super.drawSelect();
            float tx = x;
            float ty = y;
            float length = items.get(accept) * Vars.tilesize;
            float r = drawrot();
            float px = tx + Mathf.cosDeg(r) * length;
            float py = ty + Mathf.sinDeg(r) * length;
            Drawf.dashLine(Pal.placing, tx, ty, px, py);
        }
        public void explode(){
            int a = range.get(this);
            for(int i = 1; i < a; i++){
                float tx = x;
                float ty = y;
                float length = i * Vars.tilesize;
                float r = drawrot();
                float px = tx + Mathf.cosDeg(r) * length;
                float py = ty + Mathf.sinDeg(r) * length;

                Tile tile = Vars.world.tileWorld(px, py);
                if(tile != null && tile.block() != null && tile.build == null){
                    tile.removeNet();
                }
                WallBlaster.this.explode(px, py, this);
            }
            Log.log(Log.LogLevel.none, "removed");
            tile.removeNet();
        }
        @Override
        public void tapped() {
            if (!items.has(accept)) return;

            if (Vars.net.client()) {
                Call.tileTap(Vars.player, tile);
                return;
            }
            explode();
        }
    }
}
