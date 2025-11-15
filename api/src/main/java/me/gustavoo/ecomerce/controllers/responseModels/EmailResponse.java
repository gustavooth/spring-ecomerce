package me.gustavoo.ecomerce.controllers.responseModels;

public class EmailResponse {
    public String loginToken;
    public String nextStep;

    public EmailResponse(String loginToken, String nextStep) {
        this.loginToken = loginToken;
        this.nextStep = nextStep;
    }
}
