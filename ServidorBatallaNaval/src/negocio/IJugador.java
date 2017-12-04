/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Irdevelo
 */
public interface IJugador extends Remote {
    
    public boolean iniciarSesion(String nombreJugador, String contrasena)throws RemoteException;
    public boolean verificarExistenciaCuenta(String nombreJugador)throws RemoteException;
    public boolean registrarJugador(Jugador jugador)throws RemoteException;
    public boolean verificarJugadorConectado(String nombreJugador)throws RemoteException;
}
