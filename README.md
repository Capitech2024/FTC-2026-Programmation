## Breve aviso
Esse repositório possui todas as mudanças de código E programação principal da equipe Capitech de número #26970 da FIRST Tech Challenge. Entretanto, não espere ser um tutorial pois será apenas uma demonstração sobre o que cada pasta armazena e o que cada código faz.
## Bem vindo!
Seja bem vindo ao repositório oficial de programadores da equipe Capitech #26970 da FIRST Tech Challenge! Aqui temos a programação tanto do modo Autônomo quanto do modo TeleOperado para a temporada Biobuzz (que acontecerá de 2026 a 2027). Recomenda-se de que leia este artigo para se orientar melhor pelo repositório!!
## Começando
Se você for um programador novo na área da robótica (ou de qualquer modalidade da FIRST) terá pelo menos uma noção básica sobre o que cada coisa faz. Na FIRST Tech Challenge, temos 2 modos de jogo: TeleOperado (Onde o robô é operado manualmente por 2 pessoas ou mais) e Autônomo (O robô opera sozinho pela arena, seguindo sua programação). Esse repositório possui todos os commits das programações de cada modo, além de testes para o protótipo do robô antes do mês da temporada e outras coisas.
## Requisitos
A equipe utiliza os materiais oferecidos pelas empresas REV Robotics e GoBilda, então é importante informar primeiramente que todo o equipamento em que usamos é adquirido pelo site StemOS, contendo tudo que é necessário para as temporadas de competição da FIRST Tech Challenge. Aqui utilizamos o Driver Station para executar a programação pelo robô, mas também pode se usar um SmartPhone com (no mínimo) android 7.0 (Nougat).
## Android Studio 
O Ambiente de Desenvolvimento Integrado (IDE) utilizado pela Capitech é o Android Studio, por oferecer mais recursos e maior versatilidade ao programar. No entanto, é necessário instalar os arquivos no repositório do [FTCRobotController](https://github.com/FIRST-Tech-Challenge/FTCRobotController), para o Driver Station reconhecer todos os arquivos necessários das programações na hora de enviar os dados ao Control Hub. 
## TeleOperado
A programação do TeleOp começa primeiramente com a configuração do seu _OpMode_ "@TeleOp", que opera o código todo. Em seguida, é feita a configuração de todos os motores _HD Hex Ultraplanetary_ e _Hd Core Hex_ seguindo suas funções no robô, como Shooter, Intake e Rodas Mecanum. Depois, ocorre o mapeamento dos hardwares, e configuração do Gamepad colocando um botão para cada função. Além das configurações iniciais, o robô se orienta pelo _Control Hub_ e _Expansion Hub_ para enviar os dados diretamente para o robô através de uma rede Wi-fi (ou via cabo) e faz a telemetria. Depois disso, a programação finalmente começa definindo os comandos do robô.
## Autônomo
A programação do modo Autônomo ocorre de forma parecida com tudo que foi visto no TeleOperado, seguindo os mesmos fundamentos. A única diferença é que, o modo autônomo exige muito mais estratégia e cálculo sobre o que cada componente vai fazer, já que não vai ser operado por um usuário. Além disso, é alterado o _OpMode_ do código, sendo o "@Autonomous". 
#### Atenção:
No modo Autônomo e Tele Operado é altamente recomendado que não se use a potência máxima dos motores de 100% (1.0). Ao invés disso, recomenda-se que use porcentagens menores como 80%, 75%, etc. Isso causa a drenagem rápida da bateria nas partidas.
## Notas Finais
No momento que este README está sendo publicado, o repositório ainda não está completo, e coisas podem mudar. Agradecemos por visitar o repositório oficial da equipe de número #26970 da FIRST Tech Challenge!


#Adicionando o titulo