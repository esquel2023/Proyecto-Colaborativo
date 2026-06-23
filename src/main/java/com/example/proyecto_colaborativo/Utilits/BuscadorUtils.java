package com.example.proyecto_colaborativo.Utilits;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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

                // Si el item es nulo, evita un NullPointerException en el criterio
                if (item == null) {
                    return true;
                }

                // Si está vacío, muestra
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Limpia el texto de búsqueda una sola vez por cada cambio, no por cada ítem
                String textoLimpio = newValue.toLowerCase().trim();

                // Evalúa el criterio personalizado
                return criterioBusqueda.test(item,textoLimpio);

            });
        });

        // 3. Vincular el ordenamiento de la tabla con la lista filtrada
        SortedList<T> listaOrdenada = new SortedList<>(listafiltrada);
        listaOrdenada.comparatorProperty().bind(table.comparatorProperty());
        // 4. Asignar la lista ordenada y filtrada a la tabla
        table.setItems(listaOrdenada);
        
    }
}

