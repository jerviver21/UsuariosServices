package com.vi.usuarios.services;

import com.vi.comun.exceptions.LlaveDuplicadaException;
import com.vi.usuarios.dominio.Groups;
import com.vi.usuarios.dominio.Resource;
import com.vi.usuarios.dominio.Rol;
import com.vi.usuarios.dominio.Users;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.exception.ConstraintViolationException;

/**
 * @author Jerson Viveros
 */
@Stateless
public class RolesServices implements RolesServicesLocal {

    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager em;

     @Override
    public void create(Rol entity) throws LlaveDuplicadaException{
        try {
            em.persist(entity);
        } catch (ConstraintViolationException e) {
            throw new LlaveDuplicadaException("El menú ya existe");
        }
    }

    @Override
    public void edit(Rol entity) throws LlaveDuplicadaException{
        try {
            em.merge(entity);
        } catch (ConstraintViolationException e) {
            throw new LlaveDuplicadaException("El menú ya existe");
        }
    }
    
    @Override
    public Rol find(Object id) {
        return em.find(Rol.class, id);
    }

    

    @Override
    public void remove(Rol rol){
        rol.setRecursos(new HashSet<Resource>());
        Rol r = em.merge(rol);
        em.remove(r);
    }


    @Override
    public Set<Resource> findResourceByRol(Rol rol){
        rol = (Rol)em.find(Rol.class, rol.getId());
        rol.getRecursos().size();
        return rol.getRecursos();
    }


    @Override
    public List<Rol> findAll(){
        List<Rol> roles = em.createNamedQuery("Rol.findAll").getResultList();
        return roles;
    }

    @Override
    public Rol findByCodigo(String codigo) {
        Rol rol = null;
        List<Rol> roles = em.createNamedQuery("Rol.findByCodigo").setParameter("codigo", codigo).getResultList();
        if(roles.size() > 0){
            return roles.get(0);
        }
        return rol;
    }

    @Override
    public List<Groups> findGruposByRol(String rol) {
        List<Groups> grupos = em.createQuery("SELECT g "
                + "FROM Groups g JOIN g.roles r "
                + "WHERE r.codigo =:rol ").setParameter("rol", rol).getResultList();
        return grupos;
    }
    
    @Override
    public List<Users> findUsersByRol(String rol) {
        List<Users> users = em.createQuery("SELECT u "
                        + "FROM Users u JOIN u.grupos g JOIN g.roles r "
                        + "WHERE r.codigo = :rol")
                .setParameter("rol", rol).getResultList();
        return users;
    }


    
}
