public class App {
    public static void main(String[] args) throws Exception {
        EstatisticasEscolares teste = new EstatisticasEscolares();

        teste.lerArquivo("Resources\\teste.txt");

        //teste.calcularEstatisticasPorAno();

        teste.calcularEstatisticasPorDisciplina();
    }
}
