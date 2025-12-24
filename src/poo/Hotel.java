package poo;

import java.util.*;
import java.util.stream.Collectors;

public class Hotel {

    private final ArrayList<Reserva> reservas;
    private final Map<String, Reserva> indexPorId = new HashMap<>();
    private final Map<String, Cliente> clientes = new HashMap<>(); // chave: hashedNif
    private final Scanner scanner = new Scanner(System.in);

    public Hotel() {
        reservas = BaseDeDados.carregarReservas();

        // carregar clientes
        for (Cliente c : BaseDeDados.carregarClientes()) {
            if (c.getHashedNif() != null) {
                clientes.put(c.getHashedNif(), c);
            } else {
                // garantir id e guardar sob id para clientes sem NIF
                if (c.getId() == null) c.setIdIfNull(java.util.UUID.randomUUID().toString());
                clientes.put(c.getId(), c);
            }
        }

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
        BaseDeDados.guardarReservas(reservas);
        BaseDeDados.guardarClientes(new ArrayList<>(clientes.values()));
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

        System.out.print("Contacto: ");
        String contato = scanner.nextLine();
        if (!Validador.contatoValido(contato)) {
            System.out.println("Contacto inválido.\n");
            return;
        }
        if (contatoDuplicado(contato, null)) {
            System.out.println("Contacto já está associado a outra reserva.\n");
            return;
        }

        // Número de hóspedes
        System.out.print("Número de hóspedes: ");
        String numHospedesStr = scanner.nextLine();
        if (!Validador.numHospedesValido(numHospedesStr)) {
            System.out.println("Número de hóspedes inválido.\n");
            return;
        }
        int numHospedes = Integer.parseInt(numHospedesStr.trim());

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

        Reserva nova = new Reserva(nome, quarto, contato, data, dono, numHospedes);

        adicionarReserva(nova);
        System.out.println("Reserva criada!\n");
    }

