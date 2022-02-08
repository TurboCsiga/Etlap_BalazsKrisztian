package com.example.etlap_balazskrisztian;

import javafx.event.ActionEvent;
        import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;

        import java.sql.SQLException;
        import java.util.List;
        import java.util.Optional;

public class KategoriaController extends Controller{
    @javafx.fxml.FXML
    private Button KategoriaHozzaadClick;
    @javafx.fxml.FXML
    private Button KategoriaTorolClick;
    @javafx.fxml.FXML
    private TableView<Kategoria> categoryTable;
    @javafx.fxml.FXML
    private TableColumn<Kategoria, Integer> kategoriaId;
    @javafx.fxml.FXML
    private TableColumn<Kategoria, String> kategoriaName;

    public void initialize() {
        kategoriaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        kategoriaName.setCellValueFactory(new PropertyValueFactory<>("Név"));

        try {
            fillTable();
        } catch (SQLException e) {
            errorAlert(e);
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void KategoriaHozzaadClick(ActionEvent actionEvent) {
        try {
            Controller kategoriaHozzaad = ujAblak("kategoria.fxml", "Kategórisa hozzáadása", 400, 400);
            kategoriaHozzaad.getStage().setOnHiding(event -> {
                try { fillTable(); } catch (SQLException e) {
                    errorAlert(e);
                    e.printStackTrace();
                }
            });

            kategoriaHozzaad.getStage().show();
        } catch (Exception e) {
            errorAlert(e);
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void KategoriaTorolClick(ActionEvent actionEvent) {
        if (categoryTable.getSelectionModel().getSelectedIndex() == -1) {
            alert("Válassza ki a törlendő sort");
            return;
        }

        Kategoria selected = categoryTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Biztos töröli ezt a kategóriát?");
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType resultType = result.orElse(ButtonType.CANCEL);

        try {
            if (resultType == ButtonType.OK) {
                if (new EtelDb().kategoriaTorles(selected.getId()) == 1) {
                    alert("Törlés sikertelen");
                } else {
                    alert("Törlés sikeres");
                }
                fillTable();
            }
        } catch (SQLException e) {
            errorAlert(e);
            e.printStackTrace();
        }
    }

    public void fillTable() throws SQLException {
        EtelDb db = new EtelDb();
        List<Kategoria> list = db.getKategoria();

        categoryTable.getItems().clear();

        for (Kategoria c: list) {
            categoryTable.getItems().add(c);
        }
    }
}