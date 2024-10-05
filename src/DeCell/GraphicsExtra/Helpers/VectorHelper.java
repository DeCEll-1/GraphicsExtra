package DeCell.GraphicsExtra.Helpers;

import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class VectorHelper {

    /**
     * returns the middle point of a list of vectors </br>
     * <i><b>if 2 bounds are located in the same place it will return NaN and wont work</b></i>
     *
     * @param list list of the vectors to get the center of, only for vector2f
     * @return if the list is 0 will return null, otherwise returns the middle point
     */
    public static Vector2f GetTheMiddlePointInAVector2fList(List<Vector2f> list) {

        if (list.size() == 0) return null;

        Vector2f totalVectorData = null;

        for (Vector2f location : list) {
//gets the middle point
            if (totalVectorData == null) {
                totalVectorData = location;
            } else {
                totalVectorData.x = (totalVectorData.x + location.x);
                totalVectorData.y = (totalVectorData.y + location.y);
            }

        }

        Vector2f realVector = new Vector2f(totalVectorData.x / list.size(), totalVectorData.y / list.size());

        return realVector;
    }

    public static Vector2f AvarageVectors(Vector2f firstVector, Vector2f secondVector) {

        Vector2f avaragedVector = new Vector2f();

        avaragedVector.x = (firstVector.x + secondVector.x) / 2f;
        avaragedVector.y = (firstVector.y + secondVector.y) / 2f;

        return avaragedVector;

    }


    public static Vector2f aproachVectors(Vector2f aproachant, Vector2f target, float aproachAmount) {

        if (aproachAmount == 0f) return target;

        Vector2f aproachedVector = new Vector2f(

                MathUtils.getPointOnCircumference(
                        aproachant,
                        aproachAmount,
                        VectorUtils.getAngle(aproachant, target)
                )

        );

        return aproachedVector;
    }

    //in square
    public static boolean isNear(Vector2f is, Vector2f near, float radius) {

        if (
                toPositive(is.x - near.x) > radius || toPositive(is.y - near.y) > radius
        ) {
            return false;
        }
        return true;

    }

    public static float toPositive(float num) {
        return (num > 0) ? num : -num;
    }
}
