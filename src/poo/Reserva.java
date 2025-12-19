package poo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

public class Reserva implements Comparable<Reserva> {
    private String id;
    private String nome;      // hóspede
    private String quarto;    // manter como String para simplicidade
    private String contato;   // telefone/email
    private String data;      // DD/MM/AAAA
    private String dono;      // quem fez a reserva (para filtrar cliente)
    private int numHospedes = 1; // número de hóspedes
    private boolean pago = false; // estado do pagamento

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public Reserva(String nome, String quarto, String contato, String data, String dono) {
        this(nome, quarto, contato, data, dono, 1);
    }

    public Reserva(String nome, String quarto, String contato, String data, String dono, int numHospedes) {
        this.nome = nome;
        this.quarto = quarto;
        this.contato = contato;
        this.data = data;
        this.dono = dono;
        this.numHospedes = numHospedes;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public void setIdIfNull(String id) { if (this.id == null) this.id = id; }

    public String getNome() { return nome; }
    public String getQuarto() { return quarto; }
    public String getContato() { return contato; }
    public String getData() { return data; }
    public String getDono() { return dono; }
    public int getNumHospedes() { return numHospedes; }
    public boolean isPago() { return pago; }

    public void setNome(String nome) { this.nome = nome; }
    public void setQuarto(String quarto) { this.quarto = quarto; }
    public void setContato(String contato) { this.contato = contato; }
    public void setData(String data) { this.data = data; }
    public void setDono(String dono) { this.dono = dono; }
    public void setNumHospedes(int numHospedes) { this.numHospedes = numHospedes; }
    public void setPago(boolean pago) { this.pago = pago; }

    @Override
    public String toString() {
        return "Nome: " + nome +
                " | Quarto: " + quarto +
                " | Contato: " + contato +
                " | Data: " + data +
                " | Hóspedes: " + numHospedes +
                " | Pago: " + (pago ? "sim" : "não");
    }

    @Override
    public int compareTo(Reserva o) {
        try {
            LocalDate d1 = LocalDate.parse(this.data, FMT);
            LocalDate d2 = LocalDate.parse(o.data, FMT);
            int cmp = d1.compareTo(d2);
            if (cmp != 0) return cmp;
        } catch (Exception e) {
            // ignorar erro de parsing e comparar como string
        }
        int cmp = this.quarto.compareTo(o.quarto);
        if (cmp != 0) return cmp;
        return this.nome.compareToIgnoreCase(o.nome);
    }
}
