/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;

import Persistencia.exceptions.IllegalOrphanException;
import Persistencia.exceptions.NonexistentEntityException;
import Persistencia.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
/**
 *
 * @author Irdevelo
 */
public class JugadorJpaController implements Serializable {

    public JugadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Jugador jugador) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puntaje puntaje = jugador.getPuntaje();
            if (puntaje != null) {
                puntaje = em.getReference(puntaje.getClass(), puntaje.getNombreJugador());
                jugador.setPuntaje(puntaje);
            }
            Partida partida = jugador.getPartida();
            if (partida != null) {
                partida = em.getReference(partida.getClass(), partida.getNombreJugador());
                jugador.setPartida(partida);
            }
            em.persist(jugador);
            if (puntaje != null) {
                Jugador oldJugadorOfPuntaje = puntaje.getJugador();
                if (oldJugadorOfPuntaje != null) {
                    oldJugadorOfPuntaje.setPuntaje(null);
                    oldJugadorOfPuntaje = em.merge(oldJugadorOfPuntaje);
                }
                puntaje.setJugador(jugador);
                puntaje = em.merge(puntaje);
            }
            if (partida != null) {
                Jugador oldJugadorOfPartida = partida.getJugador();
                if (oldJugadorOfPartida != null) {
                    oldJugadorOfPartida.setPartida(null);
                    oldJugadorOfPartida = em.merge(oldJugadorOfPartida);
                }
                partida.setJugador(jugador);
                partida = em.merge(partida);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJugador(jugador.getNombreJugador()) != null) {
                throw new PreexistingEntityException("Jugador " + jugador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Jugador jugador) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador persistentJugador = em.find(Jugador.class, jugador.getNombreJugador());
            Puntaje puntajeOld = persistentJugador.getPuntaje();
            Puntaje puntajeNew = jugador.getPuntaje();
            Partida partidaOld = persistentJugador.getPartida();
            Partida partidaNew = jugador.getPartida();
            List<String> illegalOrphanMessages = null;
            if (puntajeOld != null && !puntajeOld.equals(puntajeNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<>();
                }
                illegalOrphanMessages.add("You must retain Puntaje " + puntajeOld + " since its jugador field is not nullable.");
            }
            if (partidaOld != null && !partidaOld.equals(partidaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<>();
                }
                illegalOrphanMessages.add("You must retain Partida " + partidaOld + " since its jugador field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (puntajeNew != null) {
                puntajeNew = em.getReference(puntajeNew.getClass(), puntajeNew.getNombreJugador());
                jugador.setPuntaje(puntajeNew);
            }
            if (partidaNew != null) {
                partidaNew = em.getReference(partidaNew.getClass(), partidaNew.getNombreJugador());
                jugador.setPartida(partidaNew);
            }
            jugador = em.merge(jugador);
            if (puntajeNew != null && !puntajeNew.equals(puntajeOld)) {
                Jugador oldJugadorOfPuntaje = puntajeNew.getJugador();
                if (oldJugadorOfPuntaje != null) {
                    oldJugadorOfPuntaje.setPuntaje(null);
                    oldJugadorOfPuntaje = em.merge(oldJugadorOfPuntaje);
                }
                puntajeNew.setJugador(jugador);
                puntajeNew = em.merge(puntajeNew);
            }
            if (partidaNew != null && !partidaNew.equals(partidaOld)) {
                Jugador oldJugadorOfPartida = partidaNew.getJugador();
                if (oldJugadorOfPartida != null) {
                    oldJugadorOfPartida.setPartida(null);
                    oldJugadorOfPartida = em.merge(oldJugadorOfPartida);
                }
                partidaNew.setJugador(jugador);
                partidaNew = em.merge(partidaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = jugador.getNombreJugador();
                if (findJugador(id) == null) {
                    throw new NonexistentEntityException("The jugador with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador jugador;
            try {
                jugador = em.getReference(Jugador.class, id);
                jugador.getNombreJugador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jugador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Puntaje puntajeOrphanCheck = jugador.getPuntaje();
            if (puntajeOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<>();
                }
                illegalOrphanMessages.add("This Jugador (" + jugador + ") cannot be destroyed since the Puntaje " + puntajeOrphanCheck + " in its puntaje field has a non-nullable jugador field.");
            }
            Partida partidaOrphanCheck = jugador.getPartida();
            if (partidaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<>();
                }
                illegalOrphanMessages.add("This Jugador (" + jugador + ") cannot be destroyed since the Partida " + partidaOrphanCheck + " in its partida field has a non-nullable jugador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(jugador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Jugador> findJugadorEntities() {
        return findJugadorEntities(true, -1, -1);
    }

    public List<Jugador> findJugadorEntities(int maxResults, int firstResult) {
        return findJugadorEntities(false, maxResults, firstResult);
    }

    private List<Jugador> findJugadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Jugador.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Jugador findJugador(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Jugador.class, id);
        } finally {
            em.close();
        }
    }

    public int getJugadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Jugador> rt = cq.from(Jugador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public void cambiarActivado(String jugador) {
        String consulta = "UPDATE Jugador j SET j.estado = 1 WHERE j.nombreJugador = :jugador";
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery(consulta).setParameter("jugador", jugador).executeUpdate();
            em.getTransaction().commit();
        } catch (PersistenceException ex) {
            em.getTransaction().getRollbackOnly();
        } finally {
            em.close();
        }
    }
}
