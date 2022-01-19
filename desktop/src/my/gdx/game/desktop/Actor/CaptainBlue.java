package my.gdx.game.desktop.Actor;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

import my.gdx.game.desktop.Utils.Asset;
import my.gdx.game.desktop.Utils.Point;
import my.gdx.game.desktop.Utils.Size;

public class CaptainBlue extends Captain {
    public static String id = "003-";
    public CaptainBlue(String textureName,Point Position, Size size,String id,int price,String attribute,double bonus) {
        super(textureName, Position,size,id,price,attribute,bonus);
    }
    public CaptainBlue(String textureName,String id,int price,String attribute,double bonus) {
        super(textureName,id,price,attribute,bonus);
    }
    @Override
    public Animation<AtlasRegion> getAnimate() {
        // TODO Auto-generated method stub
        return Asset.captainBlueAnim;
    }
}
