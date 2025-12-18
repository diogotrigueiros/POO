package poo;

import java.util.*;
import java.util.stream.Collectors;

public class Hotel {

    private final ArrayList<Reserva> reservas;
    private final Map<String, Reserva> indexPorId = new HashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    public Hotel() {
        reservas = BaseDeDados.carregar();

        // criar IDs se faltarem (caso venham de JSON antigo)
        for (Reserva r : reservas) {
            if (r.getId() == null) {
                r.setIdIfNull(UUID.randomUUID().toString());
            }
            indexPorId.put(r.getId(), r);
        }

        guardar();
    }

    private void guardar() {
        BaseDeDados.guardar(reservas);
    }

    // ======== Adiciona reservas mantendo o índice ========
    private void adicionarReserva(Reserva r) {
        reservas.add(r);
        indexPorId.put(r.getId(), r);
        guardar();
    }

    // ======== Remover reservas mantendo o índice ========
    private void removerReservaPorIndice(int i) {
        Reserva r = reservas.remove(i);
        if (r != null) {
            indexPorId.remove(r.getId());
        }
        guardar();
    }

    // ======== Ver reservas geral ========
    public void verTodasReservas() {
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva.\n");
            return;
        }

        System.out.println("===== TODAS AS RESERVAS =====");
        for (int i = 0; i < reservas.size(); i++) {
            System.out.println("ID: " + i + " → " + reservas.get(i));
        }
        System.out.println();
    }

    // ======== Criar reserva com modo Admin ========
    public void criarReservaComoAdmin() {
        criarReserva(null, true);
    }

    // ======== Criar reserva com modo Cliente ========
    public void criarReservaComoCliente(String dono) {
        criarReserva(dono, false);
    }

    private void criarReserva(String donoFixo, boolean isAdmin) {

        String nome;
        if (isAdmin) {
            System.out.print("Nome do hóspede: ");
            nome = scanner.nextLine();
            if (!Validador.nomeValido(nome)) {
                System.out.println("Nome inválido.\n");
                return;
            }
        } else {
            nome = donoFixo;
        }

        System.out.print("Número do quarto: ");
        String quarto = scanner.nextLine();
        if (!Validador.quartoValido(quarto)) {
            System.out.println("Quarto inválido.\n");
            return;
        }

        System.out.print("Contato: ");
        String contato = scanner.nextLine();
        if (!Validador.contatoValido(contato)) {
            System.out.println("Contato inválido.\n");
            return;
        }

        System.out.print("Data (DD/MM/AAAA): ");
        String data = scanner.nextLine();
        data = Validador.validarEFormatarData(data);
        if (data == null) {
            System.out.println("Data inválida.\n");
            return;
        }

        if (!quartoDisponivel(quarto, data, null)) {
            System.out.println("Quarto ocupado.\n");
            return;
        }

        String dono = (donoFixo == null ? nome : donoFixo);

        Reserva nova = new Reserva(nome, quarto, contato, data, dono);

        adicionarReserva(nova);
        System.out.println("Reserva criada!\n");
    }

    private boolean quartoDisponivel(String quarto, String data, Integer ignorarIndex) {
        for (int i = 0; i < reservas.size(); i++) {
            if (ignorarIndex != null && ignorarIndex == i) continue;
            Reserva r = reservas.get(i);
            if (r.getQuarto().equals(quarto) && r.getData().equals(data)) {
                return false;
            }
        }
        return true;
    }

    // ======== Ver reservas por cliente ========
    public void verReservasDoCliente(String dono) {

        String finalDono = dono;

        var minhas = reservas.stream()
                .filter(r -> r.getDono() != null &&
                             r.getDono().equalsIgnoreCase(finalDono))
                .collect(Collectors.toList());

        if (minhas.isEmpty()) {
            System.out.println("Não tens reservas.\n");
            return;
        }

        System.out.println("===== AS MINHAS RESERVAS =====");
        for (Reserva r : minhas) {
            System.out.println(r);
        }
        System.out.println();
    }

    // ======== Editar como Admin ========
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

        System.out.println("Deixe em branco para manter.");

        System.out.print("Nome (" + r.getNome() + "): ");
        String nome = scanner.nextLine();
        if (!nome.isBlank()) {
            if (!Validador.nomeValido(nome)) {
                System.out.println("Invalid name.\n");
                return;
            }
            r.setNome(nome);
        }

        System.out.print("Quarto (" + r.getQuarto() + "): ");
        String quarto = scanner.nextLine();
        if (!quarto.isBlank()) {
            if (!Validador.quartoValido(quarto)) {
                System.out.println("Quarto inválido.\n");
                return;
            }
            if (!quartoDisponivel(quarto, r.getData(), id)) {
                System.out.println("Quarto ocupado.\n");
                return;
            }
            r.setQuarto(quarto);
        }

        System.out.print("Contato (" + r.getContato() + "): ");
        String contato = scanner.nextLine();
        if (!contato.isBlank()) {
            if (!Validador.contatoValido(contato)) {
                System.out.println("Contato inválido.\n");
                return;
            }
            r.setContato(contato);
        }

        System.out.print("Data (" + r.getData() + "): ");
        String data = scanner.nextLine();
        if (!data.isBlank()) {
            String novaData = Validador.validarEFormatarData(data);
            if (novaData == null) {
                System.out.println("Data inválida.\n");
                return;
            }
            if (!quartoDisponivel(r.getQuarto(), novaData, id)) {
                System.out.println("Quarto ocupado.\n");
                return;
            }
            r.setData(novaData);
        }

        guardar();
        System.out.println("Reserva atualizada!\n");
    }

    // ======== Excluir como Admin ========
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

        removerReservaPorIndice(id);
        System.out.println("Reserva removida!\n");
    }

    // ======== FILTROS ========
    public void verPorNumeroDeQuarto() {
        System.out.print("Número do quarto: ");
        String quarto = scanner.nextLine();

        String finalQuarto = quarto;

        var lista = reservas.stream()
                .filter(r -> r.getQuarto() != null &&
                             r.getQuarto().equals(finalQuarto))
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            System.out.println("Sem reservas para este quarto.\n");
            return;
        }

        imprimirLista("RESERVAS DO QUARTO " + quarto, lista);
    }

    public void verPorDia() {
        System.out.print("Data (DD/MM/AAAA): ");
        String data = scanner.nextLine();
        data = Validador.validarEFormatarData(data);

        if (data == null) {
            System.out.println("Data inválida.\n");
            return;
        }

        String finalData = data;

        var lista = reservas.stream()
                .filter(r -> r.getData() != null &&
                             r.getData().equals(finalData))
                .collect(Collectors.toList());

        if (lista.isEmpty()) {
            System.out.println("Sem reservas para esta data.\n");
            return;
        }

        imprimirLista("RESERVAS EM " + data, lista);
    }

    // ======== SORTING ========
    public void verOrdenadoPorData() {
        List<Reserva> ord = new ArrayList<>(reservas);
        Collections.sort(ord); // usa compareTo
        imprimirLista("RESERVAS POR DATA", ord);
    }

    public void verOrdenadoPorQuarto() {
        List<Reserva> ord = new ArrayList<>(reservas);
        ord.sort(Comparator.comparing(Reserva::getQuarto)
                .thenComparing(Reserva::getData));
        imprimirLista("RESERVAS POR QUARTO", ord);
    }

    public void verOrdenadoPorNome() {
        List<Reserva> ord = new ArrayList<>(reservas);
        ord.sort(Comparator.comparing(Reserva::getNome, String.CASE_INSENSITIVE_ORDER));
        imprimirLista("RESERVAS POR NOME", ord);
    }

    private void imprimirLista(String titulo, List<Reserva> lista) {
        System.out.println("===== " + titulo + " =====");
        for (Reserva r : lista) System.out.println(r);
        System.out.println();
    }

}
