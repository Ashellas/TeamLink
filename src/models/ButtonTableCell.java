package models;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Function;

public class ButtonTableCell<S> extends TableCell<S, Button> {

    private final Button tableButton;

    public ButtonTableCell(String label, Function< S, S> function) {
        this.tableButton = new Button(label);
        this.tableButton.getStylesheets().add("/stylesheets/ButtonStyleSheet.css");
        this.tableButton.getStyleClass().add("upload");
        this.tableButton.setOnAction((ActionEvent e) -> {
            function.apply(getCurrentItem());
        });
    }

    public S getCurrentItem() {
        return (S) getTableView().getItems().get(getIndex());
    }

    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label, Function< S, S> function) {
        return param -> new ButtonTableCell<>(label, function);
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(tableButton);
        }
    }
}
