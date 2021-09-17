package engine.view_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Response {
    private boolean success;
    private String feedback;

    public static Response successResponse() {
        Response response = new Response();
        response.setFeedback("Congratulations, you're right!");
        response.setSuccess(true);
        return response;
    }

    public static Response failResponse() {
        Response response = new Response();
        response.setSuccess(false);
        response.setFeedback("Wrong answer! Please, try again.");
        return response;
    }
}
