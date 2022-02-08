package com.example.etlap_balazskrisztian;

import javafx.event.ActionEvent;
        import javafx.scene.control.Button;
        import javafx.scene.control.TextField;
        import javafx.stage.Stage;
        import java.sql.SQLException;
        import java.sql.SQLIntegrityConstraintViolationException;

public class KategoriaHozzaadController extends Controller {
    @javafx.fxml.FXML
    private Button btnUjKategoria;
    @javafx.fxml.FXML
    private TextField tfKategoriaNev;

    @javafx.fxml.FXML
    public void ujKategoriaClick(ActionEvent actionEvent) {
        String name = tfKategoriaNev.getText().trim();
        if (name.isEmpty()) {
            alert("A Név mező nem lehet üres");
            return;
        }

        try {
            if (new EtelDb().kategoriaHozzaad(name) == 1) {
                alert("Sikeres hozzáadás");
                Stage stage = (Stage) btnUjKategoria.getScene().getWindow();
                stage.close();
            } else {
                alert("Sikertelen hozzáadás");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            alert("Ezzel a névvel már szerepel kategória az adatbázisban");
            e.printStackTrace();
        } catch (SQLException e) {
            errorAlert(e);
            e.printStackTrace();
        }
    }
}