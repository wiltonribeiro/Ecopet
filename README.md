# Ecopet

O EcoPet é um concurso que visa trazer para a Universidade Federal do Ceará - Campus Quixadá, práticas sustentáveis, com destaque para ações ambientais que insiram educandos e servidores em projetos voltados à preservação do meio ambiente e à manipulação de resíduos sólidos na  universidade, buscando novas práticas de mobilização ambiental dentro da universidade, para que a conscientização e preservação do meio ambiente sejam fortemente discutidas entre a comunidade acadêmica. O aplicativo conta como rede social de publicação de fotos com apelo ecológico. Os usuários que obtiverem mais curtidas em suas fotos até o fim da data de uma tag (ex: #diadaagua) são premiados.

Segue alguns exemplos das funcionalidades já implementadas
#### Login e Feed
No seguinte exemplo há o Login do estudante na aplicação, assumindo que tivera se cadastrado antes. O login via Google+ é possibilitado pela API Oficial do Google, após a entrada no sistema o usuário pode selecionar a hashtag que deseja vizualizar e observar o feed corrrespondente. As hastags carregam as fotos publicadas pelos usuários do aplicativo, além de carregar consigo o ranking de usuários com publicações mais curtidas.

 <img src="https://github.com/wiltonribeiro/ecoPET/blob/master/Preview/LoginPreview.gif" width="300" height="auto">


#### Ranking
No seguinte exemplo o ranking é solicitado para ser visualizado. A contagem de curtidas é atualizada somente caso algum outro usuário não tenha tentado ver o ranking dentro de 5 minutos. Isso evita que haja consumo dos dados, já que o entre os 5 minutos da última atualização não se torna necessário todo o cálculo novamente, apenas buscar o registro do ranking dentro do intervalo de tempo.

<img src="https://github.com/wiltonribeiro/ecoPET/blob/master/Preview/RankingPreview.gif" width="300" height="auto">


#### Perfil
No seguinte exemplo o perfil carrega todoas as fotos publicadas pelo usuário atual. Ao pressionar a foto por um pequeno instante é dado as possiblidades de exclusão e compartilhamento em otras redes sociais.

<img src="https://github.com/wiltonribeiro/ecoPET/blob/master/Preview/ProfilePreview.gif" width="300" height="auto">

#### Denuncia
Para evitar que hava publicações indevidas, há em cada publicação a possiblidade de denuncia. A denúncia carrega em si os dados da imagem, o nível/tipo de denúncia, quem solicitou e o autor da publicação. Dependendo da gravidade da denúncia, a imagem é automaticamente arquivada, mas não excluida, para observação dos administradores. Cada denuncia possui um peso, e cada denuncia e peso correspondente é registrado ao usuário autor, para que seja possível o registro do uso indevido e possível banimento da aplicação.

<img src="https://github.com/wiltonribeiro/ecoPET/blob/master/Preview/ReportPreview.gif" width="300" height="auto">

#### Publicação
No exemplo a seguir, há a publicação de uma foto por parte do usuário. A foto carrega em si a descrição e hashtag correspondente. A publicação é enviada em tempo real. No software é efetuado compressão da imagem em até 60% para que não comprometa o storage dos nossos servidores.

<img src="https://github.com/wiltonribeiro/ecoPET/blob/master/Preview/PublishPreview.gif" width="300" height="auto">

#### Sobre
A aplicação está sendo desenvolvida como ativade do PET-TI Conexão de Saberes, Campus Quixadá. O responsável pelo desenvolvimento têm sido o aluno Wilton Neto do curso de Engenharia de Software. A aplicação é de caréter mobile com desenvolvimento nativo, usando a linguagem de programação Java.

<img src="https://github.com/wiltonribeiro/ecoPET/blob/master/Preview/AboutPreview.gif" width="300" height="auto">




