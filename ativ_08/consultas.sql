-- =====================================================================
-- Consultas do sistema de biblioteca (rodar depois de biblioteca.sql)
-- =====================================================================

-- ---------------------------------------------------------------------
-- 1) Acervo completo: código, título, tipo e disponibilidade.
--    O LEFT JOIN com os empréstimos em aberto (e com usuario) mostra,
--    na mesma linha, com quem está cada item emprestado.
--    É LEFT JOIN para que os itens disponíveis também apareçam.
-- ---------------------------------------------------------------------
SELECT i.codigo,
       i.titulo,
       i.tipo,
       CASE WHEN i.disponivel THEN 'disponível' ELSE 'emprestado' END AS disponibilidade,
       u.nome AS emprestado_para
FROM item i
LEFT JOIN emprestimo e ON e.item_id = i.id
                      AND e.data_devolucao IS NULL      -- só o empréstimo em aberto
LEFT JOIN usuario u    ON u.id = e.usuario_id
ORDER BY i.codigo;


-- ---------------------------------------------------------------------
-- 2) Empréstimos em aberto, com nome do usuário e título do item.
--    Em aberto = data_devolucao IS NULL (nunca "= NULL", que nunca é verdadeiro).
-- ---------------------------------------------------------------------
SELECT u.nome   AS usuario,
       i.titulo AS item,
       e.data_retirada,
       e.data_devolucao_prevista
FROM emprestimo e
JOIN usuario u ON u.id = e.usuario_id
JOIN item    i ON i.id = e.item_id
WHERE e.data_devolucao IS NULL
ORDER BY e.data_devolucao_prevista;


-- ---------------------------------------------------------------------
-- 3) Total de multas acumuladas por usuário.
--    LEFT JOIN + COALESCE: usuário sem nenhum empréstimo aparece com 0,00
--    (com JOIN comum ele sumiria; sem COALESCE, o SUM viria NULL).
-- ---------------------------------------------------------------------
SELECT u.nome,
       u.tipo,
       COALESCE(SUM(e.valor_multa), 0) AS total_multas
FROM usuario u
LEFT JOIN emprestimo e ON e.usuario_id = u.id
GROUP BY u.id, u.nome, u.tipo
ORDER BY total_multas DESC, u.nome;


-- ---------------------------------------------------------------------
-- 4) Itens que nunca foram emprestados.
--    O LEFT JOIN mantém todos os itens; quem não tem nenhum empréstimo
--    recebe NULL em todas as colunas de emprestimo. Testamos e.id
--    (a chave primária, que nunca é NULL num empréstimo real).
-- ---------------------------------------------------------------------
SELECT i.codigo,
       i.titulo,
       i.tipo
FROM item i
LEFT JOIN emprestimo e ON e.item_id = i.id
WHERE e.id IS NULL
ORDER BY i.codigo;
