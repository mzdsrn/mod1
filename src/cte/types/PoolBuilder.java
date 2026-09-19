package cte.types;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.struct.ObjectMap;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.Liquids;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.liquid.LiquidBlock;
import mindustry.world.meta.Stat;

public class PoolBuilder extends LiquidBlock {
    public static ObjectMap<Liquid, Block> liquidToPool = null;
    public TextureRegion drill;
    public TextureRegion liquid;
    public TextureRegion bottom;
    public TextureRegion[] icon;

    public PoolBuilder(String name) {
        super(name);
        if(liquidToPool == null){
            liquidToPool = new ObjectMap<>();
            for(Liquid l : Vars.content.liquids()){
                for(Block b : Vars.content.blocks()){
                    if(b.isFloor() && b.asFloor() != null && ((Floor)b).liquidDrop == l){
                        liquidToPool.put(l, b);
                    }
                }
            }
            liquidToPool.put(Liquids.water, Vars.content.block("water"));
        }
        canOverdrive = false;
    }

    @Override
    public void init() {
        super.init();
        for(Liquid l : liquidToPool.keys()){
            liquidFilter[l.id] = true;
        }
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.output, table -> {
            table.row();
            for(Liquid l : liquidToPool.keys()){
                table.image(l.uiIcon).size(32).marginLeft(10);
                table.image(liquidToPool.get(l).uiIcon).size(32).marginRight(-10);
                table.row();
            }
        });
    }

    @Override
    public void load(){
        super.load();
        drill = Core.atlas.find(name + "-drill");
        liquid = Core.atlas.find(name + "-liquid");
        bottom = Core.atlas.find(name + "-bottom");
        icon = new TextureRegion[]{bottom, region, drill};
    }

    @Override
    public TextureRegion[] icons() {
        return icon;
    }

    public class PoolBuilderBuild extends LiquidBuild{
        public float went = 0;
        public float rotation = 0;
        public float stress = 0;
        @Override
        public void updateTile() {
            super.updateTile();
            if(liquids.currentAmount() >= 10){
                if(went > 60) {
                    Liquid l = liquids.current();
                    Block b = liquidToPool.get(l);
                    if (b != null) {
                        Call.setFloor(tile, b, Blocks.air);
                        Call.effect(Fx.flakExplosion, x, y, 0, Pal.gray);
                        Call.setTile(tile, Blocks.air, Team.derelict, 0);
                        Call.soundAt(Sounds.blockExplode1, x, y, 1, 1);
                    }
                }
                went += Time.delta;
                stress += (float)(360 / 60);
            }else{
                went = 0;
            }
            stress = Math.max(stress - 0.01f, 0);
            rotation += stress;
        }

        @Override
        public void draw() {
            Draw.rect(bottom, tile.worldx(), tile.worldy());
            if(liquids.currentAmount() > 0.001f){
                Drawf.liquid(liquid, tile.worldx(), tile.worldy(), liquids.currentAmount() / liquidCapacity * (1.1f - stress), liquids.current().color);
            }
            Draw.rect(region, tile.worldx(), tile.worldy());
            Draw.rect(drill, tile.worldx(), tile.worldy(), rotation);
        }

        @Override
        public boolean acceptLiquid(Building source, Liquid liquid){
            return (liquids.current() == liquid || liquids.currentAmount() < 10);
        }
    }
}
