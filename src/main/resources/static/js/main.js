
var gameState = 0; // 게임 상태관리 변수 0:일땐 시작전, 1: 일땐 낮 2: 일땐 처형의 시간 3: 일땐 밤 4: 일땐 게임 종료
var dayCount = 0;
var playerRoles = [];
var players = []; //서버로 부터 플레이어 정보를 받아와서 저장할 변수
var player = {
    name: 'null',
    group: 'null',
    live : true,
    ability: false, //구조는 나중에 건들기
};
var playerNum = 0;
var mafia = 0;
var citizen = 0;
var livePlayers = [];
var votedPlayer;
var yesPlayer;
var chosenPlayer;

$.ajax({
    url:"/enter/{roomid}",
    type:'GET',
    dataType:'json',
    async:false,
    success:function(data){
        players = data.players;
        playerNum = data.playerNum;

    }
});

function defaultRole(playerNum){ //현재 플레이어 수에 따라 역할 배분하는 함수
    switch(playerNum){
        case 4:
            playerRoles = [
                role['citizen'],
                role['cop'],
                role['doctor'],
                role['mafia']
            ]
            return true;
        case 5:
            playerRoles = [
                role['citizen'],
                role['cop'],
                role['doctor'],
                role['mafia'],
                role['spy']
            ]
            return true;
        case 6:
            playerRoles = [
                role['citizen'],
                role['citizen'],
                role['cop'],
                role['doctor'],
                role['mafia'],
                role['mafia']
            ]
            return true;
        case 7:
            playerRoles = [
                role['citizen'],
                role['citizen'],
                role['cop'],
                role['doctor'],
                role['mafia'],
                role['mafia'],
                role['spy']
            ]
            return true;
        case 8:
            playerRoles = [
                role['citizen'],
                role['citizen'],
                role['priest'],
                role['cop'],
                role['doctor'],
                role['mafia'],
                role['mafia'],
                role['spy']
            ]
            return true;
        default:
            return false;
    }

}

function shuffle(players){ //현재 플레이어 배열을 매개변수로 가져옴, 무작위로 섞는 함수
    var j; //랜덤 함수넣을 변수
    var x; //빈값 변수
    var i; //매개변수로 받아온 변수 저장할 변수
    for(i=players.length;i;i-=1){
        j=Math.floor(Math.random()*i);
        x=players[i-1];
        players[i-1] = players[j];
        players[j] = x;
    } //이 부분은 https://malonmiming.tistory.com/106 해당 링크를 많이 참조했음. 시간복잡도상 가장 효율적이라 판단
}

function assignRole(){ // 플레이어들에게 역할을 배정시키는 함수
    /* 서버로부터 플레이어 수를 받아 players에 저장하는 코드 작성 */
    players = shuffle(players);
    defaultRole(players.length)
    for(var i=0;i<players.length;i++){
        players[i].gameRole = playerRoles[i]; //서버쪽에서 작업권장
        players[i].join(playerRoles[i].group); //서버쪽에서 작업권장
    }
   livePlayers = players;
    $.ajax({
        url:"컨트롤러 url",
        type:'POST',
        data: JSON.stringify({
            "livePlayers" : livePlayers,
        })
    });
}

function checkWin(){ //어느 진영이 승리하였는지에 관한 함수
    $.ajax({
        url:"컨트롤러 url",
        type:'GET',
        dataType:'json',
        async:false,
        success:function(data){
        mafia = data.mafia;
        citizen = data.citizen;
        }
    });

    //json을 int값 또는 string값으로 변환해줘야됨

    var citizenWin = (mafia == 0); //서버로부터 현재 생존한 마피아의 상태를 받아오고 없다면 true값을 가지는 변수를 선언
    var mafiaWin = (mafia >= citizen); // 서버로부터 현재 생종한 마피아와 시민의 상태를 받아오고 마피아가 더 많다면 true값을 가지는 변수를 선언

    if(citizenWin){ // 만약 마피아가 0명이라면
        endGame('citizen'); // citizen을 매개변수로 가지는 gameEnd함수 호출
    }
    else if(mafiaWin){ // 만약 마피아가 시민보다 많다면
        endGame('mafia'); // mafia를 매개변수로 가지는 gameEnd 함수 호출
    }
}

