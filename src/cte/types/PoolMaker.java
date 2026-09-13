package cte.types;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.struct.ObjectMap;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.content.Liquids;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Sounds;
import mindustry.graphics.Drawf;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.liquid.LiquidBlock;

public class PoolMaker extends LiquidBlock {
    public static ObjectMap<Liquid, Block> liquidToPool = null;
    public TextureRegion drill;
    public TextureRegion liquid;
    public TextureRegion bottom;
    public TextureRegion[] icon;

    public PoolMaker(String name) {
        super(name);
        if(liquidToPool == null){
            liquidToPool = new ObjectMap<>();
            for(Liquid l : Vars.content.liquids()){
                for(Block b : Vars.content.blocks()){
                    if(b.isFloor() && b.asFloor() != null && ((Floor)b).liquidDrop == l){
                        liquidToPool.put(l, b);
                        liquidFilter[l.id] = true;
                    }
                }
            }
            liquidToPool.put(Liquids.water, Vars.content.block("water"));
        }
        canOverdrive = false;
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

    public class PoolMakerBuild extends LiquidBuild{
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
                        Fx.flakExplosion.at(x, y);
                        Call.setTile(tile, Blocks.air, Team.derelict, 0);
                        Sounds.blockExplode1.at(x, y);
                    }
                }
                went++;
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
