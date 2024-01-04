package ru.ifmo.webservices;

import org.uddi.api_v3.BusinessService;
import ru.ifmo.webservices.generated.Movie;
import ru.ifmo.webservices.generated.MovieService;
import ru.ifmo.webservices.generated.MovieWebService;

import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        JUDDIClient juddiClient = new JUDDIClient();
        String authToken = juddiClient.getAuthKey("uddiadmin", "da_password1");

        while (true) {
            System.out.println();
            System.out.println("Выберите действие:");
            System.out.println("1. Зарегистрировать сервис");
            System.out.println("2. Найти сервис по имени");
            System.out.println("3. Выйти");
            System.out.println();

            int action = Integer.parseInt(scanner.nextLine().trim());
            switch (action) {
                case 1:
                    String businessName = readString("Укажите имя бизнеса");
                    String serviceName = readString("Укажите имя сервиса");
                    String serviceDescription = readString("Укажите описание сервиса");
                    String wsdlUrl = readString("Укажите WSDL URL");

                    juddiClient.publishService(authToken, businessName, serviceName, serviceDescription, wsdlUrl);
                    break;
                case 2:
                    String searchName = readString("Укажите имя сервиса");

                    BusinessService businessService = juddiClient.findServiceByName(authToken, searchName);
                    if (businessService == null) {
                        System.out.println("Сервис с таким именем не найден");
                        break;
                    }

                    String accessPoint = businessService.getBindingTemplates().getBindingTemplate().get(0).getAccessPoint().getValue();

                    System.out.println("Service Name: " + businessService.getName().get(0).getValue());
                    System.out.println("Service Description: " + businessService.getDescription().get(0).getValue());
                    System.out.println("Service Access Point: " + accessPoint);

                    String isDoTestQuery = readString("Сделать тестовый запрос? [y - да, n - нет]");
                    if (isDoTestQuery.equals("y")) {
                        try {
                            URL serviceUrl = new URL(accessPoint);
                            MovieService movieService = new MovieWebService(serviceUrl).getMovieWebServicePort();
                            List<Movie> movies = movieService.getAllMovies();

                            System.out.println("\nAll movies:");
                            printMovieTable(movies);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Неизвестная команда");
            }
        }
    }

    private static String readString(String message) {
        System.out.println();
        System.out.println(message + ":");
        String value;
        do {
            value = scanner.nextLine().trim();
        } while (value.isEmpty());
        return value;
    }

    private static void printMovieTable(List<Movie> movies) {
        System.out.printf("%-38s | %-7s | %-7s | %-8s%n", "NAME", "RELEASE", "COUNTRY", "DURATION");
        System.out.printf("---------------------------------------------------------------------%n");
        for (Movie movie : movies) {
            System.out.printf("%-38s | %-7s | %-7s | %-8s%n",
                    movie.getName(), movie.getReleaseDate(), movie.getCountry(), movie.getDuration());
        }
    }
}
