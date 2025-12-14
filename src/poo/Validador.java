package poo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class Validador {

    // formato seguro: DD/MM/AAAA
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    // Nome com pelo menos 2 caracteres
    public static boolean nomeValido(String nome) {
        return nome != null && nome.trim().length() >= 2;
    }

    // Quarto: apenas números entre 1 e 9999
    public static boolean quartoValido(String quarto) {
        if (quarto == null || quarto.trim().isEmpty()) return false;

        try {
            int q = Integer.parseInt(quarto.trim());
            return q >= 1 && q <= 9999;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Contacto com pelo menos 9 dígitos numéricos
    public static boolean contatoValido(String contato) {
        if (contato == null) return false;

        // remove tudo que não é dígito (espaços, +351, etc)
        String clean = contato.replaceAll("\\D", "");
        return clean.length() >= 9;
    }

    // Valida a data e devolve a data formatada ou null se for inválida
    public static String validarEFormatarData(String data) {
        try {
            LocalDate d = LocalDate.parse(data, FMT);
            return d.format(FMT);
        } catch (Exception e) {
            return null; // data inválida
        }
    }
}
