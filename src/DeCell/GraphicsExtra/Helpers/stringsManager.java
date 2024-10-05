/*
By Tartiflette
 */
package DeCell.GraphicsExtra.Helpers;

import com.fs.starfarer.api.Global;

public class stringsManager {
    private static final String ML="faesector";
    
    public static String txt(String id){
        return Global.getSettings().getString(ML, id);
    }
}