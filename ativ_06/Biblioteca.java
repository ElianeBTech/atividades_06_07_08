/**
 * Controla o acervo e os empréstimos usando arrays de tamanho fixo.
 */
public class Biblioteca {

    private final ItemBiblioteca[] acervo;
    // emprestadoPara[i] guarda quem está com acervo[i] (null se disponível)
    private final Usuario[] emprestadoPara;
    private int totalItens;

    public Biblioteca(int capacidade) {
        this.acervo = new ItemBiblioteca[capacidade];
        this.emprestadoPara = new Usuario[capacidade];
        this.totalItens = 0;
    }

    public boolean cadastrarItem(ItemBiblioteca item) {
        if (totalItens >= acervo.length) {
            System.out.println("Acervo cheio: não foi possível cadastrar \"" + item.getTitulo() + "\".");
            return false;
        }
        if (buscarIndice(item.getCodigo()) != -1) {
            System.out.println("Já existe um item com o código " + item.getCodigo() + ".");
            return false;
        }
        acervo[totalItens] = item;
        totalItens++;
        return true;
    }

    public boolean emprestar(int codigoItem, Usuario usuario) {
        int indice = buscarIndice(codigoItem);
        if (indice == -1) {
            System.out.println("RECUSADO: item " + codigoItem + " não existe no acervo.");
            return false;
        }

        ItemBiblioteca item = acervo[indice];
        if (!item.isDisponivel()) {
            System.out.println("RECUSADO: \"" + item.getTitulo() + "\" já está emprestado.");
            return false;
        }
        if (!usuario.podeEmprestar()) {
            System.out.println("RECUSADO: " + usuario.getNome() + " atingiu o limite de "
                    + usuario.getLimiteItens() + " itens (" + usuario.getTipo() + ").");
            return false;
        }

        item.marcarEmprestado();
        usuario.incrementarEmprestimos();
        emprestadoPara[indice] = usuario;

        System.out.println("OK: \"" + item.getTitulo() + "\" emprestado a " + usuario.getNome()
                + " - devolver em até " + item.getPrazoDias() + " dias.");
        return true;
    }

    /**
     * Devolve o item e calcula a multa com base nos dias que ficou com o usuário.
     * Retorna o valor da multa (0 se no prazo), ou -1 se a devolução não for possível.
     */
    public double devolver(int codigoItem, int diasComItem) {
        int indice = buscarIndice(codigoItem);
        if (indice == -1) {
            System.out.println("ERRO: item " + codigoItem + " não existe no acervo.");
            return -1;
        }

        ItemBiblioteca item = acervo[indice];
        if (item.isDisponivel()) {
            System.out.println("ERRO: \"" + item.getTitulo() + "\" não está emprestado.");
            return -1;
        }

        Usuario usuario = emprestadoPara[indice];
        double multa = item.calcularMulta(diasComItem); // polimórfico: Livro ou Revista

        item.marcarDevolvido();
        usuario.decrementarEmprestimos();
        emprestadoPara[indice] = null;

        System.out.printf(java.util.Locale.forLanguageTag("pt-BR"), "DEVOLVIDO: \"%s\" por %s após %d dias - multa: R$ %.2f%n",
                item.getTitulo(), usuario.getNome(), diasComItem, multa);
        return multa;
    }

    /** Percorre o acervo em um único laço, sem verificar o tipo de cada item. */
    public void listarAcervo() {
        System.out.println("---------------- ACERVO ----------------");
        for (int i = 0; i < totalItens; i++) {
            System.out.println(acervo[i]); // toString() usa getTipo/getPrazoDias/getMultaPorDia
        }
        System.out.println("----------------------------------------");
    }

    private int buscarIndice(int codigo) {
        for (int i = 0; i < totalItens; i++) {
            if (acervo[i].getCodigo() == codigo) {
                return i;
            }
        }
        return -1;
    }
}
