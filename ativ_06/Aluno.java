public class Aluno extends Usuario {

    private final String matricula;

    public Aluno(String nome, String matricula) {
        super(nome);
        this.matricula = matricula;
    }

    @Override
    public int getLimiteItens() {
        return 3;
    }

    @Override
    public String getTipo() {
        return "Aluno";
    }

    public String getMatricula() {
        return matricula;
    }
}
