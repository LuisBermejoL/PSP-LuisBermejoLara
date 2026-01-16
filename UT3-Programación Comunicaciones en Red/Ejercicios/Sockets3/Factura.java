package Ejercicios.Sockets3;

import java.io.Serializable;
import java.time.LocalDate;
@SuppressWarnings("serial")

public class Factura implements Serializable{
    String numeroFactura;
    LocalDate fechaFactura;
    double importeFactura;
    String tipoIVA;
    double IVA;
    double importeTotal;

    public Factura(String numeroFactura, LocalDate fechaFactura, double importeFactura, String tipoIVA) {
        super();
        this.numeroFactura = numeroFactura;
        this.fechaFactura = fechaFactura;
        this.importeFactura = importeFactura;
        this.tipoIVA = tipoIVA;
    }

    public Factura() {super();}
    
    public String getNumeroFactura() {return numeroFactura;}
    public void setNumeroFactura(String numeroFactura) {this.numeroFactura = numeroFactura;}
    public LocalDate getFechaFactura() {return fechaFactura;}
    public void setFechaFactura(LocalDate fechaFactura) {this.fechaFactura = fechaFactura;}
    public double getImporteFactura() {return importeFactura;}
    public void setImporteFactura(double importeFactura) {this.importeFactura = importeFactura;}
    public String getTipoIVA() {return tipoIVA;}
    public void setTipoIVA(String tipoIVA) {this.tipoIVA = tipoIVA;}
    public double getIVA() {return IVA;}
    public void setIVA(double iVA) {IVA = iVA;}
    public double getImporteTotal() {return importeTotal;}
    public void setImporteTotal(double importeTotal) {this.importeTotal = importeTotal;}
}