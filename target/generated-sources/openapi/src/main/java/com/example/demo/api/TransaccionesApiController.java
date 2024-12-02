package com.example.demo.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-02T02:37:50.172028800-05:00[America/Lima]")
@Controller
@RequestMapping("${openapi.accountsMicroservice.base-path:}")
public class TransaccionesApiController implements TransaccionesApi {

    private final TransaccionesApiDelegate delegate;

    public TransaccionesApiController(@org.springframework.beans.factory.annotation.Autowired(required = false) TransaccionesApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new TransaccionesApiDelegate() {});
    }

    @Override
    public TransaccionesApiDelegate getDelegate() {
        return delegate;
    }

}
