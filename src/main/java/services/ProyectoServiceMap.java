package services;

import java.util.Collection;
import java.util.HashMap;

import classes.Incidente;
import services.EditException;
import classes.Proyecto;
import interfaces.IProyectoService;

public class ProyectoServiceMap implements IProyectoService {

    private HashMap<Integer, Proyecto> proyectoMap;

    public ProyectoServiceMap() {
        this.proyectoMap = new HashMap<Integer, Proyecto>();
    }

    public ProyectoServiceMap(HashMap<Integer, Proyecto> proyectoMap) {
        this.proyectoMap = proyectoMap;
    }

    @Override
    public void addProyecto(Proyecto proyecto) {
        proyectoMap.put(proyecto.getId(), proyecto);
    }

    @Override
    public Collection<Proyecto> getProyectos() {
        return proyectoMap.values();
    }

    @Override
    public Collection<Incidente> getIncidentes(Integer id) {
        return proyectoMap.get(id).getIncidentes();
    }

    @Override
    public void addIncidente(Integer idProyecto, Incidente incidente) {
        proyectoMap.get(idProyecto).addIncidente(incidente);
    }

    @Override
    public Proyecto getProyecto(Integer id) {
        return proyectoMap.get(id);
    }

    @Override
    public Proyecto editProyecto(Proyecto proyecto) throws EditException {
        try {
            if (proyecto.getId() == null) {
                throw new EditException("El id no puede ser nulo");
            }
            Proyecto proyectoEditar = proyectoMap.get(proyecto.getId());
            if (proyecto.getTitulo() != null) {
                proyectoEditar.setTitulo(proyecto.getTitulo());
            }
            if (proyecto.getPropietario() != null) {
                proyectoEditar.setPropietario(proyecto.getPropietario());
            }
            return proyectoEditar;
        } catch (Exception exception) {
            throw new EditException(exception.getMessage());
        }
    }

    @Override
    public void deleteProyecto(Integer id) {
        proyectoMap.remove(id);
    }

    @Override
    public boolean proyectoExists(Integer id) {
        return proyectoMap.containsKey(id);
    }
}
