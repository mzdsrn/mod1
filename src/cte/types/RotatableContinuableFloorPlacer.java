package cte.types;

import mindustry.Vars;
import mindustry.content.Fx;

public class RotatableContinuableFloorPlacer extends RotatableFloorPlacer {
    public RotatableContinuableFloorPlacer(String name){
        super(name);
    }
    public class RotatableContinuableFloorPlacerBuild extends RotatableFloorPlacer.RotatableFloorPlacerBuild {
        @Override
        public void placed(float px, float py) {
            super.placed(px, py);
            if(canPlaceOn(Vars.world.tileWorld(px, py), team, rotation) && !Vars.world.tileWorld(px, py).solid()){
                Vars.world.tileWorld(px, py).setBlock(RotatableContinuableFloorPlacer.this, team, rotation);
                tile.removeNet();
                Fx.breakBlock.at(x, y);
            }
        }
    }
}
