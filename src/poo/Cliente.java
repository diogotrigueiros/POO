package poo;
import java.util.Scanner;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Objects;
import java.util.UUID;

public class Cliente extends Utilizador {

    private String id;
    private String hashedNif; // não guardar NIF em claro

    public Cliente() {
        super("", TipoUtilizador.CLIENTE);
    }

    public Cliente(String nome) {
        super(nome, TipoUtilizador.CLIENTE);
        this.id = UUID.randomUUID().toString();
    }

    public Cliente(String nome, String nif) {
        super(nome, TipoUtilizador.CLIENTE);
        this.hashedNif = hashNif(nif);
        this.id = UUID.randomUUID().toString();
    }

    public String getId() { return id; }
    public void setIdIfNull(String id) { if (this.id == null) this.id = id; }

    public String getHashedNif() { return hashedNif; }

    public String getIdentificacao() { return (hashedNif != null) ? hashedNif : id; }

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

    @Override
    public String toString() {
        if (hashedNif != null) {
            String h = (hashedNif.length() > 8 ? hashedNif.substring(0,8) + "..." : hashedNif);
            return "Cliente{name='" + nome + "', hashedNif='" + h + "'}";
        } else if (id != null) {
            String s = (id.length() > 8 ? id.substring(0,8) + "..." : id);
            return "Cliente{name='" + nome + "', id='" + s + "'}";
        } else {
            return "Cliente{name='" + nome + "'}";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente c = (Cliente) o;
        if (this.hashedNif != null && c.hashedNif != null) {
            return this.hashedNif.equals(c.hashedNif);
        }
        if (this.id != null && c.id != null) {
            return this.id.equals(c.id);
        }
        return Objects.equals(this.nome, c.nome);
    }

    @Override
    public int hashCode() {
        if (hashedNif != null) return Objects.hash(hashedNif);
        if (id != null) return Objects.hash(id);
        return Objects.hash(nome);
    }
}