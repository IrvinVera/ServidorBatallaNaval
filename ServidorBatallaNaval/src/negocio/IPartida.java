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
 * @author iro19
 */
public interface IPartida extends Remote {

    public void actualizarPartidasGanadas() throws RemoteException;
    public void actualizarPartidasPerdidas() throws RemoteException;
}
