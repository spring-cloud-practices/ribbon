package ribbon.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ProductRestController {

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        log.info("START ProductRestController.getProducts()");
        // try {
        //     Thread.sleep(500);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
        // if (true) throw new RuntimeException("server error");

        List<Product> products = LongStream.rangeClosed(1, 3)
                .boxed()
                .map(l -> new Product(l, "product" + l))
                .collect(Collectors.toList());

        log.info("END ProductRestController.getProducts()");
        return ResponseEntity.ok(products);
    }

}
