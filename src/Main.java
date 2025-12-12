import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("===== Login =====");
            System.out.println("1 - Admin");
            System.out.println("2 - Cliente");
            System.out.println("3 - Sair");
            System.out.println("Opção: ");
            String perfil = sc.nextLine();
            System.out.println();

            if (perfil.equals("1")) {
                while (true) {
                    System.out.println("===== Admin =====");
                    System.out.println("1 - Criar reserva");
                    System.out.println("2 - Ver todas as reservas");
                    System.out.println("3 - Editar reserva");
                    System.out.println("4 - Excluir reserva");
                    System.out.println("5 - Ver por nº de quarto");
                    System.out.println("6 - Ver por dia");
                    System.out.println("7 - Terminar sessão");
                    System.out.println("Opção: ");
                    String op = sc.nextLine();
                    System.out.println();

                    switch (op) {
                        case "1":
                            hotel.criarReservaComoAdmin();
                            break;
                        case "2":
                            hotel.verTodasReservas();
                            break;
                        case "3":
                            hotel.editarReservaAdmin();
                            break;
                        case "4":
                            hotel.excluirReservaAdmin();
                            break;
                        case "5":
                            hotel.verPorNumeroDeQuarto();
                            break;
                        case "6":
                            hotel.verPorDia();
                            break;
                        case "7":
                            System.out.println("Sessão encerrada \n");
                            break;
                        default:
                            System.out.println("Opção inválida \n");
                    }
                    if (op.equals("7"))
                        break;
                }
            } else if (perfil.equals("2")) {
                System.out.println("O teu nome: ");
                String cliente = sc.nextLine().trim();
                if (!Validador.nomeValido(cliente)) {
                    System.out.println("Nome inválido \n");
                    continue;
                }
                System.out.println();
                while (true) {
                    System.out.println("===== Cliente (" + cliente + ") =====");
                    System.out.println("1 - Criar a minha reserva");
                    System.out.println("2 - Ver as minhas reservas");
                    System.out.println("3 - Terminar sessão");
                    System.out.println("Opção: ");
                    String op = sc.nextLine();
                    System.out.println();

                    switch (op) {
                        case "1":
                            hotel.criarReservaComoCliente(cliente);
                            break;
                        case "2":
                            hotel.verReservasDoCliente(cliente);
                            break;
                        case "3":
                            System.out.println("Sessão terminada \n");
                            break;
                        default:
                            System.out.println("Opção inválida \n");
                    }
                    if (op.equals("3"))
                        break;
                }
            } else if (perfil.equals("3")) {
                System.out.println("Programa encerrado");
                return;
            } else {
                System.out.println("Opção inválida \n");
            }
        }
    }
}