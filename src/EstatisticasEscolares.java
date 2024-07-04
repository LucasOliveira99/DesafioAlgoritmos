import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstatisticasEscolares {
    //ano vai ser a chave, a nota vai ser o valor (nao sei se a nota pode ser quebrada ou nao entao vou deixar assim)
    private Map<Integer, List<Double>> notasPorAno;
    //id vai ser a chave, deixei string pq sei la posso trocar pro nome do aluno tambem
    private Map<String, List<Double>> notasPorAluno;

    public EstatisticasEscolares() {
        this.notasPorAno = new HashMap<>();
        this.notasPorAluno = new HashMap<>();
    }

    public void lerArquivo(String caminhoArquivo) throws IOException {
        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;

            while ((linha = leitor.readLine()) != null) {
                                        //split define a virgula como delimitador para dividir a linha e pegar os valores
                String[] dados = linha.split(",");
                String id = dados[0];
                double nota1 = Double.parseDouble(dados[1]);
                double nota2 = Double.parseDouble(dados[2]);
                double nota3 = Double.parseDouble(dados[3]);
                double nota4 = Double.parseDouble(dados[4]);
                int ano = Integer.parseInt(dados[5]);
                double media = (nota1 + nota2 + nota3 + nota4) / 4;

                            //computeIfAbsent verifica se o valor (no caso ano) já está definido, se nao estiver cria um novo arraylist com k->new arraylist (obrigado youtubers indianos)
                            //dps disso a media eh adicionada a lista 
                notasPorAno.computeIfAbsent(ano, k -> new ArrayList<>()).add(media);
                notasPorAluno.computeIfAbsent(id, k -> new ArrayList<>()).add(media);
            }
        }
    }

    private double calcularMedia(List<Double> notas) {
                                                            //orElse funciona caso a Stream estiver vazia e não ter como calcular a média 
        return notas.stream().mapToDouble(a -> a).average().orElse(0.0);
    }

    //nem sabia oq era isso, obrigado professor Ferretto vc eh um amor <3 https://youtu.be/2KjlM-5FVqA
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
                                                                                                    //orElse funciona caso a Stream estiver vazia e não ter como calcular a média 
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
}
