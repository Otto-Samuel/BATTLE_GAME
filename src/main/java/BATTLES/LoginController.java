package BATTLES;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginController {
    
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;
    @FXML private Button btnLogin;
    @FXML private Button btnCadastro;
    @FXML private Label lblMensagem;
    
    @FXML
    private void initialize() {
        btnLogin.setOnAction(e -> fazerLogin());
        btnCadastro.setOnAction(e -> Main.mostrarTelaCadastro());
    }
    
    private void fazerLogin() {
        String usuario = txtUsuario.getText();
        String senha = txtSenha.getText();
        
        if (usuario.isEmpty() || senha.isEmpty()) {
            lblMensagem.setText("Preencha todos os campos!");
            return;
        }
        
        Jogador jogador = Main.getSistemaAutenticacao().autenticar(usuario, senha);
        
        if (jogador != null) {
            lblMensagem.setText("Login bem-sucedido!");
            Main.mostrarMenuPrincipal(jogador);
        } else {
            lblMensagem.setText("Usuário ou senha incorretos!");
        }
    }
}