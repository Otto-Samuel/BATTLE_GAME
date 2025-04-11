package BATTLES.Arena;

import java.util.Random;
import java.util.Scanner;

import BATTLES.FilaEncadeada;
import BATTLES.Item;
import BATTLES.ListaEncadeada;
import BATTLES.PilhaEncadeada;

import java.util.InputMismatchException;

public class Arena {
    private FilaEncadeada<Entidade> ordemTurnos;
    private PilhaEncadeada<Entidade> eliminados;
    private ListaEncadeada<Entidade> participantes;
    private int turnoAtual;
    private boolean emAndamento;
    private Scanner scanner;
    
    public Arena() {
        this.ordemTurnos = new FilaEncadeada<>();
        this.eliminados = new PilhaEncadeada<>();
        this.participantes = new ListaEncadeada<>();
        this.turnoAtual = 0;
        this.emAndamento = false;
        this.scanner = new Scanner(System.in);
    }
    
    public void iniciarBatalha(ListaEncadeada<Entidade> participantes) {
        this.participantes = participantes;
        
        for (int i = 0; i < participantes.tamanho(); i++) {
            Entidade e = participantes.get(i);
            if (e.estaVivo()) {
                ordemTurnos.enqueue(e);
            }
        }
        
        embaralharOrdemTurnos();
        
        turnoAtual = 1;
        emAndamento = true;
        System.out.println("Batalha iniciada com " + ordemTurnos.size + " participantes!");
    }
    
    private void embaralharOrdemTurnos() {
        Entidade[] array = new Entidade[ordemTurnos.size];
        int index = 0;
        while (!ordemTurnos.isEmpty()) {
            array[index++] = ordemTurnos.dequeue();
        }
        
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Entidade temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        
        for (Entidade e : array) {
            ordemTurnos.enqueue(e);
        }
    }
    
    public void executarTurno() {
        if (!emAndamento || ordemTurnos.isEmpty()) {
            return;
        }
        
        System.out.println("\n=== Turno " + turnoAtual + " ===");
        
        Entidade atuante = ordemTurnos.dequeue();
        
        if (!atuante.estaVivo()) {
            System.out.println(atuante.getNome() + " está morto e não pode agir.");
            return;
        }
        
        System.out.println("Vez de " + atuante.getNome());
        System.out.println("HP: " + atuante.getVidaAtual() + "/" + atuante.getVidaMaxima());
        
        if (atuante instanceof Personagem) {
            executarTurnoJogador((Personagem) atuante);
        } else {
            executarTurnoMonstro(atuante);
        }
        
        if (verificarVencedor()) {
            emAndamento = false;
            anunciarVencedor();
        }
        
        turnoAtual++;
    }
    
    private void executarTurnoJogador(Personagem jogador) {
        try {
            System.out.println("\nEscolha sua ação:");
            System.out.println("1. Atacar");
            System.out.println("2. Usar habilidade");
            System.out.println("3. Usar item");
            System.out.println("4. Defender");
            System.out.print("Opção: ");
            
            int opcao = lerOpcao(1, 4);
            Entidade alvo = escolherAlvo(jogador);
            
            switch (opcao) {
                case 1:
                    jogador.atacar(alvo);
                    break;
                case 2:
                    usarHabilidade(jogador, alvo);
                    break;
                case 3:
                    usarItem(jogador, alvo);
                    break;
                case 4:
                    System.out.println(jogador.getNome() + " está se defendendo!");
                    jogador.curar(5);
                    break;
            }
            
            verificarDerrota(jogador, alvo);
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Perdeu o turno.");
            scanner.nextLine();
        }
    }

    private void usarItem(Personagem jogador, Entidade alvo) {
        if (jogador.getInventario().getItens().tamanho() == 0) {
            System.out.println("Você não tem itens no inventário!");
            return;
        }
        
        System.out.println("\nItens disponíveis:");
        for (int i = 0; i < jogador.getInventario().getItens().tamanho(); i++) {
            Item item = jogador.getInventario().getItens().get(i);
            System.out.println((i+1) + ". " + item.getNome() + 
                             " (" + item.getDescricao() + 
                             ") x" + item.getQuantidade());
        }
        
        System.out.print("Escolha um item: ");
        int itemIndex = lerOpcao(1, jogador.getInventario().getItens().tamanho()) - 1;
        
        jogador.usarItem(itemIndex, alvo);
    }
    
    private int lerOpcao(int min, int max) {
        while (true) {
            try {
                int opcao = scanner.nextInt();
                scanner.nextLine();
                if (opcao >= min && opcao <= max) {
                    return opcao;
                }
                System.out.print("Opção inválida. Tente novamente: ");
            } catch (InputMismatchException e) {
                System.out.print("Por favor, digite um número: ");
                scanner.nextLine();
            }
        }
    }
    
    private void usarHabilidade(Personagem jogador, Entidade alvo) {
        if (jogador.getHabilidades().tamanho() > 0) {
            System.out.println("\nHabilidades disponíveis:");
            for (int i = 0; i < jogador.getHabilidades().tamanho(); i++) {
                Habilidade h = jogador.getHabilidades().get(i);
                System.out.println((i+1) + ". " + h.getNome() + 
                                 " (Dano: " + h.getDano() + 
                                 ", Custo: " + h.getCustoMana() + ")");
            }
            System.out.print("Escolha uma habilidade: ");
            int habilidadeEscolhida = lerOpcao(1, jogador.getHabilidades().tamanho()) - 1;
            
            Habilidade h = jogador.getHabilidades().get(habilidadeEscolhida);
            jogador.usarHabilidade(h, alvo);
            System.out.println(jogador.getNome() + " usou " + h.getNome() + 
                             " em " + alvo.getNome() + "!");
        } else {
            System.out.println("Você não tem habilidades disponíveis!");
        }
    }
    
