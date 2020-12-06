/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package line98;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 *
 * @author prdox
 */
public class Piece extends StackPane{
//    private PieceType type;
    
    public Piece(int x, int y) {
        try {
            relocate(x * Line98.TILE_SIZE, y * Line98.TILE_SIZE);
            FileInputStream inputstream =
                    new FileInputStream("./src/asset/20160713080812!GO_Poké_Ball.png");
            Image image = new Image(inputstream);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(30);
            imageView.setFitHeight(30);
            imageView.setPreserveRatio(true);
            getChildren().addAll(imageView);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Piece.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
