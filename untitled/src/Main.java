// Mariana Galvão Soares

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Obter o número de filas e clientes
        System.out.print("Digite o número de filas (recursos): ");
        int k = scanner.nextInt();

        System.out.print("Digite o número de clientes no sistema: ");
        int n = scanner.nextInt();

        // Inicializar arrays para taxas de visitação e tempos médios de serviço
        double[] vi = new double[k];
        double[] si = new double[k];

        // Obter as taxas de visitação
        System.out.println("Digite as taxas de visitação (V[i]) para cada fila:");
        for (int i = 0; i < k; i++) {
            System.out.print("V[" + (i + 1) + "]: ");
            vi[i] = scanner.nextDouble();
        }

        // Obter os tempos médios de serviço
        System.out.println("Digite os tempos médios de serviço (S[i]) para cada fila:");
        for (int i = 0; i < k; i++) {
            System.out.print("S[" + (i + 1) + "]: ");
            si[i] = scanner.nextDouble();
        }

        // Inicializar matrizes para os cálculos
        double[][] ni = new double[n + 1][k]; // Número médio de clientes por fila
        double[] ri = new double[k];          // Tempo médio de resposta por fila
        double r0 = 0;                        // Tempo médio de resposta do sistema
        double x0 = 0;                        // Throughput do sistema
        double[] utilizacao = new double[k];  // Vetor para armazenar a utilização de cada dispositivo

        // Inicializar número médio de clientes para 0 clientes no sistema
        for (int i = 0; i < k; i++) {
            ni[0][i] = 0;
        }

        // Executar o algoritmo MVA
        for (int clientes = 1; clientes <= n; clientes++) {
            // Calcular tempo de resposta de cada fila
            for (int i = 0; i < k; i++) {
                ri[i] = si[i] * (1 + ni[clientes - 1][i]);
            }

            // Calcular tempo de resposta do sistema
            r0 = 0;
            for (int i = 0; i < k; i++) {
                r0 += vi[i] * ri[i];
            }

            // Calcular throughput do sistema (garantindo que x0 esteja calculado)
            x0 = clientes / r0;

            // Calcular throughput de cada fila e número médio de clientes
            for (int i = 0; i < k; i++) {
                double xi = vi[i] * x0;        // Throughput de cada fila
                ni[clientes][i] = xi * ri[i]; // Número médio de clientes em cada fila
            }
        }

        // Calcular resultados adicionais
        double totalClientesNoSistema = 0;
        double totalUtilizacao = 0;
        double totalTempoDeResposta = 0;
        double totalTempoDeEspera = 0;

        System.out.println("\nResultados por fila:");

        // Exibir resultados por fila
        for (int i = 0; i < k; i++) {
            double ui = si[i] * (vi[i] * x0); // Utilização do servidor i (Ui = Si * Xi)
            utilizacao[i] = ui; // Armazenando a utilização de cada dispositivo

            double wi = ri[i] - si[i]; // Tempo médio de espera na fila i

            System.out.printf("\nFila %d:\n", i + 1);
            System.out.printf("  Número médio de clientes (N[%d]) = %.2f%n", n, ni[n][i]);
            System.out.printf("  Tempo médio de resposta (R[%d]) = %.2f%n", i + 1, ri[i]);
            System.out.printf("  Tempo médio de espera (W[%d]) = %.2f%n", i + 1, wi);
            System.out.printf("  Utilização do servidor (U[%d]) = %.2f%n", i + 1, ui);

            totalClientesNoSistema += ni[n][i]; // Número médio de clientes no sistema
            totalUtilizacao += ui; // Utilização total do sistema
            totalTempoDeResposta += vi[i] * ri[i]; // Tempo médio de resposta ponderado
            totalTempoDeEspera += vi[i] * wi; // Tempo médio de espera ponderado
        }

        // Exibir resultados gerais do sistema
        System.out.println("\nResultados do sistema todo:");
        System.out.printf("Tempo médio de resposta do sistema (R0): %.2f%n", totalTempoDeResposta);
        System.out.printf("Tempo médio de espera do sistema (W): %.2f%n", totalTempoDeEspera);
        System.out.printf("Utilização total do sistema (U): %.2f%n", totalUtilizacao);
        System.out.printf("Número médio de clientes no sistema (L): %.2f%n", totalClientesNoSistema);



        scanner.close();
    }
}
