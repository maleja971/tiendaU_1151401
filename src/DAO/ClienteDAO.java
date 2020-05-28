/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DAO.exceptions.NonexistentEntityException;
import DTO.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import DTO.Tienda;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class ClienteDAO implements Serializable {

    public ClienteDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public ClienteDAO(){
        this.emf = Conexion.getEm();
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) {
        if (cliente.getTiendaList() == null) {
            cliente.setTiendaList(new ArrayList<Tienda>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Tienda> attachedTiendaList = new ArrayList<Tienda>();
            for (Tienda tiendaListTiendaToAttach : cliente.getTiendaList()) {
                tiendaListTiendaToAttach = em.getReference(tiendaListTiendaToAttach.getClass(), tiendaListTiendaToAttach.getId());
                attachedTiendaList.add(tiendaListTiendaToAttach);
            }
            cliente.setTiendaList(attachedTiendaList);
            em.persist(cliente);
            for (Tienda tiendaListTienda : cliente.getTiendaList()) {
                tiendaListTienda.getClienteList().add(cliente);
                tiendaListTienda = em.merge(tiendaListTienda);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getId());
            List<Tienda> tiendaListOld = persistentCliente.getTiendaList();
            List<Tienda> tiendaListNew = cliente.getTiendaList();
            List<Tienda> attachedTiendaListNew = new ArrayList<Tienda>();
            for (Tienda tiendaListNewTiendaToAttach : tiendaListNew) {
                tiendaListNewTiendaToAttach = em.getReference(tiendaListNewTiendaToAttach.getClass(), tiendaListNewTiendaToAttach.getId());
                attachedTiendaListNew.add(tiendaListNewTiendaToAttach);
            }
            tiendaListNew = attachedTiendaListNew;
            cliente.setTiendaList(tiendaListNew);
            cliente = em.merge(cliente);
            for (Tienda tiendaListOldTienda : tiendaListOld) {
                if (!tiendaListNew.contains(tiendaListOldTienda)) {
                    tiendaListOldTienda.getClienteList().remove(cliente);
                    tiendaListOldTienda = em.merge(tiendaListOldTienda);
                }
            }
            for (Tienda tiendaListNewTienda : tiendaListNew) {
                if (!tiendaListOld.contains(tiendaListNewTienda)) {
                    tiendaListNewTienda.getClienteList().add(cliente);
                    tiendaListNewTienda = em.merge(tiendaListNewTienda);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getId();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<Tienda> tiendaList = cliente.getTiendaList();
            for (Tienda tiendaListTienda : tiendaList) {
                tiendaListTienda.getClienteList().remove(cliente);
                tiendaListTienda = em.merge(tiendaListTienda);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
