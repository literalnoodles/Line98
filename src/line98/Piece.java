/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package line98;


import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author prdox
 */
public class Piece extends StackPane{

    private ColorType cType;
    private PieceType pType;
    
    enum ColorType {
        YELLOW,
        PINK,
        RED,
        GREEN,
        AQUA,
        BROWN,
        BLUE
    }
    
    enum PieceType {
        SEED,
        FULL,
        DEAD
    }
    
    private Color getColorFromType() {
        switch(getColorType()) {
            case YELLOW:
                return Color.YELLOW;
            case PINK:
                return Color.PINK;
            case RED:
                return Color.RED;
            case GREEN:
                return Color.GREEN;
            case AQUA:
                return Color.AQUAMARINE;
            case BROWN:
                return Color.BROWN;
            case BLUE:
                return Color.BLUE;
            default:
                return Color.YELLOW;
        }
    }
    
    public ColorType getColorType() {
        return this.cType;
    }
    
    public PieceType getPieceType() {
        return this.pType;
    } 
    
    public Piece(PieceType pType, ColorType cType, int x, int y) {
        this.cType = cType;
        this.pType = pType;
        int tileSize = Line98.TILE_SIZE;
        relocate(x * tileSize, y * tileSize);
        
        double wSize, hSize, shadow = 0;
        
        if (pType == PieceType.FULL) {
            wSize = 0.3125;
            hSize = 0.26;
            shadow = 0.07;
        }
        else {
            wSize = 0.12;
            hSize = 0.1;
            shadow = 0.03;
        }
        
        if (pType != PieceType.DEAD) {
            Ellipse bg = new Ellipse(tileSize * wSize, tileSize * hSize);
            bg.setFill(Color.BLACK);
            bg.setStroke(Color.BLACK);
            bg.setStrokeWidth(tileSize * 0.03);
            bg.setTranslateX((tileSize - tileSize * wSize * 2) / 2);
            bg.setTranslateY((tileSize - tileSize * hSize * 2) / 2 + tileSize * shadow);

            Ellipse ellipse = new Ellipse(tileSize * wSize, tileSize * hSize);
            ellipse.setFill(getColorFromType());
            ellipse.setStroke(Color.BLACK);
            ellipse.setStrokeWidth(tileSize * 0.03);
            ellipse.setTranslateX((tileSize - tileSize * wSize * 2) / 2);
            ellipse.setTranslateY((tileSize - tileSize * hSize * 2) / 2);

            getChildren().addAll(bg, ellipse);
        } else {
            Text text = new Text();
            text.setText("x");
            text.setFont(Font.font("", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            text.setTranslateX(23);
            text.setTranslateY(4);
            getChildren().addAll(text);
        }
    }
}
