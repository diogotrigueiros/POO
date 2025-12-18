package poo;
public abstract class Utilizador {

    protected String nome;
    protected TipoUtilizador tipo;

    public Utilizador(String nome, TipoUtilizador tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public String getNome() { return nome; }
    public TipoUtilizador getTipo() { return tipo; }

    public abstract void mostrarMenu(Hotel hotel);
}