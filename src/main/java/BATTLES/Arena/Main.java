package BATTLES.Arena;

import java.util.Random;
import java.util.Scanner;
/*
 * @autor: Otto , Dario, Renato, tiagoFerreira 
 * @data: 09/04/2025
 * @license: MIT LICENSE
 */

import BATTLES.SistemaAutenticacao;
import BATTLES.EstruturaDeDados.ListaEncadeada;
import BATTLES.InventarioeItem.Item;
import BATTLES.PersonagemeMonstro.Entidade;
import BATTLES.PersonagemeMonstro.Jogador;
import BATTLES.PersonagemeMonstro.Monstro;
import BATTLES.PersonagemeMonstro.Personagem;

// Otto
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
        if (jogadorAtual != null) {
            System.out.println("\nCadastro realizado com sucesso!");
            System.out.println("Criando seu primeiro personagem...");
            criarPersonagemInicial();
        }
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
            System.out.println("3. Iniciar Batalha PvP");
            System.out.println("4. Loja");
            System.out.println("5. Ver Status");
            System.out.println("6. Sair da Conta");
            System.out.print("Escolha: ");

            int opcao = lerInteiro(1, 6);

            switch (opcao) {
                case 1:
                    gerenciarPersonagens();
                    break;
                case 2:
                    iniciarBatalhaPVE();
                    break;
                case 3:
                    iniciarBatalhaPVP();
                    break;
                case 4:
                    acessarLoja();
                    break;
                case 5:
                    jogadorAtual.exibirInfo();
                    break;
                case 6:
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

        System.out.println("\n=== PREPARAR BATALHA PvE ===");
        System.out.print("Dificuldade (1-Fácil, 2-Médio, 3-Difícil): ");
        int dificuldade = lerInteiro(1, 3);

        ListaEncadeada<Entidade> participantes = new ListaEncadeada<>();
        Personagem personagemJogador = jogadorAtual.getPersonagemAtual();
        participantes.adicionar(personagemJogador);

        int numMonstros = dificuldade + 1;
        for (int i = 0; i < numMonstros; i++) {
            String nomeMonstro = gerarNomeMonstro();
            int nivelMonstro = dificuldade + new Random().nextInt(2);
            int vidaMonstro = 20 + (dificuldade * 15);
            int expMonstro = 20 * dificuldade;
            
            participantes.adicionar(new Monstro(nomeMonstro, nivelMonstro, vidaMonstro, 0, expMonstro));
        }

        Arena arena = new Arena();
        arena.iniciarBatalha(participantes);

        while (arena.isEmAndamento()) {
            arena.executarTurno();
            if (!arena.isEmAndamento()) {
                break;
            }
            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }

         
        if (!personagemJogador.estaVivo()) {
            System.out.println("Você foi derrotado! Tente novamente.");
            jogadorAtual.adicionarMoedas(10);
            System.out.println("Ganhou 10 moedas por sua tentativa!");
        } else {
            int monstrosDerrotados = numMonstros - participantes.tamanho() + 1;
            int xpGanho = 20 * dificuldade * monstrosDerrotados;
            personagemJogador.ganharExperiencia(xpGanho);
            System.out.println("Parabéns! Você venceu a batalha PvE, derrotando " + monstrosDerrotados + " de " + numMonstros + " monstros!");
            jogadorAtual.adicionarMoedas(50 * dificuldade);
            System.out.println("Ganhou " + (50 * dificuldade) + " moedas!");
        }

        arena.finalizar();
    }

    private static void iniciarBatalhaPVP() {
        if (jogadorAtual.getPersonagemAtual() == null) {
            System.out.println("Selecione um personagem primeiro!");
            return;
        }

        System.out.println("\n=== PREPARAR BATALHA PvP ===");
        System.out.println("1. Lutar contra AdminHero (personagem padrão)");
        System.out.println("2. Escolher um personagem criado");
        System.out.println("3. Voltar");
        System.out.print("Escolha: ");

        int opcao = lerInteiro(1, 3);

        if (opcao == 3) {
            return; 
        }

        ListaEncadeada<Entidade> participantes = new ListaEncadeada<>();
        Personagem personagemJogador = jogadorAtual.getPersonagemAtual();
        participantes.adicionar(personagemJogador);

        Personagem adversario = null;

        if (opcao == 1) {
            
            Jogador admin = null;
            for (int i = 0; i < sistema.getJogadores().tamanho(); i++) {
                if (sistema.getJogadores().get(i).getNome().equals("admin")) {
                    admin = sistema.getJogadores().get(i);
                    break;
                }
            }

            if (admin == null || admin.getPersonagens().tamanho() == 0) {
                System.out.println("Oponente padrão não disponível!");
                return;
            }

            adversario = admin.getPersonagens().get(0);
            if (!adversario.estaVivo()) {
                System.out.println("AdminHero não está vivo! Reinicie o jogo para restaurar.");
                return;
            }
        } else if (opcao == 2) {
            ListaEncadeada<Personagem> personagensDisponiveis = new ListaEncadeada<>();
            for (int i = 0; i < sistema.getJogadores().tamanho(); i++) {
                Jogador j = sistema.getJogadores().get(i);
                for (int k = 0; k < j.getPersonagens().tamanho(); k++) {
                    Personagem p = j.getPersonagens().get(k);

                    if (p != personagemJogador && p.estaVivo()) {
                        personagensDisponiveis.adicionar(p);
                    }
                }
            }

            if (personagensDisponiveis.tamanho() == 0) {
                System.out.println("Nenhum personagem disponível para batalha!");
                return;
            }

            System.out.println("\nPersonagens disponíveis para batalha:");
            for (int i = 0; i < personagensDisponiveis.tamanho(); i++) {
                Personagem p = personagensDisponiveis.get(i);
                System.out.printf("%d. %s (Nível: %d (%.1f%%), HP: %d/%d)%n",
                        i + 1, p.getNome(), p.getNivel(), p.getPorcentagemExperiencia(),
                        p.getVidaAtual(), p.getVidaMaxima());
            }
            System.out.print("Escolha o adversário: ");
            int index = lerInteiro(1, personagensDisponiveis.tamanho()) - 1;
            adversario = personagensDisponiveis.get(index);
        }

        participantes.adicionar(adversario);
        System.out.println("Batalha PvP: " + personagemJogador.getNome() + " vs. " + adversario.getNome() + "!");
        Arena arena = new Arena();
        arena.iniciarBatalha(participantes);

        while (arena.isEmAndamento()) {
            arena.executarTurno();
            if (!arena.isEmAndamento()) {
                break;
            }
            System.out.println("\nPressione Enter para continuar...");
            scanner.nextLine();
        }

        for (int i = 0; i < participantes.tamanho(); i++) {
            Entidade e = participantes.get(i);
            if (e.estaVivo() && e instanceof Personagem && ((Personagem)e).getJogador() != null) {
                ((Personagem)e).ganharExperiencia(50);
                ((Personagem)e).getJogador().adicionarMoedas(100);
                System.out.println(((Personagem)e).getJogador().getNome() + " venceu a batalha PvP e ganhou 100 moedas!");
                break;
            }
        }
        arena.finalizar();
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