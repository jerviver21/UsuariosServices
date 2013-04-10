
package com.vi.usuarios.services;

import com.vi.comun.exceptions.LlaveDuplicadaException;
import com.vi.usuarios.dominio.Groups;
import com.vi.usuarios.dominio.Resource;
import com.vi.usuarios.dominio.Rol;
import com.vi.usuarios.dominio.Users;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.exception.ConstraintViolationException;

/**
 * @author Jerson Viveros
 */
@Stateless
public class UsuariosServices implements UsuariosServicesLocal {

    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager em;

     @Override
    public void create(Users entity) throws LlaveDuplicadaException{
        try {
            em.persist(entity);
        } catch (ConstraintViolationException e) {
            throw new LlaveDuplicadaException("El menú ya existe");
        }
    }

    @Override
    public void edit(Users entity) throws LlaveDuplicadaException{
        try {
            em.merge(entity);
        } catch (ConstraintViolationException e) {
            throw new LlaveDuplicadaException("El menú ya existe");
        }
    }
    
    @Override
    public Users find(Object id) {
        return em.find(Users.class, id);
    }
    
    
    @Override
    public void remove(Users user){
        user.setGrupos(new ArrayList<Groups>());
        Users u = em.merge(user);
        em.remove(u);
    }
    

    @Override
    public List<Users> findAll(){
        List<Users> usuarios = em.createNamedQuery("Users.findAll").getResultList();
        return usuarios;
    }

    @Override
    public Users findByUser(String usr){
        List<Users> usuarios = em.createQuery("SELECT u FROM Users u WHERE u.usr = '"+usr+"'").getResultList();
        if(usuarios.size() > 0){
            return usuarios.get(0);
        }
        return null;
    }


    @Override
    public List<Users> findByUserFragment(String usr){
        List<Users> usuarios = em.createQuery("SELECT u FROM Users u WHERE u.usr LIKE '%"+usr+"%'").getResultList();
        return usuarios;
    }
    
    @Override
    public List<Groups> findGroupsUser(Users usr){
        usr = (Users)em.find(Users.class, usr.getId());
        usr.getGrupos().size();
        return usr.getGrupos();
    }
    
    @Override
    public Users findFullUser(String usr)throws NoResultException{
        Users usuario = (Users)em.createNamedQuery("Users.findUserByUsr")
                .setParameter("usr", usr).getSingleResult();
        usuario.setRecursos(new TreeSet<Resource>());
        List<Groups> grupos = usuario.getGrupos();
        for(Groups grupo:grupos){
            List<Rol> roles = grupo.getRoles();
            for(Rol rol : roles){
                Set<Resource> recursos = rol.getRecursos();
                for(Resource recurso:recursos){
                    System.out.println(recurso.getNombre()+" - "+recurso.getUrl());
                }
                usuario.getRecursos().addAll(recursos);
            }
        }
        return usuario;
    }

    @Override
    public boolean isUsuarioDisponible(String usr) {
        List<Users> users = em.createQuery("SELECT u FROM Users u WHERE u.usr = :usr ").setParameter("usr", usr).getResultList();
        if(users.isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    
    
}
