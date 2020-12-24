/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package line98;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author prdox
 */
public class Line98 extends Application {

    public static final int TILE_SIZE = 60;
    public static final int W = 540;
    public static final int H = 540;
    
    public static final int X_TILES = W / TILE_SIZE;
    public static final int Y_TILES = H / TILE_SIZE;
    
    public static Piece activePiece;
    
    private void movePiece(Piece piece, int newX, int newY) {
        pieceArray[piece.x][piece.y] = null;
        grid[piece.x][piece.y].setUnactive();
        
        if (pieceArray[newX][newY] != null) {
            pieceArray[newX][newY].toBack();
        }
        piece.toFront();
//        System.out.printf("Set (%s, %s) to null\n", piece.x, piece.y);
        piece.move(newX, newY);
    }
    
    public static void OnFinishMove(Piece piece, int newX, int newY) {
        Piece oldPiece = pieceArray[newX][newY];
        pieceArray[newX][newY] = piece; // set piece for the new position
        activePiece = null; // reset activePiece
        // if move to seed -> remove the seed
        if (oldPiece != null && oldPiece.pType == Piece.PieceType.SEED) {
            pieceGroup.getChildren().remove(oldPiece);
        }
        
        generateSeed();
    }
    
    private void handleMouseClick(double posX, double posY)
    {
        int x = (int) posX / (TILE_SIZE);
        int y = (int) posY / (TILE_SIZE);
//        System.out.println(x);
//        System.out.println(y);
        
        if (activePiece == null) {
            if (pieceArray[x][y] != null && pieceArray[x][y].pType == Piece.PieceType.FULL) {
                activePiece = pieceArray[x][y];
                grid[x][y].setActive();
            }
            return;
        }
        
        // case select again
        if (x == activePiece.x && y == activePiece.y) {
            activePiece = null;
            grid[x][y].setUnactive();
            return;
        }
        
        // case select another full piece
        if (pieceArray[x][y] != null &&
                pieceArray[x][y].pType == Piece.PieceType.FULL) {
            grid[activePiece.x][activePiece.y].setUnactive();
            activePiece = null;
            return;
        }
        
        // try to move to new position
        movePiece(activePiece, x, y);
    }
    
    public static boolean generateSeed() {
        int countEmpty = 0;
        
        for (int x = 0; x < X_TILES; x++) {
            for (int y = 0; y < Y_TILES; y++) {
                countEmpty += (pieceArray[x][y] == null ? 1 : 0);
            }
        }
        
        if (countEmpty < 3) return false;
        int[] randomNum = new Random().ints(0, countEmpty).distinct().limit(3).toArray();
        List<Integer> randomNumList = Arrays.stream(randomNum).boxed().collect(Collectors.toList());
        
        int curTile = 0;
        for (int i = 0; i < 81; i++) {
            int y = (int) i / 9;
            int x = (int) i % 9;
            if (pieceArray[x][y] != null) {
                continue;
            }
            
            if (randomNumList.contains(curTile)) {
                makePiece(Piece.PieceType.SEED, x, y);
            }
            
            curTile++;
        }
        
        return true;
    }
    
    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(W, H);
        root.getChildren().addAll(tileGroup, pieceGroup);
        int[] randomNum = new Random().ints(0, 81).distinct().limit(6).toArray();
        for (int i = 0; i < randomNum.length; i++) {
            int y =(int) randomNum[i] / 9;
            int x = randomNum[i] % 9;
            if (i < 3) {
                // generate seed
                makePiece(Piece.PieceType.SEED, x, y);
            } else {
                // generate full
                makePiece(Piece.PieceType.FULL, x, y);
            }
        }
        
        for (int x = 0; x < X_TILES; x++) {
            for (int y = 0; y < Y_TILES; y++) {
                Tile tile = new Tile(x, y);
                grid[x][y] = tile;
                tileGroup.getChildren().add(tile);
            }
        }
        return root;
    }
    
    private static Piece makePiece(Piece.PieceType pType, int x, int y) {
        Piece piece = new Piece(pType, x, y);
        pieceArray[x][y] = piece;
        pieceGroup.getChildren().add(piece);
        return piece;
    }
    
    public static Group tileGroup = new Group();
    public static Group pieceGroup = new Group();
    
    public static Piece[][] pieceArray = new Piece[X_TILES][Y_TILES];
    public static Tile[][] grid = new Tile[X_TILES][Y_TILES];

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Line 98");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setOnMouseClicked((event) -> {
            handleMouseClick(event.getX(), event.getY());
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
