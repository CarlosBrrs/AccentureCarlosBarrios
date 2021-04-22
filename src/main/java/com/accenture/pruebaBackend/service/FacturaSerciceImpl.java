package com.accenture.pruebaBackend.service;

import com.accenture.pruebaBackend.domain.Factura;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.accenture.pruebaBackend.dao.FacturaRepository;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Service
public class FacturaSerciceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;
    
    DecimalFormat df = new DecimalFormat("#.00");

    @Override
    public String agregarFactura(Factura factura) {
        String respuesta;
        String cantidadProd = factura.getCantidadProductos();
        cantidadProd = cantidadProd.replace(" ", "");
        String valorUnitProd = factura.getPrecioUnitarioProductos();
        valorUnitProd = valorUnitProd.replace(" ", "");
        String[] cantidadProdSep = cantidadProd.split(",");
        String[] valorUnitProdSep = valorUnitProd.split(",");
        int valorSinIva = 0;
        for (int i = 0; i < valorUnitProdSep.length; i++) {
            int cantprod = Integer.parseInt(cantidadProdSep[i]);
            int valorunit = Integer.parseInt(valorUnitProdSep[i]);
            valorSinIva = (cantprod * valorunit) + valorSinIva;
        }
        double valorConIva = valorSinIva / (1-factura.getIva());
        if (valorConIva > 100000) {
            factura.setValorFacturaSinIva(valorSinIva);
            factura.setValorFacturaConIva(valorConIva);
            factura.setDomicilio(0);
            LocalDateTime horaMaxEditar = LocalDateTime.now().plusHours(5);
            LocalDateTime horaMaxEliminar = LocalDateTime.now().plusHours(12);
            factura.setHoraMaxEditar(horaMaxEditar);
            factura.setHoraMaxEliminar(horaMaxEliminar);
            facturaRepository.save(factura);
            respuesta = "FACTURA GENERADA EXITOSAMENTE\n\n"
                    + "ID de factura: " + factura.getIdFactura()
                    + "\nNombre cliente:" + factura.getNombreCliente()
                    + "\nDirección cliente: " + factura.getDireccionCliente()
                    + "\nCANTIDAD DE PRODUCTOS:"
                    + "\n" + factura.getProductosInventario()
                    + "=\n" + cantidadProd
                    + "\nPrecio unitario de productos ($): \n" + factura.getPrecioUnitarioProductos()
                    + "\nValor sin IVA: $ " + factura.getValorFacturaSinIva()
                    + "\nValor domicilio: $ " + factura.getDomicilio()
                    + "\nTOTAL: $ " + df.format(factura.getValorFacturaConIva())
                    + "\n\nGRACIAS POR SU COMPRA";
            return respuesta;

        } else if (valorConIva > 70000) {
            int domicilio = 5000;
            factura.setValorFacturaSinIva(valorSinIva);
            factura.setDomicilio(domicilio);
            factura.setValorFacturaConIva(valorConIva + factura.getDomicilio());
            LocalDateTime horaMaxEditar = LocalDateTime.now().plusHours(5);
            LocalDateTime horaMaxEliminar = LocalDateTime.now().plusHours(12);
            factura.setHoraMaxEditar(horaMaxEditar);
            factura.setHoraMaxEliminar(horaMaxEliminar);
            facturaRepository.save(factura);
            respuesta = "FACTURA GENERADA EXITOSAMENTE\n\n"
                    + "ID de factura: " + factura.getIdFactura()
                    + "\n Nombre cliente:" + factura.getNombreCliente()
                    + "\n Dirección cliente: " + factura.getDireccionCliente()
                    + "\nCANTIDAD DE PRODUCTOS:"
                    + "\n" + factura.getProductosInventario()
                    + "=\n" + cantidadProd
                    + "\nPrecio unitario de productos: \n" + factura.getPrecioUnitarioProductos()
                    + "\nValor sin IVA: $ " + factura.getValorFacturaSinIva()
                    + "\nValor domicilio: $ " + factura.getDomicilio()
                    + "\nTOTAL: $ " + df.format(factura.getValorFacturaConIva())
                    + "\n\nGRACIAS POR SU COMPRA";
            return respuesta;
        } else {
            respuesta = "Factura no generada. Total inferior a $70.000";
            return respuesta;
        }
    }

    @Override
    public String borrarFactura(Long idFactura) {
        String respuesta;
        Factura facturaBorrar = facturaRepository.findById(idFactura).get();
        if (facturaBorrar.getHoraMaxEliminar().isBefore(LocalDateTime.now())) { //Si se pasa
            double valorCancelar = facturaBorrar.getValorFacturaConIva() * 0.1;
            respuesta = "Factura eliminada después del tiempo máximo, debe cancelar: $" + valorCancelar;
            facturaRepository.delete(facturaBorrar);
            return respuesta;
        } else {
            respuesta = "Factura eliminada antes del tiempo máximo, debe cancelar: $" + 0;
            facturaRepository.delete(facturaBorrar);
            return respuesta;
        }
    }

    @Override
    @Transactional
    public String editarFactura(Factura facturaModificada, Long idFactura) {
        String mensaje;
        Factura facturaBD = facturaRepository.findById(idFactura).get();
        Factura facturaModificar = facturaModificada;
        if (facturaBD.getHoraMaxEditar().isAfter(LocalDateTime.now())) {
            String cantidadProd = facturaModificar.getCantidadProductos();
            cantidadProd = cantidadProd.replace(" ", "");
            String valorUnitProd = facturaModificar.getPrecioUnitarioProductos();
            valorUnitProd = valorUnitProd.replace(" ", "");
            String[] cantidadProdSep = cantidadProd.split(",");
            String[] valorUnitProdSep = valorUnitProd.split(",");
            int valorSinIva = 0;
            for (int i = 0; i < valorUnitProdSep.length; i++) {
                int cantprod = Integer.parseInt(cantidadProdSep[i]);
                int valorunit = Integer.parseInt(valorUnitProdSep[i]);
                valorSinIva = (cantprod * valorunit) + valorSinIva;
            }
            double valorConIva = valorSinIva / (1-facturaModificar.getIva());
            if (facturaBD.getValorFacturaConIva() > valorConIva) {
                mensaje = "Nueva factura es menor que la original, operación no permitida";
                return mensaje;

            } else {
                facturaModificar.setValorFacturaSinIva(valorSinIva);
                facturaModificar.setValorFacturaConIva(valorConIva);
                if (facturaModificar.getValorFacturaConIva() > 100000) {
                    facturaModificar.setDomicilio(0);
                    facturaModificar.setHoraMaxEditar(facturaBD.getHoraMaxEditar());
                    facturaModificar.setHoraMaxEliminar(facturaBD.getHoraMaxEliminar());
                    facturaRepository.save(facturaModificar);
                    return mensaje = "Factura actualizada satisfactoriamente";
                } else {
                    int domicilio = 5000;
                    facturaModificar.setDomicilio(domicilio);
                    facturaModificar.setValorFacturaConIva(valorConIva + domicilio);
                    facturaModificar.setHoraMaxEditar(facturaBD.getHoraMaxEditar());
                    facturaModificar.setHoraMaxEliminar(facturaBD.getHoraMaxEliminar());
                    facturaRepository.save(facturaModificar);
                    return mensaje = "Factura actualizada satisfactoriamente";
                }
            }
        }
        return mensaje = "Operación no permitida. Excedió el tiempo máximo de modificación";
    }
}
