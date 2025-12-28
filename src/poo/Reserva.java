package poo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Objects;

/**
 Representa uma reserva do hotel.
 Implementa Comparable para ordenação por data, com fallback para comparação por quarto e nome
 caso a data não possa ser parseada. Contém informação essencial: id, hóspede, quarto, contacto,
 data, dono (quem efetuou a reserva), número de hóspedes e estado de pagamento.
 */
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
        return "ID: " + (id != null ? id : "(sem id)") +
                " | Nome: " + nome +
                " | Quarto: " + quarto +
                " | Contacto: " + contato +
                " | Data: " + data +
                " | Hóspedes: " + numHospedes +
                " | Pago: " + (pago ? "sim" : "não");
    }

    @Override
    public int compareTo(Reserva o) {
        // Tenta ordenar por data (parsing estrito). Se falhar, faz fallback para ordenar por quarto e nome.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reserva)) return false;
        Reserva r = (Reserva) o;
        // Se existir id, usa-o como identificador único; caso contrário, usa nome/quarto/data
        if (this.id != null && r.id != null) {
            return this.id.equals(r.id);
        }
        return Objects.equals(this.nome, r.nome) &&
               Objects.equals(this.quarto, r.quarto) &&
               Objects.equals(this.data, r.data);
    }


    @Override
    public int hashCode() {
        if (id != null) return Objects.hash(id);
        return Objects.hash(nome, quarto, data);
    }
}
