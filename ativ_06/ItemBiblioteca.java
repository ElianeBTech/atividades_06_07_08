/**
 * Classe base de todo item do acervo.
 * Prazo e multa variam por tipo de item, por isso são métodos abstratos.
 */
public abstract class ItemBiblioteca {

    private final int codigo;
    private final String titulo;
    private boolean disponivel;

    public ItemBiblioteca(int codigo, String titulo) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.disponivel = true;
    }

    // ---- Métodos abstratos: cada subclasse define o próprio valor ----
    public abstract int getPrazoDias();

    public abstract double getMultaPorDia();

    public abstract String getTipo();

    // ---- Comportamento comum, que usa os métodos abstratos (polimorfismo) ----
    public double calcularMulta(int diasComItem) {
        int diasAtraso = diasComItem - getPrazoDias();
        return diasAtraso > 0 ? diasAtraso * getMultaPorDia() : 0.0;
    }

    /*
     * Não existe setDisponivel() público. A disponibilidade só muda como
     * consequência de um empréstimo ou devolução, feitos pela classe
     * Biblioteca (mesmo pacote) - por isso estes métodos são package-private.
     */
    void marcarEmprestado() {
        this.disponivel = false;
    }

    void marcarDevolvido() {
        this.disponivel = true;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.forLanguageTag("pt-BR"), "[%d] %-8s %-32s prazo: %2d dias | multa: R$ %.2f/dia | %s",
                codigo, getTipo(), titulo, getPrazoDias(), getMultaPorDia(),
                disponivel ? "disponível" : "emprestado");
    }
}
