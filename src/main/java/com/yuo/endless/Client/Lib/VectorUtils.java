package com.yuo.endless.Client.Lib;

/**
 * Created by covers1624 on 4/10/2016.
 */
public class VectorUtils {

    /**
     * Calculates the int direction a normal is facing.
     *
     * @param normal The normal to calculate from.
     * @return The direction the normal is facing.
     */
    public static int findSide(Vector3 normal) {
        if (normal.y <= -0.99) {
            return 0;
        }
        if (normal.y >= 0.99) {
            return 1;
        }
        if (normal.z <= -0.99) {
            return 2;
        }
        if (normal.z >= 0.99) {
            return 3;
        }
        if (normal.x <= -0.99) {
            return 4;
        }
        if (normal.x >= 0.99) {
            return 5;
        }
        return -1;
    }

}
