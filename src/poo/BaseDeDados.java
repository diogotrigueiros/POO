package poo;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

/*
 Camada simples de persistência usando ficheiros JSON via Gson.
 Fornece métodos para guardar/carregar listas de reservas e clientes.
 */
public class BaseDeDados {
    private static final String FICHEIRO_RESERVAS = "reservas.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // Guarda a lista de reservas em formato JSON no ficheiro definido.
    public static void guardarReservas(ArrayList<Reserva> reservas) {
        try (Writer w = new FileWriter(FICHEIRO_RESERVAS)) {
            gson.toJson(reservas, w);
        } catch (IOException e) {
            System.out.println("Erro ao guardar reservas: " + e.getMessage());
        }
    }


    // Carrega as reservas do ficheiro JSON. Se ocorrer qualquer erro, devolve uma lista vazia.

    public static ArrayList<Reserva> carregarReservas() {
        try (Reader r = new FileReader(FICHEIRO_RESERVAS)) {
            ArrayList<Reserva> lista = gson.fromJson(r, new TypeToken<ArrayList<Reserva>>(){}.getType());
            return (lista != null) ? lista : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // ======== Clientes persistence ========
    private static final String FICHEIRO_CLIENTES = "clientes.json";

    // Guarda a lista de clientes (com NIF em hash) no ficheiro definido.
    public static void guardarClientes(ArrayList<Cliente> clientes) {
        try (Writer w = new FileWriter(FICHEIRO_CLIENTES)) {
            gson.toJson(clientes, w);
        } catch (IOException e) {
            System.out.println("Erro ao guardar clientes: " + e.getMessage());
        }
    }

    // Carrega clientes do ficheiro JSON. Em caso de erro, devolve lista vazia.
    public static ArrayList<Cliente> carregarClientes() {
        try (Reader r = new FileReader(FICHEIRO_CLIENTES)) {
            ArrayList<Cliente> lista = gson.fromJson(r, new TypeToken<ArrayList<Cliente>>(){}.getType());
            return (lista != null) ? lista : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
