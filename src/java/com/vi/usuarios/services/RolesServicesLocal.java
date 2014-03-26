package com.vi.usuarios.services;

import com.vi.comun.exceptions.LlaveDuplicadaException;
import com.vi.usuarios.dominio.Groups;
import com.vi.usuarios.dominio.Licencia;
import com.vi.usuarios.dominio.Resource;
import com.vi.usuarios.dominio.Rol;
import com.vi.usuarios.dominio.Users;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;

/**
 * @author Jerson Viveros
 */
@Local
public interface RolesServicesLocal {
    void create(Rol user)throws LlaveDuplicadaException;

    void edit(Rol user)throws LlaveDuplicadaException;

    void remove(Rol user);

    Rol find(Object id);

    public List<Rol> findAll();
    
    public List<Rol> findByLicencia(Licencia licencia);

    public Set<Resource> findResourceByRol(Rol rol);

    public Rol findByCodigo(String admistradoR);

    List<Groups> findGruposByRol(String rol);
    
    List<Users> findUsersByRol(String rol);
}
