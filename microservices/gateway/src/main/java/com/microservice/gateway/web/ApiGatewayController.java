package com.microservice.gateway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(ApiGatewayController.class);

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
        log.debug("Forwarding GET {} for userId={} role={}", url, request.getAttribute("gatewayUserId"), request.getAttribute("gatewayUserRole"));
        try {
            ResponseEntity<String> response = restClient.get()
                    .uri(url)
                    .header("X-User-Id", String.valueOf(request.getAttribute("gatewayUserId")))
                    .header("X-User-Role", String.valueOf(request.getAttribute("gatewayUserRole")))
                    .retrieve()
                    .toEntity(String.class);
            log.debug("GET {} completed with status {}", url, response.getStatusCode());
            return relayResponse(response);
        } catch (Exception ex) {
            log.error("GET {} failed: {}", url, ex.getMessage(), ex);
            throw ex;
        }
    }

    private ResponseEntity<String> forwardPost(String url, String body, HttpServletRequest request) {
        log.debug("Forwarding POST {} for userId={} role={} bodyLength={}", url, request.getAttribute("gatewayUserId"), request.getAttribute("gatewayUserRole"), body == null ? 0 : body.length());
        try {
            ResponseEntity<String> response = restClient.post()
                    .uri(url)
                    .header("X-User-Id", String.valueOf(request.getAttribute("gatewayUserId")))
                    .header("X-User-Role", String.valueOf(request.getAttribute("gatewayUserRole")))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .toEntity(String.class);
            log.debug("POST {} completed with status {}", url, response.getStatusCode());
            return relayResponse(response);
        } catch (Exception ex) {
            log.error("POST {} failed: {}", url, ex.getMessage(), ex);
            throw ex;
        }
    }

    private ResponseEntity<String> relayResponse(ResponseEntity<String> upstreamResponse) {
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(upstreamResponse.getStatusCode());
        if (upstreamResponse.getHeaders().getContentType() != null) {
            builder.contentType(upstreamResponse.getHeaders().getContentType());
        }
            // Removed unnecessary Accept header relay
        return builder.body(upstreamResponse.getBody());
    }
}
