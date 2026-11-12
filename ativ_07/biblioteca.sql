-- =====================================================================
-- Sistema de Biblioteca - modelo relacional (PostgreSQL)
-- Sem herança: o tipo do item e do usuário é definido pela coluna "tipo".
-- =====================================================================

-- Permite rodar o script mais de uma vez.
-- A ordem do DROP é a inversa do CREATE (emprestimo depende das outras duas).
DROP TABLE IF EXISTS emprestimo;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS item;

-- ---------------------------------------------------------------------
-- 1) item: livros e revistas na mesma tabela
-- ---------------------------------------------------------------------
CREATE TABLE item (
    id          INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    codigo      VARCHAR(20)  NOT NULL UNIQUE,
    titulo      VARCHAR(200) NOT NULL,
    tipo        VARCHAR(10)  NOT NULL CHECK (tipo IN ('livro', 'revista')),
    autor       VARCHAR(150),            -- usado por livro
    edicao      INTEGER CHECK (edicao > 0), -- usado por revista
    disponivel  BOOLEAN NOT NULL DEFAULT TRUE,

    -- Livro precisa de autor; revista precisa de edição.
    CONSTRAINT ck_item_campos_por_tipo CHECK (
        (tipo = 'livro'   AND autor  IS NOT NULL) OR
        (tipo = 'revista' AND edicao IS NOT NULL)
    )
);

-- ---------------------------------------------------------------------
-- 2) usuario: alunos e professores na mesma tabela
-- ---------------------------------------------------------------------
CREATE TABLE usuario (
    id            INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nome          VARCHAR(150) NOT NULL,
    tipo          VARCHAR(10)  NOT NULL CHECK (tipo IN ('aluno', 'professor')),
    limite_itens  INTEGER      NOT NULL CHECK (limite_itens > 0)
);

-- ---------------------------------------------------------------------
-- 3) emprestimo: criada por último, pois referencia item e usuario
-- ---------------------------------------------------------------------
CREATE TABLE emprestimo (
    id                       INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    item_id                  INTEGER NOT NULL REFERENCES item(id),
    usuario_id               INTEGER NOT NULL REFERENCES usuario(id),
    data_retirada            DATE    NOT NULL DEFAULT CURRENT_DATE,
    data_devolucao_prevista  DATE    NOT NULL,
    data_devolucao           DATE,   -- sem NOT NULL: fica vazia enquanto o item não voltar
    valor_multa              NUMERIC(8, 2) NOT NULL DEFAULT 0 CHECK (valor_multa >= 0),

    CONSTRAINT ck_emprestimo_prevista CHECK (data_devolucao_prevista >= data_retirada),
    CONSTRAINT ck_emprestimo_devolucao CHECK (data_devolucao IS NULL OR data_devolucao >= data_retirada)
);

-- =====================================================================
-- Dados de teste
-- =====================================================================

-- Itens: 2 livros e 2 revistas (o id é gerado automaticamente)
INSERT INTO item (codigo, titulo, tipo, autor, edicao) VALUES
    ('L001', 'Dom Casmurro',       'livro',   'Machado de Assis', NULL),
    ('L002', 'O Cortiço',          'livro',   'Aluísio Azevedo',  NULL),
    ('R001', 'Superinteressante',  'revista', NULL,               450),
    ('R002', 'Galileu',            'revista', NULL,               389);

-- Usuários: aluno (até 3 itens) e professor (até 5 itens)
INSERT INTO usuario (nome, tipo, limite_itens) VALUES
    ('Ana',    'aluno',     3),
    ('Carlos', 'professor', 5);

-- Empréstimo EM ABERTO: Ana pegou "Dom Casmurro" (livro, prazo 14 dias)
INSERT INTO emprestimo (item_id, usuario_id, data_retirada, data_devolucao_prevista)
SELECT i.id, u.id, DATE '2026-11-05', DATE '2026-11-05' + 14
FROM item i, usuario u
WHERE i.codigo = 'L001' AND u.nome = 'Ana';

-- O item emprestado e ainda não devolvido fica indisponível
UPDATE item SET disponivel = FALSE WHERE codigo = 'L001';

-- Empréstimo JÁ DEVOLVIDO: Carlos pegou "Superinteressante" (revista, prazo 7 dias)
-- e devolveu 3 dias depois do prazo -> multa de 3 x R$ 1,00 = R$ 3,00
INSERT INTO emprestimo (item_id, usuario_id, data_retirada, data_devolucao_prevista,
                        data_devolucao, valor_multa)
SELECT i.id, u.id, DATE '2026-10-20', DATE '2026-10-20' + 7, DATE '2026-10-30', 3.00
FROM item i, usuario u
WHERE i.codigo = 'R001' AND u.nome = 'Carlos';
-- "Superinteressante" já voltou, então continua com disponivel = TRUE.

-- =====================================================================
-- Consulta de conferência
-- =====================================================================
SELECT e.id,
       i.titulo,
       i.tipo        AS tipo_item,
       u.nome,
       u.tipo        AS tipo_usuario,
       e.data_retirada,
       e.data_devolucao_prevista,
       e.data_devolucao,
       e.valor_multa,
       CASE WHEN e.data_devolucao IS NULL THEN 'em aberto' ELSE 'devolvido' END AS situacao
FROM emprestimo e
JOIN item    i ON i.id = e.item_id
JOIN usuario u ON u.id = e.usuario_id
ORDER BY e.id;
