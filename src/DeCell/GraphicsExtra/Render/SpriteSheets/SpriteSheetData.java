package DeCell.GraphicsExtra.Render.SpriteSheets;

import DeCell.GraphicsExtra.Render.ModernOpengl.Shader;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.graphics.SpriteAPI;
import org.json.JSONException;
import org.json.JSONObject;
import org.lwjgl.util.vector.Vector2f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class SpriteSheetData {
    //
    public Vector2f size = new Vector2f(64, 64);
    public Vector2f nextPOT = new Vector2f(64, 64);

    public SpriteSheetData SetShape(Vector2f shape) {
        this.size = shape;
        int xPOT = 1;
        while (xPOT < size.x)
            xPOT *= 2;
        int yPOT = 1;
        while (yPOT < size.y)
            yPOT *= 2;
        if (xPOT > yPOT)
            nextPOT = new Vector2f(xPOT, xPOT);
        else if (yPOT > xPOT)
            nextPOT = new Vector2f(yPOT, yPOT);
        else
            nextPOT = new Vector2f(xPOT, yPOT);
        return this;
    }

    //
    public SpriteAPI tex = Global.getSettings().getSprite("illustrations", "teahouse");

    public SpriteSheetData SetTexture(SpriteAPI tex) {
        this.tex = tex;
        this.size = new Vector2f(tex.getWidth(), tex.getHeight());
        return this;
    }

    //
    public List<SpriteData> spriteDatas = new ArrayList<>();

    //
    public SpriteSheetData UpdateShader(Shader shader, String textureName) {
        shader.bind();

        SpriteData sprite = new SpriteData();

        for (SpriteData spriteData : spriteDatas) {
            if (Objects.equals(spriteData.name, textureName)) {
                sprite = spriteData;
                break;
            }
        }

        shader.SetVector2f("size", this.size)
                .SetVector2f("nextPOTSize", nextPOT)
                .SetVector2f("targetLoc", sprite.loc)
                .SetVector2f("targetSize", sprite.size)
                .SetTexture("sheet", tex, tex.getTextureId());

        shader.unbind();

        return this;
    }

    //
    public static SpriteSheetData GenerateFromJSON(String path, SpriteAPI tex) throws JSONException, IOException {

        JSONObject jsonObject = Global.getSettings().getMergedJSON(path);

        JSONObject list = jsonObject.getJSONObject("frames");

        SpriteSheetData data = new SpriteSheetData().SetTexture(tex);
        List<SpriteData> spriteDatas = new ArrayList<>();
        Iterator<String> keys = list.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject sprite = list.getJSONObject(key);

            Vector2f loc = new Vector2f(sprite.getJSONObject("frame").getInt("x"), sprite.getJSONObject("frame").getInt("y"));
            Vector2f size = new Vector2f(sprite.getJSONObject("frame").getInt("w"), sprite.getJSONObject("frame").getInt("h"));


            spriteDatas.add(new SpriteData().SetLoc(loc).SetSize(size).SetName(key));
        }
        data.spriteDatas = spriteDatas;


//        JSONObject shape = jsonObject.getJSONObject("meta").getJSONObject("size");
//        data.SetShape(new Vector2f(shape.getInt("w"), shape.getInt("h")));

        data.SetShape(new Vector2f(tex.getWidth(), tex.getHeight()));

        return data;

    }

}
