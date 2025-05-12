package MovieRent;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Files {

    public void movieFileWrite(ArrayList<Movie> movies) throws IOException{
        FileWriter movieFile = new FileWriter("movie.txt", true);
        for(Movie b: movies){
            String movieInfo = b.getTitle() + ", " + b.getPrice() + "\n";
            movieFile.write(movieInfo);
        }
        movieFile.close();
    }

    public void customerFileWrite(ArrayList<Customer> customers) throws IOException{
        FileWriter customerFile = new FileWriter("customer.txt", true);
        for(Customer c: customers){
            String outputText = c.getUsername() + ", " + c.getPassword() + ", " + c.getPoints() + "\n";
            customerFile.write(outputText);
        }
        customerFile.close();
    }

    public void movieFileReset() throws IOException {
        FileWriter movieFile = new FileWriter("movie.txt", false);
        movieFile.close();
    }

    public void customerFileReset() throws IOException {
        FileWriter customerFile = new FileWriter("customer.txt", false);
        customerFile.close();
    }

    public ArrayList<Movie> readMovieFile() throws IOException{
        Scanner scan = new Scanner(new FileReader("movie.txt"));
        ArrayList<Movie> tempMovieHolder = new ArrayList<>();

        while(scan.hasNext()) {
            String[] movieInfo = scan.nextLine().split(",");
            String title = movieInfo[0];
            double price = Double.parseDouble(movieInfo[1]);
            tempMovieHolder.add(new Movie(title, price));
        }
        return tempMovieHolder;
    }

    public ArrayList<Customer> readCustomerFile() throws IOException{
        Scanner scan = new Scanner(new FileReader("customer.txt"));
        ArrayList<Customer> tempCustomerHolder = new ArrayList<>();

        while(scan.hasNext()) {
            String[] customerInfo = scan.nextLine().split(", ");
            String username = customerInfo[0];
            String password = customerInfo[1];
            int points = Integer.parseInt(customerInfo[2]);
            tempCustomerHolder.add(new Customer(username, password));
            tempCustomerHolder.get(tempCustomerHolder.size()-1).setPoints(points);
        }
        return tempCustomerHolder;
    }

}


