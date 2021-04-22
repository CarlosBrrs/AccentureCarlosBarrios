package com.accenture.pruebaBackend.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
@Table(name = "facturas")
public class Factura implements Serializable {
    
    private static final long serialVersionUTD = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFactura;
    
    @NotEmpty
    private String nombreCliente;
    
    @NotEmpty
    private String direccionCliente;
    
    private String productosInventario;
    
    private String cantidadProductos;
    
    private String precioUnitarioProductos;
    
    private final double iva = 0.19;
    
    private double domicilio;
    
    private double valorFacturaSinIva;
    
    private double valorFacturaConIva;
    
    private LocalDateTime horaMaxEditar;
    
    private LocalDateTime horaMaxEliminar;
    
    
}
