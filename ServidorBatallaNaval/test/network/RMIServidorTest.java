package network;

import Persistencia.Partida;
import negocio.Jugador;
import negocio.Puntaje;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Irvin Dereb Vera López.
 * @author Israel Reyes Ozuna.
 */
public class RMIServidorTest {

    @Test
    public void testIniciarSesion() throws Exception {
        System.out.println("iniciarSesion");
        String nombreJugador = "isrozuna";
        String contrasena = "d6f581d378718452812449e3b5bcea04d2fcf2aafcd4f1a598d4e016c0eecb1c";
        RMIServidor instance = new RMIServidor();
        boolean expResult = true;
        boolean result = instance.iniciarSesion(nombreJugador, contrasena);
        assertEquals(expResult, result);
    }

    @Test
    public void testIniciarSesionFallido() throws Exception {
        System.out.println("iniciarSesion");
        String nombreJugador = "irdevelo";
        String contrasena = "dsada";
        RMIServidor instance = new RMIServidor();
        boolean expResult = false;
        boolean result = instance.iniciarSesion(nombreJugador, contrasena);
        assertEquals(expResult, result);
    }

    @Test
    public void testVerificarExistenciaCuenta() throws Exception {
        System.out.println("verificarExistenciaCuenta");
        String nombreJugador = "irdevelo";
        RMIServidor instance = new RMIServidor();
        boolean expResult = true;
        boolean result = instance.verificarExistenciaCuenta(nombreJugador);
        assertEquals(expResult, result);
    }

    @Test
    public void testVerificarExistenciaCuentaFallido() throws Exception {
        System.out.println("verificarExistenciaCuenta");
        String nombreJugador = "Romario";
        RMIServidor instance = new RMIServidor();
        boolean expResult = false;
        boolean result = instance.verificarExistenciaCuenta(nombreJugador);
        assertEquals(expResult, result);
    }

    @Test
    public void testRegistrarJugador() throws Exception {
        System.out.println("registrarJugador");
        Jugador jugador = new Jugador();
        jugador.setNombre("Pablo");
        jugador.setApellidos("Perez");
        jugador.setNombreJugador("Pablin");
        jugador.setContrasena("123");
        jugador.setCorreo("pablin@hotmail.com");
        RMIServidor instance = new RMIServidor();
        boolean expResult = false;
        boolean result = instance.registrarJugador(jugador);
        assertEquals(expResult, result);
    }

    @Test
    public void testRegistrarJugadorFallido() throws Exception {
        System.out.println("registrarJugador");
        Jugador jugador = new Jugador();
        RMIServidor instance = new RMIServidor();
        boolean expResult = false;
        boolean result = instance.registrarJugador(jugador);
        assertEquals(expResult, result);
    }

    @Test
    public void testRegistrarJugadorEnTablaPuntaje() {
        System.out.println("registrarJugadorEnTablaPuntaje");
        Puntaje puntaje = new Puntaje();
        puntaje.setNombreJugador("Roger");
        puntaje.setPuntosTotales(0);
        RMIServidor instance = new RMIServidor();
        instance.registrarJugadorEnTablaPuntaje(puntaje.getNombreJugador());
    }

    @Test
    public void testRegistrarJugadorEnTablaPuntajeFallido() {
        System.out.println("registrarJugadorEnTablaPuntaje");
        String nombreJugador = null;
        RMIServidor instance = new RMIServidor();
        instance.registrarJugadorEnTablaPuntaje(nombreJugador);
    }

    @Test
    public void testRegistrarJugadorEnTablaPartida() {
        System.out.println("registrarJugadorEnTablaPartida");
        Partida partida = new Partida();
        partida.setNombreJugador("irdevelo");
        RMIServidor instance = new RMIServidor();
        instance.registrarJugadorEnTablaPartida(partida.getNombreJugador());
    }

    @Test
    public void testRegistrarJugadorEnTablaPartidaFallido() {
        System.out.println("registrarJugadorEnTablaPartida");
        String nombreJugador = null;
        RMIServidor instance = new RMIServidor();
        instance.registrarJugadorEnTablaPartida(nombreJugador);
    }

    @Test
    public void testVerificarJugadorConectado() throws Exception {
        System.out.println("verificarJugadorConectado");
        String nombreJugador = "Jose";
        RMIServidor instance = new RMIServidor();
        boolean expResult = false;
        boolean result = instance.verificarJugadorConectado(nombreJugador);
        assertEquals(expResult, result);
    }

    @Test
    public void testVerificarJugadorConectadoFallido() throws Exception {
        System.out.println("verificarJugadorConectado");
        String nombreJugador = null;
        RMIServidor instance = new RMIServidor();
        boolean expResult = false;
        boolean result = instance.verificarJugadorConectado(nombreJugador);
        assertEquals(expResult, result);
    }

    @Test
    public void testActualizarPuntajeJugador() throws Exception {
        System.out.println("actualizarPuntajeJugador");
        int puntajeObtenido = 0;
        String nombreJugador = "irdevelo";
        RMIServidor instance = new RMIServidor();
        instance.actualizarPuntajeJugador(puntajeObtenido, nombreJugador);
    }

    @Test
    public void testActualizarPartidasGanadas() throws Exception {
        System.out.println("actualizarPartidasGanadas");
        String nombreJugador = "irdevelo";
        RMIServidor instance = new RMIServidor();
        instance.actualizarPartidasGanadas(nombreJugador);
    }

    @Test
    public void testActualizarPartidasPerdidas() throws Exception {
        System.out.println("actualizarPartidasPerdidas");
        String nombreJugador = "irdevelo";
        RMIServidor instance = new RMIServidor();
        instance.actualizarPartidasPerdidas(nombreJugador);
    }

    @Test
    public void testCerrarSesion() throws Exception {
        System.out.println("cerrarSesion");
        String nombreJugador = "irdevelo";
        RMIServidor instance = new RMIServidor();
        instance.cerrarSesion(nombreJugador);
    }

    @Test
    public void testCerrarSesionFallido() throws Exception {
        System.out.println("cerrarSesion");
        String nombreJugador = null;
        RMIServidor instance = new RMIServidor();
        instance.cerrarSesion(nombreJugador);
    }

    @Test
    public void testObtenerIPRMI() throws Exception {
        System.out.println("obtenerIPRMI");
        RMIServidor instance = new RMIServidor();
        boolean expResult = true;
        boolean result = instance.obtenerIPRMI();
        assertEquals(expResult, result);
    }

}
