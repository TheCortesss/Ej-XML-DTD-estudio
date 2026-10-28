package es.ciudadescolar.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.ciudadescolar.modelo.Alumno;

public class XML_Manager {
    
    private static final Logger LOG = LoggerFactory.getLogger(XML_Manager.class);

    public static List<Alumno> parsearAlumnosALista(File xml) {
        List<Alumno> listaAlumnos = null;

        // PREGUNTAR A JOSE SI NOS DA EL CODIGO DESDE AQUI -----------------------------
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        dbf.setIgnoringElementContentWhitespace(true);
        //db.setErrorHandler(new AlumnoErrorHandler()); PREGUNTAR SI ES NECESARIO HACER UN ERRORHANDLER


        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            
        // HASTA AQUI -----------------------------


            Document doc = db.parse(xml);

            Element elementoRaiz = doc.getDocumentElement();
            LOG.trace("Elemento raiz: " + elementoRaiz.getNodeName());

            NodeList listaNodos = elementoRaiz.getElementsByTagName("alumno");

            listaAlumnos = new ArrayList<Alumno>();

            for (int i = 0; i < listaNodos.getLength(); i++) {
                Node nodoAlumno = listaNodos.item(i);
                if (nodoAlumno.getNodeType() == Node.ELEMENT_NODE) { //PREGUNTAR SI IMPORTANTE
                Element elementoAlumno = (Element) nodoAlumno;
                Node nodoExp = elementoAlumno.getFirstChild();
                Node nodoNombre = nodoExp.getNextSibling();
                Node nodoEdad = elementoAlumno.getLastChild();

                Alumno al = new Alumno(nodoExp.getTextContent(), nodoNombre.getTextContent(), Integer.parseInt(nodoEdad.getTextContent()));
                listaAlumnos.add(al);
                LOG.trace("Alumno agregado a la lista: " + al.toString());
                }
            }

        } catch (ParserConfigurationException e) {
            LOG.error("ERROR durante el parseo del XML " + e.getMessage());
        } catch (SAXException e) {
            LOG.error("ERROR durante el parseo del XML " + e.getMessage());
        } catch (IOException e) {
            LOG.error("ERROR durante el parseo del XML " + e.getMessage());
        }

        return  listaAlumnos;
    }

    public static void generarXML(List<Alumno> listaAlumnos, File xmlSalida, File dtdValidador) {
        Element elementoRaiz = null;
        Element elementoAlumno = null;
        Element elementoEdad = null;
        // PREGUNTAR A JOSE SI NOS DA EL CODIGO DESDE AQUI -----------------------------
        DocumentBuilderFactory dbf = null;
        DocumentBuilder db = null;
        Document documento = null;

        dbf = DocumentBuilderFactory.newInstance();
        try {
            db = dbf.newDocumentBuilder();
            // HASTA AQUI ------------------------------------------------------------

            documento = db.newDocument();

            elementoRaiz = documento.createElement("estudiantes");
            documento.appendChild(elementoRaiz);

            for (Alumno al: listaAlumnos) {
                elementoAlumno = documento.createElement("alumno");
                elementoAlumno.setAttribute("nom", al.getNombre());
                elementoAlumno.setAttribute("edad", al.getEdad().toString());
                elementoAlumno.setAttribute("exp", al.getExpediente());

                elementoEdad = documento.createElement("edad");
                elementoEdad.setTextContent(al.getEdad().toString());
                elementoAlumno.appendChild(elementoEdad);

                elementoRaiz.appendChild(elementoAlumno);
            }

            // Y DESDE AQUI ------------------------------------------------------------

            TransformerFactory tf = null;
            Transformer t = null;
            DOMSource ds = null;
            StreamResult sr = null;

            //Comprovación previa de si existe el fichero
            if (xmlSalida.exists()){
                xmlSalida.delete();
                LOG.info("El fichero de salida YA existia, se procede a borrar");
            }

            tf = TransformerFactory.newInstance();
            t = tf.newTransformer();
            ds = new DOMSource(documento);
            FileWriter fw = new FileWriter(xmlSalida);
            sr = new StreamResult(fw);

            //Queremos que nuestro XML de salida se valide contra el DTD recibido como parametro:
            t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtdValidador.getName());
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.VERSION, "1.0");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            
            t.transform(ds, sr);

            // HASTA AQUI ------------------------------------------------------------

        } catch (ParserConfigurationException e) {
            LOG.trace("ERROR al crear el documento " + e.getMessage());
        } catch (TransformerConfigurationException e) {
            LOG.trace("ERROR al crear el documento " + e.getMessage());
        } catch (IOException e) {
            LOG.trace("ERROR al crear el documento " + e.getMessage());
        } catch (TransformerException e) {
            LOG.trace("ERROR al crear el documento " + e.getMessage());
        }
    }

}
