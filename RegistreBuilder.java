import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class RegistreBuilder extends Task {

    int vertices;
    int edges;
    int[][] matrix;
    int colours;
    int counter1 = 0;
    int counter2 = 0;
    int counter3 = 0;
    String decipher;
    int variables;

    List<Integer> clauses = new ArrayList<>();
    List<Integer> correct = new ArrayList<>();

    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        formulateOracleQuestion();
        askOracle();
        decipherOracleAnswer();
        writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {

        Scanner scanner = new Scanner(System.in);

        this.vertices = scanner.nextInt();
        this.edges = scanner.nextInt();
        this.colours = scanner.nextInt();
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
    //first condition: complexity O(k * n)
            for (int i = 1; i <= vertices; i++) {
                for (int v = 1; v <= colours; v++) {
                    clauses.add(i * 10 + v);
                    if (v == colours) {
                        clauses.add(0);
                        counter1++;
                    }
                }
            }
    //second condition: complexity O(k ^2 * n)
            for (int v = 1; v <= vertices; v++) {
                for (int i = 1; i < colours; i++) {
                    for (int j = i + 1; j <= colours; j++) {
                        if (i != j) {
                            clauses.add((-1) * (v * 10 + i));
                            clauses.add((-1) * (v * 10 + j));
                            clauses.add(0);
                            counter2++;
                        }
                    }
                }
            }
    //third condition: complexity O(k * n^2)
            for (int v = 1; v < vertices; v++) {
                for (int w = v + 1; w <= vertices; w++) {
                    for (int i = 1; i <= colours; i++) {
                        if (v != w && matrix[v][w] == 1) {
                            clauses.add((-1) * (v * 10 + i));
                            clauses.add((-1) * (w * 10 + i));
                            clauses.add(0);
                            counter3++;
                        }
                    }
                }
            }

            FileWriter fileWriter = new FileWriter("sat.cnf");
            fileWriter.write("p cnf ");
            int variables = vertices * colours;
            fileWriter.write(variables + " ");
            int clausesNr = counter1 + counter2 + counter3;
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
        File file = new File("sat.sol");
        Scanner scanner = new Scanner(file);

        decipher = scanner.nextLine();
        if (Objects.equals(decipher, "True")) {
            variables = scanner.nextInt();
            for (int i = 0; i < variables; i++) {
                int element = scanner.nextInt();
                if (element > 0) {
                    correct.add(element);
                }
            }
        }
    }

    @Override
    public void writeAnswer() throws IOException {
        if (Objects.equals(decipher, "False")) {
            System.out.println("False");
        } else {
            System.out.println("True");
            for (int i = 0; i < correct.size(); i++) {
                System.out.print(correct.get(i) % 10 + " ");
            }
        }
    }
}
