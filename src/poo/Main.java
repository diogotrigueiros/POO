package poo;

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
                    System.out.println("5 - Filtrar por nº de quarto");
                    System.out.println("6 - Filtrar por data");
                    System.out.println("7 - Ordenar por data");
                    System.out.println("8 - Ordenar por nº de quarto (numérico)");
                    System.out.println("9 - Ordenar por nome");
                    System.out.println("10 - Ordenar por nº hóspedes");
                    System.out.println("11 - Ordenar por estado de pagamento");
                    System.out.println("12 - Terminar sessão");
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
                            hotel.verOrdenadoPorData();
                            break;
                        case "8":
                            hotel.verOrdenadoPorQuartoNumerico();
                            break;
                        case "9":
                            hotel.verOrdenadoPorNome();
                            break;
                        case "10":
                            hotel.verOrdenadoPorNumHospedes();
                            break;
                        case "11":
                            hotel.verOrdenadoPorPago();
                            break;
                        case "12":
                            System.out.println("Sessão encerrada \n");
                            break;
                        default:
                            System.out.println("Opção inválida \n");
                    }
                    if (op.equals("12"))
                        break;
                }
            } else if (perfil.equals("2")) {
                System.out.print("És cliente registado? (s/n): ");
                String reg = sc.nextLine().trim();
                String cliente = null;
                if (reg.equalsIgnoreCase("s")) {
                    System.out.print("NIF: ");
                    String nif = sc.nextLine().trim();
                    if (nif.isBlank()) { System.out.println("NIF inválido\n"); continue; }
                    String hashed = null;
                    try { java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
                          hashed = java.util.HexFormat.of().formatHex(md.digest(nif.getBytes("UTF-8"))); }
                    catch (Exception e) { System.out.println("Erro a processar NIF.\n"); continue; }
                    var c = hotel.getClienteByHashedNif(hashed);
                    if (c == null) { System.out.println("Cliente não encontrado.\n"); continue; }
                    cliente = c.getNome();
                } else {
                    System.out.println("O teu nome: ");
                    cliente = sc.nextLine().trim();
                    // Perguntar ao utilizador se pretende registar o NIF
                    System.out.print("Queres registar-te com o NIF? (s/n): ");
                    String wantReg = sc.nextLine().trim();
                    if (wantReg.equalsIgnoreCase("s")) {
                        System.out.print("NIF (somente números): ");
                        String nif = sc.nextLine().trim();
                        if (hotel.registarCliente(cliente, nif)) {
                            System.out.println("Registo efetuado. Agora podes autenticar com o teu NIF no próximo login.\n");
                        } else {
                            System.out.println("Registo falhou (NIF inválido ou já registado).\n");
                        }
                    }
                }
                if (!Validador.nomeValido(cliente)) {
                    System.out.println("Nome inválido \n");
                    continue;
                }
                System.out.println();
                while (true) {
                    System.out.println("===== Cliente (" + cliente + ") =====");
                    System.out.println("1 - Criar a minha reserva");
                    System.out.println("2 - Ver as minhas reservas");
                    System.out.println("3 - Pagar reserva");
                    System.out.println("4 - Terminar sessão");
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
                            hotel.pagarReservaDoCliente(cliente);
                            break;
                        case "4":
                            System.out.println("Sessão terminada \n");
                            break;
                        default:
                            System.out.println("Opção inválida \n");
                    }
                    if (op.equals("4"))
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