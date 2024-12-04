package airport;

import com.skillbox.airport.Airport;
import com.skillbox.airport.Flight;
import com.skillbox.airport.Terminal;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.stream.Stream;


public class Main {

    public static long findCountAircraftWithModelAirbus(Airport airport, String model) {
        return airport.getTerminals().stream()
                .flatMap(terminal -> Stream.concat(
                        terminal.getParkedAircrafts().stream(),
                        terminal.getFlights().stream()
                                .map(Flight::getAircraft)
                ))
                .filter(aircraft -> aircraft.getModel().toLowerCase().startsWith(model.toLowerCase()))
                .count();
    }


    public static Map<String, Integer> findMapCountParkedAircraftByTerminalName(Airport airport) {
        //TODO Метод должен вернуть словарь с количеством припаркованных самолетов в каждом терминале.
        return airport.getTerminals().stream()
                .collect(Collectors.toMap(
                        Terminal::getName,
                        terminal -> terminal.getParkedAircrafts().size()
                ));
    }

    public static List<Flight> findFlightsLeavingInTheNextHours(Airport airport, int hours) {
        //TODO Метод должен вернуть список отправляющихся рейсов в ближайшее количество часов.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusHours(hours);

        return airport.getTerminals().stream()
                .flatMap(terminal -> terminal.getFlights().stream())
                .filter(flight -> flight.getType() == Flight.Type.DEPARTURE)
                .filter(flight -> {
                    Date date = Date.from(flight.getDate());
                    LocalDateTime departureTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                    return departureTime.isAfter(now) && departureTime.isBefore(end);
                })
                .collect(Collectors.toList());
    }

    public static Optional<Flight> findFirstFlightArriveToTerminal(Airport airport, String terminalName) {
        //TODO Найти ближайший прилет в указанный терминал.
        return airport.getTerminals().stream()
                .filter(terminal -> terminal.getName().equals(terminalName))
                .flatMap(terminal -> terminal.getFlights().stream())
                .filter(flight -> flight.getType() == Flight.Type.ARRIVAL)
                .min(Comparator.comparing(Flight::getDate));
    }
}