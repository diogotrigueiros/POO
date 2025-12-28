package poo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

/**
 Coleção de métodos utilitários para validação e normalização
 de dados fornecidos pelo utilizador (nome, quarto, contacto, data, etc).
 Centraliza as regras e facilita testes e manutenção.
 */
public class Validador {

    // formato seguro: DD/MM/AAAA
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    // Verifica se um nome é considerado válido (>= 2 caracteres)
    public static boolean nomeValido(String nome) {
        return nome != null && nome.trim().length() >= 2;
    }

    /*
     Verifica se o número do quarto é válido.
     Aceita apenas inteiros entre 1 e 9999 (mantém a simplicidade).
     */
    public static boolean quartoValido(String quarto) {
        if (quarto == null || quarto.trim().isEmpty()) return false;

        try {
            int q = Integer.parseInt(quarto.trim());
            return q >= 1 && q <= 9999;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*
     Valida se o contacto contém pelo menos 9 dígitos (após remover caracteres não numéricos).
     Permite formatos com espaços, prefixos ou símbolos como +351.
     */
    public static boolean contatoValido(String contacto) {
        if (contacto == null) return false;

        // remove tudo que não é dígito (espaços, +351, etc)
        String clean = contacto.replaceAll("\\D", "");
        return clean.length() >= 9;
    }

    /*
     Normaliza um contacto removendo caracteres não numéricos (útil para comparações).
     */
    public static String normalizarContato(String contacto) {
        if (contacto == null) return "";
        return contacto.replaceAll("\\D", "");
    }

    /*
     Tenta validar e formatar a data no padrão DD/MM/AAAA.
     Devolve a data formatada se válida, ou null caso inválida.
     */
    public static String validarEFormatarData(String data) {
        try {
            LocalDate d = LocalDate.parse(data, FMT);
            return d.format(FMT);
        } catch (Exception e) {
            return null; // data inválida
        }
    }

    /*
     Verifica se o número de hóspedes é um inteiro válido entre 1 e 100.
     */
    public static boolean numHospedesValido(String s) {
        if (s == null || s.isBlank()) return false;
        try {
            int n = Integer.parseInt(s.trim());
            return n >= 1 && n <= 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
