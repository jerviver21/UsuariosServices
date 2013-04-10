
package com.vi.usuarios.services;

import com.vi.comun.exceptions.LlaveDuplicadaException;
import com.vi.usuarios.dominio.Groups;
import com.vi.usuarios.dominio.Users;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.NoResultException;

/**
 * @author Jerson Viveros 
 */
@Local
public interface UsuariosServicesLocal {
    void create(Users user)throws LlaveDuplicadaException;

    void edit(Users user)throws LlaveDuplicadaException;

    void remove(Users user);

    Users find(Object id);

    public List<Users> findAll();

    public Users findByUser(String usr);

    public List<Users> findByUserFragment(String usr);
    
    public List<Groups> findGroupsUser(Users usr);
    
    public Users findFullUser(String usr)throws NoResultException;

    public boolean isUsuarioDisponible(String usr);
    
}
