package BATTLES;
import java.util.UUID;

public class Jogador {
    private String id;
    private String nome;
    private String senha;
    private int saldoMoedas;
    private ListaEncadeada<Personagem> personagens;
    private Personagem personagemAtual;

    public Jogador(String nome, String senha) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.senha = senha;
        this.saldoMoedas = 100; // Saldo inicial
        this.personagens = new ListaEncadeada<>();
    }

    // Métodos de autenticação
    public boolean autenticar(String senha) {
        return this.senha.equals(senha);
    }

    // Gerenciamento de personagens
    public void criarPersonagem(String nome, int vidaMaxima, int manaMaxima) {
        Personagem novoPersonagem = new Personagem(nome, 1, vidaMaxima, manaMaxima, this);
        personagens.adicionar(novoPersonagem);
        if (personagemAtual == null) {
            personagemAtual = novoPersonagem;
        }
        System.out.println("Personagem " + nome + " criado com sucesso!");
    }

    public void selecionarPersonagem(int index) {
        if (index >= 0 && index < personagens.tamanho()) {
            personagemAtual = personagens.get(index);
            System.out.println("Personagem selecionado: " + personagemAtual.getNome());
        } else {
            System.out.println("Índice de personagem inválido!");
        }
    }

    // Sistema de moedas e recompensas
    public void adicionarMoedas(int quantidade) {
        if (quantidade > 0) {
            saldoMoedas += quantidade;
            System.out.println(nome + " recebeu " + quantidade + " moedas. Total: " + saldoMoedas);
        }
    }

    public boolean gastarMoedas(int quantidade) {
        if (saldoMoedas >= quantidade && quantidade > 0) {
            saldoMoedas -= quantidade;
            System.out.println(nome + " gastou " + quantidade + " moedas. Restante: " + saldoMoedas);
            return true;
        }
        System.out.println("Moedas insuficientes!");
        return false;
    }

    // Métodos de acesso
    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getSaldoMoedas() {
        return saldoMoedas;
    }

    public ListaEncadeada<Personagem> getPersonagens() {
        return personagens;
    }

    public Personagem getPersonagemAtual() {
        return personagemAtual;
    }

    public void setPersonagemAtual(Personagem personagemAtual) {
        this.personagemAtual = personagemAtual;
    }

    // Método para exibir informações do jogador
    public void exibirInfo() {
        System.out.println("\n=== Informações do Jogador ===");
        System.out.println("Nome: " + nome);
        System.out.println("Moedas: " + saldoMoedas);
        System.out.println("Personagens: " + personagens.tamanho());
        
        if (personagemAtual != null) {
            System.out.println("\nPersonagem Atual:");
            System.out.println("Nome: " + personagemAtual.getNome());
            System.out.println("Nível: " + personagemAtual.getNivel());
            System.out.println("HP: " + personagemAtual.getVidaAtual() + "/" + personagemAtual.getVidaMaxima());
        }
    }

    // Método para listar todos os personagens
    public void listarPersonagens() {
        if (personagens.tamanho() == 0) {
            System.out.println("Nenhum personagem criado ainda!");
            return;
        }

        System.out.println("\n=== Seus Personagens ===");
        for (int i = 0; i < personagens.tamanho(); i++) {
            Personagem p = personagens.get(i);
            System.out.println((i+1) + ". " + p.getNome() + 
                             " (Nível " + p.getNivel() + 
                             ", HP: " + p.getVidaAtual() + "/" + p.getVidaMaxima() + ")");
        }
    }
}