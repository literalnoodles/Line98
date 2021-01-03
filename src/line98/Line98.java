/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package line98;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
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
    
    public static final int SCORE_W = 200;
    
    public static final int X_TILES = W / TILE_SIZE;
    public static final int Y_TILES = H / TILE_SIZE;
    
    public static final int clearSize = 5;
    
    public static Piece activePiece;
    
    protected final static int[][] moveDir = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}};
    protected final static int[][] clearDir = {{0, 1}, {1, 0}, {1, -1}, {1, 1}};
    
    protected static int totalScore = 0;
    
    private void movePiece(Piece piece, int newX, int newY) {
        List<Integer> path = findingPath(piece.x, piece.y, newX, newY);
        if (path.size() == 0) return;
        
        pieceArray[piece.x][piece.y] = null;
        grid[piece.x][piece.y].setUnactive();
        
        if (pieceArray[newX][newY] != null) {
            pieceArray[newX][newY].toBack();
        }
        piece.toFront();
        piece.movePath(path);
    }
    
    private List<Integer> findingPath(int x, int y, int newX, int newY) {
        boolean[][] visited = new boolean[9][9];
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                visited[i][j] = false;
            }
        }
        
        int[][] backTrack = new int[9][9];
        for (int i = 0; i < backTrack.length; i++) {
            for (int j = 0; j < backTrack[i].length; j++) {
                backTrack[i][j] = -1;
            }
        }
        
        visited[x][y] = true;
        
        Queue<int[]> queueNode = new LinkedList<>();
        int[] node = {x, y};
        queueNode.add(node);
        boolean found = false;
        while (!queueNode.isEmpty() && !found) {
            int[] head = queueNode.remove();
            int index = 0;
            for (int[] dir: moveDir) {
                int cx = head[0] + dir[0];
                int cy = head[1] + dir[1];
                if (isValidCoordinate(cx, cy) && !visited[cx][cy]) {
                    int[] cNode = {cx, cy};
                    queueNode.add(cNode);
                    visited[cx][cy] = true;
                    backTrack[cx][cy] = index;
                }
                
                if (cx == newX && cy == newY) {
                    found = true;
                }
                
                index++;
            }
        }
        
        List<Integer> path = new ArrayList<>();
        
        if (found) {
            // reconstruct the path
            int cx = newX;
            int cy = newY;
            while (cx != x || cy != y) {
                path.add(0, backTrack[cx][cy]);
                int index = backTrack[cx][cy];
                cx -= moveDir[index][0];
                cy -= moveDir[index][1];
            }
        }
        
        return path;
    }
    
    private boolean isValidCoordinate(int x, int y) {
        if (x < 0 || y < 0 || x > 8 || y > 8)
            return false;
        if (pieceArray[x][y] != null && pieceArray[x][y].pType == Piece.PieceType.FULL)
            return false;
        return true;
    }
    
    private static boolean hasPiece(int x, int y) {
        if (x < 0 || y < 0 || x > 8 || y > 8)
            return false;
        if (pieceArray[x][y] == null)
            return false;
        return true;
    }
    
    public static void OnFinishMove(Piece piece, int newX, int newY) {
        
        Piece oldPiece = pieceArray[newX][newY];
        pieceArray[newX][newY] = piece; // set piece for the new position
        activePiece = null; // reset activePiece
        // if move to seed -> remove the seed
        if (oldPiece != null && oldPiece.pType == Piece.PieceType.SEED) {
            pieceGroup.getChildren().remove(oldPiece);
        }
        
        if (!clearOnPiece(piece)) {
            List<int[]> growList = growAllPieces();
            clearOnMultiPiece(growList);
            if (!generateSeed()) {
                db.insertScore(totalScore);
                List<Integer> scoreList = db.getTopScore();
                String result = "";
                if (scoreList.get(0) == totalScore) {
                    result += "You reached a new highscore!!!\n";
                }
                result += "Your score is " + totalScore + "\nClick OK to start a new game";
                resultDialog.setContentText(result);
                resultDialog.show();
            };
        }
        scoreboard.updateScore();
    }
    
    private void handleMouseClick(double posX, double posY)
    {
        int x = (int) posX / (TILE_SIZE);
        int y = (int) posY / (TILE_SIZE);
        
        if (x >= X_TILES || y >= Y_TILES) return;
        
        if (activePiece == null) {
            if (pieceArray[x][y] != null && pieceArray[x][y].pType == Piece.PieceType.FULL) {
                activePiece = pieceArray[x][y];
                grid[x][y].setActive();
            }
            return;
        }
    
        // case select again
        if (x == activePiece.x && y == activePiece.y) {
//            activePiece = null;
//            grid[x][y].setUnactive();
            return;
        }
        
        // case select another full piece
        if (pieceArray[x][y] != null &&
                pieceArray[x][y].pType == Piece.PieceType.FULL) {
            grid[activePiece.x][activePiece.y].setUnactive();
//            activePiece = null;
            grid[x][y].setActive();
            activePiece = pieceArray[x][y];
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
                makePiece(Piece.PieceType.SEED, null, x, y);
            }
            
            curTile++;
        }
        
        return true;
    }
    
    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(W + SCORE_W, H);
        root.getChildren().addAll(tileGroup, pieceGroup, scoreGroup);
        scoreGroup.getChildren().add(scoreboard);
        int[] randomNum = new Random().ints(0, 81).distinct().limit(6).toArray();
        for (int i = 0; i < randomNum.length; i++) {
            int y =(int) randomNum[i] / 9;
            int x = randomNum[i] % 9;
            if (i < 3) {
                // generate seed
                makePiece(Piece.PieceType.SEED, null, x, y);
            } else {
                // generate full
                makePiece(Piece.PieceType.FULL, null, x, y);
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
    
    private static Piece makePiece(Piece.PieceType pType, Piece.ColorType cType, int x, int y) {
        Piece piece = new Piece(pType, cType, x, y);
        pieceArray[x][y] = piece;
        pieceGroup.getChildren().add(piece);
        return piece;
    }
    
    private static void growPiece(Piece piece) {
        // check if the piece is not a seed -> return
        if (piece.pType != Piece.PieceType.SEED) return;
        Piece.ColorType oldColor = pieceArray[piece.x][piece.y].cType;
        // remove the seed
        deletePiece(piece.x, piece.y);
        // generate Piece
        makePiece(Piece.PieceType.FULL, oldColor, piece.x, piece.y);
    }
    
    private static List<int[]> growAllPieces() {
        List<int[]> growList = new ArrayList<>();
        for (int x = 0; x < X_TILES; x++) {
            for (int y = 0; y < Y_TILES; y++) {
                Piece piece = pieceArray[x][y];
                if (piece != null && piece.pType == Piece.PieceType.SEED) {
                    growPiece(piece);
                    int[] cor = {piece.x, piece.y};
                    growList.add(cor);
                }
            }
        }
        return growList;
    }
    
    private static void deletePiece(int x, int y) {
        Piece oldPiece = pieceArray[x][y];
        if (oldPiece == null)
            return ;
        
        if (oldPiece.pType == Piece.PieceType.FULL) {
            totalScore += 10;
        }
        
        pieceGroup.getChildren().remove(oldPiece);
        pieceArray[x][y] = null;
    }
    
    private static List<int[]> getSameType(Piece piece, int dirX, int dirY) {
        int mvIndex = 1;
        int x = piece.x;
        int y = piece.y;
        Piece.ColorType color = piece.cType;
        List<int[]> list = new ArrayList<>();
        
        while (true) {
            int cx = x + mvIndex * dirX;
            int cy = y + mvIndex * dirY;
            if (!hasPiece(cx, cy)) {
                break;
            }

            Piece cPiece = pieceArray[cx][cy];
            if (cPiece.pType !=  Piece.PieceType.FULL || cPiece.cType != color) {
                break;
            }

            mvIndex++;
            int[] p = {cx, cy};
            list.add(p);
        }
        
        return list;
    }
    
    private static List<int[]> getSameType(Piece piece) {
        int x = piece.x;
        int y = piece.y;
        Piece.ColorType color = piece.cType;
        List<int[]> clearPieces = new ArrayList<>();
        for (int[] dir : clearDir) {
            // check for first direction
            List<int[]> addList = new ArrayList<>();
            int dirX = dir[0];
            int dirY = dir[1];
            addList.addAll(getSameType(piece, dirX, dirY));
            
            // check for other direction
            addList.addAll(getSameType(piece, -dirX, -dirY));
            
            if (addList.size() >= (clearSize - 1)) {
                clearPieces.addAll(addList);
                // add to clearPieces
            }
        }
        return clearPieces;
    }
    
    private static boolean clearOnPiece(Piece piece) {
        List<int[]> clearPieces = new ArrayList<>();
        clearPieces.addAll(getSameType(piece));
        int[] p = {piece.x, piece.y};
        clearPieces.add(p);
        
        if (clearPieces.size() > 1) {
            for (int[] coordinate : clearPieces) {
                int cx = coordinate[0];
                int cy = coordinate[1];
                deletePiece(cx, cy);
            }
            return true;
        }
        
        return false;
    }
    
    private static void clearOnMultiPiece(List<int[]> list) {
        List<int[]> clearList = new ArrayList<>();
        for (int[] cor : list) {
            int x = cor[0];
            int y = cor[1];
            List<int[]> clearPieces = new ArrayList<>();
            clearPieces.addAll(getSameType(pieceArray[x][y]));
            if (clearPieces.size() > 0) {
                clearList.addAll(clearPieces);
                int[] p = {x, y};
                clearList.add(p);
            }
        }
        
        if (clearList.size() > 0) {
            for (int[] coordinate : clearList) {
                int cx = coordinate[0];
                int cy = coordinate[1];
                deletePiece(cx, cy);
            }
        }
    }
    
    public static Group tileGroup;
    public static Group pieceGroup;
    public static Group scoreGroup;
    
    public static Piece[][] pieceArray;
    public static Tile[][] grid;
    public static ScoreBoard scoreboard;
    public static Dialog<String> resultDialog;
    public static DbConnection db;

    @Override
    public void start(Stage primaryStage) {
        newGame(primaryStage);
    }
    
    public void newGame(Stage stage) {
        init();
        Scene scene = new Scene(createContent());
        scoreboard.updateHighScore();
        scoreboard.newgameBtn.setOnAction(e -> {
            newGame(stage);
        });
        
        // dialog showing game result
        resultDialog = new Dialog<String>();
        resultDialog.setTitle("Game over");
        ButtonType type = new ButtonType("OK", ButtonData.OK_DONE);
        resultDialog.getDialogPane().getButtonTypes().add(type);
        Button button = (Button) resultDialog.getDialogPane().lookupButton(type);
        button.setOnAction((event) -> {
            newGame(stage);
        });
        
        stage.setTitle("Line 98");
        stage.setScene(scene);
        stage.show();
        scene.setOnMouseClicked((event) -> {
            handleMouseClick(event.getX(), event.getY());
        });
    }
    
    public void init() {
        tileGroup = new Group();
        pieceGroup = new Group();
        scoreGroup = new Group();
        scoreboard = new ScoreBoard();
        pieceArray = new Piece[X_TILES][Y_TILES];
        grid = new Tile[X_TILES][Y_TILES];
        totalScore = 0;
        db = DbConnection.getInstance();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
