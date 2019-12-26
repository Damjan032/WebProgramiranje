package exceptions;

public class BadRequestException extends RuntimeException{
    String message;

    public BadRequestException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
