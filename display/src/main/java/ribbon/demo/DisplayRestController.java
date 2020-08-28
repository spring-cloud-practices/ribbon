package ribbon.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class DisplayRestController {

    private static String V1_PRODUCTS_API_URI = "/api/v1/products";

    private static String PRODUCT_SERVICE_URL = "http://localhost:8080";

    private static String PRODUCT_SERVICE_RIBBON_URL = "http://product";

    private static String PRODUCT_SERVICE_URL_TEMPLATE = "http://%s:%s";

    private final RestTemplate restTemplate;

    private final RestTemplate restTemplateByRibbon;

    private final LoadBalancerClient loadBalancerClient;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        return restTemplate.exchange(
                PRODUCT_SERVICE_URL + V1_PRODUCTS_API_URI,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
    }

    @GetMapping("/products/by-ribbon")
    public ResponseEntity<List<Product>> getProductsByRibbon() {
        return restTemplateByRibbon.exchange(
                PRODUCT_SERVICE_RIBBON_URL + V1_PRODUCTS_API_URI,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
    }

    @GetMapping("/products/by-load-balancer-client")
    public ResponseEntity<List<Product>> getProductsByLoadBalancerClient() {
        ServiceInstance product = loadBalancerClient.choose("product");

        log.info(String.format(PRODUCT_SERVICE_URL_TEMPLATE + V1_PRODUCTS_API_URI,
                product.getHost(), product.getPort()));

        return restTemplate.exchange(
                String.format(PRODUCT_SERVICE_URL_TEMPLATE + V1_PRODUCTS_API_URI,
                        product.getHost(), product.getPort()),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {});
    }

}
