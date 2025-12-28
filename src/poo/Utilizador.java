package poo;

/*
 Classe base para utilizadores do sistema (Admin ou Cliente).
 Contém o nome e o tipo do utilizador, assim como o contrato para
 o método `mostrarMenu` que cada subtipo deve implementar.
 */
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