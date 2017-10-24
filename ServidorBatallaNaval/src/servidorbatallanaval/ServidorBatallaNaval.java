package servidorbatallanaval;

import Negocio.ICuenta;
import Persistencia.Jugador;
import Persistencia.JugadorJpaController;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ServidorBatallaNaval extends Application implements ICuenta {

    @Override
    public void start(Stage primaryStage) {

        ServidorBatallaNaval servidor = new ServidorBatallaNaval();
        try {
            ICuenta stub = (ICuenta) UnicastRemoteObject.exportObject(servidor, 0);
            Registry registry = LocateRegistry.getRegistry("127.0.0.1");
            registry.bind("ServidorBatallaNaval", stub);
        } catch (RemoteException | AlreadyBoundException ex) {
            Logger.getLogger(ServidorBatallaNaval.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public boolean iniciarSesion(String nombreJugador, String contrasena) throws RemoteException {
        boolean usuarioEncontrado = false;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ServidorBatallaNavalPU", null);
        JugadorJpaController jugadorControlador = new JugadorJpaController(entityManagerFactory);

        try {
            Jugador cuenta = jugadorControlador.findJugador(nombreJugador);
            if (cuenta.getNombreJugador().equals(nombreJugador) && cuenta.getContrasena().equals(contrasena)) {
                usuarioEncontrado = true;
            } 
        } catch (Exception ex) {
            Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuarioEncontrado;
    }
}
