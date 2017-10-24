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

/**
 *
 * @author Irdevelo
 */
public class PuntajeJpaController implements Serializable {

    public PuntajeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Puntaje puntaje) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Jugador jugadorOrphanCheck = puntaje.getJugador();
        if (jugadorOrphanCheck != null) {
            Puntaje oldPuntajeOfJugador = jugadorOrphanCheck.getPuntaje();
            if (oldPuntajeOfJugador != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Jugador " + jugadorOrphanCheck + " already has an item of type Puntaje whose jugador column cannot be null. Please make another selection for the jugador field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador jugador = puntaje.getJugador();
            if (jugador != null) {
                jugador = em.getReference(jugador.getClass(), jugador.getNombreJugador());
                puntaje.setJugador(jugador);
            }
            em.persist(puntaje);
            if (jugador != null) {
                jugador.setPuntaje(puntaje);
                jugador = em.merge(jugador);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPuntaje(puntaje.getNombreJugador()) != null) {
                throw new PreexistingEntityException("Puntaje " + puntaje + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Puntaje puntaje) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puntaje persistentPuntaje = em.find(Puntaje.class, puntaje.getNombreJugador());
            Jugador jugadorOld = persistentPuntaje.getJugador();
            Jugador jugadorNew = puntaje.getJugador();
            List<String> illegalOrphanMessages = null;
            if (jugadorNew != null && !jugadorNew.equals(jugadorOld)) {
                Puntaje oldPuntajeOfJugador = jugadorNew.getPuntaje();
                if (oldPuntajeOfJugador != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Jugador " + jugadorNew + " already has an item of type Puntaje whose jugador column cannot be null. Please make another selection for the jugador field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (jugadorNew != null) {
                jugadorNew = em.getReference(jugadorNew.getClass(), jugadorNew.getNombreJugador());
                puntaje.setJugador(jugadorNew);
            }
            puntaje = em.merge(puntaje);
            if (jugadorOld != null && !jugadorOld.equals(jugadorNew)) {
                jugadorOld.setPuntaje(null);
                jugadorOld = em.merge(jugadorOld);
            }
            if (jugadorNew != null && !jugadorNew.equals(jugadorOld)) {
                jugadorNew.setPuntaje(puntaje);
                jugadorNew = em.merge(jugadorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = puntaje.getNombreJugador();
                if (findPuntaje(id) == null) {
                    throw new NonexistentEntityException("The puntaje with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Puntaje puntaje;
            try {
                puntaje = em.getReference(Puntaje.class, id);
                puntaje.getNombreJugador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The puntaje with id " + id + " no longer exists.", enfe);
            }
            Jugador jugador = puntaje.getJugador();
            if (jugador != null) {
                jugador.setPuntaje(null);
                jugador = em.merge(jugador);
            }
            em.remove(puntaje);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Puntaje> findPuntajeEntities() {
        return findPuntajeEntities(true, -1, -1);
    }

    public List<Puntaje> findPuntajeEntities(int maxResults, int firstResult) {
        return findPuntajeEntities(false, maxResults, firstResult);
    }

    private List<Puntaje> findPuntajeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Puntaje.class));
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

    public Puntaje findPuntaje(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Puntaje.class, id);
        } finally {
            em.close();
        }
    }

    public int getPuntajeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Puntaje> rt = cq.from(Puntaje.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
