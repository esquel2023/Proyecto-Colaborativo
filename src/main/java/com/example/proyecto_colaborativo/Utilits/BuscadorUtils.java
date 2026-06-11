package com.example.proyecto_colaborativo.Utilits;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.function.BiPredicate;

public class BuscadorUtils {

    /**
     * Configura un filtro en tiempo real para cualquier TableView.
     *
     * @param <T>          Tipo de objeto que contiene la tabla.
     * @param txtBuscar    El TextField donde escribe el usuario.
     * @param table        La TableView que se va a filtrar.
     * @param listaOriginal La lista de datos original (ObservableList).
     * @param criterioBusqueda Expresión que define si el objeto coincide con el texto escrito.
     */

    public static <T> void configuradorBuscador(
            TextField txtBuscar,
            TableView<T> table,
            ObservableList<T> listaOriginal,
            BiPredicate<T, String> criterioBusqueda) {

        // 1. Envolver la lista original en una FilteredList
        FilteredList<T> listafiltrada = new FilteredList<>(listaOriginal, p -> true);

        // 2. Escuchar los cambios del TextField
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            listafiltrada.setPredicate(item -> {
                // Si está vacío, muestra
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String textoLimpio = newValue.toLowerCase().trim();

                // Evalúa el criterio personalizado que definas en el controlador
                return criterioBusqueda.test(item, textoLimpio);
            });
        });

        // 3. Asignar la lista filtrada a la tabla (Va afuera del listener, se asigna una sola vez)
        table.setItems(listafiltrada);
    }
}

