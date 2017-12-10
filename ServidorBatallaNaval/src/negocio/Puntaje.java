package negocio;

import java.io.Serializable;

/**
 * Plantilla con la información de puntaje.
 *
 * @author Irvin Dereb Vera López.
 * @author Israel Reyes Ozuna.
 */
public class Puntaje implements Serializable {

    private int puntosTotales;
    private String nombreJugador;

    /**
     *
     * @return
     */
    public String getNombreJugador() {
        return nombreJugador;
    }

    /**
     *
     * @param nombreJugador
     */
    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }
    
    /**
     *
     * @return
     */
    public int getPuntosTotales() {
        return puntosTotales;
    }

    /**
     *
     * @param puntosTotales
     */
    public void setPuntosTotales(int puntosTotales) {
        this.puntosTotales = puntosTotales;
    }

}
