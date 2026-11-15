/*
    Alisson da Silva Pereira          Matricula: 2615050052
    João Gustavo Mendonça Nascimento  Matrícula: 2615050034
    José Belmont Neto                 Matrícula: 2615050065
    Richard Emmanuel Alves de Castro  Matricula: 2615050002
 */

import java.util.Scanner;

class Pedido {
    private int codigo;
    private String prato;
    private int mesa;
    private int tempoPreparo;

    public Pedido(int codigo, String prato, int mesa, int tempoPreparo) {
        this.codigo = codigo;
        this.prato = prato;
        this.mesa = mesa;
        this.tempoPreparo = tempoPreparo;
    }

    public int getCodigo() { return codigo; }
    public String getPrato() { return prato; }
    public int getMesa() { return mesa; }
    public int getTempoPreparo() { return tempoPreparo; }

    @Override
    public String toString() {
        return "{Pedido #" + codigo + " | Prato: " + prato
                + " | Mesa: " + mesa + " | Preparo: " + tempoPreparo + " min}";
    }
}

class FilaCircular {
    private Pedido[] dados;
    private int inicio;
    private int fim;
    private int tamanho;

    public FilaCircular(int capacidade) {
        this.dados = new Pedido[capacidade];
        this.inicio = 0;
        this.fim = 0;
        this.tamanho = 0;
    }

    public boolean estaVazia() { return tamanho == 0; }

    public boolean estaCheia() { return tamanho == dados.length; }

    // Adicionar no final da fila
    public boolean adicionar(Pedido p) {
        if (estaCheia()) {
            return false;
        }
        dados[fim] = p;
        fim = (fim + 1) % dados.length; // volta ao inicio do array
        tamanho++;
        return true;
    }

    // Remover o primeiro da fila
    public Pedido remover() {
        if (estaVazia()) {
            return null;
        }
        Pedido removido = dados[inicio];
        dados[inicio] = null;
        inicio = (inicio + 1) % dados.length;
        tamanho--;
        return removido;
    }

    // Remover um pedido especifico pelo codigo
    public Pedido removerPorCodigo(int codigo) {
        int pos = posicaoPorCodigo(codigo);
        if (pos == -1) {
            return null;
        }
        Pedido removido = obter(pos);
        for (int i = pos; i < tamanho - 1; i++) {
            dados[(inicio + i) % dados.length] = dados[(inicio + i + 1) % dados.length];
        }
        fim = (fim - 1 + dados.length) % dados.length;
        dados[fim] = null;
        tamanho--;
        return removido;
    }

    // Buscar pelo nome do prato
    public Pedido buscarPorPrato(String prato) {
        for (int i = 0; i < tamanho; i++) {
            Pedido p = obter(i);
            if (p.getPrato().equalsIgnoreCase(prato)) {
                return p;
            }
        }
        return null;
    }

    public void listar() {
        if (estaVazia()) {
            System.out.println("A fila esta vazia.");
            return;
        }
        for (int i = 0; i < tamanho; i++) {
            System.out.println((i + 1) + "o -> " + obter(i));
        }
    }

    public int contar() { return tamanho; }

    public int capacidade() { return dados.length; }

    // Posicao 1 = primeiro da fila
    public int posicaoPorPrato(String prato) {
        for (int i = 0; i < tamanho; i++) {
            if (obter(i).getPrato().equalsIgnoreCase(prato)) {
                return i + 1;
            }
        }
        return -1;
    }

    private int posicaoPorCodigo(int codigo) {
        for (int i = 0; i < tamanho; i++) {
            if (obter(i).getCodigo() == codigo) {
                return i;
            }
        }
        return -1;
    }

    public Pedido obter(int i) {
        if (i < 0 || i >= tamanho) {
            return null;
        }
        return dados[(inicio + i) % dados.length];
    }

