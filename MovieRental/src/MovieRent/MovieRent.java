package MovieRent;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class MovieRent extends Application {

    private final Renter owner = new Renter();
    private Customer currentCustomer;
    private static final Files files = new Files();

    Button loginButton = new Button("Login");
    Button logoutButton = new Button("Logout");
    Button moviesButton = new Button("Movie");
    Button customersButton = new Button("Customers");
    Button backButton = new Button("\uD83E\uDC60");
    Button buyButton = new Button("Buy");
    Button redeemButton = new Button("Redeem points & Buy");
    TextField userTextField = new TextField();
    PasswordField passTextField = new PasswordField();
    HBox hb = new HBox();

    TableView<Movie> moviesTable = new TableView<>();
    final TableView.TableViewFocusModel<Movie> defaultFocusModel = moviesTable.getFocusModel();
    ObservableList<Movie> movies = FXCollections.observableArrayList();

    public ObservableList<Movie> addMovies(){
        movies.addAll(Renter.movies);
        return movies;
    }

    TableView<Customer> customersTable = new TableView<>();
    ObservableList<Customer> customers = FXCollections.observableArrayList();

    public ObservableList<Customer> addCustomers(){
        customers.addAll(owner.getCustomers());
        return customers;
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("𝚁𝚎𝚗𝚝 𝙰 𝙼𝚘𝚟𝚒𝚎");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(loginScreen(false), 605, 550));
        primaryStage.show();
        System.out.println("Opened Renting Application");

        try{
            owner.restockArrays();
            System.out.println("Arrays restocked from files");
        }
        catch (IOException e){
            System.out.println("File Importing Error");
        }

        loginButton.setOnAction(e -> {
            boolean logged_in = false;

            if(userTextField.getText().equals(owner.getUsername()) && passTextField.getText().equals(owner.getUsername())) {
                primaryStage.setScene(new Scene(ownerStartScreen(), 605, 550));
                logged_in = true;
            }
            for(Customer c: owner.getCustomers()) {
                if (userTextField.getText().equals(c.getUsername()) && passTextField.getText().equals(c.getPassword())) {
                    currentCustomer = c;
                    primaryStage.setScene(new Scene(customerHomeScreen(0), 605, 550));
                    logged_in = true;
                }
            }
            if(!logged_in) {
                primaryStage.setScene(new Scene(loginScreen(true), 605, 550));
            }
        });

        logoutButton.setOnAction(e -> {
            primaryStage.setScene(new Scene(loginScreen(false), 605, 550));
            for(Movie b: Renter.movies){
                b.setSelect(new CheckBox());
            }
            userTextField.clear();
            passTextField.clear();
        });

        moviesButton.setOnAction(e -> primaryStage.setScene(new Scene(moviesTableScreen(), 605, 550)));

        customersButton.setOnAction(e -> primaryStage.setScene(new Scene(customerTableScreen(), 605, 550)));
        backButton.setOnAction(e -> primaryStage.setScene(new Scene(ownerStartScreen(), 605, 550)));

        redeemButton.setOnAction(e -> {
            boolean movieSelected = false;
            for(Movie b: Renter.movies) {
                if (b.getSelect().isSelected()) {
                    movieSelected = true;
                }
            }
            if(!movieSelected){
                primaryStage.setScene(new Scene(customerHomeScreen(1), 605, 550));
            } else if(currentCustomer.getPoints() == 0){
                primaryStage.setScene(new Scene(customerHomeScreen(2), 605, 550));
            } else if(currentCustomer.getPoints() != 0){
                primaryStage.setScene(new Scene(checkoutScreen(true), 605, 550));
            }
        });
        
        buyButton.setOnAction(e -> {
            boolean movieSelected = false;
            for(Movie b: Renter.movies) {
                if (b.getSelect().isSelected()) {
                    movieSelected = true;
                }
            }
            if(movieSelected){
                primaryStage.setScene(new Scene(checkoutScreen(false), 605, 550));
            } else primaryStage.setScene(new Scene(customerHomeScreen(1), 605, 550));
        });
        
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("Left the shop");
            try {
                files.movieFileReset();
                files.customerFileReset();
                System.out.println("Files reset");
                files.movieFileWrite(Renter.movies);
                files.customerFileWrite(owner.getCustomers());
                System.out.println("Files updated with current array data");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
        {
            buyButton.setStyle("-fx-background-color: #e6e68c;");
            redeemButton.setStyle("-fx-background-color: #e6e68c;");
            customersButton.setStyle("-fx-background-color: #cacca3;" + "-fx-font-size:25;" + "-fx-background-radius: 10;");
            moviesButton.setStyle("-fx-background-color: #cacca3;" + "-fx-font-size:25;" + "-fx-background-radius: 10;");
            logoutButton.setStyle("-fx-background-color: #e6e68c;");
            backButton.setStyle("-fx-background-color: #edecc0;" + "-fx-font-size:14;");
            loginButton.setStyle("-fx-background-color: #e6e68c;");

            customersTable.setStyle("-fx-control-inner-background: #cacca3;" +
                    "-fx-selection-bar: #e6e68c; -fx-selection-bar-non-focused: #e6e68c;" + "-fx-border-color: #cacca3;" +
                    "-fx-table-cell-border-color: #cacca3;" + "-fx-background-color: #cacca3;");

            moviesTable.setStyle("-fx-control-inner-background: #cacca3;" + "-fx-border-color: #cacca3;" +
                    "-fx-selection-bar: #e6e68c; -fx-selection-bar-non-focused: #e6e68c;" +
                    "-fx-table-cell-border-color: #cacca3;" + "-fx-background-color: #cacca3;" + "-fx-column-header-background: #cacca3;");
        }
    }

    public Group loginScreen(boolean loginError){
        Group lis = new Group();

        HBox header = new HBox();
        Label brand = new Label("📽 𝙼𝚘𝚟𝚒𝚎 𝚁𝚎𝚗𝚝 📽");
        brand.setFont(new Font("Arial", 35));
        header.getChildren().add(brand);
        brand.setStyle("-fx-font-weight: bold;");
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20, 0, 0, 0));


        VBox loginBox = new VBox();
        loginBox.setPadding(new Insets(30,65,45,65));
        loginBox.setStyle("-fx-background-color: #97a055;" + "-fx-background-radius: 10 10 10 10;");
        loginBox.setSpacing(6);
        Text user = new Text("Username");
        userTextField.setStyle("-fx-background-color: #f5f5dc;");
        passTextField.setStyle("-fx-background-color: #f5f5dc;");
        Text pass = new Text("Password");
        loginButton.setMinWidth(174);
        loginBox.getChildren().addAll(user, userTextField, pass, passTextField, loginButton);

        if(loginError){
            Text errorMsg = new Text("Incorrect username or password.");
            errorMsg.setFill(Color.RED);
            loginBox.getChildren().add(errorMsg);
        }

        VBox bg = new VBox();
        bg.getChildren().addAll(header, loginBox);
        bg.setStyle("-fx-background-color: #edecc0;");
        bg.setPadding(new Insets(80, 280, 200, 150));
        bg.setSpacing(80);

        lis.getChildren().addAll(bg);
        return lis;
    }
    
    public Group customerHomeScreen(int type){
        Group moviestore = new Group();
        moviesTable.getItems().clear();
        moviesTable.getColumns().clear();
        moviesTable.setFocusModel(null);

        Font font = new Font(14);
        Text welcomeMsg = new Text("Welcome, " + currentCustomer.getUsername() + ".");
        welcomeMsg.setFont(font);
        Text status1 = new Text(" Status: ");
        status1.setFont(font);
        Text status2 = new Text(currentCustomer.getStatus());
        status2.setFont(font);
        status2.setStyle("-fx-font-weight: bold;" + "-fx-font-size: 14px;" +
                "-fx-stroke: black;" + "-fx-stroke-width: 0.5px;");

        if(currentCustomer.getStatus().equals("PREMIUM")){
            status2.setFill(Color.AZURE);
        }else status2.setFill(Color.BROWN);

        Text points = new Text(" Points: " + currentCustomer.getPoints());
        points.setFont(font);

        //Movie name column
        TableColumn<Movie, String> nameColumn = new TableColumn<>("Title");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));  // Should match the property name

        //Movie price column
        TableColumn<Movie, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setStyle("-fx-alignment: CENTER;");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Checkbox column
        TableColumn<Movie, String> selectColumn = new TableColumn<>("Select");
        selectColumn.setMinWidth(100);
        selectColumn.setStyle("-fx-alignment: CENTER;");
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));

        moviesTable.setItems(addMovies());
        moviesTable.getColumns().addAll(nameColumn, priceColumn, selectColumn);

        HBox info = new HBox();
        info.getChildren().addAll(status1, status2, points);
        BorderPane header = new BorderPane();
        header.setLeft(welcomeMsg);
        header.setRight(info);

        HBox bottom = new HBox();
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        bottom.setSpacing(5);
        bottom.getChildren().addAll(buyButton, redeemButton, logoutButton);

        VBox vbox = new VBox();
        String errMsg = "";
        if(type == 1){
            errMsg = "Please select a movie before proceeding.";
        }
        else if(type == 2){
            errMsg =  "No points available to redeem.";
        }
        Text warning = new Text(errMsg);
        warning.setFill(Color.RED);
        vbox.setStyle("-fx-background-color: #edecc0;");
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(40, 200, 30, 100));
        vbox.getChildren().addAll(header, moviesTable, bottom, warning);

        moviestore.getChildren().addAll(vbox);

        return moviestore;
    }

    public Group checkoutScreen(boolean usedPoints){
        Group cos = new Group();
        double total, subtotal = 0, discount;
        int pointsEarned, i = 0, movieCount = 0;
        String[][] moviesBought = new String[25][2];

        for(Movie b: Renter.movies){
            if(b.getSelect().isSelected()){
                subtotal += b.getPrice();
                moviesBought[i][0] = b.getTitle();
                moviesBought[i][1] = String.valueOf(b.getPrice());
                i++;
            }
        }

        if(usedPoints){
            if((double)currentCustomer.getPoints()/100 >= subtotal){
                discount = subtotal;
                currentCustomer.setPoints(-(int)subtotal*100);
            }
            else{
                discount = ((double)currentCustomer.getPoints()/100);
                currentCustomer.setPoints(-currentCustomer.getPoints());
            }
        }else discount = 0;

        total = subtotal - discount;
        pointsEarned = (int)total*10;
        currentCustomer.setPoints(pointsEarned);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setSpacing(15);
        header.setPadding(new Insets(0,0,25,0));
        Label brandName = new Label("𝙼𝚘𝚟𝚒𝚎");
        brandName.setFont(new Font("Arial", 35));
        header.getChildren().add(brandName);

        VBox receipt = new VBox();
        receipt.setSpacing(7);
        Text receiptTxt = new Text("Receipt");
        receiptTxt.setFont(Font.font(null, FontWeight.BOLD, 12));
        Line thickLine = new Line(0, 150, 400, 150);
        thickLine.setStrokeWidth(3);
        receipt.getChildren().addAll(receiptTxt, thickLine);

        VBox receiptItems = new VBox();
        receiptItems.setStyle("-fx-background-color: #d6d68b;");
        receiptItems.setSpacing(7);
        for (i = 0; i<25; i++) {
            if(moviesBought[i][0] != null){
                Text movieTitle = new Text(moviesBought[i][0]);
                Text moviePrice = new Text(moviesBought[i][1]);
                BorderPane item = new BorderPane();
                item.setLeft(movieTitle);
                item.setRight(moviePrice);
                Line thinLine = new Line(0, 150, 400, 150);
                receiptItems.getChildren().addAll(item, thinLine);
                movieCount++;
            }
        }

        ScrollPane scrollReceipt = new ScrollPane(receiptItems);
        scrollReceipt.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollReceipt.setStyle("-fx-background-color:transparent;");
        scrollReceipt.setFitToWidth(true);
        if(movieCount<=4){
            scrollReceipt.setFitToHeight(true);
        } else scrollReceipt.setPrefHeight(130);


        Text subtotalTxt = new Text("Subtotal: $" + (Math.round(subtotal*100.0))/100.0);
        Text pointsDisc = new Text("Points Discount: $" + (Math.round(discount*100.0))/100.0);
        Text totalTxt = new Text("Total: $" + (Math.round(total*100.0))/100.0);
        totalTxt.setFont(new Font("Arial", 15));
        Line thickLine2 = new Line(0, 150, 400, 150);
        thickLine2.setStrokeWidth(3);
        receipt.getChildren().addAll(scrollReceipt, subtotalTxt, pointsDisc, totalTxt, thickLine2);

        VBox bottom = new VBox();
        bottom.setSpacing(40);
        bottom.setAlignment(Pos.CENTER);
        Text info = new Text("You've earned " + pointsEarned + " points " +
                "and your status is " + currentCustomer.getStatus() + "\n\t\t\t\ttHave a nice day!");
        bottom.getChildren().addAll(info, logoutButton);

        VBox screen = new VBox();
        screen.setStyle("-fx-background-color: #edecc0;");
        screen.setPadding(new Insets(60,105,500,100));
        screen.setAlignment(Pos.CENTER);
        screen.setSpacing(10);
        screen.getChildren().addAll(header, receipt, bottom);

        cos.getChildren().addAll(screen);
        Renter.movies.removeIf(b -> b.getSelect().isSelected());
        return cos;
    }//complete

    public VBox ownerStartScreen() {
        VBox osc = new VBox(15);
        osc.setStyle("-fx-background-color: #edecc0;");
        osc.setAlignment(Pos.CENTER);
        osc.setPadding(new Insets(80, 0, 30, 0));

        moviesButton.setMinWidth(150);
        customersButton.setMinWidth(150);
        logoutButton.setMinWidth(150);

        osc.getChildren().addAll(moviesButton, customersButton, logoutButton);

        return osc;
    }

    public Group moviesTableScreen() {
        Group bt = new Group();
        hb.getChildren().clear();
        moviesTable.getItems().clear();
        moviesTable.getColumns().clear();
        moviesTable.setFocusModel(defaultFocusModel);

        Label label = new Label("Movies");
        label.setFont(new Font("Arial", 20));

        //Movie name column
        TableColumn<Movie, String> nameColumn = new TableColumn<>("Title");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        //Movie price column
        TableColumn<Movie, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setStyle("-fx-alignment: CENTER;");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        moviesTable.setItems(addMovies());
        moviesTable.getColumns().addAll(nameColumn, priceColumn);

        final TextField addMovieTitle = new TextField();
        addMovieTitle.setPromptText("Title");
        addMovieTitle.setMaxWidth(nameColumn.getPrefWidth());
        final TextField addMoviePrice = new TextField();
        addMoviePrice.setMaxWidth(priceColumn.getPrefWidth());
        addMoviePrice.setPromptText("Price");
        addMovieTitle.setStyle("-fx-background-color: #f5f5dc;");
        addMoviePrice.setStyle("-fx-background-color: #f5f5dc;");

        VBox core = new VBox();
        final Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #e6e68c;");
        Label movieAddErr = new Label("Invalid Input");
        movieAddErr.setTextFill(Color.color(1,0,0));

        addButton.setOnAction(e -> {
            try {
                double price = Math.round((Double.parseDouble(addMoviePrice.getText()))*100);
                Renter.movies.add(new Movie(addMovieTitle.getText(), price/100));
                //makes new movie and adds it to arraylist
                moviesTable.getItems().clear(); //refresh page so new movies can be accessed
                moviesTable.setItems(addMovies());
                addMovieTitle.clear(); //clears text fields
                addMoviePrice.clear();
                core.getChildren().remove(movieAddErr); //removes a previous Invalid Input error if there was one
            }
            catch (Exception exception){
                if(!core.getChildren().contains(movieAddErr)){
                    core.getChildren().add(movieAddErr);
                }
            }
        });

        final Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #e6e68c;");
        deleteButton.setOnAction(e -> {
            Movie selectedItem = moviesTable.getSelectionModel().getSelectedItem(); //selects row highlighted
            moviesTable.getItems().remove(selectedItem); //removes from table view as soon as delete pressed
            Renter.movies.remove(selectedItem); //actually removes from the arraylist
        });


        hb.getChildren().addAll(addMovieTitle, addMoviePrice, addButton, deleteButton);
        hb.setSpacing(3);
        hb.setAlignment(Pos.CENTER);

        HBox back = new HBox();
        back.setPadding(new Insets(5));
        back.getChildren().addAll(backButton);

        core.setAlignment(Pos.CENTER);
        core.setSpacing(5);
        core.setPadding(new Insets(0, 0, 0, 150));
        core.getChildren().addAll(label, moviesTable, hb);

        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #edecc0;");
        vbox.setPadding(new Insets(0, 200, 60, 0));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(back, core);


        bt.getChildren().addAll(vbox);

        return bt;
    }//complete

    public Group customerTableScreen() {
        Group ct = new Group();
        hb.getChildren().clear();
        customersTable.getItems().clear();
        customersTable.getColumns().clear();

        Label label = new Label("Customers");
        label.setFont(new Font("Arial", 20));

        //Customer username column
        TableColumn<Customer, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setMinWidth(140);
        usernameCol.setStyle("-fx-alignment: CENTER;");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        //Customer password column
        TableColumn<Customer, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setMinWidth(140);
        passwordCol.setStyle("-fx-alignment: CENTER;");
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        //Customer points column
        TableColumn<Customer, Integer> pointsCol = new TableColumn<>("Points");
        pointsCol.setMinWidth(100);
        pointsCol.setStyle("-fx-alignment: CENTER;");
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        customersTable.setItems(addCustomers());
        customersTable.getColumns().addAll(usernameCol, passwordCol, pointsCol);

        final TextField addUsername = new TextField();
        addUsername.setPromptText("Username");
        addUsername.setMaxWidth(usernameCol.getPrefWidth());
        final TextField addPassword = new TextField();
        addPassword.setMaxWidth(passwordCol.getPrefWidth());
        addPassword.setPromptText("Password");
        addPassword.setStyle("-fx-background-color: #f5f5dc;");
        addUsername.setStyle("-fx-background-color: #f5f5dc;");

        VBox core = new VBox();
        Text customerAddErr = new Text("Customer already exists!");
        customerAddErr.setFill(Color.color(1,0,0));
        final Button addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #e6e68c;");
        addButton.setOnAction(e -> {
            boolean duplicate = false;

            for(Customer c: owner.getCustomers()){
                if((c.getUsername().equals(addUsername.getText()) && c.getPassword().equals(addPassword.getText())) ||
                        (addUsername.getText().equals(owner.getUsername()) && addPassword.getText().equals(owner.getPassword()))){
                    duplicate = true;
                    if(!core.getChildren().contains(customerAddErr)){
                        core.getChildren().add(customerAddErr);
                    }
                }
            }

            if(!(addUsername.getText().equals("") || addPassword.getText().equals("")) && !duplicate) {
                owner.addCustomer(new Customer(addUsername.getText(), addPassword.getText())); //for the actual arraylist
                customersTable.getItems().clear(); //this is to refresh the table with actual values instead of visual ones
                customersTable.setItems(addCustomers());
                core.getChildren().remove(customerAddErr); //remove any previous error text messages
                addPassword.clear(); //clear text fields
                addUsername.clear();
            }
        });

        final Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #e6e68c;");
        deleteButton.setOnAction(e -> {
            Customer selectedItem = customersTable.getSelectionModel().getSelectedItem();
            customersTable.getItems().remove(selectedItem); //remove from tableview
            //customers.remove(selectedItem); //and this removes from the observable one
            owner.deleteCustomer(selectedItem); //removes from the actual arraylist
        });

        hb.getChildren().addAll(addUsername, addPassword, addButton, deleteButton);
        hb.setAlignment(Pos.CENTER);
        hb.setSpacing(3);

        HBox back = new HBox();
        back.setPadding(new Insets(5));
        back.getChildren().addAll(backButton);

        core.setAlignment(Pos.CENTER);
        core.setSpacing(5);
        core.setPadding(new Insets(0,0,0,110));
        core.getChildren().addAll(label, customersTable, hb);

        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #edecc0;");
        vbox.setPadding(new Insets(0, 150, 60, 0));
        vbox.getChildren().addAll(back, core);
        vbox.setAlignment(Pos.CENTER);

        ct.getChildren().addAll(vbox);
        return ct;
    }//complete

    public static void main(String[] args) {
        launch(args);
    }
}