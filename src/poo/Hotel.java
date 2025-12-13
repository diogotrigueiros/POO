package poo;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Hotel {

    private final ArrayList<Reserva> reservas;
    private final Scanner scanner = new Scanner(System.in);

    public Hotel() {
        reservas = BaseDeDados.carregar();
    }

    private void guardar() {
        BaseDeDados.guardar(reservas);
    }

    private boolean quartoDisponivel(String quarto, String data, Integer ignorarIndex) {
        for (int i = 0; i < reservas.size(); i++) {
            if (ignorarIndex != null && i == ignorarIndex) continue;
            Reserva r = reservas.get(i);
            if (r.getQuarto().equals(quarto) && r.getData().equals(data)) {
                return false;
            }
        }
        return true;
    }

    // CRUDZITO
    public void criarReservaComoAdmin() {
        criarReserva(null, true);
    }

    public void criarReservaComoCliente(String dono) {
        criarReserva(dono, false);
    }

    private void criarReserva(String donoFixo, boolean isAdmin) {
        String nome;

        if (isAdmin) {
            System.out.print("Nome do hóspede: ");
            nome = scanner.nextLine();

            if (!Validador.nomeValido(nome)) {
                System.out.println("⚠ Nome inválido.\n");
                return;
            }

        } else {
            nome = donoFixo;
        }

        System.out.print("Número do quarto: ");
        String quarto = scanner.nextLine();

        if (!Validador.quartoValido(quarto)) {
            System.out.println("⚠ Quarto inválido (1..9999).\n");
            return;
        }

        System.out.print("Contato (telefone/email): ");
        String contato = scanner.nextLine();

        if (!Validador.contatoValido(contato)) {
            System.out.println("⚠ Contato inválido (mín. 9 dígitos).\n");
            return;
        }

        System.out.print("Data (DD/MM/AAAA): ");
        String data = scanner.nextLine();
        data = Validador.validarEFormatarData(data);

        if (data == null) {
            System.out.println("⚠ Data inválida.\n");
            return;
        }

        if (!quartoDisponivel(quarto, data, null)) {
            System.out.println("⚠ Quarto já ocupado nessa data.\n");
            return;
        }

        String dono = (donoFixo != null) ? donoFixo : nome;

        reservas.add(new Reserva(nome, quarto, contato, data, dono));
        guardar();
        System.out.println("✔ Reserva criada!\n");
    }

    public void verTodasReservas() {
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva.\n");
            return;
        }

        System.out.println("===== TODAS AS RESERVAS =====");
        for (int i = 0; i < reservas.size(); i++) {
            System.out.println("ID " + i + " → " + reservas.get(i));
        }
        System.out.println();
    }

    public void verReservasDoCliente(String dono) {

        String finalDono = dono;

        var minhas = reservas.stream()
                .filter(r -> r.getDono().equalsIgnoreCase(finalDono))
                .collect(Collectors.toList());

        if (minhas.isEmpty()) {
            System.out.println("Ainda não tens reservas.\n");
            return;
        }

        System.out.println("===== AS MINHAS RESERVAS =====");
        for (Reserva r : minhas) {
            System.out.println("ID (global) " + reservas.indexOf(r) + " → " + r);
        }
        System.out.println();
    }

    public void editarReservaAdmin() {
        verTodasReservas();
        if (reservas.isEmpty()) return;

        System.out.print("ID da reserva a editar: ");
        String idStr = scanner.nextLine();

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            System.out.println("ID inválido.\n");
            return;
        }

        if (id < 0 || id >= reservas.size()) {
            System.out.println("ID inexistente.\n");
            return;
        }

        Reserva r = reservas.get(id);

        System.out.println("Deixa em branco para manter o valor atual.");

        System.out.print("Nome (" + r.getNome() + "): ");
        String nome = scanner.nextLine();
        if (!nome.isBlank()) {
            if (!Validador.nomeValido(nome)) {
                System.out.println("⚠ Nome inválido.\n");
                return;
            }
            r.setNome(nome);
        }

        System.out.print("Quarto (" + r.getQuarto() + "): ");
        String quarto = scanner.nextLine();
        if (!quarto.isBlank()) {

            if (!Validador.quartoValido(quarto)) {
                System.out.println("⚠ Quarto inválido.\n");
                return;
            }

            if (!quartoDisponivel(quarto, r.getData(), id)) {
                System.out.println("⚠ Quarto já ocupado nessa data.\n");
                return;
            }

            r.setQuarto(quarto);
        }

        System.out.print("Contato (" + r.getContato() + "): ");
        String contato = scanner.nextLine();
        if (!contato.isBlank()) {

            if (!Validador.contatoValido(contato)) {
                System.out.println("⚠ Contato inválido.\n");
                return;
            }

            r.setContato(contato);
        }

        System.out.print("Data (" + r.getData() + "): ");
        String data = scanner.nextLine();

        if (!data.isBlank()) {

            String novaData = Validador.validarEFormatarData(data);

            if (novaData == null) {
                System.out.println("⚠ Data inválida.\n");
                return;
            }

            if (!quartoDisponivel(r.getQuarto(), novaData, id)) {
                System.out.println("⚠ Quarto já ocupado nessa data.\n");
                return;
            }

            r.setData(novaData);
        }

        guardar();
        System.out.println("✔ Reserva atualizada!\n");
    }

    public void excluirReservaAdmin() {
        verTodasReservas();
        if (reservas.isEmpty()) return;

        System.out.print("ID da reserva a excluir: ");
        String idStr = scanner.nextLine();

        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (Exception e) {
            System.out.println("ID inválido.\n");
            return;
        }

        if (id < 0 || id >= reservas.size()) {
            System.out.println("ID inexistente.\n");
            return;
        }

        reservas.remove(id);
        guardar();
        System.out.println("✔ Reserva removida!\n");
    }

    // Filtros
    public void verPorNumeroDeQuarto() {
        System.out.print("Número do quarto: ");
        String quarto = scanner.nextLine();

        if (!Validador.quartoValido(quarto)) {
            System.out.println("⚠ Quarto inválido.\n");
            return;
        }

        String finalQuarto = quarto;

        var lista = reservas.stream()
                .filter(r -> r.getQuarto().equals(finalQuarto))
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            System.out.println("Sem reservas para o quarto " + quarto + ".\n");
            return;
        }

        System.out.println("===== RESERVAS DO QUARTO " + quarto + " =====");
        for (Reserva r : lista) System.out.println(r);
        System.out.println();
    }

    public void verPorDia() {
        System.out.print("Data (DD/MM/AAAA): ");
        String data = scanner.nextLine();
        data = Validador.validarEFormatarData(data);

        if (data == null) {
            System.out.println("⚠ Data inválida.\n");
            return;
        }

        String finalData = data;

        var lista = reservas.stream()
                .filter(r -> r.getData().equals(finalData))
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            System.out.println("Sem reservas em " + data + ".\n");
            return;
        }

        System.out.println("===== RESERVAS EM " + data + " =====");
        for (Reserva r : lista) System.out.println(r);
        System.out.println();
    }
}
