/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package line98;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author prdox
 */
public class Tile extends Rectangle{
    public Tile(int x, int y) {
        setWidth(Line98.TILE_SIZE);
        setHeight(Line98.TILE_SIZE);
        relocate(x * Line98.TILE_SIZE, y * Line98.TILE_SIZE);
        setFill(Color.WHITESMOKE);
        setStroke(Color.BLACK);
    }
    
    public void setActive() {
        setFill(Color.ANTIQUEWHITE);
    }
    
    public void setUnactive() {
        setFill(Color.WHITESMOKE);
    }
}
