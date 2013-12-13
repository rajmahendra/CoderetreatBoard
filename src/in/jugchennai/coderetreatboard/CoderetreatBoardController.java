/*
 * Copyright 2013 Rajmahendra Hegde & William Siqueira.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package in.jugchennai.coderetreatboard;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import jfxtras.labs.scene.control.gauge.Battery;
import jfxtras.labs.scene.control.gauge.SixteenSegment;

/**
 *
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 * @author William Siqueira <william.a.siqueira@gmail.com>
 */
public class CoderetreatBoardController implements Initializable {

    //CHANGE THE TOTAL TIME OF EACH SLIDE HERE    
    private final double TOTAL_TIME_PER_SLIDE_IN_SECONDS = 5;
    // private final double TOTAL_TIME_PER_SLIDE_IN_SECONDS = 45 * 60;
    // SLIDES CAN BE ADDED HERE, JUST ADD THE FILE TO THE PACKAGE AND DECLARE IT HERE
    //private final String[] SLIDES = {"Slide1.fxml", "Slide2.fxml", "Slide3.fxml"};
    private final String[] SLIDES = {"Slide1.fxml"};
    // THAT'S THE TIMELINE, YOU CAN ADD CONTROLS TO CONTROL IT.
    Timeline tl = new Timeline();
    
    // THAT'S THE TRANSITION OF THE SLIDES. IT'S A SIMPLE ONE, FEEL FREE TO ADD A BETTER
    FadeTransition transition = new FadeTransition();
    private final SimpleDoubleProperty missingSeconds = new SimpleDoubleProperty();
    
    @FXML
    Pane batteryPane;

    @FXML
    Pane startPane;

    @FXML
    Pane counterPane;

    @FXML
    Pane slidePane;

    @FXML
    Pane controlPane;

    @FXML
    Button btnStart;

    @FXML
    Label lblMensagem;

    @FXML
    Button btnNext;

    List<SixteenSegment> segments = new ArrayList<>();
    private int index = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeStuff();
        addJFXtrasComponents();
        createTimer();
    }

    private void initializeStuff() {
        controlPane.visibleProperty().bind(tl.statusProperty().isEqualTo(Animation.Status.STOPPED));
        slidePane.visibleProperty().bind(tl.statusProperty().isEqualTo(Animation.Status.RUNNING));
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setNode(slidePane);
        transition.setDuration(new Duration((1000)));
    }

    private void addJFXtrasComponents() {
        addBattery();
        addCounter();
    }

    private void addBattery() {
        Battery battery = new Battery();
        battery.setPrefHeight(10);
        battery.setPrefSize(80, 250);
        battery.setChargeCondition(Battery.ChargeCondition.CHARGED);
        battery.chargingLevelProperty().bind(missingSeconds.divide(TOTAL_TIME_PER_SLIDE_IN_SECONDS));
        batteryPane.getChildren().add(battery);
    }

    private void addCounter() {
        final GridPane pane = new GridPane();
        pane.setPadding(new Insets(5));
        pane.setHgap(0);
        pane.setVgap(5);
        pane.setAlignment(Pos.TOP_CENTER);
        for (int i = 0; i < 5; i++) {
            SixteenSegment segment = new SixteenSegment();
            segment.setPrefSize(50, 100);
            segment.setColor(Color.WHITE);
            segments.add(segment);
            pane.add(segment, i, 1);
        }
        counterPane.getChildren().add(pane);
    }

    @FXML
    private void startNext(ActionEvent e) {
        System.out.println(index);
        loadSlide(index);
        updateIndex();
        lblMensagem.setVisible(index != 0);
        missingSeconds.set(TOTAL_TIME_PER_SLIDE_IN_SECONDS);
        tl.play();
        transition.play();
    }

    private void loadSlide(int slideIndex) {
        try {
            Parent slide = FXMLLoader.load(getClass().getResource(SLIDES[slideIndex]));
            slidePane.getChildren().setAll(slide);
            System.out.println(SLIDES[slideIndex]);
        } catch (IOException ex) {
            System.out.println("Fatal error loading slide");
            ex.printStackTrace();
        }
    }

    private void createTimer() {
        missingSeconds.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                // need to update the display with the seconds... TODO; Improve this, there are better ways to do this
                String seconds = String.valueOf((int) missingSeconds.get() % 60);
                String minutes = String.valueOf((int) missingSeconds.get() / 60);
                if (seconds.length() == 1) {
                    seconds = "0" + seconds;
                }
                if (minutes.length() == 1) {
                    minutes = "0" + minutes;
                }
                segments.get(4).setCharacter(seconds.charAt(1));
                segments.get(3).setCharacter(seconds.charAt(0));
                segments.get(2).setCharacter(':');
                segments.get(1).setCharacter(minutes.charAt(1));
                segments.get(0).setCharacter(minutes.charAt(0));
            }
        });
        KeyFrame k = new KeyFrame(new Duration(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                updateTime();
            }
        });
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.getKeyFrames().add(k);
        tl.play();
    }

    private void updateTime() {
        double value = missingSeconds.get();
        if (value > 0) {
            missingSeconds.set(--value);
        } else {
            tl.stop();            
        }
    }

    private void updateIndex() {
        index++;
        if (index == SLIDES.length) {
            index = 0;
        }
    }
}