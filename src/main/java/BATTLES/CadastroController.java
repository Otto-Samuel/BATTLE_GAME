package BATTLES;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class CadastroController {
    
    @FXML private TextField txtNovoUsuario;
    @FXML private PasswordField txtNovaSenha;
    @FXML private PasswordField txtConfirmarSenha;
    @FXML private Button btnCadastrar;
    @FXML private Button btnVoltar;
    @FXML private Label lblMensagem;
    
    @FXML
    private void initialize() {
        btnCadastrar.setOnAction(e -> cadastrarUsuario());
        btnVoltar.setOnAction(e -> Main.mostrarTelaLogin());
    }
    
    private void cadastrarUsuario() {
        String usuario = txtNovoUsuario.getText();
        String senha = txtNovaSenha.getText();
        String confirmar = txtConfirmarSenha.getText();
        
        if (usuario.isEmpty() || senha.isEmpty()) {
            lblMensagem.setText("Preencha todos os campos!");
            return;
        }
        
        if (!senha.equals(confirmar)) {
            lblMensagem.setText("As senhas não coincidem!");
            return;
        }
        
        Jogador novoJogador = Main.getSistemaAutenticacao().cadastrar(usuario, senha);
        
        if (novoJogador != null) {
            lblMensagem.setText("Cadastro realizado com sucesso!");
            Main.mostrarMenuPrincipal(novoJogador);
        } else {
            lblMensagem.setText("Nome de usuário já existe!");
        }
    }
}