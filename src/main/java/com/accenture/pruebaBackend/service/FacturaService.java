package com.accenture.pruebaBackend.service;

import com.accenture.pruebaBackend.domain.Factura;

public interface FacturaService {
    
    String agregarFactura(Factura factura);
    
    String borrarFactura(Long idFactura);
    
    String editarFactura(Factura factura, Long idFactura);
    
    //Factura buscarFactura(Long idFactura);
    
}
