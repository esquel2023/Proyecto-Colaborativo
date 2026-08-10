package com.example.proyecto_colaborativo.API;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServiciolienteApi {


        private final HttpClient client = HttpClient.newHttpClient();
        private final String BASE_URL = "http://localhost:8080/clientes";

        // 1. LLAMAR AL GET (Obtener todos los clientes)
        public void obtenerTodosLosClientes() {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL))
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("--- Todos los Clientes (JSON) ---");
                System.out.println(response.body());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 2. LLAMAR AL GET POR USUARIO (Ejemplo: buscar a "pepe")
        public void obtenerClientePorUsuario(String usuario) {
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/" + usuario)) // Queda: http://localhost:8080/clientes/pepe
                        .GET()
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("--- Cliente Buscado (" + usuario + ") ---");
                System.out.println("Código Estado: " + response.statusCode());
                System.out.println("Respuesta: " + response.body());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}



