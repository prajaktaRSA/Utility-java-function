import java.io.*;
import java.util.*;

public class Utilityfunction {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Input csv file path:");
        String inputFile = scanner.nextLine();

        System.out.print("Output csv file path:");
        String outputFile = scanner.nextLine();

        System.out.print("Choose one operation from (Add/Sort/ConvertToFarenheit/Edit/Delete)");
        String operation = scanner.nextLine();

        try {
            List<Countries> entries = readCSVFile(inputFile);
            //Code for all operations
            if ("Add".equalsIgnoreCase(operation)) {
                String country = userInput("Enter country: ");
                double temperature = Double.parseDouble(userInput("Input temperature in Clecius:"));
                entries.add(new Countries(country, temperature));
            }
            else if ("Sort".equalsIgnoreCase(operation)) {
                Collections.sort(entries, (E1,E2)-> String.CASE_INSENSITIVE_ORDER.compare(E1.getCountry(),E2.getCountry()));
            } else if ("Convert".equalsIgnoreCase(operation)) {
                for (Countries entry : entries) {
                    double fahrenheit = celsiusToFahrenheit(entry.getTemperature());
                    entry.setTemperature(fahrenheit);
                }
            }  else if ("Edit".equalsIgnoreCase(operation)) {
                int index = Integer.parseInt(userInput("Input the index of the entry to edit (indexing starts from 0):"));
                if (index >= 0 && index < entries.size()) {
                    String country = userInput("Enter new country: ");
                    double temperature = Double.parseDouble(userInput("Input new temperature in Celsius:"));
                    entries.set(index, new Countries(country, temperature));
                } else {
                    System.out.println("Invalid index.");
                }
            } else if ("Delete".equalsIgnoreCase(operation)) {
                int index = Integer.parseInt(userInput("Input the index of the entry to delete (indexing starts from 0):"));
                if (index >= 0 && index < entries.size()) {
                    entries.remove(index);
                } else {
                    System.out.println("Invalid index");
                }
            } else {
                System.out.println("Invalid operation");
                return;
            }

            writeCSVFile(outputFile, entries);
            System.out.println("Output file contents:");
            for (Countries entry : entries) {
                System.out.println(entry.toString_());
            }
        } catch (IOException e) {
            System.err.println("Error:" + e.getMessage());
        }
    }
//Method for reading csv file
    public static List<Countries> readCSVFile(String filePath) throws IOException {
        List<Countries> entries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String country = parts[0];
                double temperature = Double.parseDouble(parts[1]);
                entries.add(new Countries(country, temperature));
            }
        }
        return entries;
    }
//Method for writing csv file
    public static void writeCSVFile(String filePath, List<Countries> entries) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("Country" + "," + "Temperature");
            bw.newLine();            
            for (Countries entry : entries) {
                bw.write(entry.getCountry()+"," + entry.getTemperature());
                bw.newLine();
            }
        }
    }

    public static String userInput(String prompt) {
        System.out.print(prompt);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static double celsiusToFahrenheit(double celsius) {
        return (celsius*9/5)+32;
    }
}

class Countries {
    private String country;
    private double temperature;

    public Countries(String country, double temperature) {
        this.country = country;
        this.temperature = temperature;
    }

    public String getCountry() {
        return country;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    //@Override
    public String toString_() {
        return "Country: " + country + ", Temperature: " + temperature;
    }
}