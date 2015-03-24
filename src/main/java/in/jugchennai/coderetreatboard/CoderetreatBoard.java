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

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Rajmahendra Hegde <rajmahendra@gmail.com>
 * @author William Siqueira <william.a.siqueira@gmail.com>
 */
public class CoderetreatBoard extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("CoderetreatBoard.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add("coderetreat.css");
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                System.exit(1);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
