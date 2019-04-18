package services;

import java.util.Collection;
import java.util.HashMap;

import services.EditException;
import classes.Usuario;
import interfaces.IUsuarioService;

public class UsuarioServiceMap implements IUsuarioService {

    private HashMap<Integer, Usuario> usuarioMap;

    public UsuarioServiceMap() {
        this.usuarioMap = new HashMap<>();
    }

    public UsuarioServiceMap(HashMap<Integer, Usuario> usuarioMap) {
        this.usuarioMap = usuarioMap;
    }

    @Override
    public void addUsuario(Usuario usuario) {
        usuarioMap.put(usuario.getId(), usuario);
    }

    @Override
    public Collection<Usuario> getUsuarios() {
        return usuarioMap.values();
    }

    @Override
    public Usuario getUsuario(Integer id) {
        return usuarioMap.get(id);
    }

    @Override
    public Usuario editUsuario(Usuario usuario) throws EditException {
        try {
            if (usuario.getId() == null) {
                throw new EditException("El id no puede ser nulo");
            }
            Usuario usuarioEditar = usuarioMap.get(usuario.getId());
            if (usuario.getNombre() != null) {
                usuarioEditar.setNombre(usuario.getNombre());
            }
            if (usuario.getApellido() != null) {
                usuarioEditar.setApellido(usuario.getApellido());
            }
            return usuarioEditar;
        } catch (Exception exception) {
            throw new EditException(exception.getMessage());
        }
    }

    @Override
    public void deleteUsuario(Integer id) {
        usuarioMap.remove(id);
    }

    @Override
    public boolean usuarioExists(Integer id) {
        return usuarioMap.containsKey(id);
    }
}
