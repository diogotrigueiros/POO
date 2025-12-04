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
        for(int i = 0; i < reservas.size(); i++) {
            if (ignorarIndex != null && i == ignorarIndex) continue;
            Reserva r = reservas.get(i);
            if (r.getQuarto().equals(quarto) && r.getData().equals(data)) {
                return false;
            }
    }
    return true;
    }

    public void criarReservaComoAdmin(){
        criarReserva(null, true);
    }
    public void criarReservaComoCliente(String dono) {
        criarReserva(dono, false);
    }
    public void criarReserva(String donoFixo, boolean isAdmin){
        String nome;
        if(isAdmin){
            System.out.println("Nome do hóspede:");
            nome = scanner.nextLine();
            if(!Validador.nomeValido(nome)){
                System.out.println("Nome inválido \n");
                return;
            }
        } else {
            nome = donoFixo;
        }
        System.out.println("Número do quarto");
        String quarto = scanner.nextLine();

        if (!Validador.quartoValido(quarto)) {
            System.out.println("Quarto inválido (1..9999) \n");
            return;
        }
        System.out.println("Contacto (telefone/email): ");
        String contato = scanner.nextLine();
        if(!Validador.contatoValido(contato)) {
            System.out.println("Contacto inválido (min. 9 digitios)\n");
            return;
        }
        System.out.println("Data (DD/MM/AAAA):");
        String data = scanner.nextLine();
        data = Validador.validarEFormatarData(data);

        if (data == null) {
            System.out.println("Data inválida \n");
            return;
        }
        if(!quartoDisponivel(quarto, data, null)){
            System.out.println("Quarto já ocupado nessa data\n");
            return;
        }
        String dono = (donoFixo != null) ? donoFixo : nome;

        reservas.add(new Reserva(nome, quarto contato, data, dono));
        guardar();
        System.out.println("Reserva criada!\n");
    }

    public void verTodasReservas(){
        if (reservas.isEmpty()) {
            System.out.println("NNenhuma reserva\n");
            return;
        }
        System.out.println("Todas as reservas");
        for(int i = 0; i < reservas.size(); i++) {
            System.out.println("ID" + i + " -> " + reservas.get(i));
        }
        System.out.println();
        }
    public void verReservasDoCliente(String dono) {
        String finalDono = dono;
        var minhas = reservas.stream()
                .filter(r -> r.getDono().equalsIgnoreCase(finalDono))
                .collect(Collectors.toList());
        if(minhas.isEmpty()) {
            System.out.println("Ainda não tens reservas \n");
            return;
        }

        System.out.println("As minhas reservas");
        for(Reserva r : minhas) {
            System.out.println("ID (global)" + reservas.indexOf(r) + " -> " + r);
        }
        System.out.println();
    }

    public void editarReservaAdmin(){
        verTodasReservas();
        if (reservas.isEmpty()) return;

        System.out.println("ID da reserva a editar: ");
        String idStr = scanner.nextLine();

        int id;
        try {
            id =Integer.parseInt(idStr);
        } catch (Exception e) {
            System.out.println("ID inexistente \n");
            return;
        }

        if (id < 0 || id >= reservas.size()) {
            System.out.println("ID inexistente \n");
            return;
        }
        Reserva r = reservas.get(id);

        System.out.println("Deixa em branco para manter o valor atual");

        System.out.println("Nome (" + r.getNome() + "): ");
        String nome = scanner.nextLine();
        if(!nome.isBlank()) {
            if (!Validador.nomeValido(nome)){
                System.out.println("Nome inválido\n");
                return;
            }
            r.setNome(nome);
        }
        System.out.println("Quarto (" + r.getQuarto() + "): ");
        String quarto = scanner.nextLine();
        if(!quarto.isBlank()) {
            if(!Validador.quartoValido(quarto)) {
                System.out.println("Quarto inválido\n");
                return;
            }
        if (!quartoDisponivel(quarto. r.getData(), id)) {
            System.out.println("Quarto já ocupado nessa data\n");
            return;
        }
        r.setQuarto(quarto)
        }
        System.out.println("Contacto (" + r.getContato() + "): ");
        String contato = scanner.nextLine();
        if (!contato.isBlank()) {
            if (!Validador.contatoValido(contato)) {
                System.out.println("Contato inválido\n");
                return;
            }
        r.setContato(contato);

        }
        System.out.println("Data(" + r.getData() + "): ");
        String data = scanner.nextLine();

    }
}


// incompleto, ainda em desenvolvimento...

