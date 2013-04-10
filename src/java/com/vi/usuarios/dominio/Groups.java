/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.usuarios.dominio;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author jerviver21
 */
@Entity
@Table(name = "groups")
@NamedQueries({
    @NamedQuery(name = "Groups.findAll", query = "SELECT g FROM Groups g"),
    @NamedQuery(name = "Groups.findByCodigo", query = "SELECT g FROM Groups g WHERE g.codigo=:codigo")
})
public class Groups implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Basic(optional = false)
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "descripcion")
    private String descripcion;

    @ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.MERGE)
    @JoinTable(name="group_rol",
            joinColumns=@JoinColumn(name="id_group", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="id_rol",referencedColumnName="id"))
    private List<Rol> roles;
    
    @Transient
    private boolean addUser;
     
    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    public Groups() {
    }

    public Groups(Long id) {
        this.id = id;
    }

    public Groups(Long id, String codigo) {
        this.id = id;
        this.codigo = codigo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
        if (!(object instanceof Groups)) {
            return false;
        }
        Groups other = (Groups) object;
        if ((this.id == null && other.getId() != null) || (this.id != null && !this.id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return codigo;
    }

    /**
     * @return the addUser
     */
    public boolean isAddUser() {
        return addUser;
    }

    /**
     * @param addUser the addUser to set
     */
    public void setAddUser(boolean addUser) {
        this.addUser = addUser;
    }




   
    
}
