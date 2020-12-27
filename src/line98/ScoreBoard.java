/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package line98;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 *
 * @author prdox
 */
public class ScoreBoard extends StackPane{
    
    private Text score;
    
    public ScoreBoard() {
        Rectangle board = new Rectangle();
        board.setWidth(Line98.SCORE_W);
        board.setHeight(Line98.H);
        relocate(Line98.X_TILES * Line98.TILE_SIZE, 0);
        board.setFill(Color.ALICEBLUE);
        board.setStroke(Color.BLACK);
        
        Text text = new Text();
        text.setText("Score");
        text.setFont(Font.font("", FontWeight.BOLD, FontPosture.REGULAR, 30));
        text.setTranslateY(-(Line98.TILE_SIZE * Line98.Y_TILES / 2 - 30));
        
        Text text2 = new Text();
        text2.setText("Highscore");
        text2.setFont(Font.font("", FontWeight.BOLD, FontPosture.REGULAR, 30));
        text2.setTranslateY(-(Line98.TILE_SIZE * Line98.Y_TILES / 2 - 150));
        
        score = new Text();
        score.setText(String.valueOf(Line98.totalScore));
        score.setFont(Font.font("", FontWeight.BOLD, FontPosture.REGULAR, 30));
        score.setTranslateY(-(Line98.TILE_SIZE * Line98.Y_TILES / 2 - 70));
        
        Text highScore = new Text();
        highScore.setText(String.valueOf(3000));
        highScore.setFont(Font.font("", FontWeight.BOLD, FontPosture.REGULAR, 30));
        highScore.setTranslateY(-(Line98.TILE_SIZE * Line98.Y_TILES / 2 - 190));
        
        getChildren().addAll(board, text, score, text2, highScore);
    }
    
    public void updateScore() {
        score.setText(String.valueOf(Line98.totalScore));
    }
}