function vote(){ // 투표와 관련된 함수

    if(gameState == 1){ // 죽은사람은 권한이 없게 처리해야하는데,,, how?
        alert("투표를 시작합니다. (1분간 진행)");
        $.ajax({
            url:"컨트롤러 url",
            type:'GET',
            dataType:'json',
            async:false,
            success:function(data){
            livePlayers = data.livePlayers;
            }
        });
        $.('button').click(function(){
            $.ajax({
                url:"컨트롤러 url",
                type:'POST',
                data: JSON.stringify({//search["email"] = $("#email").val();
                    "votedPlayer" : //HTML내 이름 속성값을 참고하여 선정
                })
            });
        });
        /* 서버로부터 살아있는 사용자들의 목록을 받아온 뒤,
                사용자들이 서로 투표할 수 있게하는 코드작성 여기서
                가장 많은 표를 받은 플레이어는 votedPlayer가 됨*/
        setTimeout(function(){
            $.ajax({
                url:"컨트롤러 url",
                type:'GET',
                dataType:'json',
                async:false,
                success:function(data){
                votedPlayer = data.votedPlayer;
                }
            }); //string 값으로 변환 필요
            if(votedPlayer){ // 만약 과반수 이상의 표를 받은 플레이어가 있다면
                executationTime(); // 처형의 시간으로 이동
            }
            else{
                nightTime(); // 밤으로 이동
            }
        },60000);



    }
    if(gameState == 2){
        /* votedPlayer에 대한 찬성/반대 화면을 출력해주고
        서버로 부터 찬성/반대에 대한 결과를 받아오는 코드 작성*/
        //팝업 구현 or 채팅 구현??? 좀더 연구필요
        if(yesPlayer>livePlayers.length/3){ // 만약 찬성측이 생존해있는 플레이어 수 1/3 이상이라면 실행
            diePlayer(votedPlayer); // diePlayer라는 함수를 실행시켜 플레이어를 사망처리함 diePlayer함수는 나중에 구현할 계획
            nightTime();
        }
        else{
            nightTime();
        }
    }
    if(gameState == 3){
        alert("투표를 시작합니다. (1분간 진행)");
        $.ajax({
            url:"컨트롤러 url",
            type:'GET',
            dataType:'json',
            async:false,
            success:function(data){
            livePlayers = data.livePlayers;
            }
        });
        $.('button').click(function(){
            $.ajax({
                url:"컨트롤러 url",
                type:'POST',
                data: JSON.stringify({//search["email"] = $("#email").val();
                    "votedPlayer" : //HTML내 이름 속성값을 참고하여 선정
                })
            });
        });
        /* 서버로부터 살아있는 사용자들의 목록을 받아온 뒤,
        사용자들이 서로 투표할 수 있게하는 코드작성 여기서
        가장 많은 표를 받은 플레이어는 votedPlayer가 됨 이때, 이 화면은 마피아 그룹만 볼수있게 코드 작성*/
        setTimeout(function(){
            $.ajax({
                url:"컨트롤러 url",
                type:'GET',
                dataType:'json',
                async:false,
                success:function(data){
                votedPlayer = data.votedPlayer;
                }
            }); //string 값으로 변환 필요
            if(!votedPlayer){// 만약 표가 나뉘어 votedPlayer가 정해지지 않았다면
                votedPlayer = livePlayers[(livePlayers.lenght*Math.random())]; // 랜덤으로 한명 선택
                diePlayer(votedPlayer);

            }
            else{
                diePlayer(voetdPlayer);
            }
        },60000);
    }
}

function diePlayer(Player){
    $.ajax({
        url:"컨트롤러 url",
        type:'POST',
        data : stringify({
            "diePlayer" : Player
        }),
        success:function(data){
        }
    });
    $.ajax({
        url:"컨트롤러 url",
        type:'GET',
        dataType : 'json',
        success:function(data){
            livePlayers = data.livePlayers;
        }
    });
}

function dayTime(duration){ //낮시간 진행시키는 함수
    if(gameState !== 4){
        gameState = 1;
        dayCount++;
        /* 낮화면을 출력해주는 css 스타일 변경 */
        setTimeout(vote(),180000); // 3분뒤 투표실행
    }
}


function executationTime(){
    if(gameState !== 4){
        gameState = 2;
        /* 처형의 시간 화면을 출력해주는 css 스타일 변경 */
        setTimeout(vote(),60000); // 1분뒤 투표실행
    }
}

function nightTime(){ //일반 시민들 스크립트 처리는 어떻게 할것인가,,?
    if(gameState !== 4){
        gameState = 3;
        /* 밤 화면을 출력해주는 코드 작성
                여기서는 서버로부터 플레이어의 직업과 그룹정보를 받아오는 코드도 작성 */
        $.ajax({
            url:"컨트롤러 url",
            type:'GET',
            dataType : 'json',
            success:function(data){
                player = data.player;
            }
        });

        if(player.ability == true){ // 만약 능력을 가지고 있는 플레이어가 있다면
            alert("대상을 선택해주세요");
            $.('button').click(function(){
                $.ajax({
                    url:"컨트롤러 url",
                    type:'POST',
                    data: JSON.stringify({//search["email"] = $("#email").val();
                        "chosenPlayer" : //HTML내 이름 속성값을 참고하여 선정
                    })
                });
            });
            setTimeout(player.abilityFunc(chosenPlayer),15000);
        }
        if(player.group == 'mafia'){ // 만약 플레이어의 그룹이 마피아팀이라면
            /* 마피아팀 전용 채팅방 구현 */
            setTimeout(vote(),60000); // 1분뒤 투표실행
        }
        nextDay(); // 밤이 모두 끝난후 결과를 집계하고 다음 낮으로 넘어가기 전 처리해주는 nextDay()라는 함수 실행
    }

}

function nextDay(){
    if(playerNum/2 >= livePlayers){
        checkWin();
    }
    else{
        dayTime(dayCount);
    }
}

function initGame(){
    if(defaultRole(players.length)==false){ // 이부분을 그냥 day.html에서 판단하게 넘길까 생각중
        /* 현재 플레이어 인원수가 부족하다는 상태메시지 출력*/
        alert("현재 인원 부족");
        break;
    }
    assignRole();
    dayTime(0);
}

function endGame(group){ //게임을 끝내고 결과창을 출력하는 함수
    gameState = 4;
    if(group=='citizen'){
        /* 시민팀의 승리를 나타내는 웹페이지 또는 애니메이션 출력 */
    }
    else if(group=='mafia'){
        /* 마피아팀의 승리를 나타내는 웹페이지 또는 애니메이션 출력 */
    }
}


