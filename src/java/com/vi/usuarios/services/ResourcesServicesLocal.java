
package com.vi.usuarios.services;

import com.vi.comun.exceptions.LlaveDuplicadaException;
import com.vi.usuarios.dominio.Resource;
import java.util.List;
import javax.ejb.Local;

/**
 * @author root
 */
@Local
public interface ResourcesServicesLocal {
    void create(Resource user)throws LlaveDuplicadaException;

    void edit(Resource user)throws LlaveDuplicadaException;

    void remove(Resource user);

    Resource find(Object id);

    public List<Resource> findAll(String language);

    public Resource findByUrl(String permisostrabajoactivar_empleadosxhtml);
}
