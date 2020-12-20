/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package line98;


import javafx.animation.PathTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 *
 * @author prdox
 */
public class Piece extends StackPane{

    public ColorType cType;
    public PieceType pType;
    
    public int x;
    public int y;
    
    private double oldPosX ;
    private double oldPosY ;
    
    private double originPosX ;
    private double originPosY ;
    
    private double offsetPosX;
    private double offsetPosY;
    
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
        int tileSize = Line98.TILE_SIZE;
        this.cType = cType;
        this.pType = pType;
        this.x = x;
        this.y = y;
        originPosX = x * tileSize;
        originPosY = y * tileSize;
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
        calculateOffset();
        
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
            ellipse.setTranslateX(offsetPosX);
            ellipse.setTranslateY(offsetPosY);
            getChildren().addAll(bg, ellipse);
        } else {
            Text text = new Text();
            text.setText("x");
            text.setFont(Font.font("", FontWeight.NORMAL, FontPosture.REGULAR, 30));
            text.setTranslateX(offsetPosX);
            text.setTranslateY(offsetPosY);
            getChildren().addAll(text);
        }
    }
    
    private void calculateOffset() {
        int tileSize = Line98.TILE_SIZE;
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
            offsetPosX = (tileSize - tileSize * wSize * 2) / 2;
            offsetPosY = (tileSize - tileSize * hSize * 2) / 2;
        }
        else {
            offsetPosX = 23;
            offsetPosY = 4;
        }
    }
    
    public void move(int newX, int newY) {
        int tileSize = Line98.TILE_SIZE;
        oldPosX = -offsetPosX + tileSize/2 - originPosX + x * tileSize;
        oldPosY = -offsetPosY + tileSize/2 - originPosY + y * tileSize;
        Path path = new Path();
        path.getElements().add(new MoveTo(oldPosX, oldPosY));
        path.getElements().add(new LineTo(oldPosX + (newX - x)*tileSize, oldPosY + (newY - y)*tileSize));

        PathTransition transition = new PathTransition();
        transition.setPath(path);
        transition.setNode(this);
        transition.setDuration(Duration.seconds(1));
        transition.play();
//        oldX = -offsetPosX + tileSize/2 - originX + newX * tileSize;
//        oldY = -offsetPosY + tileSize/2 - originY + newY * tileSize;
        x = newX;
        y = newY;
    }
}
