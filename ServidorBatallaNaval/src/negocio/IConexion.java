package negocio;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz que contiene los métodos remotos para la conexión con el servidor
 * RMI.
 *
 * @author Irvin Dereb Vera López.
 * @author Israel Reyes Ozuna.
 */
public interface IConexion extends Remote {

    /**
     * Permite verificar si existe conexion con el servidor RMI.
     *
     * @return Un valor verdadero si se establece la conexión con el servidor
     * RMI o un valor falso en caso de lo contrario
     * @throws RemoteException puede arrojar esta excepción si ocurre un fallo
     * con el servidor RMI
     */
    public boolean obtenerIPRMI() throws RemoteException;

}
