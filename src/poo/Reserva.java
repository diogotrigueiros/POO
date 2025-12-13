package poo;

public class Reserva {
    private String nome;      // hóspede
    private String quarto;    // manter como String para simplicidade
    private String contato;   // telefone/email
    private String data;      // DD/MM/AAAA
    private String dono;      // quem fez a reserva (para filtrar cliente)

    public Reserva(String nome, String quarto, String contato, String data, String dono) {
        this.nome = nome;
        this.quarto = quarto;
        this.contato = contato;
        this.data = data;
        this.dono = dono;
    }

    public String getNome() { return nome; }
    public String getQuarto() { return quarto; }
    public String getContato() { return contato; }
    public String getData() { return data; }
    public String getDono() { return dono; }

    public void setNome(String nome) { this.nome = nome; }
    public void setQuarto(String quarto) { this.quarto = quarto; }
    public void setContato(String contato) { this.contato = contato; }
    public void setData(String data) { this.data = data; }
    public void setDono(String dono) { this.dono = dono; }

    @Override
    public String toString() {
        return "Nome: " + nome +
                " | Quarto: " + quarto +
                " | Contato: " + contato +
                " | Data: " + data;
    }
}
