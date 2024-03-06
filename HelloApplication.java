package com.example.demo6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.List;

public class HelloApplication extends Application {

    //player
    private double playerSpeed = 10;
    private double playerSize = 60;
    private Circle player = new Circle(playerSize);

    //bots
    public List<Circle> bots = new ArrayList<>();
    private int botCount = 1;
    private int botRounds = 1;



    //foods
    private Circle food1 = new Circle(10);

    //Badguys
    private double botSize = 50;
    private double botSpeed = 3;
    private Circle bot = new Circle(botSize);
    private boolean botUp = false;
    private boolean botLeft = false;
    private boolean botDown = false;
    private boolean botRight = false;

    //game
    private boolean playerDied = false;
    private boolean botDied = false;


    private boolean wDown = false;
    private boolean aDown = false;
    private boolean sDown = false;
    private boolean dDown = false;

    Group group = new Group();

    @Override
    public void start(Stage primaryStage) {

        bots.add(bot);
        group.getChildren().add(bot);
        group.getChildren().add(food1);
        group.getChildren().add(player);
        Scene scene = new Scene(group, 1800, 950, Color.GREY);

        // Player random spawning in window:
        player.setCenterX(Math.round(Math.random() * 1700));
        player.setCenterY(Math.round(Math.random() * 900));

        // Player lol
        player.setFill(Color.YELLOW);
        player.setId("hello");

        // Food random spawning + coloring
        food1.setCenterX(Math.round(Math.random() * 1700));
        food1.setCenterY(Math.round(Math.random() * 900));
        food1.setFill(Color.GREEN);

        //Bot random spawn + colour
        bot.setCenterX(Math.round(Math.random() * 1700));
        bot.setCenterY(Math.round(Math.random() * 900));
        bot.setFill(Color.CRIMSON);



        primaryStage.setTitle("test");
        primaryStage.setScene(scene);
        primaryStage.show();

        // when pressed down, corresponding boolean set to true. This will later allow for diagonal movement.
        scene.setOnKeyPressed(
                event -> {
                    switch (event.getCode()) {
                        case W:
                            wDown = true;
                            break;
                        case A:
                            aDown = true;
                            break;
                        case S:
                            sDown = true;
                            break;
                        case D:
                            dDown = true;
                            break;
                    }
                });
        // when released, corresponding boolean set to false, helps for diagonal movement.
        scene.setOnKeyReleased(
                event -> {
                    switch (event.getCode()) {
                        case W:
                            wDown = false;
                            break;
                        case A:
                            aDown = false;
                            break;
                        case S:
                            sDown = false;
                            break;
                        case D:
                            dDown = false;
                            break;
                    }
                });

        // AnimationTimer for continuous movement. Runs playerMovement() each loop.
        // playerMovement() checks which booleans are true and moves accordingly.
        // I used brute force to account for every possible combination of having keys pressed down.
        // Brute forced using tons of if statements with tons of conditions
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                botEatPlayer();
                if(playerDied){
                    playerDied = false;
                    primaryStage.setTitle("GAME OVER");
                    Group deathGroup = new Group();
                    Scene deathScene = new Scene(deathGroup, 1800, 950, Color.BLACK);
                    primaryStage.setScene(deathScene);
                }
                playerEatBot();
                botMovementBooleans();
                botMove();
                playerMovement();
                eatFood1();
            }
        };
        timer.start();

    }

    public void playerMovement() {

        if (wDown && aDown && dDown && (player.getCenterY()>=0&&player.getCenterX()>=0&&player.getCenterX()<=1800)) {
            player.setCenterY(player.getCenterY() - playerSpeed); //Wad - Upward movement while player is not above window. (centerY not below 0)
        } else if (sDown && aDown && dDown && (player.getCenterX()>=0&&player.getCenterY()<=950&&player.getCenterX()<=1800)) {
            player.setCenterY(player.getCenterY() + playerSpeed); //sad - Downward movement while player is not below window (centerY not above 1800)
        } else if (aDown && wDown && sDown && (player.getCenterY()>=0&&player.getCenterX()>=0&&player.getCenterY()<=950)) {
            player.setCenterX(player.getCenterX() - playerSpeed); //aws - Left movement while player is not too far left (centerX not below 0)
        } else if (dDown && wDown && sDown && (player.getCenterY()>=0&&player.getCenterY()<=950&&player.getCenterX()<=1800)) {
            player.setCenterX(player.getCenterX() + playerSpeed); //dws - Right movement while player is not too far right (centerX not above 1800)
        } else if (wDown && aDown && (player.getCenterY()>=0&&player.getCenterX()>=0)) {
            player.setCenterY(player.getCenterY() - playerSpeed);
            player.setCenterX(player.getCenterX() - playerSpeed); //wa - Diagonal up&right movement while player is not above window or too far right.
        } else if (wDown && dDown && (player.getCenterY()>=0&&player.getCenterX()<=1800)) {
            player.setCenterY(player.getCenterY() - playerSpeed);
            player.setCenterX(player.getCenterX() + playerSpeed); //wd - Diagonal up&left movement while player is not above window or too far left.
        } else if (sDown && aDown && (player.getCenterX()>=0&&player.getCenterY()<=950)) {
            player.setCenterY(player.getCenterY() + playerSpeed);
            player.setCenterX(player.getCenterX() - playerSpeed); //sa - Diagonal down&right movement while player is not below window or too far right.
        } else if (sDown && dDown && (player.getCenterY()<=950&&player.getCenterX()<=1800)) {
            player.setCenterY(player.getCenterY() + playerSpeed);
            player.setCenterX(player.getCenterX() + playerSpeed); //sd - Diagonal down&left movement while player is not below window or too far left.
        } else if (wDown && (player.getCenterY()>=0)) {
            player.setCenterY(player.getCenterY() - playerSpeed); //w - Upward movement while player is not above window. (centerY not below 0)
        } else if (aDown && (player.getCenterX()>=0)) {
            player.setCenterX(player.getCenterX() - playerSpeed); //a - Downward movement while player is not below window (centerY not above 1800)
        } else if (sDown && (player.getCenterY()<=950)) {
            player.setCenterY(player.getCenterY() + playerSpeed); //s - Left movement while player is not too far left (centerX not below 0)
        } else if (dDown && (player.getCenterX()<=1800)) {
            player.setCenterX(player.getCenterX() + playerSpeed); //d - Right movement while player is not too far right (centerX not above 1800)
        }
    }

    public void eatFood1(){
        if(player.getCenterX()>food1.getCenterX()-player.getRadius()&&player.getCenterX()<food1.getCenterX()+player.getRadius()&&player.getCenterY()>food1.getCenterY()-player.getRadius()&&player.getCenterY()<food1.getCenterY()+player.getRadius()){
            food1.setCenterX(Math.round(Math.random() * 1700));
            food1.setCenterY(Math.round(Math.random() * 900));
            playerSize+=3;
            player.setRadius(playerSize);
            playerSpeed = playerSpeed - playerSpeed/140;
        }
    }

    public void botMovementBooleans(){
        // Booleans for 4 movement directions:
        if(bot.getCenterY()<player.getCenterY()){botDown=true;}
        if(bot.getCenterY()>player.getCenterY()){botUp=true;}
        if(bot.getCenterX()<player.getCenterX()){botRight=true;}
        if(bot.getCenterX()>player.getCenterX()){botLeft=true;}

        if(bot.getCenterY()<player.getCenterY()){botUp=false;}
        if(bot.getCenterY()>player.getCenterY()){botDown=false;}
        if(bot.getCenterX()<player.getCenterX()){botLeft=false;}
        if(bot.getCenterX()>player.getCenterX()){botRight=false;}


    }

    public void botMove(){
        // All possible movement key combinations that give a resultant movement:
        if(botUp&&botLeft&&botRight){bot.setCenterY(bot.getCenterY()-botSpeed);}//wad
        if(botDown&&botLeft&&botRight){bot.setCenterY(bot.getCenterY()+botSpeed);}//sad
        if(botLeft&&botUp&&botDown){bot.setCenterX(bot.getCenterX()-botSpeed);}//aws
        if(botRight&&botUp&&botDown){bot.setCenterX(bot.getCenterX()+botSpeed);}//dws
        if(botUp&&botLeft){
            bot.setCenterY(bot.getCenterY()-botSpeed);
            bot.setCenterX(bot.getCenterX()-botSpeed);
        }//wa
        if(botUp&&botRight){
            bot.setCenterY(bot.getCenterY()-botSpeed);
            bot.setCenterX(bot.getCenterX()+botSpeed);
        }//wd
        if(botDown&&botLeft){
            bot.setCenterY(bot.getCenterY()+botSpeed);
            bot.setCenterX(bot.getCenterX()-botSpeed);
        }//sa
        if(botDown&&botRight){
            bot.setCenterY(bot.getCenterY()+botSpeed);
            bot.setCenterX(bot.getCenterX()+botSpeed);
        }//sd
        if(botUp){bot.setCenterY(bot.getCenterY()-botSpeed);}
        if(botLeft){bot.setCenterX(bot.getCenterX()-botSpeed);}
        if(botDown){bot.setCenterY(bot.getCenterY()+botSpeed);}
        if(botRight){bot.setCenterX(bot.getCenterX()+botSpeed);}
    }

    public void botEatPlayer(){
        if(bot.getCenterX()>player.getCenterX()-bot.getRadius()&&bot.getCenterX()<player.getCenterX()+bot.getRadius()&&bot.getCenterY()>player.getCenterY()-bot.getRadius()&&bot.getCenterY()<player.getCenterY()+bot.getRadius()&&botSize>=playerSize+20){
            playerDied = true;
        }
    }

    public void playerEatBot(){
        if(player.getCenterX()>bot.getCenterX()-player.getRadius()&&player.getCenterX()<bot.getCenterX()+player.getRadius()&&player.getCenterY()>bot.getCenterY()-player.getRadius()&&player.getCenterY()<bot.getCenterY()+player.getRadius()&&playerSize>=botSize+5){
            bot.setFill(Color.RED);
            bot.setCenterX(Math.random()*1800);
            bot.setCenterY(Math.random()*900);
            botSize = playerSize + 40;
            bot.setRadius(botSize);
            botSpeed = botSpeed*0.5;
            Circle bot2 = new Circle(botSize);
            bot2.setCenterX(Math.random()*1800);
            bot2.setCenterY(Math.random()*800);
            bot2.setFill(Color.CRIMSON);
            group.getChildren().add(bot2);

        }
    }
    public void botSpawn(){
        bot.setCenterX(Math.round(Math.random() * 1700));
        bot.setCenterY(Math.round(Math.random() * 900));
        bot.setFill(Color.CRIMSON);
    }



    public static void main(String[] args) {
        launch(args);
    }
}