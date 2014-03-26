/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vi.usuarios.dominio;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author Jerson Viveros
 */
@Entity
@Table(name = "rol")
@NamedQueries({
    @NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r"),
    @NamedQuery(name = "Rol.findByLicencia", query = "SELECT r FROM Rol r WHERE r.licencia =:licencia or r.licencia is null"),
    @NamedQuery(name = "Rol.findByCodigo", query = "SELECT r FROM Rol r WHERE r.codigo=:codigo")
})
public class Rol implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Basic(optional = false)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @Column(name = "descripcion")
    private String descripcion;
    @Transient
    private boolean addGroup;
    
    @JoinColumn(name = "id_licencia", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Licencia licencia;
    

    @ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(name="rol_resource",
            joinColumns=@JoinColumn(name="id_rol", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="id_resource",referencedColumnName="id"))
    private Set<Resource> recursos;

    public Set<Resource> getRecursos() {
        return recursos;
    }

    public void setRecursos(Set<Resource> recursos) {
        this.recursos = recursos;
    }
    

    public Rol() {
    }

    public Rol(Long id) {
        this.id = id;
    }

    public Rol(Long id, String codigo, String descripcion) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
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
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
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
     * @return the addGroup
     */
    public boolean isAddGroup() {
        return addGroup;
    }

    /**
     * @param addGroup the addGroup to set
     */
    public void setAddGroup(boolean addGroup) {
        this.addGroup = addGroup;
    }

    /**
     * @return the licencia
     */
    public Licencia getLicencia() {
        return licencia;
    }

    /**
     * @param licencia the licencia to set
     */
    public void setLicencia(Licencia licencia) {
        this.licencia = licencia;
    }



   

}