    // Mostra a posicao, quem esta na frente e quem vem depois
    public void mostrarPosicao(String prato) {
        int pos = posicaoPorPrato(prato);
        if (pos == -1) {
            System.out.println("Pedido com o prato '" + prato + "' nao encontrado.");
            return;
        }
        Pedido atual = obter(pos - 1);
        Pedido frente = obter(pos - 2);
        Pedido atras = obter(pos);

        System.out.println("Item: " + atual);
        System.out.println("Posicao na fila: " + pos + "o de " + tamanho);
        System.out.println("Na frente dele: " + (frente != null ? frente : "ninguem (e o primeiro da fila)"));
        System.out.println("Depois dele:    " + (atras != null ? atras : "ninguem (e o ultimo da fila)"));
    }
}

public class Cozinha {
    private static int proximoCodigo = 1;

    public static void main(String[] args) {
        FilaCircular fila = new FilaCircular(10);
        Scanner sc = new Scanner(System.in);

        fila.adicionar(new Pedido(proximoCodigo++, "Feijoada", 3, 40));
        fila.adicionar(new Pedido(proximoCodigo++, "Lasanha", 5, 30));
        fila.adicionar(new Pedido(proximoCodigo++, "Strogonoff", 1, 25));
        fila.adicionar(new Pedido(proximoCodigo++, "Pizza", 7, 20));
        fila.adicionar(new Pedido(proximoCodigo++, "Omelete", 2, 10));

        int opcao;
        do {
            System.out.println("\n===== COZINHA - FILA DE PEDIDOS =====");
            System.out.println("1 - Adicionar pedido");
            System.out.println("2 - Remover proximo pedido (o primeiro da fila)");
            System.out.println("3 - Remover pedido especifico (por codigo)");
            System.out.println("4 - Buscar pedido pelo nome do prato");
            System.out.println("5 - Listar todos os pedidos");
            System.out.println("6 - Contar pedidos");
            System.out.println("7 - Ver posicao de um pedido (frente e depois)");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            opcao = lerInteiro(sc);

            switch (opcao) {
                case 1:
                    if (fila.estaCheia()) {
                        System.out.println("Fila cheia! Capacidade maxima: " + fila.capacidade());
                        break;
                    }
                    System.out.print("Prato: ");
                    String prato = sc.nextLine();
                    System.out.print("Mesa: ");
                    int mesa = lerInteiro(sc);
                    System.out.print("Tempo de preparo (min): ");
                    int tempo = lerInteiro(sc);
                    Pedido novo = new Pedido(proximoCodigo++, prato, mesa, tempo);
                    fila.adicionar(novo);
                    System.out.println("Adicionado: " + novo);
                    break;

                case 2:
                    Pedido removido = fila.remover();
                    System.out.println(removido != null
                            ? "Pedido removido: " + removido
                            : "A fila esta vazia.");
                    break;

                case 3:
                    System.out.print("Codigo do pedido: ");
                    int codigo = lerInteiro(sc);
                    Pedido rem = fila.removerPorCodigo(codigo);
                    System.out.println(rem != null
                            ? "Removido: " + rem
                            : "Pedido nao encontrado.");
                    break;

                case 4:
                    System.out.print("Nome do prato: ");
                    Pedido achado = fila.buscarPorPrato(sc.nextLine());
                    System.out.println(achado != null ? "Encontrado: " + achado : "Pedido nao encontrado.");
                    break;

                case 5:
                    System.out.println("--- Pedidos na fila ---");
                    fila.listar();
                    break;

                case 6:
                    System.out.println("Total de pedidos: " + fila.contar()
                            + " de " + fila.capacidade());
                    break;

                case 7:
                    System.out.print("Nome do prato: ");
                    fila.mostrarPosicao(sc.nextLine());
                    break;

                case 0:
                    System.out.println("Encerrando...");
                    break;

                default:
                    System.out.println("Opcao invalida.");
            }
        } while (opcao != 0);

        sc.close();
    }

    private static int lerInteiro(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Digite um numero valido: ");
            }
        }
    }
}
