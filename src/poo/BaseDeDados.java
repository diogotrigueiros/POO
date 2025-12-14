package poo;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class BaseDeDados {
    private static final String FICHEIRO = "reservas.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void guardar(ArrayList<Reserva> reservas) {
        try (Writer w = new FileWriter(FICHEIRO)) {
            gson.toJson(reservas, w);
        } catch (IOException e) {
            System.out.println("Erro ao guardar dados: " + e.getMessage());
        }
    }

    public static ArrayList<Reserva> carregar() {
        try (Reader r = new FileReader(FICHEIRO)) {
            ArrayList<Reserva> lista = gson.fromJson(r, new TypeToken<ArrayList<Reserva>>(){}.getType());
            return (lista != null) ? lista : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
