# ManagementProjects-RestAssured

## Resumo do Projeto

Este projeto consiste em um sistema back-end desenvolvido com o framework Spring Boot, dedicado ao gerenciamento de projetos.

### Recuperação de Senha

Para garantir a segurança das contas dos usuários, o sistema oferece um processo de recuperação de senha. O procedimento é simples e seguro:

1. **Solicitação de Recuperação:**
   - O usuário envia uma solicitação de recuperação de senha, informando seu endereço de e-mail associado à conta.

2. **Geração de Token:**
   - Após receber a solicitação, o sistema gera automaticamente um token único com uma validade de 3 horas e salva no banco de dados.

3. **Envio do Token por E-mail:**
   - O token gerado é enviado para o e-mail fornecido pelo usuário. Este token é essencial para a redefinição da senha.

4. **Redefinição de Senha:**
   - Com o token recebido, o usuário pode acessar a funcionalidade de redefinição de senha no sistema.

5. **Token com Validade:**
   - É crucial ressaltar que o token tem uma validade de 3 horas, garantindo a segurança do processo.

6. **Segurança**
   - O sistema tem toda uma segurança caso o usuário informe um email errado, uma senha fraca ou passe um token inválido.

### Este mecanismo oferece uma solução eficaz e segura para situações em que os usuários necessitam redefinir suas senhas de maneira controlada e confiável.

---

### Gerenciamento de Projetos

O módulo de gerenciamento de projetos oferece funcionalidades robustas para administração eficiente. Destacam-se:

- **Consulta de Projetos:**
  - Verificação detalhada de projetos existentes e tratamento adequado para casos de projetos não encontrados.

- **Listagem de Projetos:**
  - Listagem paginada de projetos, com suporte a filtros por nome, facilitando a navegação e busca específica.

- **Adição e Atualização de Projetos:**
  - Capacidade de criar e atualizar projetos, com validações de segurança e integridade dos dados.

- **Exclusão de Projetos:**
  - Funcionalidade segura para excluir projetos, com tratamento adequado para diferentes cenários, incluindo dependências.

### Estes recursos garantem um gerenciamento eficaz do ciclo de vida dos projetos, desde a consulta até a manipulação segura dos dados.

---

### Gerenciamento de Tarefas

O módulo de gerenciamento de tarefas oferece funcionalidades robustas para administração eficiente das atividades associadas aos projetos. Destacam-se:

- **Consulta de Tarefas:**
  - Verificação detalhada de tarefas existentes, incluindo informações do projeto ao qual estão vinculadas.

- **Listagem de Tarefas:**
  - Listagem paginada de tarefas, com suporte a filtros por nome, facilitando a navegação e busca específica.

- **Adição e Atualização de Tarefas:**
  - Capacidade de criar e atualizar tarefas, com validações de segurança e integridade dos dados.

- **Exclusão de Tarefas:**
  - Somente usuários administradores e responsáveis podem excluir ou atualizar a tarefa.
    - Funcionalidade segura para excluir tarefas, com tratamento adequado para diferentes cenários, incluindo dependências.

- Detalhes de Tarefas por Projeto:
  - Consulta específica de tarefas de um projeto, incluindo informações detalhadas sobre cada tarefa.

### Estas funcionalidades garantem uma gestão eficaz das tarefas associadas aos projetos, proporcionando controle e visibilidade sobre as atividades em andamento.
---

  **[Link para o Meu Repositório com RestAssured](https://github.com/Luann2003/managementProjects-RestAssured)**


# Autor
Luan Victor de Ramos Luciano

[LinkedIn](https://www.linkedin.com/in/luan-luciano-1603b4197/)
