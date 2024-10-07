package DeCell.GraphicsExtra.Helpers;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.lazywizard.lazylib.MathUtils;
import org.lwjgl.util.vector.Vector2f;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Helper {


    /**
     * did this because i didnt knew beam.getRayEndPrevFrame() gets the end point of the beam
     */
    public static Vector2f EndOfBeamGiver(BeamAPI beam) {
        return MathUtils.getPointOnCircumference(beam.getWeapon().getLocation(), beam.getLength(), beam.getWeapon().getCurrAngle());
    }


    /**
     * keeps the angle between 0 and 360<br/>
     * like 340 + 60 = 400 but this will make it 40 <br/>
     *
     * @param angle     base angle
     * @param PlusMinus how much you want to increase/decrease the angle
     * @return if the proccess completed successfully returns true, false othervise. so instead of crashing it just, doesnt work
     */
    public static float GetPlusMinusAngle(float angle, float PlusMinus) {
        try {

            if (angle + PlusMinus < 360 && angle + PlusMinus > 0) return angle + PlusMinus;

            if (angle + PlusMinus > 360) return (angle + PlusMinus) - 360f;

            if (angle + PlusMinus < 0) return (angle + PlusMinus) + 360f;


            return 0f;
        } catch (Exception ex) {
            return 0f;
        }
    }

    public static float lerp(float start, float stop, float amount) {
        return (float) ((start * (1.0 - amount)) + (stop * amount));
    }

    private static Map<String, Vector2f> origins = new HashMap<>();

    public static Vector2f getShipOrigin(ShipAPI ship) throws JSONException, IOException {
        if (ship == null)
            return new Vector2f();
        if (ship.getHullSpec() == null)
            return new Vector2f();
        if (ship.getHullSpec().getShipFilePath() == null || Objects.equals(ship.getHullSpec().getShipFilePath(), ""))
            return new Vector2f();
        if (origins.containsKey(ship.getHullSpec().getShipFilePath())) {
            return new Vector2f(origins.get(ship.getHullSpec().getShipFilePath()));
        } else {

            String shipFilePath = "blablabla/" + ship.getHullSpec().getShipFilePath().replaceAll("\\\\", "/");

            shipFilePath = "data/" + shipFilePath.split("data/")[1];

            JSONArray coords = Global.getSettings().loadJSON(shipFilePath).getJSONArray("center");

            Vector2f center = new Vector2f(Float.parseFloat(coords.get(0).toString()), Float.parseFloat(coords.get(1).toString()));

            origins.put(ship.getHullSpec().getShipFilePath(), new Vector2f(center.x, center.y));

            return getShipOrigin(ship);
        }
    }

    public static Vector2f getShipCenter(ShipAPI ship) throws JSONException, IOException {
        Vector2f center = Helper.getShipOrigin(ship);
        center.set(center.x - ship.getSpriteAPI().getWidth() / 2f, center.y - ship.getSpriteAPI().getHeight() / 2f);
        center.set(ship.getLocation().x + center.x, ship.getLocation().y + center.y);
        return center;
    }

}
