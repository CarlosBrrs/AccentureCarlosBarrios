package com.accenture.pruebaBackend.dao;

import com.accenture.pruebaBackend.domain.Factura;
import org.springframework.data.repository.CrudRepository;

public interface FacturaRepository extends CrudRepository<Factura, Long>{
    
}
