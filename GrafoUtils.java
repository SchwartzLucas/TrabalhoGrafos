// Aluno: Lucas Schwartz de Souza

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class GrafoUtils {

    public static void main(String[] args) {
        int[][] matriz = {
            {0, 1, 1, 0},
            {1, 0, 1, 1},
            {1, 1, 0, 1},
            {0, 1, 1, 0}
        };

        System.out.println(tipoDoGrafo(matriz));
        System.out.println(arestasDoGrafo(matriz));
        System.out.println(grausDoVertice(matriz));
        System.out.println(buscaEmProfundidade(matriz));
    }

    private static boolean ehDirigido(int[][] matriz) {
        int n = matriz.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matriz[i][j] != matriz[j][i]) {
                    return true;
                }
            }
        }
        return false;
    }

    // Método 1: tipo do grafo
    public static String tipoDoGrafo(int[][] matriz) {
        int n = matriz.length;
        boolean dirigido = ehDirigido(matriz);
        boolean multigrafo = false;
        boolean completo = true;
        boolean nulo = true;

        int[] grau = new int[n];
        int[] outDeg = new int[n];
        int[] inDeg = new int[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = matriz[i][j];
                if (val != 0) nulo = false;
                if (val > 1) multigrafo = true;
                if (i == j && val > 0) multigrafo = true;
                if (i != j && val == 0) completo = false;

                if (dirigido) {
                    outDeg[i] += val;
                    inDeg[j] += val;
                } else {
                    grau[i] += val;
                    if (i == j) grau[i] += val; // laço conta +2
                }
            }
        }

        boolean regular;
        if (dirigido) {
            regular = Arrays.stream(inDeg).allMatch(x -> x == inDeg[0]) &&
                      Arrays.stream(outDeg).allMatch(x -> x == outDeg[0]);
        } else {
            regular = Arrays.stream(grau).allMatch(x -> x == grau[0]);
        }

        List<String> partes = new ArrayList<>();
        partes.add(dirigido ? "Dirigido" : "Não-dirigido");
        partes.add(multigrafo ? "Multigrafo" : "Simples");
        partes.add(regular ? "Regular" : "Não-regular");
        partes.add(completo ? "Completo" : "Não-completo");
        partes.add(nulo ? "Nulo" : "Não-nulo");

        return "Tipo: " + String.join(", ", partes);
    }

    // Método 2: arestas
    public static String arestasDoGrafo(int[][] matriz) {
        int n = matriz.length;
        boolean dirigido = ehDirigido(matriz);
        List<String> arestas = new ArrayList<>();
        int total = 0;

        if (dirigido) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < matriz[i][j]; k++) {
                        arestas.add(formatVertice(i) + "->" + formatVertice(j));
                        total++;
                    }
                }
            }
        } else {
            for (int i = 0; i < n; i++) {
                for (int j = i; j < n; j++) {
                    for (int k = 0; k < matriz[i][j]; k++) {
                        arestas.add("(" + formatVertice(i) + "," + formatVertice(j) + ")");
                        total++;
                    }
                }
            }
        }

        return "Quantidade de arestas: " + total + "\nArestas: " +
               (arestas.isEmpty() ? "Nenhuma" : String.join(", ", arestas));
    }

    // Método 3: graus
    public static String grausDoVertice(int[][] matriz) {
        int n = matriz.length;
        boolean dirigido = ehDirigido(matriz);
        StringBuilder sb = new StringBuilder();

        if (dirigido) {
            int[] inDeg = new int[n];
            int[] outDeg = new int[n];
            int total = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    outDeg[i] += matriz[i][j];
                    inDeg[j] += matriz[i][j];
                    total += matriz[i][j];
                }
            }
            sb.append("Total de arestas: ").append(total).append("\n");
            for (int i = 0; i < n; i++) {
                sb.append(formatVertice(i)).append(": out = ").append(outDeg[i])
                  .append(", in = ").append(inDeg[i]).append("\n");
            }
            sb.append("Sequência de out-degrees: ").append(Arrays.toString(outDeg)).append("\n");
            sb.append("Sequência de in-degrees: ").append(Arrays.toString(inDeg)).append("\n");
        } else {
            int[] grau = new int[n];
            int soma = 0;
            for (int i = 0; i < n; i++) {
                int g = 0;
                for (int j = 0; j < n; j++) {
                    g += matriz[i][j];
                    if (i == j) g += matriz[i][j];
                }
                grau[i] = g;
                soma += g;
            }
            int total = soma / 2;
            sb.append("Total de arestas: ").append(total).append("\n");
            for (int i = 0; i < n; i++) {
                sb.append(formatVertice(i)).append(": grau = ").append(grau[i]).append("\n");
            }
            sb.append("Sequência de graus: ").append(Arrays.toString(grau)).append("\n");
        }
        return sb.toString();
    }

    // Método 4: DFS
    public static String buscaEmProfundidade(int[][] matriz) {
        int n = matriz.length;
        boolean[] visitado = new boolean[n];
        List<String> ordem = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                dfs(i, matriz, visitado, ordem);
            }
        }

        return "Ordem da DFS: " + (ordem.isEmpty() ? "Nenhum" : String.join(" -> ", ordem));
    }

    private static void dfs(int v, int[][] matriz, boolean[] visitado, List<String> ordem) {
        visitado[v] = true;
        ordem.add(formatVertice(v));
        for (int j = 0; j < matriz.length; j++) {
            if (matriz[v][j] > 0 && !visitado[j]) {
                dfs(j, matriz, visitado, ordem);
            }
        }
    }

    private static String formatVertice(int idx) {
        if (idx >= 0 && idx < 26) return String.valueOf((char)('A' + idx));
        return String.valueOf(idx);
    }
}