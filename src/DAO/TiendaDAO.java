/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.IllegalOrphanException;
import DAO.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Cliente;
import java.util.ArrayList;
import java.util.List;
import DTO.Servicio;
import DTO.Tienda;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class TiendaDAO implements Serializable {

    public TiendaDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public TiendaDAO(){
        this.emf = Conexion.getEm();
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tienda tienda) {
        if (tienda.getClienteList() == null) {
            tienda.setClienteList(new ArrayList<Cliente>());
        }
        if (tienda.getServicioList() == null) {
            tienda.setServicioList(new ArrayList<Servicio>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : tienda.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getId());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            tienda.setClienteList(attachedClienteList);
            List<Servicio> attachedServicioList = new ArrayList<Servicio>();
            for (Servicio servicioListServicioToAttach : tienda.getServicioList()) {
                servicioListServicioToAttach = em.getReference(servicioListServicioToAttach.getClass(), servicioListServicioToAttach.getId());
                attachedServicioList.add(servicioListServicioToAttach);
            }
            tienda.setServicioList(attachedServicioList);
            em.persist(tienda);
            for (Cliente clienteListCliente : tienda.getClienteList()) {
                clienteListCliente.getTiendaList().add(tienda);
                clienteListCliente = em.merge(clienteListCliente);
            }
            for (Servicio servicioListServicio : tienda.getServicioList()) {
                Tienda oldTiendaOfServicioListServicio = servicioListServicio.getTienda();
                servicioListServicio.setTienda(tienda);
                servicioListServicio = em.merge(servicioListServicio);
                if (oldTiendaOfServicioListServicio != null) {
                    oldTiendaOfServicioListServicio.getServicioList().remove(servicioListServicio);
                    oldTiendaOfServicioListServicio = em.merge(oldTiendaOfServicioListServicio);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tienda tienda) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tienda persistentTienda = em.find(Tienda.class, tienda.getId());
            List<Cliente> clienteListOld = persistentTienda.getClienteList();
            List<Cliente> clienteListNew = tienda.getClienteList();
            List<Servicio> servicioListOld = persistentTienda.getServicioList();
            List<Servicio> servicioListNew = tienda.getServicioList();
            List<String> illegalOrphanMessages = null;
            for (Servicio servicioListOldServicio : servicioListOld) {
                if (!servicioListNew.contains(servicioListOldServicio)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Servicio " + servicioListOldServicio + " since its tienda field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cliente> attachedClienteListNew = new ArrayList<Cliente>();
            for (Cliente clienteListNewClienteToAttach : clienteListNew) {
                clienteListNewClienteToAttach = em.getReference(clienteListNewClienteToAttach.getClass(), clienteListNewClienteToAttach.getId());
                attachedClienteListNew.add(clienteListNewClienteToAttach);
            }
            clienteListNew = attachedClienteListNew;
            tienda.setClienteList(clienteListNew);
            List<Servicio> attachedServicioListNew = new ArrayList<Servicio>();
            for (Servicio servicioListNewServicioToAttach : servicioListNew) {
                servicioListNewServicioToAttach = em.getReference(servicioListNewServicioToAttach.getClass(), servicioListNewServicioToAttach.getId());
                attachedServicioListNew.add(servicioListNewServicioToAttach);
            }
            servicioListNew = attachedServicioListNew;
            tienda.setServicioList(servicioListNew);
            tienda = em.merge(tienda);
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    clienteListOldCliente.getTiendaList().remove(tienda);
                    clienteListOldCliente = em.merge(clienteListOldCliente);
                }
            }
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    clienteListNewCliente.getTiendaList().add(tienda);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                }
            }
            for (Servicio servicioListNewServicio : servicioListNew) {
                if (!servicioListOld.contains(servicioListNewServicio)) {
                    Tienda oldTiendaOfServicioListNewServicio = servicioListNewServicio.getTienda();
                    servicioListNewServicio.setTienda(tienda);
                    servicioListNewServicio = em.merge(servicioListNewServicio);
                    if (oldTiendaOfServicioListNewServicio != null && !oldTiendaOfServicioListNewServicio.equals(tienda)) {
                        oldTiendaOfServicioListNewServicio.getServicioList().remove(servicioListNewServicio);
                        oldTiendaOfServicioListNewServicio = em.merge(oldTiendaOfServicioListNewServicio);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tienda.getId();
                if (findTienda(id) == null) {
                    throw new NonexistentEntityException("The tienda with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tienda tienda;
            try {
                tienda = em.getReference(Tienda.class, id);
                tienda.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tienda with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Servicio> servicioListOrphanCheck = tienda.getServicioList();
            for (Servicio servicioListOrphanCheckServicio : servicioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tienda (" + tienda + ") cannot be destroyed since the Servicio " + servicioListOrphanCheckServicio + " in its servicioList field has a non-nullable tienda field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cliente> clienteList = tienda.getClienteList();
            for (Cliente clienteListCliente : clienteList) {
                clienteListCliente.getTiendaList().remove(tienda);
                clienteListCliente = em.merge(clienteListCliente);
            }
            em.remove(tienda);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tienda> findTiendaEntities() {
        return findTiendaEntities(true, -1, -1);
    }

    public List<Tienda> findTiendaEntities(int maxResults, int firstResult) {
        return findTiendaEntities(false, maxResults, firstResult);
    }

    private List<Tienda> findTiendaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tienda.class));
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

    public Tienda findTienda(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tienda.class, id);
        } finally {
            em.close();
        }
    }

    public int getTiendaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tienda> rt = cq.from(Tienda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
