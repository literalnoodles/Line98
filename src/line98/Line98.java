/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package line98;

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
    
    private Piece activePiece;
    
    private void setActivePiece(double posX, double posY)
    {
        int x = (int) posX / (TILE_SIZE);
        int y = (int) posY / (TILE_SIZE);
//        System.out.println(x);
//        System.out.println(y);
//        System.out.println(grid[x][y]);
        if (grid[x][y] == null) {
            if (activePiece != null) {
                grid[activePiece.x][activePiece.y] = null; // set old tile to null
                activePiece.move(x, y);
                grid[x][y] = activePiece; // set to new tile
            }
        }
    }
    
    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(W, H);
        root.getChildren().addAll(tileGroup, pieceGroup);
        for (int y = 0; y < Y_TILES; y++) {
            for (int x = 0; x < X_TILES; x++) {
                Tile tile = new Tile(x, y);
//                grid[x][y] = tile;
                tileGroup.getChildren().add(tile);
                if (x == 1 && y == 3) {
                    Piece piece = null;
                    piece = makePiece(Piece.PieceType.FULL ,Piece.ColorType.BLUE, x, y);
                    pieceGroup.getChildren().add(piece);
                    piece.move(2, 2);
                    activePiece = piece;
//                    piece.move(3, 5);
//                    grid[2][1] = piece;
//                    activePiece = piece;
                }
            }
        }
        return root;
    }
    
    private Piece makePiece(Piece.PieceType pType ,Piece.ColorType cType, int x, int y) {
        Piece piece = new Piece(pType, cType, x, y);
        return piece;
    }
    
    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();
    
    private Piece[][] grid = new Piece[X_TILES][Y_TILES];

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Line 98");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setOnMouseClicked((event) -> {
//            activePiece.move(3, 4);
            setActivePiece(event.getX(), event.getY());
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
