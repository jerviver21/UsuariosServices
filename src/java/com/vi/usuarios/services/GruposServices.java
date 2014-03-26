package com.vi.usuarios.services;

import com.vi.comun.exceptions.LlaveDuplicadaException;
import com.vi.usuarios.dominio.Groups;
import com.vi.usuarios.dominio.Licencia;
import com.vi.usuarios.dominio.Rol;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.exception.ConstraintViolationException;

/**
 * @author root
 */
@Stateless
public class GruposServices implements GruposServicesLocal {
    
    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager em;

    public GruposServices() {
    }
    
    @Override
    public void create(Groups entity) throws LlaveDuplicadaException{
        try {
            em.persist(entity);
        } catch (ConstraintViolationException e) {
            throw new LlaveDuplicadaException("El registro ya existe");
        }
    }

    @Override
    public void edit(Groups entity) throws LlaveDuplicadaException{
        try {
            em.merge(entity);
        } catch (ConstraintViolationException e) {
            throw new LlaveDuplicadaException("El registro ya existe");
        }
    }
    
    @Override
    public Groups find(Object id) {
        return em.find(Groups.class, id);
    }

    @Override
    public void remove(Groups group){
        group.setRoles(new ArrayList<Rol>());
        Groups g = em.merge(group);
        em.remove(g);
    }
    
    @Override
    public List<Rol> findRolesByGroup(Groups group){
        group = (Groups)em.find(Groups.class, group.getId());
        group.getRoles().size();
        return group.getRoles();
    }

    @Override
    public List<Groups> findAll(){
        List<Groups> roles = em.createNamedQuery("Groups.findAll").getResultList();
        return roles;
    }
    
    @Override
    public List<Groups> findByLicencia(Licencia licencia){
        List<Groups> roles = em.createNamedQuery("Groups.findByLicencia").setParameter("licencia", licencia).getResultList();
        return roles;
    }

    @Override
    public Groups findByCodigo(String codigo) {
        List<Groups> grupo = em.createQuery("SELECT g FROM Groups g WHERE g.codigo =:codigo").setParameter("codigo", codigo).getResultList();
        if(grupo.size() > 0){
            return grupo.get(0);
        }
        return null;
    }




 
}
