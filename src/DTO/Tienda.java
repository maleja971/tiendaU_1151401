
package DTO;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;



public class Tienda implements Serializable {

    private static final long serialVersionUID = 1L;
    
     private Integer id;
     private String nombre;
     private String lema;
     private String descripcion;
     private String email;
     private String clave;
     private String propietario;
     private String facebook;
     private String web;
     private String imagen;
     private List<Cliente> clienteList;
     private List<Servicio> servicioList;

    public Tienda() {
    }

    public Tienda(Integer id) {
        this.id = id;
    }

    public Tienda(Integer id, String nombre, String lema, String email, String clave, String propietario, String facebook, String web, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.lema = lema;
        this.email = email;
        this.clave = clave;
        this.propietario = propietario;
        this.facebook = facebook;
        this.web = web;
        this.imagen = imagen;
    }
    public Tienda(Integer id, String nombre, String lema, String descripcion, String email,String clave, String propietario, String facebook, String web, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.lema = lema;
        this.descripcion = descripcion;
        this.email = email;
        this.clave = clave;
        this.propietario = propietario;
        this.facebook = facebook;
        this.web = web;
        this.imagen = imagen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLema() {
        return lema;
    }

    public void setLema(String lema) {
        this.lema = lema;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @XmlTransient
    public List<Cliente> getClienteList() {
        return clienteList;
    }

    public void setClienteList(List<Cliente> clienteList) {
        this.clienteList = clienteList;
    }

    @XmlTransient
    public List<Servicio> getServicioList() {
        return servicioList;
    }

    public void setServicioList(List<Servicio> servicioList) {
        this.servicioList = servicioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tienda)) {
            return false;
        }
        Tienda other = (Tienda) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DTO.Tienda[ id=" + id + " ]";
    }
    
}
