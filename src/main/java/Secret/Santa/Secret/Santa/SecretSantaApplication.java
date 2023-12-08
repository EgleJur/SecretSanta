package Secret.Santa.Secret.Santa;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Secret Santa API", version = "1.0",
        description = "Secret Santa API"))
@SecurityScheme(name = "arapi", scheme = "bearer", bearerFormat = "JWT", type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER, description = "JWT Authorization header using Bearer scheme")
public class SecretSantaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecretSantaApplication.class, args);
    }

}
