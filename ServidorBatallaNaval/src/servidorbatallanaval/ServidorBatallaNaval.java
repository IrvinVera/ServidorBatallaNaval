/*--------------------------------------------------------*/
 /*        Servidor RMI para Batalla naval tuerta
    @version 1.0 / 11 de diciembre de 2017
    Desarrolladores: Irvin Dereb Vera López
                     Israel Reyes Ozuna
    Descripción: Mediante este servidor se logra una conexión
                remota para conectar con una base datos
                mediante el framework de JPA
/*--------------------------------------------------------*/
package servidorbatallanaval;

import javafx.application.Application;
import javafx.stage.Stage;
import network.RMIServidor;

/**
 *
 * @author Irvin Dereb Vera López.
 * @author Israel Reyes Ozuna.
 */
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
