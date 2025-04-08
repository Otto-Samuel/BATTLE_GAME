//import javafx.scene.layout.Label;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Cadastro extends Application{
    @Override
    public void start(Stage primaryStage) {
        // Criando o título no topo
        //Labeled titulo;
        //titulo.setText();

        // Criando botões dentro de um HBox
        Button btn1 = new Button("Botão 1");
        Button btn2 = new Button("Botão 2");
        Button btn3 = new Button("Botão 3");

        HBox hbox = new HBox(10, btn1, btn2, btn3); // Espaçamento de 10px
        hbox.setStyle("-fx-background-color: lightgray; -fx-padding: 10px;");

        // Criando um campo de texto
        TextField campoTexto = new TextField("Digite algo...");

        // Criando o layout principal
        //VBox layoutPrincipal = new VBox(20, titulo, hbox, campoTexto); // Espaçamento de 20px
        //layoutPrincipal.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        // Criando a cena
        //Scene scene = new Scene(new StackPane(layoutPrincipal), 400, 300);
       //
    }

    public static void main(String[] args) {
        launch(args);
    }
}
