/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.vi.usuarios.services;


import com.vi.comun.exceptions.LlaveDuplicadaException;
import com.vi.usuarios.dominio.Groups;
import com.vi.usuarios.dominio.Licencia;
import com.vi.usuarios.dominio.Rol;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author root
 */
@Local
public interface GruposServicesLocal {
    void create(Groups user)throws LlaveDuplicadaException;

    void edit(Groups user)throws LlaveDuplicadaException;

    void remove(Groups user);

    Groups find(Object id);

    List<Groups> findAll();
    
    List<Groups> findByLicencia(Licencia licencia);

    List<Rol> findRolesByGroup(Groups group);

    Groups findByCodigo(String codigo);
}
