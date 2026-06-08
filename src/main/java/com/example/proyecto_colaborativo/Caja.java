package com.example.proyecto_colaborativo;

import java.time.LocalDateTime;
import java.util.List;

public class Caja {
    private double saldoInicial;
    private double saldoActual;
    private boolean abierta;
    private List<String> movimientos;
    private double mensaje;
    public double cierre;

    public Caja(double saldoInicial, boolean abierta, List<String> movimientos) {
        this.saldoInicial = saldoInicial;
        this.abierta = abierta;
        this.movimientos = movimientos;
    }

    private void registrarMovimiento(double monto, String descripcion) {
    }

    public void registrarIngreso(double monto, String descripcion) {
        if (monto > 0 && abierta) {
            double saldoActual = monto;
            registrarMovimiento(monto, descripcion);
        }
    }

    public void registrarEgreso(double monto, String descripcion) {

        if (monto > 0 && monto <= saldoActual && abierta) {

            saldoActual -= monto;
            registrarMovimiento(50, "Egreso:´$ + monto -  descripcion");
        } else if (monto > saldoActual) {
            System.out.println("Fondo insuficientes en caja");
        }
    }

    public void registrarMovimiento(String mensaje) {
        String timestamp = LocalDateTime.now().toString();
        movimientos.add("[" + timestamp + "]" + mensaje);
    }

}

