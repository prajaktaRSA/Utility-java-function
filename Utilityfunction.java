import java.io.*;
import java.util.*;

public class Utilityfunction {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Input csv file path:");
        String inputFile = scanner.nextLine();

        System.out.println("Output csv file path:");
        String outputFile = scanner.nextLine();

        System.out.println("Choose one operation from (Add/Sort/Convert/Edit/Delete) ");
        String operation = scanner.nextLine();

        try {
            List<CountryTemperature> entries = readCSVFile(inputFile);
            //Code for all operations
            if ("Add".equalsIgnoreCase(operation)) {
                String country = userInput("Enter country: ");
                
                String temperature = userInput("Input temperature in Celcius: ");
                
                if (country.trim().isEmpty()) {
                    System.out.println("Error: Country not specified");
                    return;
                }
                if (temperature.trim().isEmpty()) {
                    System.out.println("Error: Temperature not specified");
                    return;
                }
                
                boolean isDuplicate = false;
                for (CountryTemperature ct : entries) {
                    if (ct.getCountry().equalsIgnoreCase(country)) {
                        isDuplicate = true;
                        break;
                        
                    }
                }
                if(isDuplicate) {
                    System.out.println("Error: Country name "+ country + " already exists");
                }
                else {
                    Double newtemperature = Double.parseDouble(temperature);
                    entries.add(new CountryTemperature(country, newtemperature));
                }
                
                
            }
            else if ("Sort".equalsIgnoreCase(operation)) {
                Collections.sort(entries, (E1,E2)-> String.CASE_INSENSITIVE_ORDER.compare(E1.getCountry(),E2.getCountry()));
            } else if ("Convert".equalsIgnoreCase(operation)) {
                for (CountryTemperature entry : entries) {
                    double fahrenheit = celsiusToFahrenheit(entry.getTemperature());
                    entry.setTemperature(fahrenheit);
                }
            }  else if ("Edit".equalsIgnoreCase(operation)) {
                String editOption = userInput(" Edit Country or Temperature or both. Choose (C/T/both):  ");

                    if ("C".equalsIgnoreCase(editOption)) { //edit country only: case 1
                        String editCountry = userInput("Enter country to be edited:  ");
                        CountryTemperature entryToEdit = null;
                        for (CountryTemperature entry : entries) {
                            if (editCountry.equalsIgnoreCase(entry.getCountry())){// if countryname to be edited matches 
                                entryToEdit = entry;
                                break;
                            }
                        }
                        if (entryToEdit==null) {
                            System.out.println("Country to edit not found");
                            
                        }
                        else {
                            //country to edit found.
                            //Ask for new country value and replace country value
                            String newCountry= userInput("Enter new Country:  ");
                            entryToEdit.setCountry(newCountry);
                        }
                        

                    }
                    else if ("T".equalsIgnoreCase(editOption)) {//if edit only temp: case 2
                        String whichCountry = userInput("Enter country name to edit the temperature: ");
                        CountryTemperature entryToEdit = null;
                        for (CountryTemperature entry : entries) {
                            if (whichCountry.equalsIgnoreCase(entry.getCountry())){// if countryname to be edited matches 
                                entryToEdit = entry;
                                break;
                            }
                        }
                        if (entryToEdit==null) {
                            System.out.println("Country name not found");
                            
                        }
                        else {
                            
                            Double newTemperature = Double.parseDouble(userInput("Enter new temperature in celcius: "));
                            entryToEdit.setTemperature(newTemperature);
                        }
                    }
                    else { //Edit both ct : case 3
                        String editCountry = userInput("Enter country name to edit: ");
                        CountryTemperature entryToEdit =null;
                        for (CountryTemperature entry: entries) {
                            if (editCountry.equalsIgnoreCase(entry.getCountry())) {
                                entryToEdit = entry;
                                break;
                            }
                            if (entryToEdit==null) {
                                System.out.println("Country not found");
                                return;
                            }
                            else {
                                String country = userInput("Enter new country name: ");
                                Double temperature =Double.parseDouble(userInput("Enter new temperature: "));
                                entryToEdit.setTemperature(temperature);
                                entryToEdit.setCountry(country);
                            }
                        }
                    }

                }    
              
             else if ("Delete".equalsIgnoreCase(operation)) {
                String deleteCountry = userInput("Enter country to delete: ");
                CountryTemperature deleteEntry =null;
                for (CountryTemperature entry : entries) {
                    if (deleteCountry.equalsIgnoreCase(entry.getCountry())) {
                        deleteEntry = entry;
                        break;
                    }
                    if (deleteEntry==null) {
                        System.out.println("Country not specified. Try again! ");
                        return;
                    }
                    else {
                        entries.remove(deleteEntry);
                    }
                }
                
            } else {
                System.out.println("Invalid operation");
                return;
            }

            writeCSVFile(outputFile, entries);
            System.out.println("Output file contents:");
            for (CountryTemperature entry : entries) {
                System.out.println(entry.toString_());
            }
        } catch (IOException e) {
            System.err.println("Error:" + e.getMessage());
        }
    }
//Method for reading csv file
    public static List<CountryTemperature> readCSVFile(String filePath) throws IOException {
        List<CountryTemperature> entries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                String country = parts[0];
                double temperature = Double.parseDouble(parts[1]);
                entries.add(new CountryTemperature(country, temperature));
            }
        }
        return entries;
    }
//Method for writing csv file
    public static void writeCSVFile(String filePath, List<CountryTemperature> entries) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("Country" + "," + "Temperature");
            bw.newLine();            
            for (CountryTemperature entry : entries) {
                bw.write(entry.getCountry()+"," + entry.getTemperature());
                bw.newLine();
            }
        }
    }

    public static String userInput(String prompt) {
        
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        String input = scanner.nextLine();
        //scanner.close();
        return input;
    }

    public static double celsiusToFahrenheit(double celsius) {
        return (celsius*9/5)+32;
    }
}

class CountryTemperature {
    private String country;
    private double temperature;

    public CountryTemperature(String country, double temperature) {
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

    public void setCountry(String country) {
        this.country = country;
    }
    //@Override
    public String toString_() {
        return "Country: " + country + ", Temperature: " + temperature;
    }
}