public class Professor extends Usuario {

    private final String departamento;

    public Professor(String nome, String departamento) {
        super(nome);
        this.departamento = departamento;
    }

    @Override
    public int getLimiteItens() {
        return 5;
    }

    @Override
    public String getTipo() {
        return "Professor";
    }

    public String getDepartamento() {
        return departamento;
    }
}
