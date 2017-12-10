package negocio;

import java.io.Serializable;

/**
 * Plantilla con la información de partida.
 *
 * @author Irvin Dereb Vera López.
 * @author Israel Reyes Ozuna.
 */
public class Partida implements Serializable{

    private String nombreJugador;

    private int partidasGanadas;

    private int partidasPerdidas;


    public String getNombreJugador() {
        return nombreJugador;
    }

 
    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }


    public int getPartidasGanadas() {
        return partidasGanadas;
    }

 
    public void setPartidasGanadas(int partidasGanadas) {
        this.partidasGanadas = partidasGanadas;
    }

  
    public int getPartidasPerdidas() {
        return partidasPerdidas;
    }

   
    public void setPartidasPerdidas(int partidasPerdidas) {
        this.partidasPerdidas = partidasPerdidas;
    }
}
