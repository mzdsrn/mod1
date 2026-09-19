package cte;

import arc.util.*;
import cte.contents.Blocks;
import cte.contents.Units;
import mindustry.mod.*;
import mindustry.world.meta.BuildVisibility;

public class JavaMod extends Mod{
    public static String name = "changing-the-environment";

    public JavaMod(){
        Log.info("Loading...");
    }

    @Override
    public void loadContent(){
        Units.load();
        Blocks.load();
        mindustry.content.Blocks.coreShard.buildVisibility = BuildVisibility.shown;
    }
}
