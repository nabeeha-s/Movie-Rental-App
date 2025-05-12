package MovieRent;

import java.io.IOException;
import java.util.ArrayList;

public class Renter{
    private static final Files files = new Files();
    protected static ArrayList<Movie> movies = new ArrayList<>();
    private static final ArrayList<Customer> customers = new ArrayList<>();

    public void restockArrays() throws IOException {
        ArrayList<Movie> tempMovies = files.readMovieFile();
        ArrayList<Customer> tempCustomers = files.readCustomerFile();
        movies.addAll(tempMovies);
        customers.addAll(tempCustomers);
    }

    public String getUsername(){
        return "owner";
    }
    public String getPassword(){
        return "owner";
    }

    public void addCustomer(Customer created){
        customers.add(created);
    }

    public void deleteCustomer(Customer selected){
        customers.remove(selected);
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Customer> getCustomers(){
        return (ArrayList<Customer>) customers.clone();
    }

}
