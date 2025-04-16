package BATTLES.Arena;

import BATTLES.EstruturaDeDados.FilaEncadeada;
import BATTLES.EstruturaDeDados.ListaEncadeada;
import BATTLES.EstruturaDeDados.PilhaEncadeada;
import BATTLES.InventarioeItem.Item;
import BATTLES.PersonagemeMonstro.Entidade;
import BATTLES.PersonagemeMonstro.Habilidade;
//import BATTLES.PersonagemeMonstro.Jogador;
import BATTLES.PersonagemeMonstro.Monstro;
import BATTLES.PersonagemeMonstro.Personagem;

import java.util.Random;
import java.util.Scanner;

// Otto
public class Arena {
    private int idBatalha;
    private ListaEncadeada<Entidade> participantes;
    private FilaEncadeada<Entidade> turnos;
    private PilhaEncadeada<Entidade> eliminados;
    private boolean emAndamento;
    private int turnoAtual;
    private Scanner scanner;
    private static int contadorBatalhas = 0;

    public Arena() {
        this.idBatalha = ++contadorBatalhas;
        this.participantes = new ListaEncadeada<>();
        this.turnos = new FilaEncadeada<>();
        this.eliminados = new PilhaEncadeada<>();
        this.emAndamento = false;
        this.turnoAtual = 0;
        this.scanner = new Scanner(System.in);
    }

    public void iniciarBatalha(ListaEncadeada<Entidade> participantes) {
        this.participantes = participantes;
        this.emAndamento = true;
        this.turnoAtual = 1;

        
        ListaEncadeada<Entidade> temp = new ListaEncadeada<>();
        for (int i = 0; i < participantes.tamanho(); i++) {
            temp.adicionar(participantes.get(i));
        }
        Random rand = new Random();
        while (temp.tamanho() > 0) {
            int index = rand.nextInt(temp.tamanho());
            turnos.enqueue(temp.get(index));
            temp.remover(index);
        }

        System.out.println("Batalha #" + idBatalha + " iniciada com " + participantes.tamanho() + " participantes!");
    }

    public void executarTurno() {
        if (!emAndamento) {
            return;
        }

        Entidade atual = null;
        while (!turnos.isEmpty()) {
            atual = turnos.dequeue();
            if (atual.estaVivo()) {
                break;
            }
            atual = null;
        }

        if (atual == null) {
            emAndamento = false;
            return;
        }

        
        System.out.println("\n=== Turno " + turnoAtual + " ===");
        System.out.println("Vez de " + atual.getNome());
        if (atual instanceof Personagem) {
            Personagem p = (Personagem) atual;
            System.out.printf("Nível: %d (%.1f%%), HP: %d/%d, Mana: %d/%d%n",
                    p.getNivel(), p.getPorcentagemExperiencia(),
                    p.getVidaAtual(), p.getVidaMaxima(),
                    p.getManaAtual(), p.getManaMaxima());
        } else {
            System.out.println("HP: " + atual.getVidaAtual() + "/" + atual.getVidaMaxima());
        }

        if (atual instanceof Personagem) {
            executarAcaoJogador((Personagem) atual);
        } else if (atual instanceof Monstro) {
            Entidade alvo = escolherAlvoAleatorio(atual);
            if (alvo != null) {
                atual.atacar(alvo);
            }
        }

        for (int i = participantes.tamanho() - 1; i >= 0; i--) {
            Entidade e = participantes.get(i);
            if (!e.estaVivo()) {
                System.out.println(e.getNome() + " foi derrotado!");
                eliminados.push(e);
                participantes.remover(i);
                if (e instanceof Personagem && ((Personagem)e).getJogador() != null) {
                    emAndamento = false;
                    System.out.println("Game Over! Seu personagem foi derrotado.");
                    return;
                }
            }
        }

        if (atual.estaVivo()) {
            turnos.enqueue(atual);
        }

        if (participantes.tamanho() <= 1 || !jogadorEstaVivo()) {
            emAndamento = false;
        }

        turnoAtual++;
    }

    private boolean jogadorEstaVivo() {
        for (int i = 0; i < participantes.tamanho(); i++) {
            Entidade e = participantes.get(i);
            if (e instanceof Personagem && ((Personagem)e).getJogador() != null && e.estaVivo()) {
                return true;
            }
        }
        return false;
    }

    private void executarAcaoJogador(Personagem personagem) {
        System.out.println("\nEscolha sua ação:");
        System.out.println("1. Atacar");
        System.out.println("2. Usar habilidade");
        System.out.println("3. Usar item");
        System.out.println("4. Defender");
        System.out.print("Opção: ");

        int opcao = lerInteiro(1, 4);

        switch (opcao) {
            case 1:
                Entidade alvo = escolherAlvoJogador(personagem);
                if (alvo != null) {
                    personagem.atacar(alvo);
                }
                break;
            case 2:
                usarHabilidade(personagem);
                break;
            case 3:
                usarItem(personagem);
                break;
            case 4:
                personagem.curar(5);
                System.out.println(personagem.getNome() + " está se defendendo!");
                break;
        }
    }

