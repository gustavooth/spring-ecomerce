package me.gustavoo.services;

import me.gustavoo.services.models.UploadFilesModel;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;

@Service
public class UploadToken {
    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new SecureRandom();
    HashMap<String, UploadFilesModel> tokens;

    UploadToken() {
        this.tokens = new HashMap<>();
    }

    public static String gerarString(int tamanho) {
        if (tamanho <= 0) {
            throw new IllegalArgumentException("O tamanho deve ser maior que zero.");
        }

        // StringBuilder é mais eficiente para concatenar strings em um loop
        StringBuilder sb = new StringBuilder(tamanho);

        for (int i = 0; i < tamanho; i++) {
            // 1. Gera um índice aleatório dentro do comprimento da string de caracteres
            int indiceAleatorio = RANDOM.nextInt(CARACTERES.length());

            // 2. Pega o caractere nesse índice
            char caractereAleatorio = CARACTERES.charAt(indiceAleatorio);

            // 3. Adiciona o caractere ao StringBuilder
            sb.append(caractereAleatorio);
        }

        return sb.toString();
    }


    public String newToken(UploadFilesModel uploadFilesModel) {
        String newToken = gerarString(32);
        if (uploadFilesModel == null) {
        }
        tokens.put(newToken, uploadFilesModel);
        return newToken;
    }

    public UploadFilesModel getToken(String token) {
        return tokens.get(token);
    }

    public void removeToken(String token) {
        tokens.remove(token);
    }
}