    private boolean contatoDuplicado(String contato, Integer ignorarIndex) {
        String n = Validador.normalizarContato(contato);
        if (n == null || n.isBlank()) return false;
        for (int i = 0; i < reservas.size(); i++) {
            if (ignorarIndex != null && ignorarIndex == i) continue;
            Reserva r = reservas.get(i);
            String rn = Validador.normalizarContato(r.getContato());
            if (n.equals(rn)) return true;
        }
        return false;
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

        // Mostrar apenas reservas ativas (não pagas) no menu do cliente
        var minhas = reservas.stream()
                .filter(r -> r.getDono() != null &&
                             r.getDono().equalsIgnoreCase(finalDono) &&
                             !r.isPago())
                .collect(Collectors.toList());

        if (minhas.isEmpty()) {
            System.out.println("Não tens reservas ativas.\n");
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
                System.out.println("Nome inválido.\n");
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

        System.out.print("Contacto (" + r.getContato() + "): ");
        String contato = scanner.nextLine();
        if (!contato.isBlank()) {
            if (!Validador.contatoValido(contato)) {
                System.out.println("Contacto inválido.\n");
                return;
            }
            if (contatoDuplicado(contato, id)) {
                System.out.println("Contacto já está associado a outra reserva.\n");
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

        System.out.print("Nº Hóspedes (" + r.getNumHospedes() + "): ");
        String numStr = scanner.nextLine();
        if (!numStr.isBlank()) {
            if (!Validador.numHospedesValido(numStr)) {
                System.out.println("Número de hóspedes inválido.\n");
                return;
            }
            r.setNumHospedes(Integer.parseInt(numStr.trim()));
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

    // ======== Clientes ========
    public void registarCliente() {
        System.out.print("Nome do cliente: ");
        String nome = scanner.nextLine();
        if (!Validador.nomeValido(nome)) { System.out.println("Nome inválido.\n"); return; }

        System.out.print("NIF (somente números): ");
        String nif = scanner.nextLine();
        if (nif == null || nif.replaceAll("\\D", "").length() < 8) { System.out.println("NIF inválido.\n"); return; }

        if (registarCliente(nome, nif)) {
            System.out.println("Cliente registado!\n");
        } else {
            System.out.println("Falha no registo (NIF inválido ou já registado).\n");
        }
    }

    public boolean registarCliente(String nome, String nif) {
        if (!Validador.nomeValido(nome)) return false;
        if (nif == null || nif.replaceAll("\\D", "").length() < 8) return false;
        Cliente c = new Cliente(nome, nif);
        c.setIdIfNull(java.util.UUID.randomUUID().toString());
        if (clientes.containsKey(c.getHashedNif())) return false;
        clientes.put(c.getHashedNif(), c);
        guardar();
        return true;
    }

    public boolean registarClienteSemNif(String nome) {
        if (!Validador.nomeValido(nome)) return false;
        // evitar duplicados simples por nome para clientes sem NIF
        for (Cliente existing : clientes.values()) {
            if (existing.getHashedNif() == null && existing.getNome() != null && existing.getNome().equalsIgnoreCase(nome)) return false;
        }
        Cliente c = new Cliente(nome);
        clientes.put(c.getId(), c);
        guardar();
        return true;
    }

    public Cliente getClienteByHashedNif(String hashed) {
        return clientes.get(hashed);
    }

    public Cliente getClienteByNome(String nome) {
        if (nome == null) return null;
        for (Cliente c : clientes.values()) {
            if (c.getNome() != null && c.getNome().equalsIgnoreCase(nome)) return c;
        }
        return null;
    }

    public void pagarReservaDoCliente(String nome) {
        String finalNome = nome;
        var minhas = reservas.stream()
                .filter(r -> r.getDono() != null && r.getDono().equalsIgnoreCase(finalNome))
                .collect(java.util.stream.Collectors.toList());

        if (minhas.isEmpty()) {
            System.out.println("Não tens reservas.\n");
            return;
        }

        System.out.println("===== AS MINHAS RESERVAS =====");
        for (int i = 0; i < minhas.size(); i++) {
            System.out.println(i + " - " + minhas.get(i));
        }

        System.out.print("Escolhe o ID (posição na lista) para pagar ou 'c' para cancelar: ");
        String in = scanner.nextLine();
        if (in.equalsIgnoreCase("c")) { System.out.println(); return; }

        try {
            int idx = Integer.parseInt(in);
            if (idx < 0 || idx >= minhas.size()) { System.out.println("ID inválido.\n"); return; }
            Reserva r = minhas.get(idx);
            r.setPago(true);
            guardar();
            System.out.println("Reserva paga!\n");
        } catch (Exception e) {
            System.out.println("Entrada inválida.\n");
        }
    }

    // ======== ADMIN: clientes / remoção de duplicados ========
    public void verClientes() {
        if (clientes.isEmpty()) {
            System.out.println("Nenhum cliente registado.\n");
            return;
        }
        System.out.println("===== CLIENTES =====");
        for (Cliente c : clientes.values()) {
            System.out.println(c);
        }
        System.out.println();
    }

    public void removerReservasDuplicadas() {
        if (reservas.isEmpty()) {
            System.out.println("Nenhuma reserva.\n");
            return;
        }
        java.util.Set<Reserva> seen = new java.util.HashSet<>();
        java.util.List<Reserva> toRemove = new java.util.ArrayList<>();
        for (Reserva r : reservas) {
            if (!seen.add(r)) {
                toRemove.add(r);
            }
        }
        if (toRemove.isEmpty()) {
            System.out.println("Não foram encontradas reservas duplicadas.\n");
            return;
        }
        for (Reserva r : toRemove) {
            reservas.remove(r);
            if (r.getId() != null) {
                indexPorId.remove(r.getId());
            } else {
                indexPorId.values().removeIf(v -> v.equals(r));
            }
        }
        guardar();
        System.out.println("Removidas " + toRemove.size() + " reservas duplicadas.\n");
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

    public void verOrdenadoPorNome() {
        List<Reserva> ord = new ArrayList<>(reservas);
        ord.sort(Comparator.comparing(Reserva::getNome, String.CASE_INSENSITIVE_ORDER));
        imprimirLista("RESERVAS POR NOME", ord);
    }

    public void verOrdenadoPorQuartoNumerico() {
        List<Reserva> ord = new ArrayList<>(reservas);
        ord.sort(ReservaComparators.byRoomNumberNumeric());
        imprimirLista("RESERVAS POR NÚMERO DE QUARTO (numérico)", ord);
    }

    public void verOrdenadoPorNumHospedes() {
        List<Reserva> ord = new ArrayList<>(reservas);
        ord.sort(ReservaComparators.byNumGuests());
        imprimirLista("RESERVAS POR Nº HÓSPEDES", ord);
    }

    public void verOrdenadoPorPago() {
        List<Reserva> ord = new ArrayList<>(reservas);
        ord.sort(ReservaComparators.byPaidStatus());
        imprimirLista("RESERVAS POR ESTADO DE PAGAMENTO", ord);
    }



    private void imprimirLista(String titulo, List<Reserva> lista) {
        System.out.println("===== " + titulo + " =====");
        for (Reserva r : lista) System.out.println(r);
        System.out.println();
    }

}
