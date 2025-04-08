package BATTLES;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class MenuController {
    
    @FXML private Label lblBoasVindas;
    @FXML private Button btnJogar;
    @FXML private Button btnPersonagens;
    @FXML private Button btnLoja;
    @FXML private Button btnSair;
    
    private Jogador jogador;
    
    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
        lblBoasVindas.setText("Bem-vindo, " + jogador.getNome() + "!");
    }
    
    @FXML
    private void initialize() {
        btnJogar.setOnAction(e -> iniciarJogo());
        btnPersonagens.setOnAction(e -> gerenciarPersonagens());
        btnLoja.setOnAction(e -> acessarLoja());
        btnSair.setOnAction(e -> sair());
    }
    
    private void iniciarJogo() {
        // Implementar lógica para iniciar o jogo
        System.out.println("Iniciando jogo...");
    }
    
    private void gerenciarPersonagens() {
        // Implementar tela de gerenciamento de personagens
        System.out.println("Abrindo gerenciador de personagens...");
    }
    
    private void acessarLoja() {
        // Implementar tela da loja
        System.out.println("Abrindo loja...");
    }
    
    private void sair() {
        // Voltar para tela de login
        Main.mostrarTelaLogin();
    }
}