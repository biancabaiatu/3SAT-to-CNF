import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ReclameBuilder extends Task {

    int vertices;
    int edges;
    int[][] matrix;
    int k;
    String decipher;
    int variables;

    List<Integer> clauses = new ArrayList<>();
    List<Integer> correct = new ArrayList<>();


    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        for (int i = 1; i <= vertices; i++) {
            k = i;
            clauses = new ArrayList<>();
            formulateOracleQuestion();
            askOracle();

            Scanner scanner = new Scanner(new BufferedReader(new FileReader("sat.sol")));
            decipher = scanner.nextLine();
            if (Objects.equals(decipher, "True")) {
                variables = scanner.nextInt();
                for (int j = 0; j < variables; j++) {
                    int element = scanner.nextInt();
                    if (element > 0) {
                        correct.add(element);
                    }
                }
                break;
            }
        }
        writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {
        Scanner scanner = new Scanner(System.in);

        this.vertices = scanner.nextInt();
        this.edges = scanner.nextInt();
        matrix = new int[vertices + 1][vertices + 1];

        for (int i = 1; i <= edges; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            matrix[u][v] = matrix[v][u] = 1;
        }
    }

    @Override
    public void formulateOracleQuestion() throws IOException {
        try {

            int counter1 = 0;
            int counter2 = 0;
            int counter3 = 0;
            int counter4 = 0;
    //first condition: complexity O(k * n)
            for (int i = 1; i <= k; i++) {
                for (int v = 1; v <= vertices; v++) {
                    clauses.add(v * 10 + i);
                    if (v == vertices) {
                        clauses.add(0);
                        counter1++;
                    }
                }
            }
    //second condition: complexity O(k^2 * n)
            for (int i = 1; i <= k; i++) {
                for (int j = 1; j <= k; j++) {
                    for (int v = 1; v <= vertices; v++) {
                        if (i != j) {
                            clauses.add((-1) * (v * 10 + i));
                            clauses.add((-1) * (v * 10 + j));
                            clauses.add(0);
                            counter3++;
                        }
                    }
                }
            }
    //third condition: complexity O(n^2 * k)
            for (int u = 1; u <= vertices; u++) {
                for (int v = 1; v <= vertices; v++) {
                    if (u != v && matrix[u][v] == 1) {
                        for (int i = 1; i <= k; i++) {
                            clauses.add(u * 10 + i);
                            clauses.add(v * 10 + i);
                        }
                        clauses.add(0);
                        counter3++;
                    }
                }
            }
    //fourth condition: complexity O(k * n^2)
            for (int i = 1; i <= k; i++) {
                for (int v = 1; v <= vertices; v++) {
                    for (int w = v + 1; w <= vertices; w++){
                        clauses.add((-1) * (v * 10 + i));
                        clauses.add((-1) * (w * 10 + i));
                        clauses.add(0);
                        counter4++;
                    }
                }
            }



            FileWriter fileWriter = new FileWriter("sat.cnf");
            fileWriter.write("p cnf ");
            int variables = vertices * k;
            fileWriter.write(variables + " ");
            int clausesNr = counter1 + counter2 + counter3 + counter4;
            fileWriter.write(String.valueOf(clausesNr));
            fileWriter.write("\n");

            for (int i = 0; i < clauses.size(); i++) {
                if (clauses.get(i) == 0) {
                    fileWriter.write(String.valueOf(clauses.get(i)) + '\n' );
                } else {
                    fileWriter.write(clauses.get(i) + " ");
                }
            }

            fileWriter.close();

        } catch (IOException e) {
            e.getStackTrace();
        }

    }

    @Override
    public void decipherOracleAnswer() throws IOException {

    }

    @Override
    public void writeAnswer() throws IOException {
        for (int i = 0; i < correct.size(); i++) {
            if (i == correct.size() - 1) {
                System.out.print(correct.get(i) / 10);
            } else {
                System.out.print(correct.get(i) / 10 + " ");
            }
        }
    }
}
