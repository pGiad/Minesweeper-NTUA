module com.example.minesweeperntua {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;

    opens com.example.minesweeperntua to javafx.fxml;
    exports com.example.minesweeperntua;
}