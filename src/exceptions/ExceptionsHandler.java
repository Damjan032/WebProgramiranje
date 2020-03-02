package exceptions;

import static spark.Spark.exception;

import java.util.Arrays;
import java.util.Optional;

public class ExceptionsHandler {

    private static ExceptionsHandler instance = null;

    public static ExceptionsHandler getInstance() {
        return Optional.ofNullable(instance).orElseGet(ExceptionsHandler::new);
    }

    public void init() {
        exception(UnauthorizedException.class, (e, req, res) -> {
            res.status(401);
            res.type("application/json");
            res.body("{ \"ErrorMessage\" : \"Unauthorized\"}");
        });
        exception(NotFoundException.class, (e, req, res) -> {
            res.status(404);
            res.type("application/json");
            res.body("{ \"ErrorMessage\" : \"Resource not found\"}");
        });
        exception(BadRequestException.class, (e, req, res) -> {
            res.status(400);
            res.type("application/json");
            res.body("{ \"ErrorMessage\" : \"" + e.getMessage() + "\"}");
        });

        exception(InternalServerErrorException.class, (e, req, res) -> {
            res.status(500);
            res.type("application/json");
            res.body("{ \"ErrorMessage\" : \"" + Arrays.toString(e.getStackTrace()) + "\"}");
        });
    }
}
