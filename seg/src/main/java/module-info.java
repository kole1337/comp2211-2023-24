module com.example.seg {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires java.logging;
    requires com.opencsv;

    requires java.desktop;
    requires org.jfree.jfreechart;

    opens com.application.login to javafx.fxml;
    exports com.application.login;
    exports com.application.dashboard;
    exports com.application.files;
    exports com.application.admin;
}