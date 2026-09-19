package cte.types;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.struct.ObjectMap;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Sounds;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.meta.Stat;

// Same with PoolBuilder
public class OreBuilder extends Block {
    public static ObjectMap<Item, Block> itemToOre = null;
    public TextureRegion bottom;
    public TextureRegion[] icon;

    public OreBuilder(String name) {
        super(name);
        if(itemToOre == null){
            itemToOre = new ObjectMap<>();
            for(Item i : Vars.content.items()){
                for(Block b : Vars.content.blocks()){
                    if(b instanceof OreBlock && !((OreBlock) b).wallOre && b.itemDrop == i){
                        itemToOre.put(i, b);
                    }
                }
            }
        }
        hasItems = true;
        canOverdrive = false;
        update = true;
        solid = true;
    }

    @Override
    public void init() {
        super.init();
        for(Item i : itemToOre.keys()){
            itemFilter[i.id] = true;
        }
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.output, table -> {
            table.row();
            for(Item i : itemToOre.keys()){
                table.image(i.uiIcon).size(38).marginLeft(10);
                table.image(itemToOre.get(i).uiIcon).size(38).marginRight(-10);
                table.row();
            }
        });
    }

    @Override
    public void load(){
        super.load();
        bottom = Core.atlas.find(name + "-bottom");
        icon = new TextureRegion[]{bottom, region};
    }

    @Override
    public TextureRegion[] icons() {
        return icon;
    }

    public class OreBuilderBuild extends Building{
        public float progress = 0;
        public Floor recorded;
        @Override
        public void updateTile() {
            super.updateTile();
            if(items.any() && items.get(items.first()) >= itemCapacity){
                if(progress > items.first().cost * 10 + items.first().hardness * 20) {
                    recorded = tile.floor();
                    Item i = items.first();
                    Block b = itemToOre.get(i);
                    if (b != null) {
                        Call.setFloor(tile, recorded, itemToOre.get(i));
                        Fx.flakExplosion.at(x, y);
                        Call.setTile(tile, Blocks.air, Team.derelict, 0);
                        Sounds.blockExplode1.at(x, y);
                    }
                }
                progress += Time.delta;
            }else{
                progress = 0;
            }
        }

        @Override
        public void draw() {
            Draw.rect(bottom, tile.worldx(), tile.worldy());
            Draw.rect(region, tile.worldx(), tile.worldy());
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return items.empty() || (item.equals(items.first()));
        }
    }
}
