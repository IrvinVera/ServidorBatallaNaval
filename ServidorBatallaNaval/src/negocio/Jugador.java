package negocio;

import java.io.Serializable;

/**
 * Plantilla con la información del jugador.
 *
 * @author Irvin Dereb Vera López.
 * @author Israel Reyes Ozuna.
 */
public class Jugador implements Serializable {

    private String nombreJugador;

    private String contrasena;

    private String correo;

    private String nombre;

    private String apellidos;

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Permite verificar si el nombre de jugador elegido tiene el formato
     * corecto
     *
     * @param nombreJugador Clave del jugador para ingresar al sistema.
     * @return Un valor verdadero si el nombre del jugador tiene el formato
     * correcto o un valor falso en caso de lo contrario
     */
    public static boolean verificarNombreUsuarioCorrecto(String nombreJugador) {
        boolean nombreUsuarioCorrecto = true;
        String caracteres[] = nombreJugador.split(" ");
        if (caracteres.length > 1) {
            nombreUsuarioCorrecto = false;
        }
        return nombreUsuarioCorrecto;
    }
}
