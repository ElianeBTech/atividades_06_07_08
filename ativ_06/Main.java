public class Main {

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca(10);

        // ---- Cadastro do acervo ----
        biblioteca.cadastrarItem(new Livro(1, "Dom Casmurro", "Machado de Assis"));
        biblioteca.cadastrarItem(new Livro(2, "O Cortiço", "Aluísio Azevedo"));
        biblioteca.cadastrarItem(new Livro(3, "Use a Cabeça! Java", "Kathy Sierra"));
        biblioteca.cadastrarItem(new Revista(4, "Superinteressante", 450));
        biblioteca.cadastrarItem(new Revista(5, "Galileu", 389));

        // ---- Cadastro de usuários ----
        Usuario aluno = new Aluno("Ana", "2026001");
        Usuario professor = new Professor("Carlos", "Computação");

        System.out.println("== Acervo inicial ==");
        biblioteca.listarAcervo();

        // ---- Empréstimo bem-sucedido ----
        System.out.println("\n== Empréstimos ==");
        biblioteca.emprestar(1, aluno);
        biblioteca.emprestar(4, aluno);
        biblioteca.emprestar(2, aluno);
        System.out.println(aluno);

        // ---- Empréstimo recusado por limite atingido (aluno: máx. 3) ----
        biblioteca.emprestar(5, aluno);

        // O mesmo item é aceito para o professor (limite 5)
        biblioteca.emprestar(5, professor);
        System.out.println(professor);

        System.out.println("\n== Acervo após empréstimos ==");
        biblioteca.listarAcervo();

        // ---- Devoluções: no prazo e com atraso ----
        System.out.println("\n== Devoluções ==");
        biblioteca.devolver(1, 10); // Livro, 10 dias: no prazo         -> R$ 0,00
        biblioteca.devolver(4, 10); // Revista, 3 dias de atraso x 1,00 -> R$ 3,00
        biblioteca.devolver(2, 18); // Livro, 4 dias de atraso x 0,50   -> R$ 2,00
        System.out.println(aluno);

        // Com itens devolvidos, o aluno volta a poder pegar emprestado
        biblioteca.emprestar(3, aluno);

        System.out.println("\n== Acervo final ==");
        biblioteca.listarAcervo();
    }
}
