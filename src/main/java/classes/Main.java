package classes;

import com.google.gson.Gson;
import interfaces.*;
import services.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {

        final IUsuarioService usuarioService = new UsuarioServiceMap();
        final IProyectoService proyectoService = new ProyectoServiceMap();
        final IIncidenteService incidenteService = new IncidenteServiceMap();

        loadUsuarios(usuarioService);
        loadIncidentes(incidenteService, usuarioService);
        loadProyectos(proyectoService, usuarioService, incidenteService);

        // USUARIO

        post("/usuario", ((request, response) -> {
            response.type("application/json");
            Usuario usuario = new Gson().fromJson(request.body(), Usuario.class);
            usuarioService.addUsuario(usuario);
            return new Gson().toJson(new StandardResponse(ResponseStatus.SUCCESS));
        }));

        get("/usuario", (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(new StandardResponse(
                    ResponseStatus.SUCCESS,
                    new Gson().toJsonTree(usuarioService.getUsuarios())
            ));
        });

        get("/usuario/:id", (request, response) -> {
            response.type("application/json");
            Integer id = Integer.parseInt(request.params(":id"));
            return new Gson().toJson(new StandardResponse(
                    ResponseStatus.SUCCESS,
                    new Gson().toJsonTree(usuarioService.getUsuario(id))
            ));
        });

        put("/usuario", (request, response) -> {
            response.type("application/json");
            Usuario usuario = new Gson().fromJson(request.body(), Usuario.class);
            Usuario usuarioEditado = usuarioService.editUsuario(usuario);

            if (usuarioEditado != null) {
                return new Gson().toJson(new StandardResponse(
                        ResponseStatus.SUCCESS,
                        new Gson().toJsonTree(usuarioEditado)
                ));
            } else {
                return new Gson().toJson(new StandardResponse(
                        ResponseStatus.ERROR,
                        "Error al editar el usuario."
                ));
            }
        });

        delete("/usuario/:id", (request, response) -> {
            response.type("application/json");
            Integer id = Integer.parseInt(request.params(":id"));

            if (has_proyectos(id, proyectoService) || has_incidentes(id, incidenteService)) {
                return new Gson().toJson(new StandardResponse(
                        ResponseStatus.ERROR, "No pudo borrarse el usuario porque posee proyectos o incidentes asociados."
                ));
            } else {
                usuarioService.deleteUsuario(id);
                return new Gson().toJson(new StandardResponse(
                        ResponseStatus.SUCCESS, "Usuario borrado."
                ));
            }
        });

        options("usuario/:id", (request, response) -> {
            Integer id = Integer.parseInt(request.params(":id"));
            return new Gson().toJson(new StandardResponse(
                    ResponseStatus.SUCCESS,
                    (usuarioService.usuarioExists(id) ? "El usuario existe" : "El usuario no existe")
            ));
        });

        // PROYECTO

        post("/proyecto", ((request, response) -> {
            response.type("application/json");
            Proyecto proyecto = new Gson().fromJson(request.body(), Proyecto.class);
            proyectoService.addProyecto(proyecto);
            return new Gson().toJson(new StandardResponse(ResponseStatus.SUCCESS));
        }));

        get("/proyecto", (request, response) -> {
            response.type("application/json");
            return new Gson().toJson(new StandardResponse(
                    ResponseStatus.SUCCESS,
                    new Gson().toJsonTree(proyectoService.getProyectos())
            ));
        });

        get("/proyecto/:id", (request, response) -> {
            response.type("application/json");
            Integer id = Integer.parseInt(request.params(":id"));
            return new Gson().toJson(new StandardResponse(
                    ResponseStatus.SUCCESS,
                    new Gson().toJsonTree(proyectoService.getProyecto(id))
            ));
        });

        put("/proyecto", (request, response) -> {
            response.type("application/json");
            Proyecto proyecto = new Gson().fromJson(request.body(), Proyecto.class);
            Proyecto proyectoEditado = proyectoService.editProyecto(proyecto);

            if (proyectoEditado != null) {
                return new Gson().toJson(new StandardResponse(
                        ResponseStatus.SUCCESS,
                        new Gson().toJsonTree(proyectoEditado)
                ));
            } else {
                return new Gson().toJson(new StandardResponse(
                        ResponseStatus.ERROR,
                        "Error al editar el proyecto."
                ));
            }
        });

        delete("/proyecto/:id", (request, response) -> {
            response.type("application/json");
            Integer id = Integer.parseInt(request.params(":id"));
            if (proyectoService.getProyecto(id).hasIncidentes()) {
                return new Gson().toJson(new StandardResponse(
                        ResponseStatus.ERROR, "No pudo borrarse el proyecto porque posee incidentes asociados."
                ));
            } else {
                proyectoService.deleteProyecto(id);
                return new Gson().toJson(new StandardResponse(
                        ResponseStatus.SUCCESS, "Proyecto borrado."
                ));
            }
        });

        options("proyecto/:id", (request, response) -> {
            Integer id = Integer.parseInt(request.params(":id"));
            return new Gson().toJson(new StandardResponse(
                    ResponseStatus.SUCCESS,
                    (proyectoService.proyectoExists(id) ? "El proyecto existe" : "El proyecto no existe")
            ));
        });


        // INCIDENTE
        /*
        todos los incidentes asignados a un usuario, todos
        incidentes creados por un usuario, todos los incidentes asociados a
        un proyecto, todos los incidentes abiertos (que estarían pendientes)
        y, finalmente, todos los incidentes resueltos (los que estarían
        cerrados).
         */
        post("/incidente", ((request, response) -> {
            response.type("application/json");
            Incidente incidente = new Gson().fromJson(request.body(), Incidente.class);
            incidenteService.addIncidente(incidente);
            return new Gson().toJson(new StandardResponse(ResponseStatus.SUCCESS));
        }));

        get("/incidente", (request, response) -> {
            response.type("application/json");

            if (request.queryParams("userId") != null) {
                Integer userId = Integer.parseInt(request.queryParams("userId"));
                return new Gson().toJson(new StandardResponse(
                        ResponseStatus.SUCCESS,
                        new Gson().toJsonTree(
                                incidenteService.getIncidentes()
                                        .stream()
                                        .filter(s -> s.getResponsable().getId() == userId)
                                        .toArray()
                        )
                ));
            } else if (request.queryParams("proyectoId") != null) {
                    Integer proyectoId = Integer.parseInt(request.queryParams("proyectoId"));
                try {
                    return new Gson().toJson(new StandardResponse(
                            ResponseStatus.SUCCESS,
                            new Gson().toJsonTree(proyectoService.getProyecto(proyectoId).getIncidentes())
                    ));
                } catch (Exception e) {
                    return new Gson().toJson(new StandardResponse(
                            ResponseStatus.ERROR, "No existe el proyecto con id " + proyectoId
                    ));
                }
            } else if (request.queryParams("estado") != null) {
                String estado = request.queryParams("estado");

                if (estado.equalsIgnoreCase(Estado.ASIGNADO.toString()) || estado.equalsIgnoreCase(Estado.RESUELTO.toString())) {
                    return new Gson().toJson(new StandardResponse(
                            ResponseStatus.SUCCESS,
                            new Gson().toJsonTree(
                                    incidenteService.getIncidentes()
                                            .stream()
                                            .filter(s -> s.getEstado().toString().equalsIgnoreCase(estado))
                                            .toArray()
                            )
                    ));
                } else {
                    return new Gson().toJson(new StandardResponse(
                            ResponseStatus.ERROR, "No existe el estado " + estado
                    ));
                }
            } else {
                return new Gson().toJson(new StandardResponse(
                        ResponseStatus.SUCCESS,
                        new Gson().toJsonTree(incidenteService.getIncidentes())
                ));
            }
        });



    }

    private static boolean has_incidentes(Integer idUsuario, IIncidenteService incidenteServiceMap) {
        for (Incidente i : incidenteServiceMap.getIncidentes()){
            if (i.getReportador().getId() == idUsuario || i.getResponsable().getId() == idUsuario) {
                return true;
            }
        }
        return false;
    }

    private static boolean has_proyectos(Integer idUsuario, IProyectoService iProyectoServiceMap) {
        for (Proyecto p : iProyectoServiceMap.getProyectos()) {
            if (p.getPropietario().getId() == idUsuario) {
                return true;
            }
        }
        return false;
    }

    private static void loadIncidentes(IIncidenteService incidenteServiceMap, IUsuarioService usuarioServiceMap) {
        incidenteServiceMap.addIncidente(new Incidente(
                1, Clasificacion.CRITICO, Estado.ASIGNADO, "Bug crítico",
                usuarioServiceMap.getUsuario(3), usuarioServiceMap.getUsuario(1))
        );
        incidenteServiceMap.addIncidente(new Incidente(
                2, Clasificacion.MENOR, Estado.RESUELTO, "Error cosmético",
                usuarioServiceMap.getUsuario(3), usuarioServiceMap.getUsuario(2))
        );
    }


    private static void loadProyectos(IProyectoService proyectoServiceMap, IUsuarioService usuarioServiceMap, IIncidenteService incidenteServiceMap) {
        Proyecto p1 = new Proyecto(1, "Shipping", usuarioServiceMap.getUsuario(1));
        p1.addIncidente(incidenteServiceMap.getIncidente(1));
        proyectoServiceMap.addProyecto(p1);

        Proyecto p2 = new Proyecto(2, "Core", usuarioServiceMap.getUsuario(2));
        p2.addIncidente(incidenteServiceMap.getIncidente(2));
        proyectoServiceMap.addProyecto(p2);
    }

    private static void loadUsuarios(IUsuarioService usuarioServiceMap) {
        usuarioServiceMap.addUsuario(new Usuario(1, "Juan", "Filardo"));
        usuarioServiceMap.addUsuario(new Usuario(2, "Federico", "Silva"));
        usuarioServiceMap.addUsuario(new Usuario(3, "Matías", "Brond"));
    }


}
