1) Contexto (tema)
A Prefeitura inaugurou a Biblioteca Cidadã, com acervo físico e digital.
Você foi contratado para projetar e implementar, em Java, um sistema
de gestão de empréstimos que permita cadastrar usuários, gerenciar o
acervo (livros, revistas e mídias digitais) e controlar
empréstimos/devoluções com cálculo de multa por atraso.
2) Objetivos de aprendizagem
Demonstrar, em código Java, os pilares de OO:
• Encapsulamento (atributos privados + getters/setters e
invariantes);
• Herança e polimorfismo (sobrescrita / ligação dinâmica);
• Abstração (1 classe abstrata obrigatória + 1 interface obrigatória);
• Relações: Agregação (objetos com ciclo de vida independente)
e Composição (parte-todo com dependência de vida).
3) Requisitos funcionais mínimos

1. Cadastro de usuários (Aluno, Servidor, Visitante), cada um com
regras específicas de prazo e multa.
2. Cadastro de itens de acervo: Livro, Revista (físicos) e MidiaDigital
(PDF/áudio).
3. Empréstimo e devolução (com data estimada e efetiva) e cálculo
de multa variável por tipo de usuário e tipo de item.
4. Relatórios:
o Itens disponíveis/emprestados;
o Empréstimos em atraso;
o Histórico por usuário.
5. Interface de uso: menu em linha de comando (CLI) com
operações de CRUD e empréstimo/devolução.

4) Requisitos de projeto (OO) — o que precisa aparecer no seu código
4.1 Abstração
• Crie uma classe abstrata Recurso (id, título, anoPublicacao, …).
o Métodos comuns (ex.: getDescricao() concreto) e pelo
menos um método abstrato, por exemplo double
calcularMulta(long diasAtraso).
• Crie uma interface Emprestavel com, no mínimo:
boolean emprestar(Usuario u), void devolver(), LocalDate
getDataPrevistaDevolucao().
4.2 Herança e Polimorfismo
• Recurso → especializações: Livro, Revista, MidiaDigital.
o Polimorfismo: cada tipo sobrescreve calcularMulta() com
regras distintas.
o Ex.: Livro multa por dia inteiro; Revista com carência;
MidiaDigital com limite diferente de dias.
• Usuario (classe base) → Aluno, Servidor, Visitante, alterando
prazo/multa máximos por tipo.
o Ex.: método polimórfico int prazoDiasPadrao().
4.3 Encapsulamento
• Todos os atributos privados, com getters/setters.
• Validações internas (ex.: título não vazio; ano > 0).
• Não exponha coleções internas diretamente: retorne cópias
imutáveis ou views somente leitura quando necessário.
4.4 Agregação
• Biblioteca agrega Recurso e agrega Usuario: ambos podem existir
sem a Biblioteca.
o Implementar como listas internas gerenciadas por Biblioteca
(adicionar/remover/buscar).
4.5 Composição
• Livro compõe Capitulo (um Capitulo não existe fora do Livro).
o Livro mantém a criação/remoção dos seus Capitulos; ao
remover o Livro, os capítulos deixam de existir.
4.6 Outras regras técnicas
• Usar Java 17+, organizar em packages (ex.: br.recife.biblioteca +
subpacotes modelo, servico, repositorio, ui).
• Proibir uso de frameworks; apenas Java SE.
• Cobrir exceções (ex.: tentar emprestar item já emprestado lança
exceção customizada).
• Adotar toString() nas entidades para facilitar relatórios.
• Incluir pelo menos 5 testes unitários JUnit (ex.: cálculo de multa,
prazos por tipo de usuário, composição de capítulos).
5) Escopo sugerido de classes (guia, não é solução)
• abstract class Recurso (id, titulo, anoPublicacao, disponivel, …)
o double calcularMulta(long diasAtraso) (abstrato)
o String getDescricao() (concreto)
• class Livro extends Recurso
o Composição: List<Capitulo> (classe Capitulo com numero,
titulo, paginas)
o Regras de multa próprias
• class Revista extends Recurso
o edição, periodicidade; carência de X dias na multa
• class MidiaDigital extends Recurso
o tamanhoMB, tipoArquivo; multa diferenciada
• interface Emprestavel
o emprestar(Usuario u), devolver(),
getDataPrevistaDevolucao()
• abstract class Usuario (id, nome, documento)
o int prazoDiasPadrao(); double fatorMulta()
• class Aluno extends Usuario, class Servidor extends Usuario, class
Visitante extends Usuario
• class Emprestimo
o (recurso, usuario, dataEmprestimo, dataPrevista,
dataDevolucao, status)
o Agregação com Usuario e com Recurso (podem existir fora
do empréstimo)
• class Biblioteca
o coleções internas de Usuario e Recurso (agregação)
o serviços: buscar por título/ID, listar disponíveis, registrar
empréstimo/devolução (usando Emprestavel)
• class BibliotecaCLI (menu textual)
o opções de CRUD e operações de
empréstimo/devolução/relatórios
Observação: escolha apenas uma relação concreta para cada tipo
(agregação e composição) e documente no README onde cada uma
aparece e por quê.
6) Casos de uso mínimos (aceitação)

1. Cadastrar (3) usuários: 1 Aluno, 1 Servidor, 1 Visitante.
2. Cadastrar (3) itens: 1 Livro com pelo menos 3 capítulos, 1 Revista, 1
MidiaDigital.
3. Emprestar um Livro para Aluno; devolver com atraso e exibir multa
calculada.
4. Emprestar uma MidiaDigital para Visitante (verificar regra de prazo
e multa).
5. Relatório de itens atualmente emprestados e lista de em atraso.
6. Impedir (com exceção) emprestar um item já emprestado.
7. Listar capítulos do Livro (composição visível no relatório).

7) Entregáveis obrigatórios
• Código-fonte organizado por packages e com comentários
Javadoc em classes principais.
• README.md explicando:
o Pilares de OO implementados (onde e como);
o Decisões de projeto (por que agregação/composição
foram escolhidas nesses pontos);
o Como compilar e executar (CLI) e como rodar os testes.
8) Restrições e observações
• Sem frameworks (somente Java SE).
• Persistência em memória (listas) é suficiente; se optar por arquivos,
documente.
• O sistema deve compilar e rodar via javac/java ou IDE, com
instruções claras
