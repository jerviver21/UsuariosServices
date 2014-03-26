
package com.vi.usuarios.services;

import com.vi.comun.dominio.AudMail;
import com.vi.comun.exceptions.LlaveDuplicadaException;
import com.vi.comun.exceptions.ParametroException;
import com.vi.comun.locator.ParameterLocator;
import com.vi.comun.services.MailService;
import com.vi.comun.util.Encriptador;
import com.vi.comun.util.FechaUtils;
import com.vi.usuarios.dominio.Groups;
import com.vi.usuarios.dominio.Licencia;
import com.vi.usuarios.dominio.Resource;
import com.vi.usuarios.dominio.Rol;
import com.vi.usuarios.dominio.Users;
import com.vi.utils.UsuarioEstados;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
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
    
    @EJB
    MailService mailService;
    
    @EJB
    GruposServicesLocal grupoServices;
    
    ParameterLocator locator;
    
    public UsuariosServices(){
        locator = ParameterLocator.getInstance();
    }

     @Override
    public void create(Users entity) throws LlaveDuplicadaException, Exception{
        try {
            em.persist(entity);
        } catch (Exception e) {
            if(e instanceof ConstraintViolationException || (e.getCause() != null && e.getCause() instanceof ConstraintViolationException)){
                throw new LlaveDuplicadaException("El usuario "+entity.getUsr()+" ya existe");
            }
            throw e;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void edit(Users entity) throws LlaveDuplicadaException, Exception{
        try {
            em.merge(entity);
        } catch (Exception e) {
            if(e instanceof ConstraintViolationException || (e.getCause() != null && e.getCause() instanceof ConstraintViolationException)){
                throw new LlaveDuplicadaException("El usuario "+entity.getUsr()+" ya existe");
            }
            throw e;
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
    public List<Users> findUsersByLicencia(Licencia licencia){
        List<Users> usuarios = em.createNamedQuery("Users.findByLicencia").setParameter("licencia", licencia).getResultList();
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

    @Override
    public void activarUsuario(String usr) {
        Users usuario = (Users)em.createNamedQuery("Users.findUserByUsr").setParameter("usr", usr).getSingleResult();
        usuario.setEstado(UsuarioEstados.ACTIVO);
        em.merge(usuario);
    }

    @Override
    public void desactivarUsuario(String usr) {
        Users usuario = (Users)em.createNamedQuery("Users.findUserByUsr").setParameter("usr", usr).getSingleResult();
        usuario.setEstado(UsuarioEstados.INACTIVO);
        em.merge(usuario);
    }

    @Override
    public void solicitarRestauracion(String mail) throws Exception{
        ParameterLocator locator = ParameterLocator.getInstance();
        Users usr = findByUser(mail);
        if(usr == null){
            throw new Exception("El usuario: "+mail+" no es valido o no se ha registrado en el sistema");
        }
        long codigoRestauracion = Math.abs(((int)(Math.random()*100000))+usr.getUsr().hashCode());
        usr.setCodRestauracion(codigoRestauracion+"");
        em.merge(usr);
        em.flush();
        
        //Envio de e-mail para la restauración
        String url = locator.getParameter("url");
        if(url == null){
            throw new ParametroException("No se encuentra el parámetro url");
        }


        AudMail datosMail = new AudMail();
        datosMail.setDestinatario(usr.getUsr());
        datosMail.setAsunto("Restauracion Clave Medical History System!");
        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Ingrese a la Dirección : \n ");
        mensaje.append(locator.getParameter("url"));
        mensaje.append("/registro/restaura_clave.xhtml \n");
        mensaje.append("\n Utilice el siguiente código para restaurar su clave:");
        mensaje.append(codigoRestauracion);
        datosMail.setMensaje(mensaje.toString());

        mailService.enviarMail(datosMail);
    }

    @Override
    public void restaurarClave(String claveEncryp, String clave, String codigo)throws Exception{
        List<Users> usrs = em.createNamedQuery("Users.findUserByCodRes").setParameter("cod", codigo).getResultList();
        if(usrs.isEmpty()){
            throw new Exception("El código de restauración es invalido");
        }
        Users usr = usrs.get(0);
        usr.setPwd(claveEncryp);
        usr.setClave(Encriptador.encrypt(clave));
        usr.setCodRestauracion(null);
        em.merge(usr);
        
    }

    @Override
    public void registrar(Users usuario, String grupo, boolean enviarCorreo) throws  LlaveDuplicadaException, ParametroException, MessagingException, AuthenticationFailedException{
        System.out.println("Inicio Registro!");
        usuario.setMail(usuario.getUsr());
        usuario.setClave(Encriptador.encrypt(usuario.getClave()));
        //usuario.setEstado(UsuarioEstados.INACTIVO);
        usuario.setEstado(UsuarioEstados.ACTIVO);
        Groups group =  grupoServices.findByCodigo(grupo);
        List<Groups> grupos = new ArrayList<Groups>();
        grupos.add(group);
        usuario.setGrupos(grupos);
        try {
            usuario = em.merge(usuario);
        } catch (ConstraintViolationException e) {
            throw new LlaveDuplicadaException("El usuario ya existe");
        }
        String prefijoAnos = (""+FechaUtils.getAnoActual()).substring(2) + (""+(FechaUtils.getAnoActual()+1)).substring(2);
        usuario.setNroUsuario(prefijoAnos+String.format("%08d",  usuario.getId().intValue()));
        em.merge(usuario);
        
        //throw new ParametroException("");
        //Envio de email, para activación de usuario
        /*if(enviarCorreo){
            //em.flush();
            String url = locator.getParameter("url");
            if(url == null){
                throw new ParametroException("No se encuentra el parámetro url");
            }
            
            
            AudMail datosMail = new AudMail();
            datosMail.setDestinatario(usuario.getUsr());
            datosMail.setAsunto("Activacion Usuario Sistema");
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("Ingrese a la Dirección : \n ");
            mensaje.append(locator.getParameter("url"));
            mensaje.append("/registro/activacion.xhtml \n");
            mensaje.append("\n Active el siguiente Nro de Usuario:");
            
            
            mensaje.append(usuario.getNroUsuario());
            mensaje.append("\n( Copie el nro de licencia en el campo y presione el boton activar) \n\n\n Paideia Software. (Hacemos el mejor software!) \n\n Correo Automático por favor no responda a este correo.");
            
            datosMail.setMensaje(mensaje.toString());
            
            mailService.enviarMail(datosMail);
            
            //*****************************************************
        }*/
        System.out.println("Fin Registro"); 
    }

    
    @Override
    public Users activar(String nroUsr)throws ParametroException, NoResultException{
        Users usr = (Users) em.createNamedQuery("Users.findUserByNroUsrAndEstado")
                .setParameter("cod", nroUsr)
                .setParameter("estado", UsuarioEstados.INACTIVO).getSingleResult();
        usr.setEstado(UsuarioEstados.ACTIVO);
        em.merge(usr);
        return usr;
    }
    
    @Override
    public Set<String> findRolesUser(String usr){
        List<String> roles = em.createNativeQuery("SELECT r.codigo\n" +
                                                  "FROM users as u, groups as g, user_group as ug, rol as r, group_rol as gr\n" +
                                                  "WHERE ug.id_group = g.id AND ug.id_user = u.id AND gr.id_group = g.id AND gr.id_rol = r.id AND usr = '"+usr+"'").getResultList();
        return new HashSet<String>(roles);
    }


    
    
}
