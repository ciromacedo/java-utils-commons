package org.macedo.utils.commons.cep;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

@Component
public class ViaCepClient {

    private static final String VIACEP_URL = "https://viacep.com.br/ws/%s/json/";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ViaCepClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public Optional<ViaCepResponse> consultar(String cep) {
        String cepLimpo = cep.replaceAll("\\D", "");

        if (cepLimpo.length() != 8) {
            throw new ViaCepException("CEP deve conter 8 dígitos: " + cep);
        }

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format(VIACEP_URL, cepLimpo)))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ViaCepException("ViaCEP retornou status " + response.statusCode());
            }

            ViaCepResponse viaCep = objectMapper.readValue(response.body(), ViaCepResponse.class);

            if (Boolean.TRUE.equals(viaCep.erro())) {
                return Optional.empty();
            }

            return Optional.of(viaCep);
        } catch (ViaCepException e) {
            throw e;
        } catch (Exception e) {
            throw new ViaCepException("Erro ao consultar ViaCEP: " + e.getMessage(), e);
        }
    }
}
