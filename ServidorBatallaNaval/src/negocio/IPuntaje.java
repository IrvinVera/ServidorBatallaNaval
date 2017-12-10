package negocio;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * Interface que contiene los métodos remotos pertenecientes las partidas.
 *
 * @author Irvin Dereb Vera López.
 * @author Israel Reyes Ozuna.
 */
public interface IPuntaje extends Remote {

    /**
     * Permite actualizar el puntaje de un jugador después haber disputado una
     * partida
     *
     * @param puntajeObtenido Valor obtenido de acuerdo a el triunfo o derrota
     * del jugador.
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     * @throws RemoteException
     */
    public void actualizarPuntajeJugador(int puntajeObtenido, String nombreJugador) throws RemoteException;

    /**
     * Permite obtener el ranking de los 3 mejores puntajes.
     *
     * @return @throws RemoteException
     */
    public List<negocio.Puntaje> obtenerMejoresPuntajes() throws RemoteException;

}
