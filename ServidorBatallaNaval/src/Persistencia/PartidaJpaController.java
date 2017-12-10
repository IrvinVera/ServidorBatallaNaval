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
public class PartidaJpaController implements Serializable {

    /**
     *
     * @param emf
     */
    public PartidaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    /**
     *
     * @return
     */
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
     *
     * @param partida
     * @throws IllegalOrphanException
     * @throws PreexistingEntityException
     * @throws Exception
     */
    public void create(Partida partida) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Jugador jugadorOrphanCheck = partida.getJugador();
        if (jugadorOrphanCheck != null) {
            Partida oldPartidaOfJugador = jugadorOrphanCheck.getPartida();
            if (oldPartidaOfJugador != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Jugador " + jugadorOrphanCheck + " already has an item of type Partida whose jugador column cannot be null. Please make another selection for the jugador field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Jugador jugador = partida.getJugador();
            if (jugador != null) {
                jugador = em.getReference(jugador.getClass(), jugador.getNombreJugador());
                partida.setJugador(jugador);
            }
            em.persist(partida);
            if (jugador != null) {
                jugador.setPartida(partida);
                jugador = em.merge(jugador);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPartida(partida.getNombreJugador()) != null) {
                throw new PreexistingEntityException("Partida " + partida + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     *
     * @param partida
     * @throws IllegalOrphanException
     * @throws NonexistentEntityException
     * @throws Exception
     */
    public void edit(Partida partida) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Partida persistentPartida = em.find(Partida.class, partida.getNombreJugador());
            Jugador jugadorOld = persistentPartida.getJugador();
            Jugador jugadorNew = partida.getJugador();
            List<String> illegalOrphanMessages = null;
            if (jugadorNew != null && !jugadorNew.equals(jugadorOld)) {
                Partida oldPartidaOfJugador = jugadorNew.getPartida();
                if (oldPartidaOfJugador != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Jugador " + jugadorNew + " already has an item of type Partida whose jugador column cannot be null. Please make another selection for the jugador field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (jugadorNew != null) {
                jugadorNew = em.getReference(jugadorNew.getClass(), jugadorNew.getNombreJugador());
                partida.setJugador(jugadorNew);
            }
            partida = em.merge(partida);
            if (jugadorOld != null && !jugadorOld.equals(jugadorNew)) {
                jugadorOld.setPartida(null);
                jugadorOld = em.merge(jugadorOld);
            }
            if (jugadorNew != null && !jugadorNew.equals(jugadorOld)) {
                jugadorNew.setPartida(partida);
                jugadorNew = em.merge(jugadorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = partida.getNombreJugador();
                if (findPartida(id) == null) {
                    throw new NonexistentEntityException("The partida with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     *
     * @param id
     * @throws NonexistentEntityException
     */
    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Partida partida;
            try {
                partida = em.getReference(Partida.class, id);
                partida.getNombreJugador();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The partida with id " + id + " no longer exists.", enfe);
            }
            Jugador jugador = partida.getJugador();
            if (jugador != null) {
                jugador.setPartida(null);
                jugador = em.merge(jugador);
            }
            em.remove(partida);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     *
     * @return
     */
    public List<Partida> findPartidaEntities() {
        return findPartidaEntities(true, -1, -1);
    }

    /**
     *
     * @param maxResults
     * @param firstResult
     * @return
     */
    public List<Partida> findPartidaEntities(int maxResults, int firstResult) {
        return findPartidaEntities(false, maxResults, firstResult);
    }

    private List<Partida> findPartidaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Partida.class));
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

    /**
     *
     * @param id
     * @return
     */
    public Partida findPartida(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Partida.class, id);
        } finally {
            em.close();
        }
    }

    /**
     *
     * @return
     */
    public int getPartidaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Partida> rt = cq.from(Partida.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    /**
     *
     * @param nombreJugador
     * @return
     */
    public int obtenerPartidasGanadas(String nombreJugador) {
        Object partidasGanadas;
        int ganadas = 0;
        String consulta = "Select p.partidasGanadas from Partida p where p.nombreJugador = :nombreJugador";
        EntityManager em = getEntityManager();
        try {
            partidasGanadas = em.createQuery(consulta).setParameter("nombreJugador", nombreJugador).getSingleResult();
            ganadas = Integer.parseInt(String.valueOf(partidasGanadas));
        } finally {
            em.close();
        }
        return ganadas;
    }

    /**
     *
     * @param nombreJugador
     * @return
     */
    public int obtenerPartidasPerdidas(String nombreJugador) {
        Object partidasPerdidas;
        int perdidas = 0;
        String consulta = "Select p.partidasPerdidas from Partida p where p.nombreJugador = :nombreJugador";
        EntityManager em = getEntityManager();
        try {
            partidasPerdidas = em.createQuery(consulta).setParameter("nombreJugador", nombreJugador).getSingleResult();
            perdidas = Integer.parseInt(String.valueOf(partidasPerdidas));
        } finally {
            em.close();
        }
        return perdidas;
    }

    /**
     *
     * @param nuevasGanadas
     * @param nombreJugador
     */
    public void actualizarPartidasGanadas(int nuevasGanadas, String nombreJugador) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("UPDATE Partida p SET p.partidasGanadas = '" + nuevasGanadas + "' where p.nombreJugador = '" + nombreJugador + "'").executeUpdate();
            em.getTransaction().commit();
        } catch (PersistenceException ex) {
            em.getTransaction().getRollbackOnly();
        } finally {
            em.close();
        }
    }

    /**
     *
     * @param nuevasPerdidas
     * @param nombreJugador
     */
    public void actualizarPartidasPerdidas(int nuevasPerdidas, String nombreJugador) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("UPDATE Partida p SET p.partidasPerdidas = '" + nuevasPerdidas + "' where p.nombreJugador = '" + nombreJugador + "'").executeUpdate();
            em.getTransaction().commit();
        } catch (PersistenceException ex) {
            em.getTransaction().getRollbackOnly();
        } finally {
            em.close();
        }
    }
}
