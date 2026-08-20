package com.microservice.gateway.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class ApiGatewayController {

    private final RestClient restClient;

    @Value("${services.submissions.url}")
    private String submissionsServiceUrl;

    @Value("${services.payments.url}")
    private String paymentsServiceUrl;

    public ApiGatewayController(RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping("/submissions")
    public ResponseEntity<String> listSubmissions(HttpServletRequest request) {
        return forwardGet(submissionsServiceUrl + "/submissions", request);
    }

    @PostMapping("/submissions")
    public ResponseEntity<String> createSubmission(@RequestBody String body, HttpServletRequest request) {
        return forwardPost(submissionsServiceUrl + "/submissions", body, request);
    }

    @GetMapping("/payments")
    public ResponseEntity<String> listPayments(HttpServletRequest request) {
        return forwardGet(paymentsServiceUrl + "/payments", request);
    }

    @PostMapping("/payments")
    public ResponseEntity<String> createPayment(@RequestBody String body, HttpServletRequest request) {
        return forwardPost(paymentsServiceUrl + "/payments", body, request);
    }

    private ResponseEntity<String> forwardGet(String url, HttpServletRequest request) {
        return restClient.get()
                .uri(url)
                .header("X-User-Id", String.valueOf(request.getAttribute("gatewayUserId")))
                .header("X-User-Role", String.valueOf(request.getAttribute("gatewayUserRole")))
                .retrieve()
                .toEntity(String.class);
    }

    private ResponseEntity<String> forwardPost(String url, String body, HttpServletRequest request) {
        return restClient.post()
                .uri(url)
                .header("X-User-Id", String.valueOf(request.getAttribute("gatewayUserId")))
                .header("X-User-Role", String.valueOf(request.getAttribute("gatewayUserRole")))
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toEntity(String.class);
    }
}
