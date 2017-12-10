package negocio;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface que contiene los métodos remotos pertenecientes al Jugador.
 *
 * @author Irvin Dereb Vera López.
 * @author Israel Reyes Ozuna.
 */
public interface IJugador extends Remote {

    /**
     * Permite verificar que los datos ingresados por el usuario coincidan con
     * los de la base de datos.
     *
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     * @param contrasena Contraseña del jugador para ingresar al sistema.
     * @return Un valor verdadero si el jugador esta registrado en el sistema o
     * un valor falso en caso de lo contrario.
     * @throws RemoteException
     */
    public boolean iniciarSesion(String nombreJugador, String contrasena) throws RemoteException;

    /**
     * Permite verificar si el nombre de jugador ya fue seleccionado por otro
     * usuario.
     *
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     * @return Un valor verdadero si el nombre de jugador ya fue registrado o un
     * valor falso en caso de lo contrario.
     * @throws RemoteException
     */
    public boolean verificarExistenciaCuenta(String nombreJugador) throws RemoteException;

    /**
     * Permite el registro de un jugador en el sistema.
     *
     * @param jugador Un objeto Jugador que contiene todos los datos del
     * jugador.
     * @return Un verdadero si el jugador fue registrado con éxito o un valor
     * falso en caso de lo contrario.
     * @throws RemoteException
     */
    public boolean registrarJugador(Jugador jugador) throws RemoteException;

    /**
     * Permite verificar si el jugador se encuentra conectado.
     *
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     * @return Un valor verdadero si el jugador se encuentra conectado o un
     * valor falso en caso de lo contrario
     * @throws RemoteException
     */
    public boolean verificarJugadorConectado(String nombreJugador) throws RemoteException;

    /**
     * Permite que un jugador cierre sesión.
     *
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     * @throws RemoteException
     */
    public void cerrarSesion(String nombreJugador) throws RemoteException;
}
