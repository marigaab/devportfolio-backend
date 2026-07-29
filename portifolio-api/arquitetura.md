# 🏛️ Documento de Arquitetura da Solução em Nuvem

**Projeto:** DevPortfolio  
**Provedor de Nuvem:** Oracle Cloud Infrastructure (OCI) - Always Free  
**Desenvolvedora Solo:** [Seu Nome Completo]  
**Papéis Desempenhados:** Developer Fullstack, Product Owner e Scrum Master  

---

## 1. Descrição Geral da Solução
O **DevPortfolio** é uma aplicação Web completa desenvolvida para cadastro, exibição e gerenciamento de perfis de desenvolvedores e seus respectivos projetos. A solução é composta por um frontend dinâmico em **Angular**, uma API RESTful resiliente em **Java (Spring Boot)** e um banco de dados relacional **MySQL**.

A infraestrutura foi totalmente implantada na nuvem utilizando uma instância virtual (*Compute Instance*) na **Oracle Cloud Infrastructure (OCI)**.

---

## 2. Componentes da Arquitetura e Justificativa Técnica

### ☁️ Provedor de Nuvem: Oracle Cloud Infrastructure (OCI)
* **Serviço:** OCI Always Free Tier.
* **Justificativa:** Oferece instâncias de computação e infraestrutura de rede robustas de forma gratuita e sem limitações temporárias de teste, garantindo alta disponibilidade e custo zero de infraestrutura.

### 🛡️ Rede e Segurança: OCI Security List & Virtual Cloud Network (VCN)
* **Componente:** Subrede Pública e Firewall da Nuvem.
* **Justificativa:** Controla rigorosamente o tráfego de entrada e saída. Apenas a porta **80 (HTTP)** para acesso público e a porta **22 (SSH)** para administração remota estão liberadas. A porta do banco de dados (3306) é bloqueada externamente por segurança.

### 🌐 Servidor Web e Proxy Reverso: Nginx
* **Função:** Servidor Web e Proxy Reverso.
* **Justificativa:** O Nginx recebe todo o tráfego da porta 80. Ele entrega os arquivos estáticos compilados da interface Angular diretamente ao navegador e intercepta as chamadas `/api/`, redirecionando-as internamente para o backend Java na porta 8080. Isso evita a necessidade de expor a aplicação Java diretamente e elimina problemas de CORS.

### ☕ Application Server (Backend): Java Spring Boot
* **Função:** API RESTful (Porta 8080).
* **Justificativa:** Responsável pelo processamento de todas as regras de negócio, validação de dados e comunicação com o banco de dados. Roda de forma isolada do público direto.

### 🐬 Banco de Dados: MySQL Server
* **Função:** Banco de Dados Relacional (Porta 3306 - Localhost).
* **Justificativa:** Garante a persistência e integridade referencial dos dados (relacionamento 1:1 de usuário/perfil e 1:N de perfil/projetos com exclusão em cascata). Configurado para escutar exclusivamente em `127.0.0.1`, impedindo acessos externos não autorizados.

---

## 3. Fluxo de Comunicação entre os Componentes

1. **Requisição Inicial:** O usuário acessa a aplicação pelo navegador via HTTP na **Porta 80**.
2. **Filtro de Segurança:** A **OCI Security List** valida a requisição e permite a passagem do tráfego HTTP.
3. **Recepção no Nginx:** O Nginx serve os arquivos estáticos da interface em Angular.
4. **Proxy de API:** Quando uma requisição de dados é iniciada (ex: listar perfis), o Nginx repassa a chamada `/api/` para a **Porta 8080** onde a API Java Spring Boot está escutando.
5. **Acesso ao Banco:** O Spring Boot processa a requisição e consulta localmente o **MySQL** na **Porta 3306**.
6. **Resposta:** O MySQL retorna os registros SQL, o Spring Boot converte em DTOs/JSON, e o Nginx encaminha a resposta de volta ao navegador para renderização na tela do usuário.

---

## 4. Análise Técnica e Decisões de Arquitetura

* **Containers e Balanceador de Carga:** *Não aplicável nesta etapa.* Optou-se por uma implantação direta no sistema operacional Ubuntu Server para reduzir a complexidade operacional e otimizar o uso da memória RAM da VM.
* **Limitações Conhecidas:** A arquitetura atual utiliza uma única instância virtual (Ponto Único de Falha - SPOF). Caso o tráfego aumente significativamente, a escalabilidade atual é vertical.
* **Melhorias Futuras:**
  1. Adição de um **OCI Load Balancer** para escalabilidade horizontal.
  2. Implementação de certificado HTTPS gratuito via **Let's Encrypt**.
  3. Containerização completa da aplicação com **Docker e Docker Compose**.