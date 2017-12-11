package Persistencia;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Irvin Dereb Vera López.
 * @author Israel Reyes Ozuna.
 */
@Entity
@Table(name = "puntaje")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Puntaje.findAll", query = "SELECT p FROM Puntaje p"),
    @NamedQuery(name = "Puntaje.findByNombreJugador", query = "SELECT p FROM Puntaje p WHERE p.nombreJugador = :nombreJugador"),
    @NamedQuery(name = "Puntaje.findByPuntosTotales", query = "SELECT p FROM Puntaje p WHERE p.puntosTotales = :puntosTotales")})
public class Puntaje implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nombreJugador")
    private String nombreJugador;
    @Column(name = "puntosTotales")
    private Integer puntosTotales;
    @JoinColumn(name = "nombreJugador", referencedColumnName = "nombreJugador", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Jugador jugador;

    public Puntaje() {
    }

    public Puntaje(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public Integer getPuntosTotales() {
        return puntosTotales;
    }

    public void setPuntosTotales(Integer puntosTotales) {
        this.puntosTotales = puntosTotales;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombreJugador != null ? nombreJugador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof Puntaje)) {
            return false;
        }
        Puntaje other = (Puntaje) object;
        if ((this.nombreJugador == null && other.nombreJugador != null) || (this.nombreJugador != null && !this.nombreJugador.equals(other.nombreJugador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Persistencia.Puntaje[ nombreJugador=" + nombreJugador + " ]";
    }

}
