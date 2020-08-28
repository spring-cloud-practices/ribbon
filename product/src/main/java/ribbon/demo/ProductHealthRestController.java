package ribbon.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductHealthRestController {

    @GetMapping("/health")
    public ResponseEntity health() {
        return ResponseEntity.ok().build();
    }

}
