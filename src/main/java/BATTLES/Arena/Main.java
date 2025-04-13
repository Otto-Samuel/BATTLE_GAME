package BATTLES.Arena;

import java.util.Random;
import java.util.Scanner;
/*
 * @autor: Otto Samuel Monteiro Rego
 * @data: 09/04/2025
 * @license: MIT LICENSE
 */

import BATTLES.SistemaAutenticacao;
import BATTLES.EstruturaDeDados.ListaEncadeada;
import BATTLES.InventarioeItem.Item;
import BATTLES.PersonagemeMonstro.Entidade;
import BATTLES.PersonagemeMonstro.Jogador;
import BATTLES.PersonagemeMonstro.Monstro;

public class Main {
    private static SistemaAutenticacao sistema = new SistemaAutenticacao();
    private static Jogador jogadorAtual = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== RPG BATTLES ===");
        exibirMenuPrincipal();
    }

    private static void exibirMenuPrincipal() {
        while (true) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Login");
            System.out.println("2. Cadastrar");
            System.out.println("3. Sair");
            System.out.print("Escolha: ");

            int opcao = lerInteiro(1, 3);

            switch (opcao) {
                case 1:
                    fazerLogin();
                    break;
                case 2:
                    cadastrarJogador();
                    break;
                case 3:
                    System.out.println("Até logo!");
                    scanner.close();
                    System.exit(0);
            }
        }
    }

    private static void fazerLogin() {
        System.out.print("\nNome: ");
        String nome = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        jogadorAtual = sistema.autenticar(nome, senha);
        if (jogadorAtual != null) {
            System.out.println("\nLogin bem-sucedido! Bem-vindo, " + nome + "!");
            exibirMenuJogador();
        } else {
            System.out.println("\nCredenciais inválidas!");
        }
    }

    private static void cadastrarJogador() {
        System.out.print("\nNovo nome de usuário: ");
        String nome = scanner.nextLine();
        System.out.print("Nova senha: ");
        String senha = scanner.nextLine();

        jogadorAtual = sistema.cadastrar(nome, senha);
        System.out.println("\nCadastro realizado com sucesso!");
        System.out.println("Criando seu primeiro personagem...");
        criarPersonagemInicial();
    }

    private static void criarPersonagemInicial() {
        System.out.print("\nNome do seu primeiro personagem: ");
        String nomePersonagem = scanner.nextLine();
        jogadorAtual.criarPersonagem(nomePersonagem, 100, 50);
        exibirMenuJogador();
    }

    private static void exibirMenuJogador() {
        while (jogadorAtual != null) {
            System.out.println("\n=== MENU DO JOGADOR ===");
            System.out.println("1. Gerenciar Personagens");
            System.out.println("2. Iniciar Batalha PvE");
            System.out.println("3. Loja");
            System.out.println("4. Ver Status");
            System.out.println("5. Sair da Conta");
            System.out.print("Escolha: ");

            int opcao = lerInteiro(1, 5);

            switch (opcao) {
                case 1:
                    gerenciarPersonagens();
                    break;
                case 2:
                    iniciarBatalhaPVE();
                    break;
                case 3:
                    acessarLoja();
                    break;
                case 4:
                    jogadorAtual.exibirInfo();
                    break;
                case 5:
                    jogadorAtual = null;
                    System.out.println("Desconectado com sucesso!");
                    return;
            }
        }
    }

    private static void gerenciarPersonagens() {
        while (true) {
            System.out.println("\n=== GERENCIAR PERSONAGENS ===");
            jogadorAtual.listarPersonagens();
            System.out.println("\n1. Criar Novo Personagem");
            System.out.println("2. Selecionar Personagem");
            System.out.println("3. Voltar");
            System.out.print("Escolha: ");

            int opcao = lerInteiro(1, 3);

            switch (opcao) {
                case 1:
                    criarNovoPersonagem();
                    break;
                case 2:
                    selecionarPersonagem();
                    break;
                case 3:
                    return;
            }
        }
    }

    private static void criarNovoPersonagem() {
        System.out.print("\nNome do novo personagem: ");
        String nome = scanner.nextLine();
        jogadorAtual.criarPersonagem(nome, 100, 50);
        System.out.println("Personagem " + nome + " criado com sucesso!");
    }

    private static void selecionarPersonagem() {
        if (jogadorAtual.getPersonagens().tamanho() == 0) {
            System.out.println("Você não tem personagens ainda!");
            return;
        }

        System.out.print("\nNúmero do personagem para selecionar: ");
        int index = lerInteiro(1, jogadorAtual.getPersonagens().tamanho()) - 1;
        jogadorAtual.selecionarPersonagem(index);
    }

    private static void iniciarBatalhaPVE() {
        if (jogadorAtual.getPersonagemAtual() == null) {
            System.out.println("Selecione um personagem primeiro!");
            return;
        }

        System.out.println("\n=== PREPARAR BATALHA ===");
        System.out.print("Dificuldade (1-Fácil, 2-Médio, 3-Difícil): ");
        int dificuldade = lerInteiro(1, 3);

        ListaEncadeada<Entidade> participantes = new ListaEncadeada<>();
        participantes.adicionar(jogadorAtual.getPersonagemAtual());

        // Criar monstros baseado na dificuldade !!!
        int numMonstros = dificuldade + 1;
        for (int i = 0; i < numMonstros; i++) {
            String nomeMonstro = gerarNomeMonstro();
            int nivelMonstro = dificuldade + new Random().nextInt(2);
            int vidaMonstro = 30 + (dificuldade * 20);
            int expMonstro = 20 * dificuldade;
            
            participantes.adicionar(new Monstro(nomeMonstro, nivelMonstro, vidaMonstro, 0, expMonstro));
        }

        Arena arena = new Arena();
        arena.iniciarBatalha(participantes);

        while (arena.isEmAndamento()) {
            arena.executarTurno();
            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }
    }

    private static String gerarNomeMonstro() {
        String[] prefixos = {"Goblin", "Orc", "Esqueleto", "Zumbi", "Lobisomem"};
        String[] sufixos = {"Sombrio", "Feroz", "Cruel", "do Abismo", "Assassino"};
        
        Random rand = new Random();
        return prefixos[rand.nextInt(prefixos.length)] + " " + sufixos[rand.nextInt(sufixos.length)];
    }

    private static void acessarLoja() {
        if (jogadorAtual.getPersonagemAtual() == null) {
            System.out.println("Selecione um personagem primeiro!");
            return;
        }
    
        System.out.println("\n=== LOJA ===");
        System.out.println("Saldo: " + jogadorAtual.getSaldoMoedas() + " moedas");
        System.out.println("\n1. Poção de Cura (30 moedas) - Restaura 30 HP");
        System.out.println("2. Poção de Mana (20 moedas) - Restaura 20 MP");
        System.out.println("3. Espada de Ferro (50 moedas) - +5 de dano");
        System.out.println("4. Voltar");
        System.out.print("Escolha: ");
    
        int opcao = lerInteiro(1, 4);
    
        if (opcao == 4) return;
    
        Item item = null;
        int preco = 0;
    
        switch (opcao) {
            case 1:
                item = new Item(1, "Poção de Cura", "Restaura 30 HP", 1, 30, 1);
                preco = 30;
                break;
            case 2:
                item = new Item(2, "Poção de Mana", "Restaura 20 MP", 1, 20, 1);
                preco = 20;
                break;
            case 3:
                item = new Item(3, "Espada de Ferro", "Aumenta dano em 5", 2, 5, 1);
                preco = 50;
                break;
        }
    
        if (jogadorAtual.getSaldoMoedas() >= preco) {
            jogadorAtual.gastarMoedas(preco);
            jogadorAtual.getPersonagemAtual().getInventario().adicionarItem(item);
            System.out.println("Compra realizada com sucesso! " + item.getNome() + " adicionado ao inventário.");
        } else {
            System.out.println("Moedas insuficientes!");
        }
    }

    private static int lerInteiro(int min, int max) {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine());
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.print("Valor inválido. Digite entre " + min + " e " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Por favor, digite um número válido: ");
            }
        }
    }
}