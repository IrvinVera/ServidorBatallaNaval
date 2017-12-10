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
public class PuntajeJpaController implements Serializable {

    /**
     *
     * @param emf
     */
    public PuntajeJpaController(EntityManagerFactory emf) {
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
     * @param puntaje
     * @throws IllegalOrphanException
     * @throws PreexistingEntityException
     * @throws Exception
     */
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

    /**
     *
     * @param puntaje
     * @throws IllegalOrphanException
     * @throws NonexistentEntityException
     * @throws Exception
     */
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

    /**
     *
     * @return
     */
    public List<Puntaje> findPuntajeEntities() {
        return findPuntajeEntities(true, -1, -1);
    }

    /**
     *
     * @param maxResults
     * @param firstResult
     * @return
     */
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

    /**
     *
     * @param id
     * @return
     */
    public Puntaje findPuntaje(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Puntaje.class, id);
        } finally {
            em.close();
        }
    }

    /**
     *
     * @return
     */
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

    /**
     *
     * @param nombreJugador
     * @return
     */
    public int obtenerPuntajeActual(String nombreJugador) {
        Object puntajeActual;
        int puntaje = 0;
        String consulta = "Select p.puntosTotales from Puntaje p where p.nombreJugador = :nombreJugador";
        EntityManager em = getEntityManager();
        try {
            puntajeActual = em.createQuery(consulta).setParameter("nombreJugador", nombreJugador).getSingleResult();
            puntaje = Integer.parseInt(String.valueOf(puntajeActual));
        } finally {
            em.close();
        }
        return puntaje;
    }

    /**
     *
     * @param puntosNuevos
     * @param nombreJugador
     */
    public void actualizarPuntos(int puntosNuevos, String nombreJugador) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("UPDATE Puntaje p SET p.puntosTotales = '" + puntosNuevos + "' where p.nombreJugador = '" + nombreJugador + "'").executeUpdate();
            em.getTransaction().commit();
        } catch (PersistenceException ex) {
            em.getTransaction().getRollbackOnly();
        } finally {
            em.close();
        }

    }
    
    /**
     *
     * @return
     */
    public List<negocio.Puntaje> obtenerPuntajesMaximos(){
        List<negocio.Puntaje> mejoresPuntajes = null;
        List<Persistencia.Puntaje> puntajes;
        String consulta = "Select p from Puntaje p ORDER BY p.puntosTotales DESC";        
        EntityManager em = getEntityManager();
        try{
            puntajes = em.createQuery(consulta).setMaxResults(3).getResultList();
        }finally{
            em.close();
        }
        
        mejoresPuntajes = convertir(puntajes);
        
        return mejoresPuntajes;
    }
    
    private List<negocio.Puntaje> convertir(List<Persistencia.Puntaje> puntajes){
        List<negocio.Puntaje> mejoresPuntajes = new ArrayList();
        negocio.Puntaje puntajeNe;
        
        for(Persistencia.Puntaje puntaje: puntajes){
            puntajeNe = new negocio.Puntaje();
            puntajeNe.setPuntosTotales(puntaje.getPuntosTotales());
            puntajeNe.setNombreJugador(puntaje.getNombreJugador());
            mejoresPuntajes.add(puntajeNe);
        }
        
        return mejoresPuntajes;
    }
}
