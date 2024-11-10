package DeCell.GraphicsExtra.Render.SpriteSheets;

import org.lwjgl.util.vector.Vector2f;

public class SpriteData {
    //
    public String name = "";

    public SpriteData SetName(String name) {
        this.name = name;
        return this;
    }

    //
    public Vector2f loc = new Vector2f(0f, 0f);

    public SpriteData SetLoc(Vector2f loc) {
        this.loc = loc;
        return this;
    }

    //
    public Vector2f size = new Vector2f(0f, 0f);

    public SpriteData SetSize(Vector2f size) {
        this.size = size;
        return this;
    }

}
