module com.schwada.liege {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.desktop;

    opens com.schwada.liege to javafx.fxml;
    opens com.schwada.liege.controller to javafx.fxml;
    exports com.schwada.liege;
}
