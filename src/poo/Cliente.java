package poo;
import java.util.Scanner;

public class Cliente extends Utilizador {

    public Cliente(String nome) {
        super(nome, TipoUtilizador.CLIENTE);
    }

    @Override
    public void mostrarMenu(Hotel hotel) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== MENU CLIENTE =====");
            System.out.println("1 - Criar reserva");
            System.out.println("2 - Ver minhas reservas");
            System.out.println("0 - Voltar");

            System.out.print("Opção: ");
            String op = scanner.nextLine();

            switch (op) {
                case "1": hotel.criarReservaComoCliente(nome); break;
                case "2": hotel.verReservasDoCliente(nome); break;
                case "0": return;
                default: System.out.println("Opção inválida.");
            }
        }
    }
}