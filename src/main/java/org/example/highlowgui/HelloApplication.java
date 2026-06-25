package org.example.highlowgui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class HelloApplication extends Application {

    private Canvas board;
    private Image cardImages;

    private Deck deck;
    private Hand hand;
    private String message;

    private boolean gameInProgress;


    public void start(Stage stage) throws IOException {

        URL url = getClass().getResource("/org/example/highlowgui/cards.png");

        if (url == null) {
            throw new RuntimeException("cards.png not found");
        }

        cardImages = new Image(url.toExternalForm());

        board = new Canvas(4*99 + 20, 123 + 80);

        Button higher = new Button("Higher");
        higher.setOnAction(event -> doHigher());
        Button lower = new Button("Lower");
        lower.setOnAction(event -> doLower());
        Button newGame = new Button("New Game");
        newGame.setOnAction(event -> doNewGame());

        HBox buttonBar = new HBox( higher, lower, newGame);

        higher.setPrefWidth(board.getWidth()/3.0);
        lower.setPrefWidth(board.getWidth()/3.0);
        newGame.setPrefWidth(board.getWidth()/3.0);

        BorderPane root = new BorderPane();
        root.setCenter(board);
        root.setBottom(buttonBar);

        doNewGame();

        Scene scene = new Scene(root);
        stage.setTitle("High/Low Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void doNewGame() {
        if (gameInProgress) {
            message = "Game is already running!";
            drawBoard();
            return;
        }
        deck = new Deck();
        hand = new Hand();
        deck.shuffle();
        hand.addCard(deck.dealCard());
        message = "Is the next card higher or lower.";
        gameInProgress = true;
        drawBoard();
    }

    private void doLower() {
        if (!gameInProgress) {
            message = "Click \"New Game\" to start the game!";
            drawBoard();
            return;
        }
        hand.addCard( deck.dealCard() );
        int cardCt = hand.getCardCount();
        Card thisCard = hand.getCard(cardCt - 1);
        Card prevCard = hand.getCard(cardCt - 2);
        if (thisCard.getValue() > prevCard.getValue()) {
            gameInProgress = false;
            message = "You lost!";
        }
        else if (thisCard.getValue() == prevCard.getValue()) {
            gameInProgress = false;
            message = "You lose on ties!";
        }
        else if (cardCt == 4){
            gameInProgress = false;
            message = "You win!";
        }
        else {
            message = "You got it right! Try for " + cardCt + "!";
        }
        drawBoard();
    }

    private void doHigher() {
        if (!gameInProgress) {
            message = "Click \"New Game\" to start the game!";
            drawBoard();
            return;
        }
        hand.addCard( deck.dealCard() );
        int cardCt = hand.getCardCount();
        Card thisCard = hand.getCard(cardCt - 1);
        Card prevCard = hand.getCard(cardCt - 2);
        if (thisCard.getValue() < prevCard.getValue()) {
            gameInProgress = false;
            message = "You lost!";
        }
        else if (thisCard.getValue() == prevCard.getValue()) {
            gameInProgress = false;
            message = "You lose on ties!";
        }
        else if (cardCt == 4){
            gameInProgress = false;
            message = "You win!";
        }
        else {
            message = "You got it right! Try for " + cardCt + "!";
        }
        drawBoard();
    }

    private void drawBoard() {
        GraphicsContext g = board.getGraphicsContext2D();
        g.setFill(Color.DARKGREEN);
        g.fillRect(0, 0, board.getWidth(), board.getHeight());
        g.setFill(Color.rgb(220, 255, 220));
        g.setFont(Font.font(16));
        g.fillText(message, 20, 180);
        int cardCt = hand.getCardCount();
        for (int i=0; i<cardCt; i++) {
            drawCard(g, hand.getCard(i), 20+i*99, 20);
        }
        if (gameInProgress)
            drawCard(g, null, 20 + cardCt*99, 20);
    }

    private void drawCard(GraphicsContext g, Card card, int x, int y) {
        int cardRow, cardCol;
        if(card == null){
            cardRow = 4;
            cardCol = 2;
        }
        else{
            cardRow = 3 - card.getSuit();
            cardCol = card.getValue() - 1;
        }
        double sx, sy;
        sx = 79 * cardCol;
        sy = 123 * cardRow;
        g.drawImage(cardImages, sx, sy,79,123, x, y,79,123);
    }
}
