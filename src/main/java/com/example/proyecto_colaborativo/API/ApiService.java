package com.example.proyecto_colaborativo.API;

import javafx.concurrent.Task;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiService {

    public void API(String url) {
        // Se crea un Task para procesar la petición en segundo plano
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/tienda/api/v1/productos/fake-productos"))
                        .GET() // Puedes cambiarlo a .POST(BodyPublishers...) si es necesario
                        .build();

                // Realiza la petición de forma sincrónica dentro del hilo secundario
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                return response.body(); // Retorna el JSON o texto recibido
            }
        };

        // Qué hacer cuando la petición finaliza con éxito
        task.setOnSucceeded(event -> {
            String jsonResult = task.getValue();
            System.out.println("Datos recibidos: " + jsonResult);

            // Aquí puedes actualizar tus componentes de JavaFX de forma segura
            // Ejemplo: miLabel.setText(jsonResult);
        });

        // Qué hacer si ocurre un error (por ejemplo, sin internet o URL inválida)
        task.setOnFailed(event -> {
            Throwable exception = task.getException();
            System.err.println("Error al obtener datos: " + exception.getMessage());
        });

        // Inicia el hilo secundario
        Thread thread = new Thread(task);
        thread.setDaemon(true); // Evita que el hilo bloquee el cierre de la app
        thread.start();
    }
}

