
﻿# fc3-admin-do-catalogo

API Rest para operações do serviço LXP Conteudo.

## Organização

- **fc3-admin-do-catalogo/**
  - **application/**
    - ***src***
      - main
        - java
          - com.fullcycle.admin.catalogo.application
            - UseCase 
      - test
        - java 
           - com.fullcycle.admin.catalogo.application
             - UseCaseTest
  - **domain/**
    - ***src***
      - main
        - java
          - com.fullcycle.admin.catalogo.domain
            - Category 
      - test
        - java 
           - com.fullcycle.admin.catalogo.domain
             - CategoryTest
  - **infrastructure/**
   - ***src***
      - main
        - java
          - com.fullcycle.admin.catalogo.infrastructure
            - Main 
      - test
        - java 
           - com.fullcycle.admin.catalogo.infrastructure
             - MainTest
  - README.md


## Como rodar

1. Clone do repositório

    sh
    $ git clone git@ssh.dev.azure.com:v3/technecloud/Lyceum/ms-curso-lxp
    

2. Empacotar

    sh
    $ ./gradlew build
    

3. Ou executar diretamente:

    sh
    $ ./gradlew bootRun
    

## Addendum: Procedimentos para o Desenvolvedor

### Gradle Setup

Para configurar as credenciais localmente acesse o link
[Configurar Credenciais Azure no Gradle](https://dev.azure.com/technecloud/Lyceum/_wiki/wikis/lyceum.wiki/108/Credenciais-Azure?anchor=credentials-setup),

### Liquibase

Estrutura de arquivos:

text
└── src
    └── main
        └── resources
            └── db
                └── changelog
                    ├── changes
                    └── master.xml


Seguindo as [boas práticas do liquibase](https://www.liquibase.org/get-started/best-practices), novos arquivos estão
organizados na forma de "changeSets" XML dentro da pasta changes.
Estes arquivos devem possuir uma alteração simples e são incluídos no arquivo master.xml.

Por padrão, toda vez que executarmos o projeto (e.g. gradle tasks: test, build, bootRun ... ) a task liquibaseUpdate
também será executada e o changelog será aplicado à base de dados definida no build.
Este design facilita bastante o desenvolvimento, mas é preciso ter cuidado e definir como se deseja tratar o cenário de
atualização de banco de dados em produção.
Detalhes da configuração da task liquibaseUpdate podem ser encontrados
em [liquibase.gradle](./gradle/liquibase.gradle).
O comportamento padrão pode ser desabilitado com spring.liquibase.enabled=false.

#### Fluxo de Desenvolvimento

- Execute ./gradlew liquibaseUpdate para atualizar atualizar o DB de desenvolvimento;
- Realizar alteração em entidade JPA (e.g. novo campo ou relacionamento);
- Execute ./gradlew liquibaseDiffChangelog -PrunList=diffLog;
- Um novo changelog será criado na pasta changes;
- Revise o changelog criado e inclua-o no master.xml para que as alterações reflitam no banco de dados na próxima
  execução;

*Nota:*

    Para testes é possível habilitar o contexto 'faker' do liquibase para gerar uma massa de dados através de arquivos sql em src/main/resources/db/fake-data:

    sh
    $ ./gradlew liquibaseUpdate -Pcontexts=faker
    
    Ou ao iniciar a aplicação:
    sh
    $ ./gradlew bootRun --args='--spring.liquibase.contexts=faker'
    

#### MySQL com Docker

Com docker-compose é possível ter uma base de dados MySQL executando prontamente para desenvolvimento.
Apesar da configuração de desenvolvimento utilizar o H2 como padrão para ter um "ramp up" mais simples, utilizar o mesmo
banco de dados de produção pode evitar problemas nas conversões de tipos especialmente quando utilizarmos o _"
liquibaseDiffChangelog"_.

Assim, para utilizar o MySQL localmente via docker container, basta executar o comando abaixo:

sh
$ docker-compose -f docker/mysql.yaml up


Para conectar a aplicação com este MySQL local, basta ativar o profile mysql dentro da IDE ou via linha de comando
como abaixo:

sh
$ ./gradlew clean bootRun --args='--spring.liquibase.contexts=faker --spring.profiles.active=mysql'


Acessando com mysql-client:

sh
$ mysql -u root -h localhost -P 3306 --protocol=tcp


#### Executando testes com MODE=MSSQLServer

Para executar os testes simulando as transações em MSSQLServer, basta executar o comando abaixo:

sh
$ ./gradlew test -Dspring.profiles.active=testmssql


#### MSSQLServer com Docker

Com docker-compose é possível ter uma base de dados MSSQLServer executando prontamente para desenvolvimento.
Apesar da configuração de desenvolvimento utilizar o H2 como padrão para ter um "ramp up" mais simples, utilizar o mesmo
banco de dados de produção pode evitar problemas nas conversões de tipos especialmente quando utilizarmos o _"
liquibaseDiffChangelog"_.

Assim, para utilizar o MSSQLServer localmente via docker container, basta executar o comando abaixo:

sh
$ docker-compose -f docker/mssql.yaml up


Para conectar a aplicação com este MSSQLServer local, basta ativar o profile mssql dentro da IDE ou via linha de comando
como abaixo:

sh
$ ./gradlew clean bootRun --args='--spring.liquibase.contexts=faker --spring.profiles.active=mssql'


Acessando com sqlcmd:

sh
$ sqlcmd -S localhost -U sa -P "bhimA123"


### Multi Tenancy

Os micro-serviços do Lyceum devem levar em consideração o fator multi-inquilino.
No caso deste módulo a implementação utilizada foi
descrita [aqui](https://dev.azure.com/technecloud/Lyceum/_wiki/wikis/lyceum.wiki/90/Multi-Tenant-Design).

### API First

Este micro-serviço foi desenvolvido utilizando a metodologia de API Fisrt.
Para disseminar a pratica,
criamos [aqui](https://dev.azure.com/technecloud/Lyceum/_wiki/wikis/lyceum.wiki/92/API-First-Design) uma documentação
que descreve a metodologia e nossas escolhas.

## Referências

[SpringBoot Initializer](https://start.spring.io/)

[Spring Boot Reference Guide](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)

[SpringBoot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-external-config)

[Spring Data JPA - Working with Repositories](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories)

[Spring Data JPA - Working Specifications](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#specifications)

[Spring Cloud Kubernetes - ConfigMap as PropertySource](https://docs.spring.io/spring-cloud-kubernetes/docs/current/reference/html/index.html#configmap-propertysource)

[Liquibase Docs](https://docs.liquibase.com/home.html)

[Liquibase Gradle Plugin](https://github.com/liquibase/liquibase-gradle-plugin)


