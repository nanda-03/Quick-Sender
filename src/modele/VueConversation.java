/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;

/**
 *
 * @author NANDA Brice
 */
public class VueConversation extends VBox{
    private String conversationPartner;
    private ObservableList<Node> speechBubbles = FXCollections.observableArrayList();
    
    private ScrollPane messageScroller;
    private VBox messageContainer;
    
    public VueConversation(String conversationPartner){
        super(5);
        this.conversationPartner = conversationPartner;
        setupElements();
    }

    private void setupElements(){
        setupMessageDisplay();
        getChildren().setAll(messageScroller);
        setPadding(new Insets(5));
    }

    private void setupMessageDisplay(){
        messageContainer = new VBox(5);
        messageContainer.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
       // messageContainer.setStyle("-fx-background-image: url(/image/securite.jpg);");
        /*
        BackgroundImage myBI= new BackgroundImage(new Image("my url",32,32,false,true),
        BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
          BackgroundSize.DEFAULT);
        //then you set to your node
        myContainer.setBackground(new Background(myBI));
        */
        Bindings.bindContentBidirectional(speechBubbles, messageContainer.getChildren());
        messageScroller = new ScrollPane(messageContainer);
        messageScroller.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        messageScroller.setHbarPolicy(ScrollBarPolicy.NEVER);
        messageScroller.setPrefHeight(600);
        messageScroller.prefWidthProperty().bind(messageContainer.prefWidthProperty().subtract(5));
        messageScroller.setFitToWidth(true);
        //Make the scroller scroll to the bottom when a new message is added
        speechBubbles.addListener((ListChangeListener<Node>) change -> {
             while (change.next()) {
                 if(change.wasAdded()){
                     messageScroller.setVvalue(messageScroller.getVmax());
                 }
             }
        });
    }

    public void sendMessage(String message){
        speechBubbles.add(new SpeechBox(message, SpeechDirection.RIGHT));
    }
    public void receiveMessage(String message){
        Platform.runLater(()->{
           speechBubbles.add(new SpeechBox(message,SpeechDirection.LEFT)); 
        });
    }
class SpeechBox extends HBox{
    private Color DEFAULT_SENDER_COLOR = Color.GREENYELLOW;
    private Color DEFAULT_RECEIVER_COLOR = Color.WHEAT;
    private Background DEFAULT_SENDER_BACKGROUND, DEFAULT_RECEIVER_BACKGROUND;

    private String message;
    private SpeechDirection direction;

    private Label displayedText;
    private SVGPath directionIndicator;

    public SpeechBox(String message, SpeechDirection direction){
        this.message = message;
        this.direction = direction;
        initialiseDefaults();
        setupElements();
    }

    private void initialiseDefaults(){
        DEFAULT_SENDER_BACKGROUND = new Background(
                new BackgroundFill(DEFAULT_SENDER_COLOR, new CornerRadii(5,0,5,5,false), Insets.EMPTY));
        DEFAULT_RECEIVER_BACKGROUND = new Background(
                new BackgroundFill(DEFAULT_RECEIVER_COLOR, new CornerRadii(0,5,5,5,false), Insets.EMPTY));
    }

    private void setupElements(){
        displayedText = new Label(message);
        displayedText.setPadding(new Insets(5));
        displayedText.setWrapText(true);
        directionIndicator = new SVGPath();

        if(direction == SpeechDirection.LEFT){
            configureForReceiver();
        }
        else{
            configureForSender();
        }
    }

    private void configureForSender(){
        displayedText.setBackground(DEFAULT_SENDER_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER_RIGHT);
        directionIndicator.setContent("M10 0 L0 10 L0 0 Z");
        directionIndicator.setFill(DEFAULT_SENDER_COLOR);

        HBox container = new HBox(displayedText, directionIndicator);
        //Use at most 75% of the width provided to the SpeechBox for displaying the message
        container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER_RIGHT);
    }

    private void configureForReceiver(){
        displayedText.setBackground(DEFAULT_RECEIVER_BACKGROUND);
        displayedText.setAlignment(Pos.CENTER_LEFT);
        directionIndicator.setContent("M0 0 L10 0 L10 10 Z");
        directionIndicator.setFill(DEFAULT_RECEIVER_COLOR);

        HBox container = new HBox(directionIndicator, displayedText);
        //Use at most 75% of the width provided to the SpeechBox for displaying the message
        container.maxWidthProperty().bind(widthProperty().multiply(0.75));
        getChildren().setAll(container);
        setAlignment(Pos.CENTER_LEFT);
    }
}
enum SpeechDirection{
    LEFT, RIGHT
}

 
}