    private void verificarDerrota(Entidade atuante, Entidade alvo) {
        if (!alvo.estaVivo()) {
            System.out.println(alvo.getNome() + " foi derrotado!");
            eliminados.push(alvo);
            
            if (alvo instanceof Monstro && atuante instanceof Personagem) {
                int exp = ((Monstro)alvo).getExperienciaConcedida();
                ((Personagem)atuante).ganharExperiencia(exp);
                System.out.println(atuante.getNome() + " ganhou " + exp + " pontos de experiência!");
            }
        }
        
        if (atuante.estaVivo()) {
            ordemTurnos.enqueue(atuante);
        } else {
            System.out.println(atuante.getNome() + " foi derrotado!");
            eliminados.push(atuante);
        }
    }
    
    private void executarTurnoMonstro(Entidade monstro) {
        Entidade alvo = escolherAlvo(monstro);
        monstro.atacar(alvo);
        System.out.println(monstro.getNome() + " atacou " + alvo.getNome() + "!");
        
        verificarDerrota(monstro, alvo);
    }
    
    private Entidade escolherAlvo(Entidade atuante) {
        if (atuante instanceof Monstro) {
            return escolherAlvoMonstro(atuante);
        }
        return escolherAlvoJogador(atuante);
    }
    
    private Entidade escolherAlvoMonstro(Entidade monstro) {
        ListaEncadeada<Entidade> alvosPossiveis = new ListaEncadeada<>();
        for (int i = 0; i < participantes.tamanho(); i++) {
            Entidade e = participantes.get(i);
            if (e.estaVivo() && e != monstro && e instanceof Personagem) {
                alvosPossiveis.adicionar(e);
            }
        }
        
        if (alvosPossiveis.tamanho() == 0) {
            return participantes.get(0);
        }
        
        Random rand = new Random();
        return alvosPossiveis.get(rand.nextInt(alvosPossiveis.tamanho()));
    }
    
    private Entidade escolherAlvoJogador(Entidade jogador) {
        System.out.println("\nEscolha seu alvo:");
        int index = 1;
        ListaEncadeada<Entidade> alvosDisponiveis = new ListaEncadeada<>();
        
        for (int i = 0; i < participantes.tamanho(); i++) {
            Entidade e = participantes.get(i);
            if (e.estaVivo() && e != jogador) {
                System.out.println(index + ". " + e.getNome() + 
                                 " (HP: " + e.getVidaAtual() + "/" + e.getVidaMaxima() + ")");
                alvosDisponiveis.adicionar(e);
                index++;
            }
        }
        
        int escolha = lerOpcao(1, alvosDisponiveis.tamanho()) - 1;
        return alvosDisponiveis.get(escolha);
    }
    
    public boolean verificarVencedor() {
        int vivos = 0;
        Entidade ultimoVivo = null;
        
        for (int i = 0; i < participantes.tamanho(); i++) {
            Entidade e = participantes.get(i);
            if (e.estaVivo()) {
                vivos++;
                ultimoVivo = e;
            }
        }
        
        if (vivos == 1) {
            if (ultimoVivo instanceof Personagem) {
                Personagem vencedor = (Personagem)ultimoVivo;
                System.out.println("\nParabéns! " + vencedor.getNome() + " venceu a batalha!");
                
                int recompensa = 50 * participantes.tamanho();
                vencedor.getJogador().adicionarMoedas(recompensa);
                System.out.println("Você ganhou " + recompensa + " moedas!");
            }
            return true;
        } else if (vivos == 0) {
            System.out.println("\nTodos foram derrotados! Não há vencedores.");
            return true;
        }
        
        return false;
    }
    
    public void anunciarVencedor() {
        exibirRankingFinal();
    }
    
    public void exibirRankingFinal() {
        System.out.println("\n=== Ranking Final ===");
        
        PilhaEncadeada<Entidade> copia = new PilhaEncadeada<>();
        ListaEncadeada<Entidade> ranking = new ListaEncadeada<>();
        
        while (!eliminados.isEmpty()) {
            Entidade e = eliminados.pop();
            ranking.adicionar(e);
            copia.push(e);
        }
        
        while (!copia.isEmpty()) {
            eliminados.push(copia.pop());
        }
        
        for (int i = ranking.tamanho() - 1; i >= 0; i--) {
            Entidade e = ranking.get(i);
            String status = e.estaVivo() ? "Vencedor" : "Derrotado";
            System.out.println((ranking.tamanho() - i) + "º lugar: " + 
                              e.getNome() + " (" + status + ")");
        }
    }
    
public void finalizar() {
    try {
        if (scanner != null) {
            scanner.close();
        }
    } catch (Exception e) {
        System.err.println("Erro ao fechar scanner: " + e.getMessage());
    }
}
    
    public boolean isEmAndamento() {
        return emAndamento;
    }
    
    public void pausarBatalha() {
        emAndamento = false;
    }
    
    public void continuarBatalha() {
        if (!verificarVencedor()) {
            emAndamento = true;
        }
    }
    
    public ListaEncadeada<Entidade> getParticipantes() {
        return participantes;
    }
}