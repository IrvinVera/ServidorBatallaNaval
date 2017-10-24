/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author Irdevelo
 */
@Entity
@Table(name = "partida")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Partida.findAll", query = "SELECT p FROM Partida p"),
    @NamedQuery(name = "Partida.findByNombreJugador", query = "SELECT p FROM Partida p WHERE p.nombreJugador = :nombreJugador"),
    @NamedQuery(name = "Partida.findByPartidasGanadas", query = "SELECT p FROM Partida p WHERE p.partidasGanadas = :partidasGanadas"),
    @NamedQuery(name = "Partida.findByPartidasPerdidas", query = "SELECT p FROM Partida p WHERE p.partidasPerdidas = :partidasPerdidas")})
public class Partida implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nombreJugador")
    private String nombreJugador;
    @Column(name = "partidasGanadas")
    private Integer partidasGanadas;
    @Column(name = "partidasPerdidas")
    private Integer partidasPerdidas;
    @JoinColumn(name = "nombreJugador", referencedColumnName = "nombreJugador", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private Jugador jugador;

    public Partida() {
    }

    public Partida(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public String getNombreJugador() {
        return nombreJugador;
    }

    public void setNombreJugador(String nombreJugador) {
        this.nombreJugador = nombreJugador;
    }

    public Integer getPartidasGanadas() {
        return partidasGanadas;
    }

    public void setPartidasGanadas(Integer partidasGanadas) {
        this.partidasGanadas = partidasGanadas;
    }

    public Integer getPartidasPerdidas() {
        return partidasPerdidas;
    }

    public void setPartidasPerdidas(Integer partidasPerdidas) {
        this.partidasPerdidas = partidasPerdidas;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Partida)) {
            return false;
        }
        Partida other = (Partida) object;
        if ((this.nombreJugador == null && other.nombreJugador != null) || (this.nombreJugador != null && !this.nombreJugador.equals(other.nombreJugador))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Persistencia.Partida[ nombreJugador=" + nombreJugador + " ]";
    }
    
}
