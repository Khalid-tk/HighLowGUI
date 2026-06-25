module org.example.highlowgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.highlowgui to javafx.fxml;
    exports org.example.highlowgui;
}