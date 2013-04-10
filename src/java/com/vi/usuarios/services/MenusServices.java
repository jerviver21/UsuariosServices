package com.vi.usuarios.services;


import com.vi.comun.exceptions.LlaveDuplicadaException;
import com.vi.usuarios.dominio.Menu;
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
public class MenusServices implements MenusServicesLocal {
    
    @PersistenceContext(unitName = "UsuariosPU")
    private EntityManager em;

    @Override
    public void create(Menu entity) throws LlaveDuplicadaException{
        try {
            em.persist(entity);
        } catch (ConstraintViolationException e) {
            throw new LlaveDuplicadaException("El menú ya existe");
        }
    }

    @Override
    public void edit(Menu entity) throws LlaveDuplicadaException{
        try {
            em.merge(entity);
        } catch (ConstraintViolationException e) {
            throw new LlaveDuplicadaException("El menú ya existe");
        }
    }
    
    @Override
    public Menu find(Object id) {
        return em.find(Menu.class, id);
    }

    @Override
    public void remove(Menu entity){
        em.remove(em.merge(entity));
    }
    

    @Override
    public List<Menu> findAll(String language){
        List<Menu> menus = em.createNamedQuery("Menu.findAll").getResultList();
        List<Menu> menusIdioma = new ArrayList<Menu>();
        for(Menu menu:menus){
            if(menu.getIdioma() != null &&  menu.getIdioma().equalsIgnoreCase(language)){
                menusIdioma.add(menu);
            }
        }
        return menusIdioma;
    }

    @Override
    public Menu findByNombre(String nombre) {
        List<Menu> menus = em.createQuery("Select m FROM Menu m WHERE m.nombre = :nombre")
                .setParameter("nombre", nombre).getResultList();
        if(!menus.isEmpty()){
            return menus.get(0);
        }
        return null;
    }
 
}
