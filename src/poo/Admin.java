package poo;
import java.util.Scanner;

public class Admin extends Utilizador {

    public Admin(String nome) {
        super(nome, TipoUtilizador.ADMIN);
    }

    @Override
    public void mostrarMenu(Hotel hotel) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== MENU ADMIN =====");
            System.out.println("1 - Criar reserva");
            System.out.println("2 - Ver reservas");
            System.out.println("3 - Editar reserva");
            System.out.println("4 - Excluir reserva");
            System.out.println("5 - Ver por quarto");
            System.out.println("6 - Ver por data");
            System.out.println("0 - Voltar");

            System.out.print("Opção: ");
            String op = scanner.nextLine();

            switch (op) {
                case "1": hotel.criarReservaComoAdmin(); break;
                case "2": hotel.verTodasReservas(); break;
                case "3": hotel.editarReservaAdmin(); break;
                case "4": hotel.excluirReservaAdmin(); break;
                case "5": hotel.verPorNumeroDeQuarto(); break;
                case "6": hotel.verPorDia(); break;
                case "0": return;
                default: System.out.println("Opção inválida.");
            }
        }
    }
}