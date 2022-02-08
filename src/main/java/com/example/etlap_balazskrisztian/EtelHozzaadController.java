package com.example.etlap_balazskrisztian;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.List;

public class EtelHozzaadController extends Controller{
    @FXML
    private Button etelHozzaadBtn;
    @FXML
    private ChoiceBox<Kategoria> cbKategoria;
    @FXML
    private TextArea taLeiras;
    @FXML
    private Spinner<Integer> spAr;
    @FXML
    private TextField tfEtelNev;

    public void initialize() {
        try {
            List<Kategoria> categories = new EtelDb().getKategoria();
            for (Kategoria c: categories) {
                cbKategoria.getItems().add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void etelHozzaadBtnClick(ActionEvent actionEvent) {
        String name = tfEtelNev.getText().trim();
        if (name.isEmpty()) {
            alert("A Név mező nem lehet üres");
            return;
        }

        String details = taLeiras.getText().trim();
        if (details.isEmpty()) {
            alert("A Leírás mező nem lehet üres");
            return;
        }

        int price;
        try { price = spAr.getValue(); }
        catch (NullPointerException e) {
            errorAlert(e);
            e.printStackTrace();
            alert("Az ár megadása kötelező");
            return;
        }
        catch (Exception e) {
            errorAlert(e);
            e.printStackTrace();
            alert("Az ár csak 1 és 9999 közötti szám lehet");
            return;
        }

        if (cbKategoria.getSelectionModel().getSelectedIndex() == -1) {
            alert("Kategória kiválasztása kötelező");
            return;
        }
        Kategoria kategoria = cbKategoria.getSelectionModel().getSelectedItem();

        try {
            if (new EtelDb().etelHozzaad(name, details, price, kategoria.getId()) == 1) {
                alert("Az étel hozzáadása sikeres");
            } else {
                alert("Az étel hozzáadása sikertelen");
            }
        } catch (SQLException e) {
            errorAlert(e);
        }
    }
}
