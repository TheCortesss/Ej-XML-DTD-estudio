package es.ciudadescolar;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ciudadescolar.modelo.Alumno;
import es.ciudadescolar.util.XML_Manager;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {

        File xmlEntrada = null;
        List<Alumno> listaAlumnos = null;
        File xmlSalida = null;
        File dtdValidador = null;

        LOG.debug("Num de params " + args.length);
        LOG.trace("Comenzando app");

        if (args.length == 3) {
            xmlEntrada = new File(args[0]);
            if (xmlEntrada.exists() && xmlEntrada.canRead()) {
                listaAlumnos = XML_Manager.parsearAlumnosALista(xmlEntrada);
                if (listaAlumnos != null){
                    if (!listaAlumnos.isEmpty()) {
                        LOG.info("Alumnos recuperados: ");    
                        for (Alumno al : listaAlumnos){
                            LOG.info(al.toString());
                        }
                        xmlSalida = new File(args[1]);
                        dtdValidador = new File(args[2]);
                        XML_Manager.generarXML(listaAlumnos, xmlSalida, dtdValidador);       
                    } else LOG.warn("NO SE RECUPERO NINGUN ALUMNO (LA LISTA ESTA VACIA)");
                } else LOG.warn("NO SE RECUPERO NINGUN ALUMNO (listaAlumnos = null)");
            } else LOG.error("NO SE PUEDE ACCEDER AL XML DE ENTRADA O NO EXISTE");
        } else LOG.error("NO ESTAN LOS 3 PARAMETROS NECESARIOS");

    }
}