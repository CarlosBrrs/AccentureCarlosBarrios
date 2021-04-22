package com.accenture.pruebaBackend;

import com.accenture.pruebaBackend.domain.Factura;
import com.accenture.pruebaBackend.service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/factura")
public class FacturaController {
    
    @Autowired
    private FacturaService facturaService;
    
    @PostMapping("")
    public String crearFactura(@RequestBody Factura factura){
        String respuesta = facturaService.agregarFactura(factura);
        return respuesta;
    }
    
    @DeleteMapping("/{idFactura}")
    public String eliminarFactura(@PathVariable Long idFactura){
        String respuesta = facturaService.borrarFactura(idFactura);
        return respuesta;
    }
    
    @PutMapping("/{idFactura}")
    public String modificarFactura(@RequestBody Factura facturaModificada, @PathVariable Long idFactura){
        String respuesta = facturaService.editarFactura(facturaModificada, idFactura);
        return respuesta;
    }
}
