package BATTLES;

import BATTLES.EstruturaDeDados.ListaEncadeada;
import BATTLES.PersonagemeMonstro.Jogador;

public class SistemaAutenticacao {
    private ListaEncadeada<Jogador> jogadores;
    
    public SistemaAutenticacao() {
        this.jogadores = new ListaEncadeada<>();
        Jogador admin = new Jogador("admin", "admin123");
        admin.criarPersonagem("AdminHero", 150, 100);
        jogadores.adicionar(admin);
    }
    
    public Jogador cadastrar(String nome, String senha) {
        if (jogadorExiste(nome)) {
            System.out.println("Nome de usuário já existe!");
            return null;
        }
        
        Jogador novoJogador = new Jogador(nome, senha);
        jogadores.adicionar(novoJogador);
        return novoJogador;
    }
    
    public Jogador autenticar(String nome, String senha) {
        for (int i = 0; i < jogadores.tamanho(); i++) {
            Jogador j = jogadores.get(i);
            if (j.getNome().equals(nome) && j.autenticar(senha)) {
                return j;
            }
        }
        return null;
    }
    
    private boolean jogadorExiste(String nome) {
        for (int i = 0; i < jogadores.tamanho(); i++) {
            if (jogadores.get(i).getNome().equals(nome)) {
                return true;
            }
        }
        return false;
    }
    
    public ListaEncadeada<Jogador> getJogadores() {
        return jogadores;
    }
}