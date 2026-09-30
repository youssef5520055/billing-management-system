package com.billing.ui;

import com.billing.models.*;
import com.billing.services.*;



import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.File;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.application.Platform;

import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.File;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.application.Platform;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.scene.image.*;
import javafx.scene.effect.*;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.Node;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleObjectProperty;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.ButtonBar;

public class Main_APP extends Application {
    private Stage primaryStage;
    private Store store;
    private Admin admin;
    private Customer currentCustomer;
    
    // Styling constants
    private static final String PRIMARY_COLOR = "#2C3E50";
    private static final String SECONDARY_COLOR = "#3498DB";
    private static final String ACCENT_COLOR = "#E74C3C";
    private static final String SUCCESS_COLOR = "#2ECC71";
    
    private static final String MAIN_GRADIENT = String.format(
        "-fx-background-color: linear-gradient(to bottom right, %s, %s);",
        PRIMARY_COLOR, SECONDARY_COLOR
    );
    
    private static final String BUTTON_STYLE = """
        -fx-background-color: %s;
        -fx-text-fill: white;
        -fx-font-size: 14px;
        -fx-background-radius: 5;
        -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);
    """;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.admin = new Admin("youssef", "1234", 10);
        this.store = new Store(admin);
        
        setupPrimaryStage();
        showWelcomeScreen();
    }

    private void setupPrimaryStage() {
        primaryStage.setTitle("DR/YASMIN STORE");
        try {
            Image icon = new Image(getClass().getResourceAsStream("/images/store_icon.png"));
            if (icon != null) {
                primaryStage.getIcons().add(icon);
            }
        } catch (Exception e) {
            System.out.println("Store icon not found - using default icon");
        }
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(700);
    }

    private void applyBackgroundImage(Region container) {
        try {
            Image backgroundImage = new Image(getClass().getResourceAsStream("/images/background.jpg"));
            BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
            );
            container.setBackground(new Background(background));
        } catch (Exception e) {
            System.out.println("Background image not found - using default gradient");
            container.getStyleClass().add("root");
        }
    }

    private void showWelcomeScreen() {
        StackPane mainContainer = new StackPane();
        applyBackgroundImage(mainContainer);

        VBox card = createCard();
        
        // Create logo image view with fallback
        ImageView logoView = new ImageView();
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/store_logo.png"));
            if (logo != null) {
                logoView.setImage(logo);
                logoView.setFitHeight(150);
                logoView.setFitWidth(150);
                logoView.setEffect(new DropShadow(20, Color.BLACK));
            }
        } catch (Exception e) {
            System.out.println("Store logo not found - skipping logo display");
        }

        Text storeName = createStyledText("DR/YASMIN STORE", 36);
        Text welcomeText = createStyledText("Welcome to our Mobile Store", 18);
        
        VBox buttonsBox = new VBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        
        Button adminButton = createAnimatedButton("Admin Login", SUCCESS_COLOR);
        Button customerButton = createAnimatedButton("Customer Login", SECONDARY_COLOR);
        Button exitButton = createAnimatedButton("Exit", ACCENT_COLOR);
        
        buttonsBox.getChildren().addAll(adminButton, customerButton, exitButton);
        
        // Only add logo if it was loaded successfully
        if (logoView.getImage() != null) {
            card.getChildren().add(logoView);
        }
        
        card.getChildren().addAll(
            storeName,
            welcomeText,
            new Separator(),
            buttonsBox
        );

        mainContainer.getChildren().add(card);

        adminButton.setOnAction(e -> showLoginScreen("admin"));
        customerButton.setOnAction(e -> showCustomerLoginOptions());
        exitButton.setOnAction(e -> fadeOut(mainContainer, () -> primaryStage.close()));

        Scene scene = createScene(mainContainer, 900, 700);
        
        // Try to load CSS, but continue if not found
        try {
            String css = getClass().getResource("/styles/main.css").toExternalForm();
            if (css != null) {
                scene.getStylesheets().add(css);
            }
        } catch (Exception e) {
            System.out.println("CSS file not found - using default styles");
        }
        
        primaryStage.setScene(scene);
        fadeIn(mainContainer);
        
        primaryStage.show();
        PauseTransition pt = new PauseTransition(Duration.seconds(2));
        pt.setOnFinished(ev -> {
            try {
                WritableImage snapshot = scene.snapshot(null);
                File file = new File("docs/screenshot.png");
                file.getParentFile().mkdirs();
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
                System.out.println("AUTOMATIC SCREENSHOT TAKEN AND SAVED TO docs/screenshot.png");
                Platform.exit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pt.play();

    }

    private void showLoginScreen(String userType) {
        VBox loginContainer = new VBox(20);
        applyBackgroundImage(loginContainer);
        loginContainer.setAlignment(Pos.CENTER);

        VBox card = createCard();
        
        Text title = createStyledText(userType.equals("admin") ? "Admin Login" : "Customer Login", 24);
        
        TextField usernameField = createStyledTextField("Username/Phone");
        PasswordField passwordField = createStyledPasswordField("Password");
        
        Button loginButton = createAnimatedButton("Login", SUCCESS_COLOR);
        Button backButton = createAnimatedButton("Back", ACCENT_COLOR);
        
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.getChildren().addAll(
            title,
            usernameField,
            passwordField,
            loginButton,
            backButton
        );

        card.getChildren().add(formBox);
        loginContainer.getChildren().add(card);

        loginButton.setOnAction(e -> {
            if (userType.equals("admin")) {
                if (Admin.login(usernameField.getText(), passwordField.getText())) {
                    showAdminDashboard();
                } else {
                    showAlert("Login Failed", "Invalid admin credentials", Alert.AlertType.ERROR);
                }
            } else {
                Customer customer = Customer.existingUserLogin(
                    usernameField.getText(),
                    passwordField.getText(),
                    store
                );
                if (customer != null) {
                    currentCustomer = customer;
                    showCustomerDashboard();
                } else {
                    showAlert("Login Failed", "Invalid credentials", Alert.AlertType.ERROR);
                }
            }
        });

        backButton.setOnAction(e -> showWelcomeScreen());

        Scene scene = createScene(loginContainer, 900, 700);
        fadeOut(primaryStage.getScene().getRoot(), () -> {
            primaryStage.setScene(scene);
            fadeIn(loginContainer);
        });
    }

    private void showCustomerLoginOptions() {
        VBox container = new VBox(20);
        applyBackgroundImage(container);
        container.setAlignment(Pos.CENTER);

        VBox card = createCard();
        
        Text title = createStyledText("Customer Access", 24);
        
        Button loginButton = createAnimatedButton("Existing Customer", SECONDARY_COLOR);
        Button registerButton = createAnimatedButton("New Customer", SUCCESS_COLOR);
        Button backButton = createAnimatedButton("Back", ACCENT_COLOR);

        VBox buttonsBox = new VBox(15);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(
            title,
            loginButton,
            registerButton,
            backButton
        );

        card.getChildren().add(buttonsBox);
        container.getChildren().add(card);

        loginButton.setOnAction(e -> showLoginScreen("customer"));
        registerButton.setOnAction(e -> showCustomerRegistration());
        backButton.setOnAction(e -> showWelcomeScreen());

        Scene scene = createScene(container, 900, 700);
        fadeOut(primaryStage.getScene().getRoot(), () -> {
            primaryStage.setScene(scene);
            fadeIn(container);
        });
    }

    private void showCustomerRegistration() {
        VBox container = new VBox(20);
        applyBackgroundImage(container);
        container.setAlignment(Pos.CENTER);

        VBox card = createCard();
        
        Text title = createStyledText("New Customer Registration", 24);
        
        TextField nameField = createStyledTextField("Full Name");
        TextField phoneField = createStyledTextField("Phone Number");
        TextField addressField = createStyledTextField("Address");
        PasswordField passwordField = createStyledPasswordField("Password");
        PasswordField confirmPasswordField = createStyledPasswordField("Confirm Password");
        
        Button registerButton = createAnimatedButton("Register", SUCCESS_COLOR);
        Button backButton = createAnimatedButton("Back", ACCENT_COLOR);

        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.getChildren().addAll(
            title,
            nameField,
            phoneField,
            addressField,
            passwordField,
            confirmPasswordField,
            registerButton,
            backButton
        );

        card.getChildren().add(formBox);
        container.getChildren().add(card);

        registerButton.setOnAction(e -> {
            try {
                if (!isValidPhoneNumber(phoneField.getText())) {
                    showAlert("Registration Error", "Phone number must be exactly 11 digits", Alert.AlertType.ERROR);
                    return;
                }
                
                if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                    showAlert("Registration Error", "Passwords do not match", Alert.AlertType.ERROR);
                    return;
                }
                
                Customer newCustomer = Customer.newUserRegistration(
                    nameField.getText(),
                    phoneField.getText(),
                    addressField.getText(),
                    passwordField.getText(),
                    store
                );
                
                if (newCustomer != null) {
                    currentCustomer = newCustomer;
                    saveAllData();
                    showAlert("Success", "Registration successful!", Alert.AlertType.INFORMATION);
                    showCustomerDashboard();
                }
            } catch (IllegalArgumentException ex) {
                showAlert("Registration Error", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        backButton.setOnAction(e -> showCustomerLoginOptions());

        Scene scene = createScene(container, 900, 700);
        fadeOut(primaryStage.getScene().getRoot(), () -> {
            primaryStage.setScene(scene);
            fadeIn(container);
        });
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && 
               phoneNumber.length() == 11 && 
               phoneNumber.matches("\\d+"); // Ensures all characters are digits
    }

    private void showCustomerDashboard() {
        BorderPane dashboard = new BorderPane();
        applyBackgroundImage(dashboard);

        // Top bar
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(15));
        topBar.getStyleClass().add("top-bar");

        Label welcomeLabel = new Label("Welcome, " + currentCustomer.getName());
        welcomeLabel.getStyleClass().add("subtitle-label");
        Button logoutButton = createAnimatedButton("Logout", ACCENT_COLOR);
        
        topBar.getChildren().addAll(welcomeLabel, logoutButton);
        dashboard.setTop(topBar);

        // Side navigation
        VBox sideNav = createSideNav();
        dashboard.setLeft(sideNav);

        // Main content area
        StackPane contentArea = new StackPane();
        contentArea.getStyleClass().add("card-elevated");
        contentArea.setPadding(new Insets(20));
        dashboard.setCenter(contentArea);

        // Initial content
        showPhoneCatalog(contentArea);

        logoutButton.setOnAction(e -> showWelcomeScreen());

        Scene scene = createScene(dashboard, 900, 700);
        fadeOut(primaryStage.getScene().getRoot(), () -> {
            primaryStage.setScene(scene);
            fadeIn(dashboard);
        });
    }

    private VBox createSideNav() {
        VBox sideNav = new VBox(10);
        sideNav.setPrefWidth(200);
        sideNav.setPadding(new Insets(20));
        sideNav.getStyleClass().add("sidebar");

        Button catalogButton = createNavButton("Browse Phones");
        Button ordersButton = createNavButton("My Orders");
        Button billsButton = createNavButton("My Bills");
        Button profileButton = createNavButton("Profile");

        catalogButton.setOnAction(e -> showPhoneCatalog((StackPane)((BorderPane)sideNav.getParent()).getCenter()));
        ordersButton.setOnAction(e -> showCustomerOrders((StackPane)((BorderPane)sideNav.getParent()).getCenter()));
        billsButton.setOnAction(e -> showCustomerBills((StackPane)((BorderPane)sideNav.getParent()).getCenter()));
        profileButton.setOnAction(e -> showCustomerProfile((StackPane)((BorderPane)sideNav.getParent()).getCenter()));

        sideNav.getChildren().addAll(catalogButton, ordersButton, billsButton, profileButton);
        return sideNav;
    }

    private void showPhoneCatalog(StackPane contentArea) {
        VBox catalogContainer = new VBox(20);
        catalogContainer.setAlignment(Pos.CENTER);

        Text title = createStyledText("Available Phones", 24);

        TableView<Phone> phoneTable = new TableView<>();
        phoneTable.getStyleClass().add("table-view");

        TableColumn<Phone, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));

        TableColumn<Phone, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));

        TableColumn<Phone, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Phone, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        phoneTable.getColumns().addAll(brandCol, modelCol, priceCol, stockCol);
        phoneTable.setItems(FXCollections.observableArrayList(store.getInventory()));

        Button orderButton = createAnimatedButton("Place Order", SUCCESS_COLOR);
        orderButton.setDisable(true);

        phoneTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> orderButton.setDisable(newSelection == null)
        );

        orderButton.setOnAction(e -> {
            Phone selectedPhone = phoneTable.getSelectionModel().getSelectedItem();
            if (selectedPhone != null) {
                showOrderConfirmation(selectedPhone);
            }
        });

        catalogContainer.getChildren().addAll(title, phoneTable, orderButton);
        contentArea.getChildren().setAll(catalogContainer);
    }

    private void showOrderConfirmation(Phone phone) {
        VBox container = new VBox(20);
        applyBackgroundImage(container);
        container.setAlignment(Pos.CENTER);

        VBox card = createCard();
        
        Text title = createStyledText("Confirm Order", 24);
        Text phoneDetails = createStyledText(
            String.format("%s %s - %s", phone.getBrand(), phone.getModel(), 
            String.format("$%.2f", phone.getPrice())), 18
        );

        ComboBox<String> paymentMethod = new ComboBox<>();
        paymentMethod.getItems().addAll("Cash", "Credit Card");
        paymentMethod.setValue("Cash");
        paymentMethod.getStyleClass().add("combo-box");

        Button confirmButton = createAnimatedButton("Confirm Order", SUCCESS_COLOR);
        Button cancelButton = createAnimatedButton("Cancel", ACCENT_COLOR);

        confirmButton.setOnAction(e -> {
            Bill bill = new Bill(
                currentCustomer.getName(),
                currentCustomer.getPhoneNumber(),
                currentCustomer.getAddress(),
                phone.getPrice(),
                paymentMethod.getValue()
            );

            if (paymentMethod.getValue().equals("Credit Card")) {
                showCreditCardDialog(bill);
                if (bill.isPaid()) {
                    store.placeOrder(currentCustomer, phone);
                    store.addBill(bill);
                    saveAllData();
                    showCustomerDashboard();
                }
            } else {
                // Handle cash payment
                bill.payBill(); // Mark as paid for cash payment
                store.placeOrder(currentCustomer, phone);
                store.addBill(bill);
                saveAllData();
                showAlert("Success", "Order placed successfully!", Alert.AlertType.INFORMATION);
                showCustomerDashboard();
            }
        });

        cancelButton.setOnAction(e -> showCustomerDashboard());

        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.getChildren().addAll(
            title,
            phoneDetails,
            paymentMethod,
            confirmButton,
            cancelButton
        );

        card.getChildren().add(formBox);
        container.getChildren().add(card);

        Scene scene = createScene(container, 900, 700);
        fadeOut(primaryStage.getScene().getRoot(), () -> {
            primaryStage.setScene(scene);
            fadeIn(container);
        });
    }

    private void showCreditCardDialog(Bill bill) {
        Dialog<Credit> dialog = new Dialog<>();
        dialog.setTitle("Credit Card Payment");
        dialog.setHeaderText("Enter Credit Card Details");

        // Create the custom dialog layout
        VBox content = new VBox(10);
        content.setPadding(new Insets(20, 150, 10, 10));

        TextField cardNumberField = createStyledTextField("Card Number (16 digits)");
        TextField cardHolderField = createStyledTextField("Card Holder Name");
        
        // Month ComboBox
        ComboBox<String> monthComboBox = new ComboBox<>();
        monthComboBox.getItems().addAll(
            "01", "02", "03", "04", "05", "06", 
            "07", "08", "09", "10", "11", "12"
        );
        monthComboBox.setPromptText("Month");
        monthComboBox.getStyleClass().add("combo-box");
        
        // Year ComboBox
        ComboBox<String> yearComboBox = new ComboBox<>();
        yearComboBox.getItems().addAll(
            "2024", "2025", "2026", "2027", "2028", "2029", 
            "2030", "2031", "2032", "2033", "2034"
        );
        yearComboBox.setPromptText("Year");
        yearComboBox.getStyleClass().add("combo-box");
        
        // Expiry date HBox
        HBox expiryBox = new HBox(10);
        expiryBox.getChildren().addAll(new Label("Expiry Date:"), monthComboBox, yearComboBox);
        
        PasswordField cvvField = createStyledPasswordField("CVV (3 digits)");
        cvvField.setPrefWidth(100);

        content.getChildren().addAll(
            cardNumberField,
            cardHolderField,
            expiryBox,
            cvvField
        );

        dialog.getDialogPane().setContent(content);

        ButtonType payButtonType = new ButtonType("Pay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(payButtonType, ButtonType.CANCEL);

        Node payButton = dialog.getDialogPane().lookupButton(payButtonType);
        payButton.setDisable(true);

        // Enable pay button only if all fields are valid
        ChangeListener<String> validationListener = (observable, oldValue, newValue) -> {
            boolean isValid = 
                cardNumberField.getText().matches("\\d{16}") &&
                !cardHolderField.getText().trim().isEmpty() &&
                monthComboBox.getValue() != null &&
                yearComboBox.getValue() != null &&
                cvvField.getText().matches("\\d{3}");
            
            payButton.setDisable(!isValid);
        };

        cardNumberField.textProperty().addListener(validationListener);
        cardHolderField.textProperty().addListener(validationListener);
        cvvField.textProperty().addListener(validationListener);
        monthComboBox.valueProperty().addListener((obs, oldVal, newVal) -> 
            validationListener.changed(null, null, null));
        yearComboBox.valueProperty().addListener((obs, oldVal, newVal) -> 
            validationListener.changed(null, null, null));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == payButtonType) {
                Credit creditCard = new Credit();
                try {
                    creditCard.setCardNumber(cardNumberField.getText());
                    creditCard.setCardHolderName(cardHolderField.getText());
                    creditCard.setExpiryMonth(monthComboBox.getValue());
                    creditCard.setExpiryYear(yearComboBox.getValue());
                    creditCard.setCvv(cvvField.getText());
                    
                    if (creditCard.validateCreditCard()) {
                        return creditCard;
                    } else {
                        showAlert("Error", "Invalid credit card details", Alert.AlertType.ERROR);
                    }
                } catch (IllegalArgumentException e) {
                    showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
                }
                return null;
            }
            return null;
        });

        Optional<Credit> result = dialog.showAndWait();
        if (result.isPresent()) {
            bill.setPaymentMethod("Credit Card");
            bill.payBill();
            saveAllData();
            showAlert("Success", "Payment processed successfully!", Alert.AlertType.INFORMATION);
        }
    }

    private void showAdminDashboard() {
        BorderPane dashboard = new BorderPane();
        

        // Top bar
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(15));
        topBar.getStyleClass().add("top-bar");

        Label welcomeLabel = new Label("Admin Dashboard");
        welcomeLabel.getStyleClass().add("subtitle-label");
        Button logoutButton = createAnimatedButton("Logout", ACCENT_COLOR);
        
        topBar.getChildren().addAll(welcomeLabel, logoutButton);
        dashboard.setTop(topBar);

        // Side navigation
        VBox sideNav = createAdminSideNav();
        dashboard.setLeft(sideNav);

        // Main content area
        StackPane contentArea = new StackPane();
        contentArea.getStyleClass().add("card-elevated");
        contentArea.setPadding(new Insets(20));
        dashboard.setCenter(contentArea);

        // Initial content
        showInventoryManagement(contentArea);

        logoutButton.setOnAction(e -> showWelcomeScreen());

        Scene scene = createScene(dashboard, 900, 700);
        fadeOut(primaryStage.getScene().getRoot(), () -> {
            primaryStage.setScene(scene);
            fadeIn(dashboard);
        });
    }

    private VBox createAdminSideNav() {
        VBox sideNav = new VBox(10);
        sideNav.setPrefWidth(200);
        sideNav.setPadding(new Insets(20));
        sideNav.getStyleClass().add("sidebar");

        Button inventoryButton = createNavButton("Inventory Management");
        Button customersButton = createNavButton("Customer Reports");
        Button billsButton = createNavButton("View Bills");
        Button addPhoneButton = createNavButton("Add New Phone");

        inventoryButton.setOnAction(e -> showInventoryManagement((StackPane)((BorderPane)sideNav.getParent()).getCenter()));
        customersButton.setOnAction(e -> showCustomerReports((StackPane)((BorderPane)sideNav.getParent()).getCenter()));
        billsButton.setOnAction(e -> showBillsManagement((StackPane)((BorderPane)sideNav.getParent()).getCenter()));
        addPhoneButton.setOnAction(e -> showAddPhoneForm((StackPane)((BorderPane)sideNav.getParent()).getCenter()));

        sideNav.getChildren().addAll(inventoryButton, customersButton, billsButton, addPhoneButton);
        return sideNav;
    }

    private void showInventoryManagement(StackPane contentArea) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);

        Text title = createStyledText("Inventory Management", 24);

        TableView<Phone> phoneTable = new TableView<>();
        phoneTable.getStyleClass().add("table-view");

        TableColumn<Phone, String> brandCol = new TableColumn<>("Brand");
        TableColumn<Phone, String> modelCol = new TableColumn<>("Model");
        TableColumn<Phone, Double> priceCol = new TableColumn<>("Price");
        TableColumn<Phone, Integer> stockCol = new TableColumn<>("Stock");

        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        phoneTable.getColumns().addAll(brandCol, modelCol, priceCol, stockCol);
        phoneTable.setItems(FXCollections.observableArrayList(store.getInventory()));

        Button updateStockButton = createAnimatedButton("Update Stock", SECONDARY_COLOR);
        updateStockButton.setDisable(true);

        phoneTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> updateStockButton.setDisable(newSelection == null)
        );

        updateStockButton.setOnAction(e -> {
            Phone selectedPhone = phoneTable.getSelectionModel().getSelectedItem();
            if (selectedPhone != null) {
                showUpdateStockDialog(selectedPhone, phoneTable);
            }
        });

        container.getChildren().addAll(title, phoneTable, updateStockButton);
        contentArea.getChildren().setAll(container);
    }

    private void showUpdateStockDialog(Phone phone, TableView<Phone> tableView) {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Update Stock");
        dialog.setHeaderText("Update stock for " + phone.getBrand() + " " + phone.getModel());

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        TextField stockField = createStyledTextField("New Stock Amount");
        stockField.setText(String.valueOf(phone.getStock()));

        dialog.getDialogPane().setContent(stockField);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                try {
                    return Integer.parseInt(stockField.getText());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newStock -> {
            if (newStock != null) {
                phone.setStock(newStock);
                saveAllData();
                tableView.refresh();
                showAlert("Success", "Stock updated successfully", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void showCustomerReports(StackPane contentArea) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);

        Text title = createStyledText("Customer Reports", 24);

        TextArea reportsArea = new TextArea();
        reportsArea.setPrefWidth(600);
        reportsArea.setPrefHeight(400);
        reportsArea.setEditable(false);
        reportsArea.setWrapText(true);
        reportsArea.setText(admin.viewCustomerReports(store.getCustomers()));
        reportsArea.getStyleClass().add("text-area");

        container.getChildren().addAll(title, reportsArea);
        contentArea.getChildren().setAll(container);
    }

    private void showBillsManagement(StackPane contentArea) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);

        Text title = createStyledText("Bills Management", 24);

        TableView<Bill> billsTable = new TableView<>();
        billsTable.getStyleClass().add("table-view");

        TableColumn<Bill, String> customerCol = new TableColumn<>("Customer");
        TableColumn<Bill, String> phoneCol = new TableColumn<>("Phone");
        TableColumn<Bill, Double> amountCol = new TableColumn<>("Amount");
        TableColumn<Bill, String> statusCol = new TableColumn<>("Status");

        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        // Modified status column cell value factory with null check
        statusCol.setCellValueFactory(data -> {
            Bill bill = data.getValue();
            if (bill != null) {
                return new SimpleStringProperty(bill.isPaid() ? "Paid" : "Unpaid");
            }
            return new SimpleStringProperty("");
        });

        billsTable.getColumns().addAll(customerCol, phoneCol, amountCol, statusCol);
        billsTable.setItems(FXCollections.observableArrayList(store.getBills()));

        container.getChildren().addAll(title, billsTable);
        contentArea.getChildren().setAll(container);
    }

    private void showAddPhoneForm(StackPane contentArea) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);

        VBox card = createCard();
        
        Text title = createStyledText("Add New Phone", 24);
        
        TextField brandField = createStyledTextField("Brand");
        TextField modelField = createStyledTextField("Model");
        TextField priceField = createStyledTextField("Price");
        TextField stockField = createStyledTextField("Initial Stock");
        
        Button addButton = createAnimatedButton("Add Phone", SUCCESS_COLOR);

        addButton.setOnAction(e -> {
            try {
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                
                Phone newPhone = new Phone(
                    brandField.getText(),
                    modelField.getText(),
                    price,
                    stock
                );
                
                store.addPhoneToInventory(newPhone);
                showAlert("Success", "Phone added successfully", Alert.AlertType.INFORMATION);
                showInventoryManagement(contentArea);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid price or stock value", Alert.AlertType.ERROR);
            }
        });

        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.getChildren().addAll(
            title,
            brandField,
            modelField,
            priceField,
            stockField,
            addButton
        );

        card.getChildren().add(formBox);
        container.getChildren().add(card);
        contentArea.getChildren().setAll(container);
    }

    private void showCustomerBills(StackPane contentArea) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);

        Text title = createStyledText("My Bills", 24);

        TableView<Bill> billsTable = new TableView<>();
        billsTable.getStyleClass().add("table-view");

        TableColumn<Bill, String> dateCol = new TableColumn<>("Date");
        TableColumn<Bill, String> phoneCol = new TableColumn<>("Phone");
        TableColumn<Bill, Double> amountCol = new TableColumn<>("Amount");
        TableColumn<Bill, String> statusCol = new TableColumn<>("Status");

        dateCol.setCellValueFactory(new PropertyValueFactory<>("customerName")); // You might want to add date to Bill class
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        statusCol.setCellValueFactory(data -> {
            Bill bill = data.getValue();
            if (bill != null) {
                return new SimpleStringProperty(bill.isPaid() ? "Paid" : "Unpaid");
            }
            return new SimpleStringProperty("");
        });

        billsTable.getColumns().addAll(dateCol, phoneCol, amountCol, statusCol);
        
        // Filter bills for current customer
        List<Bill> customerBills = store.getBills().stream()
            .filter(bill -> bill.getCustomerName().equals(currentCustomer.getName()))
            .collect(Collectors.toList());
        
        billsTable.setItems(FXCollections.observableArrayList(customerBills));

        container.getChildren().addAll(title, billsTable);
        contentArea.getChildren().setAll(container);
    }

    private void showCustomerProfile(StackPane contentArea) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);

        VBox card = createCard();
        
        Text title = createStyledText("My Profile", 24);
        
        // Display current info
        Text nameLabel = createStyledText("Name: " + currentCustomer.getName(), 16);
        Text phoneLabel = createStyledText("Phone: " + currentCustomer.getPhoneNumber(), 16);
        Text addressLabel = createStyledText("Address: " + currentCustomer.getAddress(), 16);
        
        // Edit fields
        TextField newAddressField = createStyledTextField("New Address");
        PasswordField newPasswordField = createStyledPasswordField("New Password");
        PasswordField confirmPasswordField = createStyledPasswordField("Confirm New Password");
        
        Button updateButton = createAnimatedButton("Update Profile", SUCCESS_COLOR);
        
        updateButton.setOnAction(e -> {
            boolean hasChanges = false;
            
            if (!newAddressField.getText().isEmpty()) {
                currentCustomer.setAddress(newAddressField.getText());
                hasChanges = true;
            }
            
            if (!newPasswordField.getText().isEmpty()) {
                if (newPasswordField.getText().equals(confirmPasswordField.getText())) {
                    currentCustomer.setPassword(newPasswordField.getText());
                    hasChanges = true;
                } else {
                    showAlert("Error", "Passwords do not match", Alert.AlertType.ERROR);
                    return;
                }
            }
            
            if (hasChanges) {
                saveAllData();
                showAlert("Success", "Profile updated successfully", Alert.AlertType.INFORMATION);
                showCustomerProfile(contentArea);
            }
        });

        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.getChildren().addAll(
            title,
            nameLabel,
            phoneLabel,
            addressLabel,
            new Separator(),
            newAddressField,
            newPasswordField,
            confirmPasswordField,
            updateButton
        );

        card.getChildren().add(formBox);
        container.getChildren().add(card);
        contentArea.getChildren().setAll(container);
    }

    private void showCustomerOrders(StackPane contentArea) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);

        Text title = createStyledText("My Orders", 24);

        TableView<Order> ordersTable = new TableView<>();
        ordersTable.getStyleClass().add("table-view");

        // Phone column
        TableColumn<Order, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getPhone().getBrand() + " " + 
                                   cellData.getValue().getPhone().getModel()));

        // Date column with proper type handling
        TableColumn<Order, String> dateCol = new TableColumn<>("Order Date");
        dateCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getOrderDate().toString()));

        // Status column
        TableColumn<Order, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().isCompleted() ? "Completed" : "Pending"));

        ordersTable.getColumns().addAll(phoneCol, dateCol, statusCol);
        
        // Filter orders for current customer
        List<Order> customerOrders = store.getOrders().stream()
            .filter(order -> order.getCustomerName().equals(currentCustomer.getName()))
            .collect(Collectors.toList());
        
        ordersTable.setItems(FXCollections.observableArrayList(customerOrders));

        // Add a refresh button
        Button refreshButton = createAnimatedButton("Refresh Orders", SECONDARY_COLOR);
        refreshButton.setOnAction(e -> {
            List<Order> updatedOrders = store.getOrders().stream()
                .filter(order -> order.getCustomerName().equals(currentCustomer.getName()))
                .collect(Collectors.toList());
            ordersTable.setItems(FXCollections.observableArrayList(updatedOrders));
        });

        container.getChildren().addAll(title, ordersTable, refreshButton);
        contentArea.getChildren().setAll(container);
    }

    // Utility methods
    private VBox createCard() {
        VBox card = new VBox(20);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(40));
        card.setMaxWidth(400);
        card.setMaxHeight(VBox.USE_PREF_SIZE);
        card.setAlignment(Pos.CENTER);
        return card;
    }

    private Text createStyledText(String content, int fontSize) {
        Text text = new Text(content);
        text.setFont(Font.font("Arial", FontWeight.BOLD, fontSize));
        text.setFill(Color.WHITE);
        text.setEffect(new DropShadow(5, Color.BLACK));
        return text;
    }

    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.getStyleClass().add("text-field");
        field.setPrefWidth(300);
        return field;
    }

    private PasswordField createStyledPasswordField(String prompt) {
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.getStyleClass().add("text-field");
        field.setPrefWidth(300);
        return field;
    }

    private Button createAnimatedButton(String text, String color) {
        Button button = new Button(text);
        button.getStyleClass().add("button");
        if (color.equals(ACCENT_COLOR)) {
            button.getStyleClass().add("danger-button");
        } else {
            button.getStyleClass().add("primary-button");
        }
        button.setPrefWidth(200);
        button.setPrefHeight(40);
        button.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        button.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
            st.setToX(1);
            st.setToY(1);
            st.play();
        });

        return button;
    }

    private Button createNavButton(String text) {
        Button button = createAnimatedButton(text, SECONDARY_COLOR);
        button.getStyleClass().remove("primary-button");
        button.getStyleClass().add("sidebar-button");
        button.setMaxWidth(Double.MAX_VALUE);
        return button;
    }

    private void fadeIn(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();
    }

    private void fadeOut(Node node, Runnable onFinished) {
        FadeTransition ft = new FadeTransition(Duration.millis(500), node);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(e -> onFinished.run());
        ft.play();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void saveAllData() {
        Files.savePhoneData(store.getInventory());
        Files.saveCustomerData(store.getCustomers());
        Files.saveBillsData(store.getBills());
    }

    private void saveCustomerData() {
        try {
            if (currentCustomer != null) {
                store.updateCustomer(currentCustomer);
                showAlert("Success", "Customer data saved successfully", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            showAlert("Error", "Error saving customer data", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // Update new customer registration function
    private void registerNewCustomer(String name, String phone, String address, String password) {
        try {
            Customer newCustomer = new Customer(name, phone, address, password);
            store.addCustomer(newCustomer);
            currentCustomer = newCustomer;
            showAlert("Success", "Customer registered successfully", Alert.AlertType.INFORMATION);
            showCustomerDashboard();
        } catch (Exception e) {
            showAlert("Error", "Error registering customer", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Scene createScene(javafx.scene.Parent root, double width, double height) {
        Scene scene = new Scene(root, width, height);
        try {
            scene.getStylesheets().add(getClass().getResource("/css/css/application.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Could not load application.css");
        }
        return scene;
    }
}