    private Entidade escolherAlvoJogador(Entidade personagem) {
        ListaEncadeada<Entidade> alvos = new ListaEncadeada<>();
        for (int i = 0; i < participantes.tamanho(); i++) {
            Entidade e = participantes.get(i);
            if (e != personagem && e.estaVivo()) {
                alvos.adicionar(e);
            }
        }

        if (alvos.tamanho() == 0) {
            System.out.println("Nenhum alvo disponível!");
            return null;
        }

        System.out.println("\nEscolha seu alvo:");
        for (int i = 0; i < alvos.tamanho(); i++) {
            Entidade e = alvos.get(i);
            System.out.println((i + 1) + ". " + e.getNome() + " (HP: " + e.getVidaAtual() + "/" + e.getVidaMaxima() + ")");
        }
        System.out.print("Escolha: ");
        int index = lerInteiro(1, alvos.tamanho()) - 1;
        return alvos.get(index);
    }

    private Entidade escolherAlvoAleatorio(Entidade personagem) {
        for (int i = 0; i < participantes.tamanho(); i++) {
            Entidade e = participantes.get(i);
            if (e instanceof Personagem && ((Personagem)e).getJogador() != null && e.estaVivo()) {
                return e;
            }
        }
        return null;
    }

    private void usarHabilidade(Personagem personagem) {
        ListaEncadeada<Habilidade> habilidades = personagem.getHabilidades();
        if (habilidades.tamanho() == 0) {
            System.out.println("Nenhuma habilidade disponível!");
            return;
        }

        System.out.println("\nHabilidades disponíveis:");
        for (int i = 0; i < habilidades.tamanho(); i++) {
            Habilidade h = habilidades.get(i);
            System.out.println((i + 1) + ". " + h.getNome() + " (Dano: " + h.getDano() + ", Custo: " + h.getCustoMana() + ")");
        }
        System.out.print("Escolha uma habilidade: ");
        int index = lerInteiro(1, habilidades.tamanho()) - 1;
        Habilidade habilidade = habilidades.get(index);

        
        if (habilidade.getNome().toLowerCase().contains("cura") || habilidade.getDano() <= 0) {
            personagem.usarHabilidade(habilidade, personagem);
            System.out.println(personagem.getNome() + " usou " + habilidade.getNome() + " em si mesmo!");
        } else {
            Entidade alvo = escolherAlvoJogador(personagem);
            if (alvo != null) {
                personagem.usarHabilidade(habilidade, alvo);
            }
        }
    }

    private void usarItem(Personagem personagem) {
        ListaEncadeada<Item> itens = personagem.getInventario().getItens();
        if (itens.tamanho() == 0) {
            System.out.println("Nenhum item disponível!");
            return;
        }

        System.out.println("\nItens disponíveis:");
        for (int i = 0; i < itens.tamanho(); i++) {
            Item item = itens.get(i);
            System.out.println((i + 1) + ". " + item.getNome() + " (" + item.getDescricao() + ") x" + item.getQuantidade());
        }
        System.out.print("Escolha um item: ");
        int index = lerInteiro(1, itens.tamanho()) - 1;
        Item item = itens.get(index);

        if (item.getNome().equals("Poção de Cura") || item.getNome().equals("Poção de Mana")) {
            personagem.usarItem(index, personagem);
            System.out.println(personagem.getNome() + " usou " + item.getNome() + " em si mesmo!");
        } else {
            Entidade alvo = escolherAlvoJogador(personagem);
            if (alvo != null) {
                personagem.usarItem(index, alvo);
            }
        }
    }

    public boolean isEmAndamento() {
        return emAndamento && (participantes.tamanho() > 1 || jogadorEstaVivo());
    }

    public void finalizar() {
        if (emAndamento) {
            emAndamento = false;
        }

        System.out.println("\n=== Ranking Final da Batalha #" + idBatalha + " ===");
        int posicao = 1;

        while (!eliminados.isEmpty()) {
            Entidade e = eliminados.pop();
            System.out.println(posicao++ + "º lugar: " + e.getNome() + " (Derrotado)");
        }

        for (int i = 0; i < participantes.tamanho(); i++) {
            Entidade e = participantes.get(i);
            System.out.println(posicao++ + "º lugar: " + e.getNome() + " (Vencedor)");
        }

        for (int i = 0; i < participantes.tamanho(); i++) {
            Entidade e = participantes.get(i);
            e.setVidaAtual(e.getVidaMaxima());
            if (e instanceof Personagem) {
                ((Personagem)e).setManaAtual(((Personagem)e).getManaMaxima());
            }
        }
    }

    private int lerInteiro(int min, int max) {
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