package BATTLES;

import java.util.Random;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class Arena {
    private FilaEncadeada<Entidade> ordemTurnos;
    private PilhaEncadeada<Entidade> eliminados;
    private ListaEncadeada<Entidade> participantes;
    private int turnoAtual;
    private boolean emAndamento;
    
    public Arena() {
        this.ordemTurnos = new FilaEncadeada<>();
        this.eliminados = new PilhaEncadeada<>();
        this.participantes = new ListaEncadeada<>();
        this.turnoAtual = 0;
        this.emAndamento = false;
    }
    
    public void iniciarBatalha(ListaEncadeada<Entidade> participantes) {
        this.participantes = participantes;
        
        // Adiciona todos os participantes vivos na fila de turnos
        for (int i = 0; i < participantes.tamanho(); i++) {
            Entidade e = participantes.get(i);
            if (e.estaVivo()) {
                ordemTurnos.enqueue(e);
            }
        }
        
        // Ordem aleatória para mais dinamismo
        embaralharOrdemTurnos();
        
        turnoAtual = 1;
        emAndamento = true;
        System.out.println("Batalha iniciada com " + ordemTurnos.size + " participantes!");
    }
    
    private void embaralharOrdemTurnos() {
        // Converte a fila para array para embaralhar
        Entidade[] array = new Entidade[ordemTurnos.size];
        int index = 0;
        while (!ordemTurnos.isEmpty()) {
            array[index++] = ordemTurnos.dequeue();
        }
        
        // Embaralha o array
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            Entidade temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
        
        // Recria a fila com a ordem embaralhada
        for (Entidade e : array) {
            ordemTurnos.enqueue(e);
        }
    }
    
    public void executarTurno() {
        if (!emAndamento || ordemTurnos.isEmpty()) {
            return;
        }
        
        System.out.println("\n=== Turno " + turnoAtual + " ===");
        
        // Pega o próximo da fila
        Entidade atuante = ordemTurnos.dequeue();
        
        if (!atuante.estaVivo()) {
            System.out.println(atuante.getNome() + " está morto e não pode agir.");
            return;
        }
        
        System.out.println("Vez de " + atuante.getNome());
        System.out.println("HP: " + atuante.getVidaAtual() + "/" + atuante.getVidaMaxima());
        
        // Se for um Personagem (jogador), mostra menu de ações
        if (atuante instanceof Personagem) {
            executarTurnoJogador((Personagem) atuante);
        } else {
            // Se for monstro, ação automática
            executarTurnoMonstro(atuante);
        }
        
        // Verifica se a batalha terminou
        if (verificarVencedor()) {
            emAndamento = false;
            anunciarVencedor();
        }
        
        turnoAtual++;
    }
    
    private void executarTurnoJogador(Personagem jogador) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\nEscolha sua ação:");
        System.out.println("1. Atacar");
        System.out.println("2. Usar habilidade");
        System.out.println("3. Usar item");
        System.out.println("4. Defender");
        System.out.print("Opção: ");
        
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Limpar buffer
        
        Entidade alvo = escolherAlvo(jogador);
        
        switch (opcao) {
            case 1:
                jogador.atacar(alvo);
                System.out.println(jogador.getNome() + " atacou " + alvo.getNome() + "!");
                break;
                
            case 2:
                if (jogador.getHabilidades().tamanho() > 0) {
                    System.out.println("\nHabilidades disponíveis:");
                    for (int i = 0; i < jogador.getHabilidades().tamanho(); i++) {
                        Habilidade h = jogador.getHabilidades().get(i);
                        System.out.println((i+1) + ". " + h.getNome() + 
                                         " (Dano: " + h.getDano() + 
                                         ", Custo: " + h.getCustoMana() + ")");
                    }
                    System.out.print("Escolha uma habilidade: ");
                    int habilidadeEscolhida = scanner.nextInt() - 1;
                    scanner.nextLine();
                    
                    if (habilidadeEscolhida >= 0 && habilidadeEscolhida < jogador.getHabilidades().tamanho()) {
                        Habilidade h = jogador.getHabilidades().get(habilidadeEscolhida);
                        jogador.usarHabilidade(h, alvo);
                        System.out.println(jogador.getNome() + " usou " + h.getNome() + 
                                         " em " + alvo.getNome() + "!");
                    }
                } else {
                    System.out.println("Você não tem habilidades disponíveis!");
                }
                break;
                
            case 3:
                // Implementar uso de itens
                System.out.println("Itens ainda não implementados!");
                break;
                
            case 4:
                // Implementar defesa
                System.out.println(jogador.getNome() + " está se defendendo!");
                jogador.curar(5); // Defesa simples cura um pouco
                break;
                
            default:
                System.out.println("Ação inválida! Perdeu o turno.");
        }
        
        // Verifica se o alvo foi derrotado
        if (!alvo.estaVivo()) {
            System.out.println(alvo.getNome() + " foi derrotado!");
            eliminados.push(alvo);
            
            // Se o alvo era um monstro, concede experiência
            if (alvo instanceof Monstro) {
                int exp = ((Monstro)alvo).getExperienciaConcedida();
                jogador.ganharExperiencia(exp);
                System.out.println(jogador.getNome() + " ganhou " + exp + " pontos de experiência!");
            }
        }
        
        // Se o jogador ainda está vivo, volta para o final da fila
        if (jogador.estaVivo()) {
            ordemTurnos.enqueue(jogador);
        } else {
            System.out.println(jogador.getNome() + " foi derrotado!");
            eliminados.push(jogador);
        }
    }
    private void executarTurnoMonstro(Entidade monstro) {
        Entidade alvo = escolherAlvo(monstro);
        monstro.atacar(alvo);
        System.out.println(monstro.getNome() + " atacou " + alvo.getNome() + "!");
        
        // Verifica se o alvo foi derrotado
        if (!alvo.estaVivo()) {
            System.out.println(alvo.getNome() + " foi derrotado!");
            eliminados.push(alvo);
        }
        
        // Se o monstro ainda está vivo, volta para o final da fila
        if (monstro.estaVivo()) {
            ordemTurnos.enqueue(monstro);
        } else {
            System.out.println(monstro.getNome() + " foi derrotado!");
            eliminados.push(monstro);
        }
    }
    
    private Entidade escolherAlvo(Entidade atuante) {
        // Implementação melhorada para escolha de alvos
        
        // Se for um monstro, escolhe aleatoriamente um jogador vivo
        if (atuante instanceof Monstro) {
            ListaEncadeada<Entidade> alvosPossiveis = new ListaEncadeada<>();
            for (int i = 0; i < participantes.tamanho(); i++) {
                Entidade e = participantes.get(i);
                if (e.estaVivo() && e != atuante && e instanceof Personagem) {
                    alvosPossiveis.adicionar(e);
                }
            }
            
            if (alvosPossiveis.tamanho() == 0) {
                // Caso não haja alvos (não deveria acontecer)
                return participantes.get(0);
            }
            
            Random rand = new Random();
            return alvosPossiveis.get(rand.nextInt(alvosPossiveis.tamanho()));
        }
        
        // Se for um jogador, mostra menu para escolher alvo
        System.out.println("\nEscolha seu alvo:");
        int index = 1;
        ListaEncadeada<Entidade> alvosDisponiveis = new ListaEncadeada<>();
        
        for (int i = 0; i < participantes.tamanho(); i++) {
            Entidade e = participantes.get(i);
            if (e.estaVivo() && e != atuante) {
                System.out.println(index + ". " + e.getNome() + 
                                 " (HP: " + e.getVidaAtual() + "/" + e.getVidaMaxima() + ")");
                alvosDisponiveis.adicionar(e);
                index++;
            }
        }
        
        Scanner scanner = new Scanner(System.in);
        System.out.print("Opção: ");
        int escolha = scanner.nextInt() - 1;
        scanner.nextLine();
        
        if (escolha >= 0 && escolha < alvosDisponiveis.tamanho()) {
            return alvosDisponiveis.get(escolha);
        } else {
            System.out.println("Alvo inválido! Atacando o primeiro alvo disponível.");
            return alvosDisponiveis.get(0);
        }
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
            // Verifica se o vencedor é um jogador
            if (ultimoVivo instanceof Personagem) {
                Personagem vencedor = (Personagem) ultimoVivo;
                System.out.println("\nParabéns! " + vencedor.getNome() + " venceu a batalha!");
                
                // Recompensas
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
        Entidade vencedor = null;
        
        for (int i = 0; i < participantes.tamanho(); i++) {
            Entidade e = participantes.get(i);
            if (e.estaVivo()) {
                vencedor = e;
                break;
            }
        }
        
        if (vencedor != null) {
            System.out.println("\n=== " + vencedor.getNome() + " venceu a batalha! ===");
            eliminados.push(vencedor); // O vencedor é o último na pilha
        }
        
        exibirRankingFinal();
    }
    
    public void exibirRankingFinal() {
        System.out.println("\n=== Ranking Final ===");
        
        // Cria uma cópia da pilha para não alterar a original
        PilhaEncadeada<Entidade> copia = new PilhaEncadeada<>();
        ListaEncadeada<Entidade> ranking = new ListaEncadeada<>();
        
        while (!eliminados.isEmpty()) {
            Entidade e = eliminados.pop();
            ranking.adicionar(e);
            copia.push(e);
        }
        
        // Restaura a pilha original
        while (!copia.isEmpty()) {
            eliminados.push(copia.pop());
        }
        
        // Exibe do primeiro eliminado ao vencedor
        for (int i = ranking.tamanho() - 1; i >= 0; i--) {
            Entidade e = ranking.get(i);
            String status = e.estaVivo() ? "Vencedor" : "Derrotado";
            System.out.println((ranking.tamanho() - i) + "º lugar: " + 
                              e.getNome() + " (" + status + ")");
        }
    }
    
    public boolean isEmAndamento() {
        return emAndamento;
    }
    
    // Métodos adicionais para melhor controle
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