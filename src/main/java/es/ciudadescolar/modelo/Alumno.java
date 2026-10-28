package es.ciudadescolar.modelo;

public class Alumno {

    private String nombre;
    private String expediente;
    private Integer edad;

    public Alumno(String nombre, String expediente, Integer edad) {
        this.nombre = nombre;
        this.expediente = expediente;
        this.edad = edad;
    }
    
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getExpediente() {
        return expediente;
    }
    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }
    public Integer getEdad() {
        return edad;
    }
    public void setEdad(Integer edad) {
        this.edad = edad;
    }
    @Override
    public String toString() {
        return "Alumno [nombre=" + nombre + ", expediente=" + expediente + ", edad=" + edad + "]";
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((expediente == null) ? 0 : expediente.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Alumno other = (Alumno) obj;
        if (expediente == null) {
            if (other.expediente != null)
                return false;
        } else if (!expediente.equals(other.expediente))
            return false;
        return true;
    }

    
}
