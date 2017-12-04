/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import negocio.Jugador;
import Persistencia.JugadorJpaController;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import servidorbatallanaval.ServidorBatallaNaval;
import negocio.IJugador;

/**
 *
 * @author Irdevelo
 */
public class RMIServidor implements IJugador {
    
    ArrayList<String> jugadoresConectados = new ArrayList<>();

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

        } catch (Exception ex) {
            Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuarioEncontrado;
    }

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
        } catch (Exception ex) {
            Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
        }

        return usuarioExistente;
    }

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
        } catch (Exception ex) {
            usuarioRegistradoExitosamente = false;
            Logger.getLogger(Persistence.class.getName()).log(Level.SEVERE, null, ex);
        }
        return usuarioRegistradoExitosamente;
    }

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

    @Override
    public boolean verificarJugadorConectado(String nombreJugador) throws RemoteException {
        boolean jugadorConectado = false;
        if(jugadoresConectados.contains(nombreJugador)){
            jugadorConectado = true;
        }else{
            jugadoresConectados.add(nombreJugador);
        }
        return jugadorConectado;
    }
    
    public void desconectarJugador(String nombreJugador){
        jugadoresConectados.remove(nombreJugador);
    }
}
