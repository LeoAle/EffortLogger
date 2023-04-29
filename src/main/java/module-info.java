module com.example.effortlogger {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.effortlogger to javafx.fxml;
    exports com.example.effortlogger;
}