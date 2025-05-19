package org.iplacex.proyectos.discografia.discos;

import java.util.List;

import org.iplacex.proyectos.discografia.artistas.IArtistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/api") 
@CrossOrigin
public class DiscoController {

    @Autowired
    private IDiscoRepository discoRepository; 

    
    @Autowired
    private IArtistaRepository artistaRepository; 

    
    @PostMapping(value = "/disco", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> HandlePostDiscoRequest(@RequestBody Disco nuevoDisco) {
        
        String artistaId = nuevoDisco.idArtista;

        //Verificar si el artista existe
        if (artistaId == null || !artistaRepository.existsById(artistaId)) {
            return new ResponseEntity<>("Error: El artista con ID " + artistaId + " no existe. No se puede crear el disco.", HttpStatus.BAD_REQUEST); 
        }
        

        try {
            Disco discoGuardado = discoRepository.insert(nuevoDisco); 
            return new ResponseEntity<>(discoGuardado, HttpStatus.CREATED); 
        } catch (Exception e) {
            return new ResponseEntity<>("Error al insertar disco: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Obtener todos los registros
    @GetMapping(value = "/discos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Disco>> HandleGetDiscosRequest() {
        List<Disco> discos = discoRepository.findAll(); 
        return new ResponseEntity<>(discos, HttpStatus.OK); 
    }

    //Obtener un registro por id
    @GetMapping(value = "/disco/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> HandleGetDiscoRequest(@PathVariable("id") String discoId) {
        Optional<Disco> discoOpt = discoRepository.findById(discoId); 

        if (discoOpt.isPresent()) { 
            return new ResponseEntity<>(discoOpt.get(), HttpStatus.OK); 
        } else {
            return new ResponseEntity<>("Disco no encontrado con ID: " + discoId, HttpStatus.NOT_FOUND); 
        }
    }

    //Obtener todos los registros de un artista especifico
    @GetMapping(value = "/artista/{id}/discos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Disco>> HandleGetDiscosByArtistaRequest(@PathVariable("id") String artistaId) {
        
        List<Disco> discosDelArtista = discoRepository.findDiscosByIdArtista(artistaId); 
        
        return new ResponseEntity<>(discosDelArtista, HttpStatus.OK);
    }
}
