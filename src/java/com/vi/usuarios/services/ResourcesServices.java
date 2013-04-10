
package com.vi.usuarios.services;

import com.vi.comun.exceptions.LlaveDuplicadaException;
import com.vi.usuarios.dominio.Resource;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.exception.ConstraintViolationException;

/**
 * @author Jerson Viveros
 */
@Stateless
public class ResourcesServices implements ResourcesServicesLocal {
    
    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager em;

     @Override
    public void create(Resource entity) throws LlaveDuplicadaException{
        try {
            em.persist(entity);
        } catch (ConstraintViolationException e) {
            throw new LlaveDuplicadaException("El menú ya existe");
        }
    }

    @Override
    public void edit(Resource entity) throws LlaveDuplicadaException{
        try {
            em.merge(entity);
        } catch (ConstraintViolationException e) {
            throw new LlaveDuplicadaException("El menú ya existe");
        }
    }
    
    @Override
    public Resource find(Object id) {
        return em.find(Resource.class, id);
    }

    @Override
    public void remove(Resource entity){
        em.remove(em.merge(entity));
    }
    
    
    @Override
    public List<Resource> findAll(String language){
        List<Resource> recursos = em.createNamedQuery("Resource.findAll").getResultList();
        List<Resource> recursosIdioma = new ArrayList<Resource>();

        for(Resource recurso : recursos){
            if(recurso.getIdioma() != null && recurso.getIdioma().equalsIgnoreCase(language)){
                recursosIdioma.add(recurso);
            }
        }


        return recursosIdioma;
    }

    @Override
    public Resource findByUrl(String url) {
        Resource resource = null;
        List<Resource> recursos = em.createNamedQuery("Resource.findByUrl").setParameter("url", url).getResultList();
        if(recursos.size() > 0){
            return recursos.get(0);
        }
        return resource;
    }

 
}
