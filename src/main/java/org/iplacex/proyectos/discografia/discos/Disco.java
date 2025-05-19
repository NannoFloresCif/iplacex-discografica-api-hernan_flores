package org.iplacex.proyectos.discografia.discos;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("discos")
public class Disco {

    @Id
    public String _id; 
    public String idArtista; 
    public String nombre;
    public int anioLanzamiento;
    public List<String> canciones;


    public Disco() {
    }

    public Disco(String idArtista, String nombre, int anioLanzamiento, List<String> canciones) {
        this.idArtista = idArtista;
        this.nombre = nombre;
        this.anioLanzamiento = anioLanzamiento;
        this.canciones = canciones;
    }
}
