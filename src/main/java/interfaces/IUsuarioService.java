package interfaces;

import java.util.Collection;
import classes.Usuario;
import services.EditException;

public interface IUsuarioService {

    public void addUsuario(Usuario usuario);
    public Collection<Usuario> getUsuarios();
    public Usuario getUsuario(Integer id);
    public Usuario editUsuario(Usuario usuario) throws EditException;
    public void deleteUsuario(Integer id);
    public boolean usuarioExists(Integer id);
}
