package com.aluracursos.java.challenge.currencyconverter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class CurrencyConverter {
    private static final String API_KEY = "YOUR_API_KEY";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/";

    public static void main(String[] args) throws Exception {

        //Interacción con el usuario

        Scanner scanner = new Scanner(System.in);

        //Loop de continuación do - while

        String continueOption = null; //Al no haber valor de inicio, puede iniciar como 'null'

        do {

            System.out.println("Ingresa la moneda de origen (ej. USD): ");
            String fromCurrency = scanner.nextLine().toUpperCase();

            System.out.println("Ingresa la moneda de destino (ej. ARS): ");
            String toCurrency = scanner.nextLine().toUpperCase();

            System.out.println("Ingresa el monto a convertir: ");
            String amountInput = scanner.nextLine();

            //Reemplaza "," por "."

            amountInput = amountInput.replace(",", ".");

            //Convertir cadena a double

            double amount = 0;
            try {
                amount = Double.parseDouble(amountInput);
            } catch (NumberFormatException e) {
                System.out.println("Formato de número inválido. Por favor, ingresa un número válido.");

                continue;
            }

            //Creación de URL con el resultado final

            String url = BASE_URL + API_KEY + "/pair/" + fromCurrency + "/" + toCurrency;

            //Creación de cliente y solicitud (HttpClient & HttpRequest)

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            //Construir respuesta

            HttpResponse<String> response = client.send
                    (request, HttpResponse.BodyHandlers.ofString());

            //Analizando la respuesta en formato JSON

            JsonObject jsonResponse =
                    JsonParser.parseString(response.body())
                            .getAsJsonObject();

            if (jsonResponse.has("conversion_rate")) {
                double conversionRate = jsonResponse.get("conversion_rate")
                        .getAsDouble();
                double convertedAmount = amount * conversionRate;

                // Interacción con el usuario

                System.out.println("Tasa de conversión actual de "
                        + fromCurrency + " a " + toCurrency
                        + ": " + conversionRate);
                System.out.println("Monto convertido: "
                        + convertedAmount + " " + toCurrency);
            } else {
                System.out.println("No se pudo obtener la tasa de conversión. Verifica las monedas ingresadas.");
            }

            //Consulta al usuario si desea realizar otra conversión

            System.out.println("¿Quieres realizar otra conversión? (s/n): ");
            continueOption = scanner.nextLine().toLowerCase();

        } while (continueOption.equals("s")); //El loop continua si el usuario ingresa "s" (sí)
        System.out.println("Gracias por usar el conversor de monedas. ¡Hasta la próxima!");
        scanner.close();
    }
}
