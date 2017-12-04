package servidorbatallanaval;


import javafx.application.Application;
import javafx.stage.Stage;
import network.RMIServidor;


public class ServidorBatallaNaval extends Application {

    @Override
    public void start(Stage primaryStage) {
        RMIServidor servidor = new RMIServidor();
        servidor.iniciarServidor();
    }

    public static void main(String[] args) {
        launch(args);
    }
}