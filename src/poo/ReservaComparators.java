package poo;

import java.util.Comparator;

/*
 Colecção de comparadores utilitários para ordenar listas de `Reserva`.
 */
public class ReservaComparators {

    public static Comparator<Reserva> byRoomNumberNumeric() {
        return (a, b) -> {
            try {
                int na = Integer.parseInt(a.getQuarto());
                int nb = Integer.parseInt(b.getQuarto());
                return Integer.compare(na, nb);
            } catch (Exception e) {
                return a.getQuarto().compareTo(b.getQuarto());
            }
        };
    }

    public static Comparator<Reserva> byNumGuests() {
        return Comparator.comparingInt(Reserva::getNumHospedes);
    }

    public static Comparator<Reserva> byPaidStatus() {
        // Pago primeiro (reversed) para listar primeiro as reservas pagas
        return Comparator.comparing(Reserva::isPago).reversed();
    }
}
