package poo;

import java.util.Comparator;

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
        return Comparator.comparing(Reserva::isPago).reversed();
    }
}
