package com.example.etlap_balazskrisztian;

import javafx.beans.value.ChangeListener;
        import javafx.beans.value.ObservableValue;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;

        import java.sql.SQLException;
        import java.util.List;
import java.util.Optional;

public class HelloController extends Controller {

    @FXML
    private Button btnEtelTorles;
    @FXML
    private Button btnKategoria;
    @FXML
    private Button btnUjEtel;
    @FXML
    private TableColumn<Etel, Integer> etelArCell;
    @FXML
    private TableColumn<Etel, String> etelNevCell;
    @FXML
    private TableView<Etel> etelTable;
    @FXML
    private TableColumn<Etel, String> etelKategoriaCell;
    @FXML
    private Spinner<Integer> spEmelesFixOsszeggel;
    @FXML
    private Spinner<Integer> spSzazalekosEmeles;
    @FXML
    private Button btnEmelesSzazalek;
    @FXML
    private Button btnEmelesFix;
    @FXML
    private ChoiceBox<Kategoria> cbKategória;


    public void initialize() {
        etelNevCell.setCellValueFactory(new PropertyValueFactory<>("Név"));
        etelKategoriaCell.setCellValueFactory(new PropertyValueFactory<>("Kategória"));
        etelArCell.setCellValueFactory(new PropertyValueFactory<>("Ár"));

        try {
            List<Kategoria> cg = new EtelDb().getKategoria();
            for (Kategoria c: cg) {
                cbKategória.getItems().add(c);
            }
            Kategoria all = new Kategoria(0,"összes");
            cbKategória.getItems().add(all);
            fillTable();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        cbKategória.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Kategoria>() {
            @Override
            public void changed(ObservableValue<? extends Kategoria> observableValue, Kategoria category, Kategoria t1) {
                try {
                    fillTable(t1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void btnEtelTorolClick(ActionEvent actionEvent) {
        if (etelTable.getSelectionModel().getSelectedIndex() == -1) {
            alert("Törléshez előbb válasszon ki egy sort");
            return;
        }

        Etel selected = etelTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Biztos törölni szeretné ezt az ételt?");
        Optional<ButtonType> result = alert.showAndWait();
        ButtonType resultType = result.orElse(ButtonType.CANCEL);

        try {
            if (resultType == ButtonType.OK) {
                if (new EtelDb().etelTorles(selected.getId()) == 1) {
                    alert("Sikeres törlés");
                } else {
                    alert("Sikertelen törlés");
                }
                fillTable();
            }
        } catch (SQLException e) {
            errorAlert(e);
            e.printStackTrace();
        }
    }

    @FXML
    public void btnUjEtelClick(ActionEvent actionEvent) {
        try {
            Controller ujAblak = ujAblak("add-food-view.fxml", "Add Food", 400, 400);
            ujAblak.getStage().setOnCloseRequest(event -> {
                try { fillTable(); } catch (SQLException e) {
                    errorAlert(e);
                    e.printStackTrace();
                }
            });

            ujAblak.getStage().show();
        } catch (Exception e) {
            errorAlert(e);
            e.printStackTrace();
        }

    }

    public void fillTable() throws SQLException {
        EtelDb db = new EtelDb();
        List<Etel> list = db.getEtel();

        etelTable.getItems().clear();

        for (Etel food : list) {
            etelTable.getItems().add(food);
        }
    }

    public void fillTable(Kategoria sortBy) throws SQLException {
        EtelDb db = new EtelDb();
        List<Etel> list = db.getEtel();

        etelTable.getItems().clear();

        if (sortBy.getNev().equals("összes")) {
            for (Etel food: list) {
                etelTable.getItems().add(food);
            }
        } else {
            for (Etel food: list) {
                if (food.getKategoria().equals(sortBy.getNev())) {
                    etelTable.getItems().add(food);
                }
            }
        }
    }

    @FXML
    public void EmelesFixClick(ActionEvent actionEvent) {
        double amount = spEmelesFixOsszeggel.getValue();

        if (etelTable.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Biztos szeretné növelni az összes étel árát?");
            Optional<ButtonType> result = alert.showAndWait();
            ButtonType resultType = result.orElse(ButtonType.CANCEL);

            if (resultType == ButtonType.OK) {
                try {
                    if (new EtelDb().emelMind(amount) == -1) {
                        alert("Sikertelen növelés");
                    } else {
                        alert("Sikeres növelés");
                    }
                } catch (SQLException e) {
                    errorAlert(e);
                    e.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            String name = etelTable.getSelectionModel().getSelectedItem().getNev();
            alert.setHeaderText("Biztos szeretné növelni "+name+" árát?");
            Optional<ButtonType> result = alert.showAndWait();
            ButtonType resultType = result.orElse(ButtonType.CANCEL);

            if (resultType == ButtonType.OK) {
                int id = etelTable.getSelectionModel().getSelectedItem().getId();

                try {
                    if (new EtelDb().emelEgy(amount, id) == -1) {
                        alert("Sikertelen növelés");
                    } else {
                        alert("Sikeres növelés");
                    }
                } catch (SQLException e) {
                    errorAlert(e);
                    e.printStackTrace();
                }
            }
        }

        try {
            fillTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void EmelesSzazalekClick(ActionEvent actionEvent) {
        double amount = 1 + spSzazalekosEmeles.getValue() / 100d;

        if (etelTable.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Biztos szeretné növelni az összes étel árát?");
            Optional<ButtonType> result = alert.showAndWait();
            ButtonType resultType = result.orElse(ButtonType.CANCEL);

            if (resultType == ButtonType.OK) {
                try {
                    if (new EtelDb().emelMind(amount) == -1) {
                        alert("Sikertelen növelés");
                    } else {
                        alert("Sikeres növelés");
                    }
                } catch (SQLException e) {
                    errorAlert(e);
                    e.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            String name = etelTable.getSelectionModel().getSelectedItem().getNev();
            alert.setHeaderText("Biztos szeretné növelni "+name+" árát?");
            Optional<ButtonType> result = alert.showAndWait();
            ButtonType resultType = result.orElse(ButtonType.CANCEL);

            if (resultType == ButtonType.OK) {
                int id = etelTable.getSelectionModel().getSelectedItem().getId();

                try {
                    if (new EtelDb().emelEgy(amount, id) == -1) {
                        alert("Sikertelen növelés");
                    } else {
                        alert("Sikeres növelés");
                    }
                } catch (SQLException e) {
                    errorAlert(e);
                    e.printStackTrace();
                }
            }
        }

        try {
            fillTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void KategoriaClick(ActionEvent actionEvent) {
        try {
            Controller categoryWindow = ujAblak("show-categories.fxml", "Categories", 210, 300);
            categoryWindow.getStage().setOnCloseRequest(event -> {
                try { fillTable(); } catch (SQLException e) {
                    errorAlert(e);
                    e.printStackTrace();
                }
            });

            categoryWindow.getStage().show();
        } catch (Exception e) {
            errorAlert(e);
            e.printStackTrace();
        }
    }

}