/**
 * Classe base de quem pode pegar itens emprestados.
 * O limite de itens varia por tipo de usuário, por isso é método abstrato.
 */
public abstract class Usuario {

    private final String nome;
    private int quantidadeEmprestada;

    public Usuario(String nome) {
        this.nome = nome;
        this.quantidadeEmprestada = 0;
    }

    public abstract int getLimiteItens();

    public abstract String getTipo();

    public boolean podeEmprestar() {
        return quantidadeEmprestada < getLimiteItens();
    }

    // Alterados apenas pela Biblioteca (package-private), nunca diretamente.
    void incrementarEmprestimos() {
        quantidadeEmprestada++;
    }

    void decrementarEmprestimos() {
        if (quantidadeEmprestada > 0) {
            quantidadeEmprestada--;
        }
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidadeEmprestada() {
        return quantidadeEmprestada;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%d/%d itens)",
                getTipo(), nome, quantidadeEmprestada, getLimiteItens());
    }
}
