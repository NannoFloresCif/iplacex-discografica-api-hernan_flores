package org.iplacex.proyectos.discografia.artistas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ArtistaController {

    @Autowired
    private IArtistaRepository artistaRepository;

    // Crear data
    @PostMapping(value = "/artista", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> HandleInsertArtistaRequest(@RequestBody Artista nuevoArtista) { // Se usa ResponseEntity<Object> para manejar errores
        try {
            
            Artista artistaGuardado = artistaRepository.insert(nuevoArtista); 
            
            return new ResponseEntity<>(artistaGuardado, HttpStatus.CREATED);

        } catch (Exception e) {
            
            return new ResponseEntity<>("Error al insertar artista: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los registros
    @GetMapping(value = "/artistas", produces = MediaType.APPLICATION_JSON_VALUE)
    
    public ResponseEntity<List<Artista>> HandleGetAristasRequest() {
        
        List<Artista> artistas = artistaRepository.findAll();
        
        return new ResponseEntity<>(artistas, HttpStatus.OK);
    }


    // Obtener un registro por ID
    @GetMapping(value = "/artista/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> HandleGetArtistaRequest(@PathVariable("id") String artistaId) {
        
        Optional<Artista> artistaOpt = artistaRepository.findById(artistaId);

        if (!artistaOpt.isPresent()) { 
            return new ResponseEntity<>("Artista no encontrado con ID: " + artistaId, HttpStatus.NOT_FOUND);
            
        } 

        return new ResponseEntity<>(artistaOpt.get(), HttpStatus.OK);
    }

    // Actualizar un registro
    @PutMapping(value = "/artista/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> HandleUpdateArtistaRequest(@PathVariable("id") String artistaId, @RequestBody Artista artistaActualizado) {
        
        if (!artistaRepository.existsById(artistaId)) {
            return new ResponseEntity<>("Artista no encontrado con ID: " + artistaId + " para actualizar", HttpStatus.NOT_FOUND);
        }

        try {
            
            artistaActualizado._id = artistaId;
            
            Artista resultado = artistaRepository.save(artistaActualizado);

            return new ResponseEntity<>(resultado, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error al actualizar artista: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // Eliminar un registro
    @DeleteMapping(value = "/artista/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> HandleDeleteArtistaRequest(@PathVariable("id") String artistaId) {
        
         if (!artistaRepository.existsById(artistaId)) {
            return new ResponseEntity<>("Artista no encontrado con ID: " + artistaId + " para eliminar", HttpStatus.NOT_FOUND);
        }

        try {
            Artista artista = artistaRepository.findById(artistaId).get();
            artistaRepository.deleteById(artistaId);
            
            return new ResponseEntity<>(artista, HttpStatus.OK); //HttpStatus.Gone

        } catch (Exception e) {

             return new ResponseEntity<>("Error al eliminar artista: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
