# Pure Energy

**Pure Energy** é um aplicativo Android desenvolvido para gerenciar cômodos e aparelhos eletrônicos de uma residência. O aplicativo oferece uma interface amigável para registrar e visualizar cômodos e aparelhos cadastrados, promovendo uma experiência intuitiva para seus usuários.

## Alunos RM

- **Keven Ike Pereira da Silva** - 553215  
- **Vitor Cruz dos Santos** - 553621  
- **José Ribeiro dos Santos Neto** - 553844  

## Funcionalidades

- **Registrar Cômodo**: Adicione novos cômodos à sua residência.
- **Registrar Aparelho**: Cadastre novos aparelhos eletrônicos e associe-os a um cômodo específico.
- **Visualizar Cômodos e Aparelhos**: Consulte todos os cômodos e aparelhos cadastrados.
- **Navegação Intuitiva**: Navegue facilmente pelas funcionalidades através de abas e menu de navegação inferior.

## Tecnologias Utilizadas

- **Kotlin**: Linguagem principal do desenvolvimento.
- **Firebase**: Autenticação e banco de dados em tempo real.
- **Android Jetpack**: Incluindo componentes como Navigation, ViewModel e LiveData.
- **Material Design**: Para uma interface de usuário moderna e responsiva.

## Estrutura do Projeto

**Link Figma:** [Design no Figma](https://www.figma.com/design/ZJkmWOORP62iR0d3RVUJZe/Untitled?node-id=0-3&node-type=frame&t=YJz8aLEKJ7pawXBW-0)
**Link Video** [Youtube](https://youtu.be/bfj2-8IdKEc) eu não tenho microfone

- **Fragments**:
  - `RegistrarComodoFragment.kt`: Responsável por registrar novos cômodos.
  - `RegistrarAparelhoFragment.kt`: Gerencia o registro de novos aparelhos eletrônicos.
  - `HomeFragment.kt`: Exibe os cômodos e aparelhos cadastrados.
- **Adapters e Utils**:
  - `CardAdapter.kt`: Adapter para exibir itens em um `RecyclerView`.
  - `GridSpacingItemDecoration.kt`: Classe para adicionar espaçamento entre os itens do `RecyclerView`.

## Como Executar

1. Clone o repositório:
   ```bash
   git clone https://github.com/pure-energy-gs/PureEnergy-GsMobileApplication.git
