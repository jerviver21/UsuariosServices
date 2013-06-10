/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vi.usuarios.dominio;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

/**
 *
 * @author jerviver21
 */
@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findUserByUsr", query = "SELECT u FROM Users u WHERE u.usr = :usr"),
    @NamedQuery(name = "Users.findUserByCodRes", query = "SELECT u FROM Users u WHERE u.codRestauracion = :cod"),
    @NamedQuery(name = "Users.findUserByNroUsrAndEstado", query = "SELECT u FROM Users u WHERE u.nroUsuario = :cod AND u.estado =:estado")
})
public class Users implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Basic(optional = false)
    @Column(name = "usr")
    private String usr;
    @Basic(optional = false)
    @Column(name = "pwd")
    private String pwd = "";
    @Column(name = "clave")
    private String clave = "";
    @Basic(optional = false)
    @Column(name = "estado")
    private Integer estado = 1;
    @Column(name = "nombre")
    private String nombre = "";
    @Column(name = "mail")
    private String mail = "";
    @Column(name = "nro_usuario")
    private String nroUsuario;
    
    @Column(name = "cod_restauracion")
    private String codRestauracion;

    @ManyToMany(fetch=FetchType.LAZY, cascade=CascadeType.MERGE)
    @JoinTable(name="user_group",
            joinColumns=@JoinColumn(name="id_user", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="id_group",referencedColumnName="id"))
    private List<Groups> grupos;
    
    @Transient
    private Set<Resource> recursos;

    @Transient
    private Set<String> rolesUsr;

    @Transient
    private Set<String> gruposUsr;
    
    /**
     * @return the grupos
     */
    public List<Groups> getGrupos() {
        return grupos;
    }

    /**
     * @param grupos the grupos to set
     */
    public void setGrupos(List<Groups> grupos) {
        this.grupos = grupos;
    }

    public Users() {
    }

    public Users(Long id) {
        this.id = id;
    }

    public Users(Long id, String usr, String pwd) {
        this.id = id;
        this.usr = usr;
        this.pwd = pwd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
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
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.getId() != null) || (this.id != null && !this.id.equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vi.permisostrabajo.dominio.Users[ id=" + id + " ]";
    }

    /**
     * @return the recursosUsr
     */
    public Set<Resource> getRecursos() {
        return recursos;
    }

    /**
     * @param recursosUsr the recursosUsr to set
     */
    public void setRecursos(Set<Resource> recursosUsr) {
        this.recursos = recursosUsr;
    }

    /**
     * @return the rol
     */
    public Set<String> getRolesUsr() {
        return rolesUsr;
    }

    /**
     * @param rol the rol to set
     */
    public void setRolesUsr(Set<String> rol) {
        this.rolesUsr = rol;
    }

    /**
     * @return the gruposUsr
     */
    public Set<String> getGruposUsr() {
        return gruposUsr;
    }

    /**
     * @param gruposUsr the gruposUsr to set
     */
    public void setGruposUsr(Set<String> gruposUsr) {
        this.gruposUsr = gruposUsr;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * @param mail the mail to set
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * @return the estado
     */
    public Integer getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    /**
     * @return the codRestauracion
     */
    public String getCodRestauracion() {
        return codRestauracion;
    }

    /**
     * @param codRestauracion the codRestauracion to set
     */
    public void setCodRestauracion(String codRestauracion) {
        this.codRestauracion = codRestauracion;
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * @return the nroUsuario
     */
    public String getNroUsuario() {
        return nroUsuario;
    }

    /**
     * @param nroUsuario the nroUsuario to set
     */
    public void setNroUsuario(String nroUsuario) {
        this.nroUsuario = nroUsuario;
    }

    


    
}
