import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstatisticasEscolares {
    // ano vai ser a chave, a nota vai ser o valor (nao sei se a nota pode ser
    // quebrada ou nao entao vou deixar assim)
    private Map<Integer, List<Double>> notasPorAno;
    // id vai ser a chave, deixei string pq sei la posso trocar pro nome do aluno
    // tambem
    private Map<String, List<Double>> notasPorAluno;
    private Map<String, List<Double>> notasPorDisciplina;

    public EstatisticasEscolares() {
        this.notasPorAno = new HashMap<>();
        this.notasPorAluno = new HashMap<>();
        this.notasPorDisciplina = new HashMap<>();

        notasPorDisciplina.put("Algoritmos", new ArrayList<>());
        notasPorDisciplina.put("POO", new ArrayList<>());
        notasPorDisciplina.put("Calculo", new ArrayList<>());
        notasPorDisciplina.put("ProjetoDeExtensao", new ArrayList<>());
    }

    public void lerArquivo(String caminhoArquivo) throws IOException {
        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;

            while ((linha = leitor.readLine()) != null) {
                // split define a virgula como delimitador para dividir a linha e pegar os
                // valores
                String[] dados = linha.split(",");
                String id = dados[0];
                double nota1 = Double.parseDouble(dados[1]);
                double nota2 = Double.parseDouble(dados[2]);
                double nota3 = Double.parseDouble(dados[3]);
                double nota4 = Double.parseDouble(dados[4]);
                int ano = Integer.parseInt(dados[5]);

                notasPorDisciplina.get("Algoritmos").add(nota1);
                notasPorDisciplina.get("POO").add(nota2);
                notasPorDisciplina.get("Calculo").add(nota3);
                notasPorDisciplina.get("ProjetoDeExtensao").add(nota4);

                double media = (nota1 + nota2 + nota3 + nota4) / 4;

                // computeIfAbsent verifica se o valor (no caso ano) já está definido, se nao
                // estiver cria um novo arraylist com k->new arraylist (obrigado youtubers
                // indianos)
                // dps disso a media eh adicionada a lista
                notasPorAno.computeIfAbsent(ano, k -> new ArrayList<>()).add(media);
                notasPorAluno.computeIfAbsent(id, k -> new ArrayList<>()).add(media);
            }
        }
    }

    private double calcularMedia(List<Double> notas) {
        // orElse funciona caso a Stream estiver vazia e não ter como calcular a média
        return notas.stream().mapToDouble(a -> a).average().orElse(0.0);
    }

    // nem sabia oq era isso, obrigado professor Ferretto vc eh um amor <3
    // https://youtu.be/2KjlM-5FVqA
    private double calcularMediana(List<Double> notas) {
        Collections.sort(notas);
        int tamanho = notas.size();
        if (tamanho % 2 == 0) {
            return (notas.get(tamanho / 2 - 1) + notas.get(tamanho / 2)) / 2.0;
        } else {
            return notas.get(tamanho / 2);
        }
    }

    private double calcularDesvioPadrao(List<Double> notas) {
        double media = calcularMedia(notas);
        // orElse funciona caso a Stream estiver vazia e não ter como calcular a média
        double variancia = notas.stream().mapToDouble(nota -> Math.pow(nota - media, 2)).average().orElse(0.0);
        return Math.sqrt(variancia);
    }

    public void calcularEstatisticasPorAno() {
        for (int ano : notasPorAno.keySet()) {
            List<Double> notas = notasPorAno.get(ano);
            double media = calcularMedia(notas);
            double mediana = calcularMediana(notas);
            double desvioPadrao = calcularDesvioPadrao(notas);
            System.out.println("Ano: " + ano);
            System.out.println("Média: " + media);
            System.out.println("Mediana: " + mediana);
            System.out.println("Desvio Padrão: " + desvioPadrao);
        }
    }

    public void calcularEstatisticasPorDisciplina() {
        for (String disciplina : notasPorDisciplina.keySet()) {
            List<Double> notas = notasPorDisciplina.get(disciplina);
            double media = calcularMedia(notas);
            double mediana = calcularMediana(notas);
            double desvioPadrao = calcularDesvioPadrao(notas);
            System.out.println("Disciplina: " + disciplina);
            System.out.println("Média: " + media);
            System.out.println("Mediana: " + mediana);
            System.out.println("Desvio Padrão: " + desvioPadrao);
        }
    }

    private double calcularMediaPorAluno(String id) {
        List<Double> notas = notasPorAluno.get(id);
        return calcularMedia(notas);
    }

    public void melhorDesempenho() {
        // iniciando a variavel dessa maneira faz ela ter o menor valor possivel que um
        // double pode ter, assim caso tenha valores negativos ainda irá funcionar
        double melhorMedia = Double.NEGATIVE_INFINITY;
        List<String> melhoresAlunos = new ArrayList<>();

        for (String aluno : notasPorAluno.keySet()) {
            double media = calcularMediaPorAluno(aluno);
            if (media > melhorMedia) {
                melhorMedia = media;
                melhoresAlunos.clear();
                melhoresAlunos.add(aluno);
            } else if (media == melhorMedia) {
                melhoresAlunos.add(aluno);
            }
        }

        System.out.println("Aluno(s) com o melhor média:");
        for (String aluno : melhoresAlunos) {
            System.out.println("Aluno: " + aluno + ", Média: " + melhorMedia);
        }
    }

    public void piorDesempenho() {
        // iniciando a variavel com o maior valor possivel para um double
        double piorMedia = Double.POSITIVE_INFINITY;
        List<String> pioresAlunos = new ArrayList<>();

        for (String aluno : notasPorAluno.keySet()) {
            double media = calcularMediaPorAluno(aluno);
            if (media < piorMedia) {
                piorMedia = media;
                pioresAlunos.clear();
                pioresAlunos.add(aluno);
            } else if (media == piorMedia) {
                pioresAlunos.add(aluno);
            }
        }

        System.out.println("Aluno(s) com o pior média:");
        for (String aluno : pioresAlunos) {
            System.out.println("Aluno: " + aluno + ", Média: " + piorMedia);
        }
    }

    public int quantidadeTotalAlunos() {
        return notasPorAluno.size();
    }

    public void classificarAlunosPorDisciplina() {
        for (String disciplina : notasPorDisciplina.keySet()) {
            List<Double> notas = notasPorDisciplina.get(disciplina);
            int aprovados = 0;
            int reprovados = 0;

            for (Double nota : notas) {
                if (nota >= 70) {
                    aprovados++;
                } else {
                    reprovados++;
                }
            }

            System.out.println("Disciplina: " + disciplina);
            System.out.println("Alunos aprovados: " + aprovados);
            System.out.println("Alunos reprovados: " + reprovados);
        }
    }

    public void criarTxtAprovadosReprovados(String pastaRetorno) throws IOException {
        try (PrintWriter printAprovados = new PrintWriter(new FileWriter(pastaRetorno + "/alunosAprovados.txt"));
                PrintWriter printReprovados = new PrintWriter(new FileWriter(pastaRetorno + "/alunos_reprovados.txt"))) {
            for (String id : notasPorAluno.keySet()) {
                List<Double> notas = notasPorAluno.get(id);
                boolean aprovado = true;

                for (Double nota : notas) {
                    if (nota < 70) {
                        aprovado = false;
                        break;
                    }
                }

                if (aprovado) {
                    printAprovados.println("ID do Aluno: " + id);
                    printAprovados.println("Notas: " + notas);
                    printAprovados.println();
                } else {
                    printReprovados.println("ID do Aluno: " + id);
                    printReprovados.println("Notas: " + notas);
                    printReprovados.println();
                }
            }
        }
    }
}