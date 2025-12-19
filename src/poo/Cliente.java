package poo;
import java.util.Scanner;
import java.security.MessageDigest;
import java.util.HexFormat;

public class Cliente extends Utilizador {

    private String hashedNif; // não guardar NIF em claro

    public Cliente() {
        super("", TipoUtilizador.CLIENTE);
    }

    public Cliente(String nome, String nif) {
        super(nome, TipoUtilizador.CLIENTE);
        this.hashedNif = hashNif(nif);
    }

    public String getHashedNif() { return hashedNif; }

    public String getIdentificacao() { return hashedNif; }

    public boolean pagar(Reserva r) {
        if (r == null) return false;
        r.setPago(true);
        return true;
    }

    @Override
    public void mostrarMenu(Hotel hotel) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== MENU CLIENTE =====");
            System.out.println("1 - Criar reserva");
            System.out.println("2 - Ver minhas reservas");
            System.out.println("3 - Pagar reserva");
            System.out.println("0 - Voltar");

            System.out.print("Opção: ");
            String op = scanner.nextLine();

            switch (op) {
                case "1": hotel.criarReservaComoCliente(nome); break;
                case "2": hotel.verReservasDoCliente(nome); break;
                case "3": hotel.pagarReservaDoCliente(nome); break;
                case "0": return;
                default: System.out.println("Opção inválida.");
            }
        }
    }

    private static String hashNif(String nif) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] h = md.digest(nif.getBytes("UTF-8"));
            return HexFormat.of().formatHex(h);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}