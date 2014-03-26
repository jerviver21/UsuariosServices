
package com.vi.usuarios.services;

import com.vi.comun.exceptions.LlaveDuplicadaException;
import com.vi.comun.exceptions.ParametroException;
import com.vi.usuarios.dominio.Groups;
import com.vi.usuarios.dominio.Licencia;
import com.vi.usuarios.dominio.Users;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.persistence.NoResultException;

/**
 * @author Jerson Viveros 
 */
@Local
public interface UsuariosServicesLocal {
    void create(Users user)throws LlaveDuplicadaException, Exception;

    void edit(Users user)throws LlaveDuplicadaException, Exception;

    void remove(Users user);

    Users find(Object id);

    public List<Users> findAll();

    public Users findByUser(String usr);

    public List<Users> findByUserFragment(String usr);
    
    public List<Groups> findGroupsUser(Users usr);
    
    public Set<String> findRolesUser(String usr);
    
    public Users findFullUser(String usr)throws NoResultException;

    public boolean isUsuarioDisponible(String usr);

    public void activarUsuario(String usr);

    public void desactivarUsuario(String email);
    
    public void solicitarRestauracion(String mail)throws Exception;

    public void restaurarClave(String claveEncryp, String clave, String codigo)throws Exception;

    public void registrar(Users usuarioRegistrar, String grupo, boolean enviarCorreo) throws  LlaveDuplicadaException, ParametroException, MessagingException, AuthenticationFailedException;

    public Users activar(String nroUsuario)throws ParametroException, NoResultException;
    
    public List<Users> findUsersByLicencia(Licencia licencia);
    
}
