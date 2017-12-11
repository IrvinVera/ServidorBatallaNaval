package network;

import negocio.Jugador;
import Persistencia.JugadorJpaController;
import Persistencia.Partida;
import Persistencia.PartidaJpaController;
import Persistencia.Puntaje;
import Persistencia.PuntajeJpaController;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import negocio.IConexion;
import servidorbatallanaval.ServidorBatallaNaval;
import negocio.IJugador;
import negocio.IPartida;
import negocio.IPuntaje;

/**
 * Plantilla con la implementacion de los métodos de las interfaces IJugador,
 * IPuntaje, IPartida, IConexion.
 *
 * @author Irvin Dereb Vera López.
 * @author Israel Reyes Ozuna.
 */
public class RMIServidor implements IJugador, IPuntaje, IPartida, IConexion {

    ArrayList<String> jugadoresConectados = new ArrayList<>();

    /**
     * Permite verificar que los datos ingresados por el usuario coincidan con
     * los de la base de datos.
     *
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     * @param contrasena Contraseña del jugador para ingresar al sistema.
     * @return Un valor verdadero si el jugador esta registrado en el sistema o
     * un valor falso en caso de lo contrario.
     * @throws RemoteException puede arrojar esta excepción si ocurre un fallo
     * con el servidor RMI
     */
    @Override
    public boolean iniciarSesion(String nombreJugador, String contrasena) throws RemoteException {

        boolean usuarioEncontrado = false;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ServidorBatallaNavalPU", null);
        JugadorJpaController jugadorControlador = new JugadorJpaController(entityManagerFactory);

        try {
            Persistencia.Jugador cuenta = jugadorControlador.findJugador(nombreJugador);
            if (cuenta != null && cuenta.getNombreJugador().equals(nombreJugador) && cuenta.getContrasena().equals(contrasena)) {
                usuarioEncontrado = true;
            }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuarioEncontrado;
    }

    /**
     * Permite verificar si el nombre de jugador ya fue seleccionado por otro
     * usuario.
     *
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     * @return Un valor verdadero si el nombre de jugador ya fue registrado o un
     * valor falso en caso de lo contrario.
     * @throws RemoteException puede arrojar esta excepción si ocurre un fallo
     * con el servidor RMI
     */
    @Override
    public boolean verificarExistenciaCuenta(String nombreJugador) throws RemoteException {
        boolean usuarioExistente = false;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ServidorBatallaNavalPU", null);
        JugadorJpaController jugadorControlador = new JugadorJpaController(entityManagerFactory);

        try {
            Persistencia.Jugador cuenta = jugadorControlador.findJugador(nombreJugador);
            if (cuenta != null) {
                usuarioExistente = true;
            }
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
        }

        return usuarioExistente;
    }

    /**
     * Permite el registro de un jugador en el sistema.
     *
     * @param jugador Un objeto Jugador que contiene todos los datos del
     * jugador.
     * @return Un verdadero si el jugador fue registrado con éxito o un valor
     * falso en caso de lo contrario.
     * @throws RemoteException puede arrojar esta excepción si ocurre un fallo
     * con el servidor RMI
     */
    @Override
    public boolean registrarJugador(Jugador jugador) throws RemoteException {
        boolean usuarioRegistradoExitosamente = true;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ServidorBatallaNavalPU", null);
        JugadorJpaController jugadorControlador = new JugadorJpaController(entityManagerFactory);

        Persistencia.Jugador jugadorNuevo = new Persistencia.Jugador();
        jugadorNuevo.setNombreJugador(jugador.getNombreJugador());
        jugadorNuevo.setContrasena(jugador.getContrasena());
        jugadorNuevo.setCorreo(jugador.getCorreo());
        jugadorNuevo.setNombre(jugador.getNombre());
        jugadorNuevo.setApellidos(jugador.getApellidos());
        try {
            jugadorControlador.create(jugadorNuevo);
            registrarJugadorEnTablaPuntaje(jugador.getNombreJugador());
            registrarJugadorEnTablaPartida(jugador.getNombreJugador());
        } catch (Exception ex) {
            usuarioRegistradoExitosamente = false;
            Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuarioRegistradoExitosamente;
    }

    /**
     * Permite registrar un jugador en la tabla puntaje un vez que se registro
     * en el sistema.
     *
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     */
    public void registrarJugadorEnTablaPuntaje(String nombreJugador) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ServidorBatallaNavalPU", null);
        PuntajeJpaController puntajeJpaController = new PuntajeJpaController(entityManagerFactory);
        Puntaje puntaje = new Puntaje();
        puntaje.setNombreJugador(nombreJugador);
        puntaje.setPuntosTotales(0);
        try {
            puntajeJpaController.create(puntaje);
        } catch (Exception ex) {
            Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Permite registrar un jugador en la tabla partida un vez que se registro
     * en el sistema.
     *
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     */
    public void registrarJugadorEnTablaPartida(String nombreJugador) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ServidorBatallaNavalPU", null);
        PartidaJpaController partidaJpaController = new PartidaJpaController(entityManagerFactory);
        Partida partida = new Partida();
        partida.setNombreJugador(nombreJugador);
        partida.setPartidasGanadas(0);
        partida.setPartidasPerdidas(0);

        try {
            partidaJpaController.create(partida);
        } catch (Exception ex) {
            Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Permite iniciar el servidor.
     */
    public void iniciarServidor() {
        RMIServidor servidor = new RMIServidor();
        try {
            IJugador stub = (IJugador) UnicastRemoteObject.exportObject(servidor, 0);
            Registry registry = LocateRegistry.getRegistry("127.0.0.1");
            registry.bind("ServidorBatallaNaval", stub);

        } catch (RemoteException | AlreadyBoundException ex) {
            Logger.getLogger(ServidorBatallaNaval.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Permite verificar si el jugador se encuentra conectado.
     *
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     * @return Un valor verdadero si el jugador se encuentra conectado o un
     * valor falso en caso de lo contrario
     * @throws RemoteException puede arrojar esta excepción si ocurre un fallo
     * con el servidor RMI
     */
    @Override
    public boolean verificarJugadorConectado(String nombreJugador) throws RemoteException {
        boolean jugadorConectado = false;
        if (jugadoresConectados.contains(nombreJugador)) {
            jugadorConectado = true;
        } else {
            jugadoresConectados.add(nombreJugador);
        }
        return jugadorConectado;
    }

    /**
     * Permite actualizar el puntaje de un jugador después haber disputado una
     * partida
     *
     * @param puntajeObtenido Valor obtenido de acuerdo a el triunfo o derrota
     * del jugador.
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     * @throws RemoteException puede arrojar esta excepción si ocurre un fallo
     * con el servidor RMI
     */
    @Override
    public void actualizarPuntajeJugador(int puntajeObtenido, String nombreJugador) throws RemoteException {
        int puntajeActual;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ServidorBatallaNavalPU", null);
        PuntajeJpaController puntajeControlador = new PuntajeJpaController(entityManagerFactory);
        puntajeActual = puntajeControlador.obtenerPuntajeActual(nombreJugador);
        puntajeActual = puntajeActual + puntajeObtenido;
        puntajeControlador.actualizarPuntos(puntajeActual, nombreJugador);
    }

    /**
     * Permite actualizar las partidas de un jugador cuando gana una
     * confrontación.
     *
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     * @throws RemoteException puede arrojar esta excepción si ocurre un fallo
     * con el servidor RMI
     */
    @Override
    public void actualizarPartidasGanadas(String nombreJugador) throws RemoteException {
        int partidasGanadasActuales;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ServidorBatallaNavalPU", null);
        PartidaJpaController partidaControlador = new PartidaJpaController(entityManagerFactory);
        partidasGanadasActuales = partidaControlador.obtenerPartidasGanadas(nombreJugador);
        partidasGanadasActuales++;
        partidaControlador.actualizarPartidasGanadas(partidasGanadasActuales, nombreJugador);
    }

    /**
     * Permite actualizar las partidas de un jugador cuando pierde una
     * confrontación.
     *
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     * @throws RemoteException puede arrojar esta excepción si ocurre un fallo
     * con el servidor RMI
     */
    @Override
    public void actualizarPartidasPerdidas(String nombreJugador) throws RemoteException {
        int partidasPerdidasActuales;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ServidorBatallaNavalPU", null);
        PartidaJpaController partidaControlador = new PartidaJpaController(entityManagerFactory);
        partidasPerdidasActuales = partidaControlador.obtenerPartidasPerdidas(nombreJugador);
        partidasPerdidasActuales++;
        partidaControlador.actualizarPartidasPerdidas(partidasPerdidasActuales, nombreJugador);
    }

    /**
     * Permite que un jugador cierre sesión.
     *
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     * @throws RemoteException puede arrojar esta excepción si ocurre un fallo
     * con el servidor RMI
     */
    @Override
    public void cerrarSesion(String nombreJugador) throws RemoteException {
        for (int i = 0; i < jugadoresConectados.size(); i++) {
            if (nombreJugador.equals(jugadoresConectados.get(i))) {
                jugadoresConectados.remove(i);
            }
        }
    }

    /**
     * Permite obtener el ranking de los 3 mejores puntajes.
     *
     * @return Una lista con objetos de la clase Puntaje.
     * @throws RemoteException puede arrojar esta excepción si ocurre un fallo
     * con el servidor RMI
     */
    @Override
    public List<negocio.Puntaje> obtenerMejoresPuntajes() throws RemoteException {
        List<negocio.Puntaje> puntajes;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ServidorBatallaNavalPU", null);
        PuntajeJpaController puntajeControlador = new PuntajeJpaController(entityManagerFactory);
        puntajes = puntajeControlador.obtenerPuntajesMaximos();
        return puntajes;
    }

    /**
     * Permite verificar si existe conexion con el servidor RMI.
     *
     * @return Un valor verdadero si se establece la conexión con el servidor
     * RMI o un valor falso en caso de lo contrario
     * @throws RemoteException puede arrojar esta excepción si ocurre un fallo
     * con el servidor RMI
     */
    @Override
    public boolean obtenerIPRMI() throws RemoteException {
        boolean ipServidorRMI = true;
        return ipServidorRMI;
    }

}
