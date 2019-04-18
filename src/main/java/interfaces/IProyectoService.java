package interfaces;

import java.util.Collection;

import classes.Incidente;
import classes.Proyecto;
import services.EditException;

public interface IProyectoService {

    public void addProyecto(Proyecto proyecto);
    public Collection<Proyecto> getProyectos();
    public Collection<Incidente> getIncidentes(Integer id);
    public void addIncidente(Integer idProyecto, Incidente incidente);
    public Proyecto getProyecto(Integer id);
    public Proyecto editProyecto(Proyecto proyecto) throws EditException;
    public void deleteProyecto(Integer id);
    public boolean proyectoExists(Integer id);
}
