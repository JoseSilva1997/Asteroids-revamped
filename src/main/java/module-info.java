module org.example.asteroidsrevamped {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.asteroidsrevamped to javafx.fxml;
    exports org.example.asteroidsrevamped;
}