package BATTLES;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
    private static Stage primaryStage;
    private static SistemaAutenticacao sistema = new SistemaAutenticacao();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        mostrarTelaLogin();
    }
    
    public static void mostrarTelaLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/battles/views/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 400, 300);
            primaryStage.setTitle("RPG Battles - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void mostrarTelaCadastro() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/battles/views/cadastro.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 400, 350);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void mostrarMenuPrincipal(Jogador jogador) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/battles/views/menu.fxml"));
            Parent root = loader.load();
            
            MenuController controller = loader.getController();
            controller.setJogador(jogador);
            
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("RPG Battles - Menu Principal");
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static SistemaAutenticacao getSistemaAutenticacao() {
        return sistema;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}